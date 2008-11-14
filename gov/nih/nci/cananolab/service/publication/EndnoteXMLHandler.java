/**
 * 
 */
package gov.nih.nci.cananolab.service.publication;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.SAXElementHandler;
import gov.nih.nci.cananolab.util.SAXEventSwitcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;

public class EndnoteXMLHandler {
	private List<PublicationBean> publicationCollection = new ArrayList<PublicationBean>();
	private PublicationBean publicationBean = null;
	private Publication publication = null;
	private static EndnoteXMLHandler onlyInstance = null;
	private StringBuffer url;
	private String pmid;
    private StringBuffer journal;
    private StringBuffer volume;
    private StringBuffer title;
    private StringBuffer year;
    private StringBuffer pageStr;
    private String startPage;
    private String endPage;
    private List<Author> authorList = null;
    private Author author = null;
    private StringBuffer fullName;
    private StringBuffer doi;
    private Set<Keyword> keywordCollection = null;
    private Keyword keyword = null;
    private StringBuffer keywordName;
    private StringBuffer publicationAbstract;
    final String[] visibilityGroups = {"Public"};    
    
    
    public static synchronized EndnoteXMLHandler getInstance() {
        if (onlyInstance == null)
            onlyInstance = new EndnoteXMLHandler();
        
        return onlyInstance;
    }
    
    private EndnoteXMLHandler(){}
	
    public PublicationBean parsePublicationXML (String inputFileName) {			
        try {      
        	File dFile = new File(inputFileName);
			InputStream istream = new FileInputStream(dFile);			
        	doParse(istream); 
        	istream.close();
        	savePublication();
        } catch(Exception ex) {
        	System.out.println("exception in parsePubMedXML, ");
        	ex.printStackTrace();
        }	        
        return publicationBean;
    }

	public boolean savePublication () {
		boolean isSuccess = true;
        try { 
        	PublicationService service = new PublicationServiceLocalImpl();    		
    		AuthorizationService authService = new AuthorizationService(
    				CaNanoLabConstants.CSM_APP_NAME);    		
        	int count = 0;        	
			for (PublicationBean pubBean : publicationCollection) {					
				System.out.println("====================== "+(++count)+"==============================");
				Publication publication = (Publication) pubBean.getDomainFile();
				publication.setStatus("published");
				publication.setCategory("peer review article");
				pubBean.setVisibilityGroups(visibilityGroups);	
				System.out.println("title:" + publication.getTitle());
				//TODO: verify if the publication in DB
				service.savePublication(publication, pubBean
	    				.getParticleNames(), pubBean.getNewFileData(), 
	    				pubBean.getAuthors());
	    		// set visibility
				authService.assignVisibility(pubBean.getDomainFile().getId()
	    				.toString(), pubBean.getVisibilityGroups());

	    		// set author visibility
				if (publication.getAuthorCollection()!=null) {
					for (Author author: publication.getAuthorCollection()) {
						if (author!=null) {
							if (author.getId()!=null && 
									author.getId().toString().trim().length()>0) {
								authService.assignPublicVisibility(author.getId().toString());
							}
						}
					}
				}
				System.out.println(count+": " + publication.getId());
			}
        } catch(Exception ex) {
        	isSuccess = false;
        	System.out.println("exception in savePublication, ");
        	ex.printStackTrace();
        }        
        return isSuccess;
	}
	
	
	private class RecordHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {			
			publicationBean = new PublicationBean();
			try {
				publicationBean.setupDomainFile(CaNanoLabConstants.FOLDER_PUBLICATION, 
					"DATA_PARSING", 0);
			}catch (Exception ex) {
				System.out.println("exception publicationBean.setupDomainFile");
				ex.printStackTrace();
			}
			publication = (Publication) publicationBean.getDomainFile();
			publicationBean.setDomainFile(publication);
		}		
		public void endElement(String uri, String localName, String qname) {
			publicationCollection.add(publicationBean);
		}
	}		
	
	private class TitleHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			title = new StringBuffer();			
		}
		
		public void characters(char[] ch, int start, int length) {
			title.append(new String(ch, start, length));
		}
		public void endElement(String uri, String localName, String qname) {
			publication.setTitle(title.toString());
		}
	}
	
	private class UrlHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			url = new StringBuffer();			
		}
		
		public void characters(char[] ch, int start, int length) {
			url.append(new String(ch, start, length));
		}
		public void endElement(String uri, String localName, String qname) {
			if (url!=null && url.length()>0 && url.indexOf("http:")!=-1) {
				publication.setUriExternal(true);
				publication.setUri(url.toString());
				int index = url.toString().indexOf("list_uids=",0);
				if (index!=-1) {
					index+=10; //length of "list_uids=" is 10
					if (url.indexOf("db=PubMed")!=-1) {
						pmid = url.substring(index).trim();
						publication.setPubMedId(new Long(pmid));
					}
				} else {
					index = url.toString().indexOf("doilookup?in_doi=", 0);
					if (index!=-1) {
						doi = new StringBuffer(url.substring(index + 17).trim());
						publication.setDigitalObjectId(doi.toString());
					}
				}
			}
			
		}
	}
	
	private class DOIHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			doi = new StringBuffer();			
		}
		
		public void characters(char[] ch, int start, int length) {
			doi.append(new String(ch, start, length));
		}
		public void endElement(String uri, String localName, String qname) {
			if (doi!=null) {
				if (doi.toString().indexOf("pii")!=-1) {
					try {
						String[] articleIds = doi.toString().split("\r\n|\r|\n");
						if (articleIds.length==2) {
							if (articleIds[0].indexOf("pii")==-1) {//DOI
								publication.setDigitalObjectId(articleIds[0]);
							}else if (articleIds[1].indexOf("pii")==-1) {//DOI
								publication.setDigitalObjectId(articleIds[1]);
							}
						}
					}catch (Exception ex) {
						System.out.println("Invalid DOI: "+doi);
					}
				}else {
					publication.setDigitalObjectId(doi.toString().trim());
				}
			}			
		}
	}
	

	
	private class VolumeHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			volume = new StringBuffer();
		}
		
		public void characters(char[] ch, int start, int length) {
			volume.append(new String(ch, start, length));
		}
		
		public void endElement(String uri, String localName, String qname) {
			publication.setVolume(volume.toString());
		}
	}
	
	private class JournalNameHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			journal = new StringBuffer();
		}
		
		public void characters(char[] ch, int start, int length) {
			journal.append(new String(ch, start, length));
		}
		
		public void endElement(String uri, String localName, String qname) {
			publication.setJournalName(journal.toString());
		}
	}		
	
	private class YearHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			year = new StringBuffer();
		}
		
		public void characters(char[] ch, int start, int length) {
			year.append(new String(ch, start, length));
		}
		
		public void endElement(String uri, String localName, String qname) {
			try {
				publication.setYear(Integer.parseInt(year.toString()));
			}catch (Exception ex) {
				//if year is not integer, ignore
			}
		}
	}
	
	private class AbstractHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			publicationAbstract = new StringBuffer();
		}
		
		public void characters(char[] ch, int start, int length) {
			publicationAbstract.append(new String(ch, start, length));
		}
		
		public void endElement(String uri, String localName, String qname) {
			publication.setDescription(publicationAbstract.toString());
		}
	}
	
	private class PageHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			pageStr = new StringBuffer();
		}
		
		public void characters(char[] ch, int start, int length) {
			pageStr.append(new String(ch, start, length));
		}
		
		public void endElement(String uri, String localName, String qname) {
			if (pageStr.toString().trim().length() > 0) {
				String[] pages = pageStr.toString().split("-");
				if (pages!=null && pages.length>0) {
					try {
						startPage = pages[0];
						if (pages.length == 2) {
							int endPagePrefixLength = pages[0].length()
									- pages[1].length();
							if (endPagePrefixLength > 0) {
								String endPagePrefix = pages[0].substring(0,
										endPagePrefixLength);
								endPage = endPagePrefix + pages[1];
							} else {
								endPage = pages[1];
							}
						} else {
							endPage = startPage;
						}
						publication.setStartPage(startPage);
						publication.setEndPage(endPage);
					} catch (NumberFormatException nfe) {
						System.out.println("publication page number format exception:"
										+ pageStr.toString());
					}
				}

			}
		}
	}
	
	private class AuthorListHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			authorList = new ArrayList<Author>();
		}
		
		public void endElement(String uri, String localName, String qname) {
			
			if (authorList!=null && authorList.size()>0) {
				if (publication.getAuthorCollection()==null) {
					publication.setAuthorCollection(new HashSet<Author>());
				}
				Calendar myCal = Calendar.getInstance();
				for (Author author : authorList) {						
					myCal.add(Calendar.SECOND, 1);
					author.setCreatedDate(myCal.getTime());
					publication.getAuthorCollection().add(author);	
				}
			}
		}
	}
	
	private class AuthorHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			author = new Author();
			fullName = new StringBuffer();
		}
		
		public void characters(char[] ch, int start, int length) {
			fullName.append(new String(ch, start, length));
		}
		
		public void endElement(String uri, String localName, String qname) {
			if (fullName!=null && !fullName.toString().trim().equalsIgnoreCase("et al.")) {
				if (fullName.indexOf(",")>0) {
					author.setLastName(fullName.toString().substring(0, fullName.indexOf(",")).trim());
					String initial = fullName.toString().substring(fullName.indexOf(",")+1).trim();					
					if (initial.indexOf(" ")>0) {						
						String[] inits = initial.split(" ");
						if (inits!=null && inits.length>0) {
							if (inits[0].indexOf(".")==-1) {
								//firstName
								author.setFirstName(inits[0].trim());
								if (initial.indexOf(" ")!=-1) {
									author.setInitial(initial.replace(inits[0], "").trim());
								}
							}else {
								author.setInitial(initial.trim());
							}
						}else {
							author.setInitial(initial.trim());
						}
					}else {
						if (initial.indexOf(".")>0 || initial.length()==1) {
							author.setInitial(initial.trim());
						}else {
							author.setFirstName(initial.trim());
						}
					}	
				}else {
					author.setLastName(fullName.toString().trim());
				}
				if (author.getFirstName()==null) {
					author.setFirstName(""); //firstName cannot be null in db
				}
				author.setCreatedBy("DATA_PARSING");
				authorList.add(author);
			}
		}
	}
	
	
	private class KeywordCollectionHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			keywordCollection = new HashSet<Keyword>();
		}
		
		public void endElement(String uri, String localName, String qname) {
			//publicationBean.setAuthors(authorList);
			publication.setKeywordCollection(keywordCollection);
		}
	}
	
	private class KeywordHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			keyword = new Keyword();
			keywordName = new StringBuffer();
		}
		
		public void characters(char[] ch, int start, int length) {
			keywordName.append(new String(ch, start, length));
		}
		
		public void endElement(String uri, String localName, String qname) {
			keyword.setName(keywordName.toString());
			keywordCollection.add(keyword);
		}
	}
	
	public void doParse(InputStream xmlinput) throws Exception
	{
		SAXEventSwitcher s = new SAXEventSwitcher();
		s.setElementHandler("record", new RecordHandler());
		s.setElementHandler("title", new TitleHandler());
		//PUBMED & DOI
		s.setElementHandler("url", new UrlHandler());
		//DOI (doi may be parsed in url)
		if (doi==null) {
			s.setElementHandler("electronic-resource-num", new DOIHandler());
		}
		s.setElementHandler("volume", new VolumeHandler());
		s.setElementHandler("year", new YearHandler());
		s.setElementHandler("full-title", new JournalNameHandler());
		if (journal==null) {
			s.setElementHandler("alt-title", new JournalNameHandler());
		}
		if (journal==null) {
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

	/**
	 * @return the publicationCollection
	 */
	public List<PublicationBean> getPublicationCollection() {
		return publicationCollection;
	}

	/**
	 * @param publicationCollection the publicationCollection to set
	 */
	public void setPublicationCollection(List<PublicationBean> publicationCollection) {
		this.publicationCollection = publicationCollection;
	}
}
