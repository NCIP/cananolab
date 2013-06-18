/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

/**
 * Class for parsing publication data file (XML) generated by End Note.
 *
 * The main function takes 3 parameters: account, password & input file name.
 *
 * The class will generate 6 output files and save parsed data to database.
 * 1."[InputFile].log" - log file of error/warning msgs occurred during parsing.
 * 2."[InputFile].all" - contains all, unfiltered publication parsed from file.
 * 3."[InputFile].db" - contains duplicated publication in database.
 * 4."[InputFile].dup" - contains duplicated publication in file.
 * 5."[InputFile].saved" - contains publications successfully saved to db.
 * 6."[InputFile].unique" - contains filtered, unique publication from file.
 */
package gov.nih.nci.cananolab.service.publication;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.SAXElementHandler;
import gov.nih.nci.cananolab.util.SAXEventSwitcher;
import gov.nih.nci.cananolab.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;

public class EndNoteXMLHandler {
	private PublicationBean publicationBean;
	private Publication publication;
	private String userName = "DATA_PARSING";
	private String pmid;
	private StringBuilder doi;
	private StringBuilder url;
	private StringBuilder journal;
	private StringBuilder volume;
	private StringBuilder title;
	private StringBuilder year;
	private StringBuilder pageStr;
	private StringBuilder accessionNumber;
	private String startPage;
	private String endPage;
	private List<Author> authorList;
	private Author author;
	private StringBuilder fullName;
	private StringBuilder keywordsStr;
	private StringBuilder keywordName;
	private StringBuilder publicationAbstract;

	// List for holding Every Publication parsed from file.
	private List<PublicationBean> publicationCollection = new ArrayList<PublicationBean>();

	// List for holding Unique Publication parsed from file.
	private List<PublicationBean> uniquePubList = new ArrayList<PublicationBean>();

	// List for holding Duplicated Publication found in file.
	private List<PublicationBean> dupPubList = new ArrayList<PublicationBean>();

	// Set for checking duplication by PubMedId.
	private Set<String> pubMedSet = new HashSet<String>();

	// Set for checking duplication by DOI.
	private Set<String> doiSet = new HashSet<String>();

	// Map for checking duplication by Title + 1st Author.
	private Map<String, PublicationBean> titleMap = new HashMap<String, PublicationBean>();

	// Marker for ignoring data from file (retrieved from PubMed).
	private boolean hasPubMedData = false;

	private InputStream inputStream;
	private PrintStream log;
	private PrintStream allLog;
	private PrintStream dbLog;
	private PrintStream dupLog;
	private PrintStream savedLog;
	private PrintStream uniqueLog;

	private static final String[] visibilityGroups = { "Public" };

	public EndNoteXMLHandler(String inputFileName, boolean moreLog)
			throws FileNotFoundException {
		this.inputStream = new BufferedInputStream(new FileInputStream(
				inputFileName));

		int index = inputFileName.lastIndexOf('.');
		if (index == -1) {
			index = inputFileName.length();
		}
		String logFileName = inputFileName.substring(0, index);
		this.log = new PrintStream(logFileName + ".log");
		if (moreLog) {
			this.allLog = new PrintStream(new BufferedOutputStream(
					new FileOutputStream(logFileName + ".all")));
			this.dbLog = new PrintStream(new BufferedOutputStream(
					new FileOutputStream(logFileName + ".db")));
			this.dupLog = new PrintStream(new BufferedOutputStream(
					new FileOutputStream(logFileName + ".dup")));
			this.savedLog = new PrintStream(new BufferedOutputStream(
					new FileOutputStream(logFileName + ".saved")));
			this.uniqueLog = new PrintStream(new BufferedOutputStream(
					new FileOutputStream(logFileName + ".unique")));
		} else {
			allLog = dbLog = dupLog = savedLog = uniqueLog = log;
		}
	}

	public boolean parsePublicationXML(UserBean user) {
		boolean isSuccess = true;
		int count = 1;
		log.println("Start at: " + Calendar.getInstance().getTime());
		try {
			userName = user.getLoginName();
			// 1.Parse input stream.
			doParse(inputStream);
			log.println("==============================================");
			log.println("Total Publication parsed: "
					+ publicationCollection.size());
			log.println("==============================================");
			log.println("Duplicated Publication found in File: "
					+ dupPubList.size());
			log.println("==============================================");
			log.println("Unique Publication found in File: "
					+ uniquePubList.size());
			log.println("==============================================");

			// 2.Print every/all publication parsed before saving.
			if (!publicationCollection.isEmpty()) {
				allLog
						.println("==============================================");
				allLog.println("Total Publication parsed: "
						+ publicationCollection.size());
				for (PublicationBean pubBean : publicationCollection) {
					printLog(pubBean, allLog, count++);
				}
			}
			// 3.Print duplicated publication before saving.
			if (!dupPubList.isEmpty()) {
				count = 1;
				dupLog
						.println("==============================================");
				dupLog.println("Duplicated Publication found in File: "
						+ dupPubList.size());
				for (PublicationBean pubBean : dupPubList) {
					this.printLog(pubBean, dupLog, count++);
				}
				dupLog
						.println("==============================================");
			}
			// 4.Print unique/filtered publication before saving.
			if (!uniquePubList.isEmpty()) {
				count = 1;
				uniqueLog
						.println("==============================================");
				uniqueLog.println("Unique Publication found in File: "
						+ uniquePubList.size());
				for (PublicationBean pubBean : uniquePubList) {
					this.printLog(pubBean, uniqueLog, count++);
				}
				uniqueLog
						.println("==============================================");
			}
			log.println("Start saving publications.");
			log.println("==============================================");
			int pubUpdated = this.savePublications(user);
			isSuccess = pubUpdated > 0;
			log.println("Saving done, total Publication saved to database: "
					+ pubUpdated);
			if (!isSuccess) {
				log.println("ERROR: Please check log file.");
			}
			log.println("==============================================");
			log.println("End at: " + Calendar.getInstance().getTime());
		} catch (Exception ex) {
			isSuccess = false;
			log.println("Exception in parsePublicationXML, ");
			ex.printStackTrace(log);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (log != null) {
					log.flush();
					log.close();
				}
				if (allLog != null) {
					allLog.flush();
					allLog.close();
				}
				if (dbLog != null) {
					dbLog.flush();
					dbLog.close();
				}
				if (dupLog != null) {
					dupLog.flush();
					dupLog.close();
				}
				if (savedLog != null) {
					savedLog.flush();
					savedLog.close();
				}
				if (uniqueLog != null) {
					uniqueLog.flush();
					uniqueLog.close();
				}
			} catch (IOException e) {
			}
		}
		return isSuccess;
	}

	public int savePublications(UserBean user) {
		int count = 0, dupCount = 0;
		PublicationService service = new PublicationServiceLocalImpl(user);
		for (PublicationBean pubBean : uniquePubList) {
			Publication publication = (Publication) pubBean.getDomainFile();
			// exclude the ones that have <volume> as accepted
			if (publication.getStatus() == null) {
				publication.setStatus("published");
			}
			if (publication.getJournalName() != null
					&& publication.getJournalName().toLowerCase().contains(
							"proceedings")
					|| publication.getJournalName() != null
					&& publication.getJournalName().toLowerCase().contains(
							"proc ")) {
				publication.setCategory("proceeding");
			} else {
				publication.setCategory("peer review article");
			}

			Long pubMedId = publication.getPubMedId();
			String doi = publication.getDigitalObjectId();
			String title = publication.getTitle().toLowerCase();
			if (title.endsWith(".")) {
				title = title.substring(0, title.length() - 1);
			}
			String firstAuthorLastName = null;
			String firstAuthorFirstName = null;
			if (!pubBean.getAuthors().isEmpty()) {
				Author firstAuthor = pubBean.getAuthors().get(0);
				firstAuthorLastName = firstAuthor.getLastName();
				firstAuthorFirstName = firstAuthor.getFirstName();
			}
			try {
				// 1.check for duplicated publication in DB by PubMedId.
				if (pubMedId != null && pubMedId != 0) {
					// replace publicationBean with parsed XML record
					PublicationBean xmlPubBean = service
							.getPublicationFromPubMedXML(pubMedId.toString());
					pubBean.copyPubMedFieldsFromPubMedXML(xmlPubBean);
					PublicationBean pBean = service.findPublicationByKey(
							"pubMedId", pubMedId, false);
					if (pBean != null) {
						Publication pub = (Publication) pBean.getDomainFile();
						if (pub != null && pub.getId() != null
								&& pub.getId() != 0) {
							log.println("Found duplicated PubMedId in DB: "
									+ pubMedId);
							dbLog
									.println("==============================================");
							dbLog.println("Found duplicated PubMedId in DB: "
									+ pubMedId);
							this.printLog(pub, dbLog, ++dupCount, true);
							// copy non PubMed fields that are already curated
							// in the database.
							pubBean.copyNonPubMedFieldsFromDatabase(pBean);
						}
					}
				}
				// 2.check for duplicated publication in DB by DOI.
				else if (!StringUtils.isEmpty(doi)) {
					PublicationBean pBean = service.findPublicationByKey(
							"digitalObjectId", doi, false);
					if (pBean != null) {
						Publication pub = (Publication) pBean.getDomainFile();
						if (pub != null && pub.getId() != null
								&& pub.getId() != 0) {
							log.println("Found duplicated DOI in DB: " + doi);
							dbLog
									.println("==============================================");
							dbLog.println("Found duplicated DOI in DB: " + doi);
							this.printLog(pub, dbLog, ++dupCount, true);

							// copy all fields already curated in the database
							pubBean.copyFromDatabase(pBean);
						}
					}
				} else {
					// 3.check for duplicated publication in DB by Title +
					// 1st
					// Author.
					PublicationBean pBean = service
							.findNonPubMedNonDOIPublication(publication
									.getCategory(), title, firstAuthorLastName,
									firstAuthorFirstName);
					// found a match, copy all fields already curated in the
					// database
					if (pBean != null) {
						pubBean.copyFromDatabase(pBean);
					}
				}
				// 4.setup publication object inside publicationBean & save.
				pubBean.setupDomain(Constants.FOLDER_PUBLICATION, user
						.getLoginName());
				System.out.println(">>>>>>" + count);
				service.savePublication(pubBean);

				// assign publication to public
				service.assignAccessibility(AccessibilityBean.CSM_PUBLIC_ACCESS,
						publication);
				this.printLog(pubBean, savedLog, count++);
			} catch (Exception ex) {
				log.println("Exception thrown in saving Publication:");
				this.printLog(pubBean, log, -2);
				ex.printStackTrace(log);
				System.out.println(">>>>>: " + ex);
			}
		}
		return count;
	}

	private class RecordHandler extends SAXElementHandler {
		public void startElement(String uri, String localName, String qname,
				Attributes atts) {
			publicationBean = new PublicationBean();
			try {
				publicationBean.setupDomainFile(Constants.FOLDER_PUBLICATION,
						userName);
			} catch (Exception ex) {
				log.println("Exception in publicationBean.setupDomainFile():");
				ex.printStackTrace(log);
			}
			publication = (Publication) publicationBean.getDomainFile();
			publicationCollection.add(publicationBean);

			// True: data retrieved from PubMed, False: data parsed from file.
			hasPubMedData = false;
		}

		public void endElement(String uri, String localName, String qname) {
			boolean duplicatedPub = false;
			// 1.filter duplicated publication by PubMedId.
			Long pubMedId = publication.getPubMedId();
			if (pubMedId != null && pubMedId != 0) {
				if (pubMedSet.contains(pubMedId)) {
					log.println("PubMedId is duplicated: " + pmid);
					duplicatedPub = true;
				} else {
					pubMedSet.add(pubMedId.toString());
				}
			} else {
				String doi = publication.getDigitalObjectId();
				// 2.filter duplicated publication by DOI.
				if (!StringUtils.isEmpty(doi)) {
					if (doiSet.contains(doi)) {
						log.println("DOI is duplicated: " + doi);
						duplicatedPub = true;
					} else {
						doiSet.add(doi);
					}
				} else {
					// 3.filter duplicated publication by Title + 1st Author's
					// last
					// name.
					String title = publication.getTitle().toLowerCase();
					if (title.endsWith(".")) {
						title = title.substring(0, title.length() - 1);
					}
					if (!StringUtils.isEmpty(title)) {
						if (titleMap.containsKey(title)) {
							log.println("Title is duplicated: " + title);

							PublicationBean pubBean = titleMap.get(title);
							List<Author> oldAuthors = pubBean.getAuthors();
							List<Author> newAuthors = publicationBean
									.getAuthors();
							if (oldAuthors != null && !oldAuthors.isEmpty()
									&& newAuthors != null
									&& !newAuthors.isEmpty()) {
								Author oldAuthor = oldAuthors.get(0);
								Author newAuthor = newAuthors.get(0);
								String oldName = oldAuthor.getLastName();
								String newName = newAuthor.getLastName();
								// Duplicated if both 1st Author's last name are
								// the
								// same.
								if (oldName != null
										&& oldName.equalsIgnoreCase(newName)) {
									// Don't replace old pubBean if new pubMedId
									// is
									// empty.
									Publication pub = (Publication) publicationBean
											.getDomainFile();
									if (pub.getPubMedId() == null
											|| pub.getPubMedId() == 0) {
										log.println("Title (" + title
												+ ") + 1st Author (" + oldName
												+ ") is duplicated: ");
										duplicatedPub = true;
									}
								} else {
									log
											.println("1st Author not match, oldName="
													+ oldName
													+ ", newName="
													+ newName);
								}
							} else {
								log
										.println("Author list not match, oldAuthors.size()="
												+ oldAuthors.size()
												+ ", newAuthors.size()="
												+ newAuthors.size());
							}
						} else {
							titleMap.put(title, publicationBean);
						}
					}
				}
			}
			if (duplicatedPub) {
				dupPubList.add(publicationBean);
			} else {
				uniquePubList.add(publicationBean);
			}
		}
	}

	private class TitleHandler extends SAXElementHandler {
		public void startElement(String uri, String localName, String qname,
				Attributes atts) {
			title = new StringBuilder();
		}

		public void characters(char[] ch, int start, int length) {
			title.append(new String(ch, start, length));
		}

		public void endElement(String uri, String localName, String qname) {
			if (!hasPubMedData) {
				publication.setTitle(title.toString());
			}
		}
	}

	private class UrlHandler extends SAXElementHandler {
		public void startElement(String uri, String localName, String qname,
				Attributes atts) {
			url = new StringBuilder();
		}

		public void characters(char[] ch, int start, int length) {
			url.append(new String(ch, start, length));
		}

		public void endElement(String uri, String localName, String qname) {
			if (!StringUtils.isEmpty(url.toString())
					&& url.indexOf("http:") != -1) {
				publication.setUriExternal(true);
				publicationBean.setExternalUrl(url.toString());
				int index = url.indexOf("list_uids=");
				if (index != -1) {
					index += 10; // length of "list_uids=" is 10
					if (url.indexOf("db=PubMed") != -1) {
						pmid = url.substring(index).trim();
						try {
							Long pubMedId = Long.valueOf(pmid);
							// // Call PubMed for accurate data.
							// PublicationBean newPubBean = new
							// PublicationBean();
							// PubMedXMLHandler phandler = PubMedXMLHandler
							// .getInstance();
							// for (; !hasPubMedData;) {
							// hasPubMedData = phandler.parsePubMedXML(
							// pubMedId, newPubBean);
							// if (hasPubMedData) {
							// publicationBean.copyFromPubMed(newPubBean);
							// } else {
							// log
							// .println("Warning: error retrieving PubMed data: "
							// + pmid);
							// // Pause 1 second then call PubMed again.
							// Thread.sleep(1000);
							// }
							// }
							// only reset PubMed ID is it's not alreay set by
							// <accession-num> tag
							if (publication.getPubMedId() == null) {
								publication.setPubMedId(pubMedId);
							}
						} catch (Exception ex) {
							log.println("Invalid PubMed ID: " + pmid);
							printLog(publication, log, -4);
						}
					}
				} else {
					index = url.indexOf("doilookup?in_doi=");
					if (index != -1) {
						doi = new StringBuilder(url.substring(index + 17)
								.trim());
						publication.setDigitalObjectId(doi.toString());
					} else {
						/**
						 * All DOI names start with "10.", see DOI specification
						 * below,
						 * http://www.doi.org/handbook_2000/enumeration.html#2.2
						 */
						index = url.indexOf("dx.doi.org/");
						if (index != -1) {
							index = url.indexOf("10.", index + 11);
							if (index != -1) {
								doi = new StringBuilder(url.substring(index)
										.trim());
								publication.setDigitalObjectId(doi.toString());
							}
						}
						// set all other http based uri
						else {
							if (publication.getUri() == null) {
								publication.setUri(url.toString());
								publication.setUriExternal(true);
							}
						}
					}
				}
			}
			// check for ISI accession number
			else if (!StringUtils.isEmpty(url.toString())) {
				int index = url.indexOf("<Go to ISI>://");
				if (index != -1) {
					int index2 = url.indexOf("//");
					String isiLink = Constants.ISI_PREFIX
							+ url.substring(index2 + 2);
					publication.setUri(isiLink);
					publication.setUriExternal(true);
				}
				// set all other uri
				else {
					publication.setUri(url.toString());
					publication.setUriExternal(true);
				}
			}
		}
	}

	private class DOIHandler extends SAXElementHandler {
		public void startElement(String uri, String localName, String qname,
				Attributes atts) {
			doi = new StringBuilder();
		}

		public void characters(char[] ch, int start, int length) {
			doi.append(new String(ch, start, length));
		}

		public void endElement(String uri, String localName, String qname) {
			if (!hasPubMedData && doi != null) {
				String doiStr = doi.toString().trim();
				/**
				 * All DOI names start with "10.", see DOI specification below,
				 * http://www.doi.org/handbook_2000/enumeration.html#2.2
				 */
				int start = doiStr.indexOf("10.");
				if (start != -1) {
					doiStr = doiStr.substring(start);
					int end = doiStr.indexOf("[doi]");
					if (end != -1) {
						doiStr = doiStr.substring(0, end);
					}
					publication.setDigitalObjectId(doiStr.trim());
				} else {
					log.println("Invalid DOI: " + doiStr);
				}
			}
		}
	}

	private class VolumeHandler extends SAXElementHandler {
		public void startElement(String uri, String localName, String qname,
				Attributes atts) {
			volume = new StringBuilder();
		}

		public void characters(char[] ch, int start, int length) {
			volume.append(new String(ch, start, length));
		}

		public void endElement(String uri, String localName, String qname) {
			if (!hasPubMedData) {
				if (volume.toString().toLowerCase().contains("accepted")
						|| volume.toString().toLowerCase().contains("in press")) {
					publication.setStatus("in press");
				}
				publication.setVolume(volume.toString());
			}
		}
	}

	private class JournalNameHandler extends SAXElementHandler {
		public void startElement(String uri, String localName, String qname,
				Attributes atts) {
			journal = new StringBuilder();
		}

		public void characters(char[] ch, int start, int length) {
			journal.append(new String(ch, start, length));
		}

		public void endElement(String uri, String localName, String qname) {
			if (!hasPubMedData) {
				publication.setJournalName(journal.toString());
			}
		}
	}

	private class YearHandler extends SAXElementHandler {
		public void startElement(String uri, String localName, String qname,
				Attributes atts) {
			year = new StringBuilder();
		}

		public void characters(char[] ch, int start, int length) {
			year.append(new String(ch, start, length));
		}

		public void endElement(String uri, String localName, String qname) {
			if (!hasPubMedData) {
				try {
					publication.setYear(Integer.valueOf(year.toString()));
				} catch (NumberFormatException ex) {
					log.println("Invalid Year string: " + year.toString());
				}
			}
		}
	}

	private class AccessionNumberHandler extends SAXElementHandler {
		public void startElement(String uri, String localName, String qname,
				Attributes atts) {
			accessionNumber = new StringBuilder();
		}

		public void characters(char[] ch, int start, int length) {
			accessionNumber.append(new String(ch, start, length));
		}

		public void endElement(String uri, String localName, String qname) {
			int index = accessionNumber.toString().indexOf("ISI:");
			if (index == -1) {
				try {
					publication
							.setPubMedId(new Long(accessionNumber.toString()));
					hasPubMedData = true;
				} catch (NumberFormatException ex) {
					log.println("Invalid PubMed ID: "
							+ accessionNumber.toString());
				}
			} else {
				int index2 = accessionNumber.toString().indexOf(":");
				String isiNumber = accessionNumber.substring(index2 + 1);
				publication.setUri(Constants.ISI_PREFIX + isiNumber);
				publication.setUriExternal(true);
			}
		}
	}

	private class AbstractHandler extends SAXElementHandler {
		public void startElement(String uri, String localName, String qname,
				Attributes atts) {
			publicationAbstract = new StringBuilder();
		}

		public void characters(char[] ch, int start, int length) {
			publicationAbstract.append(new String(ch, start, length));
		}

		public void endElement(String uri, String localName, String qname) {
			publication.setDescription(publicationAbstract.toString());
		}
	}

	private class PageHandler extends SAXElementHandler {
		public void startElement(String uri, String localName, String qname,
				Attributes atts) {
			pageStr = new StringBuilder();
		}

		public void characters(char[] ch, int start, int length) {
			pageStr.append(new String(ch, start, length));
		}

		public void endElement(String uri, String localName, String qname) {
			if (!hasPubMedData && pageStr.toString().trim().length() > 0) {
				if (pageStr.toString().endsWith(("-+"))) {
					startPage = pageStr.toString();
					endPage = "";
				} else {
					String[] pages = pageStr.toString().split("-");
					if (pages != null && pages.length > 0) {
						startPage = pages[0].trim(); // May contain space(s).
						if (pages.length == 2) {
							endPage = pages[1].trim(); // May contain space(s).
							int endPagePrefixLength = startPage.length()
									- endPage.length();
							if (endPagePrefixLength > 0) {
								String endPagePrefix = startPage.substring(0,
										endPagePrefixLength);
								endPage = endPagePrefix + endPage;
							}
						} else {
							endPage = "";
						}
						publication.setStartPage(startPage);
						publication.setEndPage(endPage);
						// try {
						// publication.setStartPage(Integer.valueOf(startPage)
						// .toString());
						// publication.setEndPage(Integer.valueOf(endPage)
						// .toString());
						// } catch (NumberFormatException ex) {
						// log
						// .println("Warning: Abnormal Page string: "
						// + pageStr);
						// publication.setStartPage(pageStr.toString().trim());
						// publication.setEndPage(null);
						// }
					}
				}
			}
		}
	}

	private class AuthorListHandler extends SAXElementHandler {
		public void startElement(String uri, String localName, String qname,
				Attributes atts) {
			authorList = new ArrayList<Author>();
		}

		public void endElement(String uri, String localName, String qname) {
			publicationBean.setAuthors(authorList);
		}
	}

	private class AuthorHandler extends SAXElementHandler {
		public void startElement(String uri, String localName, String qname,
				Attributes atts) {
			author = new Author();
			fullName = new StringBuilder();
		}

		public void characters(char[] ch, int start, int length) {
			fullName.append(new String(ch, start, length));
		}

		public void endElement(String uri, String localName, String qname) {
			String name = fullName.toString().trim();
			if (!StringUtils.isEmpty(name) && !name.equalsIgnoreCase("et al.")) {
				/**
				 * Expect following 5 formats of name (Initial is 1 char): 1.
				 * lastName, Initial(s). (Gratton, S. E.) 2. lastName, firstName
				 * Initial(s). (Hill, Haley D.) 3. lastName, firstName (Mrksich,
				 * Milan) 4. firstName lastName (Bifeng Pan) 5. Initial(s).
				 * lastName (A. K. M. Newaz) Everything else will be set as
				 * lastName.
				 */
				int index = name.indexOf(",");
				if (index > 0) {
					// 1. lastName, Initial(s). (Gratton, S. E.)
					// 2. lastName, firstName Initial(s). (Hill, Haley D.)
					// 3. lastName, firstName (Mrksich, Milan)
					author.setLastName(name.substring(0, index));
					name = name.substring(index + 1).trim();
					String[] names = name.split("[\\. \\,]|Jr\\.|Sr\\.");
					StringBuilder initial = new StringBuilder(name.length());
					StringBuilder first = new StringBuilder(name.length());
					for (int i = 0; i < names.length; i++) {
						String str = names[i].trim();
						if (str.length() > 1) {
							first.append(str).append(' ');
						} else {
							initial.append(str); // Assume 1-char Initial.
						}
					}
					author.setFirstName(first.toString().trim());
					if (!StringUtils.isEmpty(initial.toString())) {
						author.setInitial(initial.toString());
					}
				} else {
					String[] names = name.split("[\\. ]");
					if (names.length <= 1) {
						// Format not recognized, set whole string as lastName.
						author.setLastName(name);
					} else {
						// 4. firstName lastName (Bifeng Pan)
						// 5. Initial(s). lastName (A. K. M. Newaz)
						author.setLastName(names[names.length - 1]);
						StringBuilder initial = new StringBuilder(name.length());
						StringBuilder first = new StringBuilder(name.length());
						for (int i = 0; i < names.length - 1; i++) {
							String str = names[i].trim();
							if (str.length() > 1) {
								first.append(str);
							} else {
								initial.append(str); // Assume Initial is
								// 1-char.
							}
						}
						author.setFirstName(first.toString());
						if (!StringUtils.isEmpty(initial.toString())) {
							author.setInitial(initial.toString());
						}
					}
				}
				if (author.getFirstName() == null) {
					author.setFirstName(""); // firstName can't be null in DB.
				}
				author.setCreatedBy(userName);
				authorList.add(author);
			}
		}
	}

	private class KeywordCollectionHandler extends SAXElementHandler {
		public void startElement(String uri, String localName, String qname,
				Attributes atts) {
			keywordsStr = new StringBuilder();
		}

		public void endElement(String uri, String localName, String qname) {
			publicationBean.setKeywordsStr(keywordsStr.toString());
		}
	}

	private class KeywordHandler extends SAXElementHandler {
		public void startElement(String uri, String localName, String qname,
				Attributes atts) {
			keywordName = new StringBuilder();
		}

		public void characters(char[] ch, int start, int length) {
			keywordName.append(new String(ch, start, length));
		}

		public void endElement(String uri, String localName, String qname) {
			keywordsStr.append(keywordName.toString());
			keywordsStr.append("\r\n");
		}
	}

	public void doParse(InputStream xmlinput) throws Exception {
		SAXEventSwitcher s = new SAXEventSwitcher();
		s.setElementHandler("record", new RecordHandler());
		s.setElementHandler("title", new TitleHandler());
		// PUBMED enclosed in <accessionNumber>
		s.setElementHandler("accession-num", new AccessionNumberHandler());
		// PUBMED & DOI
		s.setElementHandler("url", new UrlHandler());
		// DOI (doi may be parsed in url)
		if (doi == null) {
			s.setElementHandler("electronic-resource-num", new DOIHandler());
		}
		s.setElementHandler("volume", new VolumeHandler());
		s.setElementHandler("year", new YearHandler());
		s.setElementHandler("full-title", new JournalNameHandler());
		if (journal == null) {
			s.setElementHandler("alt-title", new JournalNameHandler());
		}
		if (journal == null) {
			s.setElementHandler("secondary-title", new JournalNameHandler());
		}
		s.setElementHandler("pages", new PageHandler());
		s.setElementHandler("authors", new AuthorListHandler());
		s.setElementHandler("author", new AuthorHandler());
		s.setElementHandler("keywords", new KeywordCollectionHandler());
		s.setElementHandler("keyword", new KeywordHandler());
		s.setElementHandler("abstract", new AbstractHandler());

		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sparser = spf.newSAXParser();
		sparser.parse(xmlinput, s);
	}

	private void printLog(PublicationBean pubBean, PrintStream log, int count) {
		Publication publication = (Publication) pubBean.getDomainFile();
		this.printLog(publication, log, count);
	}

	private void printLog(Publication publication, PrintStream log, int count) {
		this.printLog(publication, log, count, false);
	}

	private void printLog(Publication publication, PrintStream log, int count,
			boolean simplified) {
		log.println("==================================== [" + count
				+ "] =====");
		log.println("publication id: " + publication.getId());
		log.println("title: " + publication.getTitle());
		if (publication.getTitle() == null) {
			log.println(count + "WARNING: title==null");
		}
		log.println("url: " + publication.getUri());
		log.println("pubmed id: " + publication.getPubMedId());
		log.println("doi: " + publication.getDigitalObjectId());
		log.println("journal: " + publication.getJournalName());
		log.println("year: " + publication.getYear());
		log.println("createdBy: " + publication.getCreatedBy());
		log.println("createdDate: " + publication.getCreatedDate());
		log.println("start page: " + publication.getStartPage());
		log.println("end page: " + publication.getEndPage());
		log.println("volume: " + publication.getVolume());
		log.println("Abstract: " + publication.getDescription());
		// if (!simplified && publication.getAuthorCollection() != null) {
		// List<Author> authorslist = new ArrayList<Author>(publication
		// .getAuthorCollection());
		// Collections.sort(authorslist, new Comparator<Author>() {
		// public int compare(Author o1, Author o2) {
		// return (int) (o1.getCreatedDate().compareTo(o2
		// .getCreatedDate()));
		// }
		// });
		// for (Author author : authorslist) {
		// log.println("       author: " + author.getLastName() + ","
		// + author.getFirstName() + "(" + author.getInitial()
		// + ")");
		// }
		// }
		// if (!simplified && publication.getKeywordCollection() != null) {
		// for (Keyword keyword : publication.getKeywordCollection()) {
		// log.println("           keyword: " + keyword.getName());
		// }
		// }
		if (!simplified && publicationBean.getAuthors() != null) {
			for (Author author : publicationBean.getAuthors()) {
				log.println("       author: " + author.getLastName() + ","
						+ author.getFirstName() + "(" + author.getInitial()
						+ ")");
			}
		}
		if (!simplified && publicationBean.getKeywordsStr() != null) {
			log.println("           keyword: "
					+ publicationBean.getKeywordsStr());
		}
	}

	public static void main(String[] args) {
		boolean isSuccess = false;
		if (args != null && args.length >= 3) {
			String userLoginName = args[0];
			String userPassword = args[1];
			String inputFileName = args[2];
			boolean moreLog = false;
			try {
				moreLog = Boolean.valueOf(args[3]);
			} catch (Exception e) {
			}
			try {
				UserBean user = new UserBean(userLoginName, userPassword);
				EndNoteXMLHandler endNotehandler = new EndNoteXMLHandler(
						inputFileName, moreLog);
				isSuccess = endNotehandler.parsePublicationXML(user);
			} catch (FileNotFoundException e) {
				System.out.println("Input file not found.");
				e.printStackTrace(System.out);
			} catch (Exception e) {
				System.out.println("Can't log in the user: " + userLoginName
						+ ", with password: " + userPassword);
				e.printStackTrace(System.out);
			}
		} else {
			System.out.println("Invalid argument!");
			System.out
					.println("java EndnoteParser <account> <password> <inputFileName> [true/false]");
		}
		System.exit(isSuccess ? 0 : 1);
	}
}
