/**
 * 
 */
package test;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.service.publication.EndnoteXMLHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.List;

/**
 * @author tanq
 * 
 */
public class EndnoteParser {
	static boolean DEBUG = true;
	
	public static void main(String[] args) {
		// String inputFileName =
		// "C:\\project\\java\\workspace\\caNanoLab\\dev\\pub.xml";
//		 String inputFileName =
//			 "C:\\project\\java\\workspace\\caNanoLab\\dev\\publications2.xml";
		// String inputFileName = "C:\\temp\\publication\\pub.xml";
		
		// contains style
		String inputFileName = "C:\\temp\\publication\\AllianceNoStyle.xml";
		// TODO: verify if <style> tag is in the inputFile
		if (DEBUG) {
			args = new String[1];
			args[0] = inputFileName;
		}
		if (args != null && args.length == 1) {
			inputFileName = args[0];
		} else {
			System.out.println("Invalid argument!");
			System.out.println("java EndnoteParser <inputFileName>");
			return;
		}
		EndnoteXMLHandler endNotehandler = EndnoteXMLHandler.getInstance();
		try {			
			endNotehandler.parsePublicationXML(inputFileName);
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
		if (DEBUG) {
			// THE FOLLOWING IS FOR DEBUG ONLY
			List<PublicationBean> publicationCollection = endNotehandler
					.getPublicationCollection();
			System.out.println("size:" + publicationCollection.size());
			FileOutputStream out; // declare a file output object
			PrintStream p; // declare a print stream object
			try {
				out = new FileOutputStream(
						"C:\\temp\\publication\\AllianceOutput.txt");
				p = new PrintStream(out);
				p.print("size:" + publicationCollection.size() + "\n");
				int count = 0;
				for (PublicationBean pubBean : publicationCollection) {
					p.println("====================== " + (++count)
							+ "==============================");
					Publication publication = (Publication) pubBean
							.getDomainFile();
					p.println("title:" + publication.getTitle());
					if (publication.getTitle() == null) {
						System.out.println(count + " title==null");
					}
					p.println("url:" + publication.getUri());
					p.println("pubmed id:" + publication.getPubMedId());
					p.println("doi:" + publication.getDigitalObjectId());
					p.println("journal:" + publication.getJournalName());
					p.println("year:" + publication.getYear());
					p.println("createdBy:" + publication.getCreatedBy());
					p.println("createdDate:" + publication.getCreatedDate());
					// if (publication.getYear()==null) {
					// System.out.println(count+" year ==null");
					// }
					p.println("start page:" + publication.getStartPage());
					p.println("end page:" + publication.getEndPage());
					p.println("volume:" + publication.getVolume());
					p.println("Abstract:" + publication.getDescription());
					// if (publication.getDescription()==null) {
					// System.out.println(count+" description ==null\n
					// "+publication.getTitle());
					// }
					if (publication.getAuthorCollection() != null) {
						for (Author author : publication.getAuthorCollection()) {
							p.println("       author:" + author.getLastName()
									+ "," + author.getFirstName() + "("
									+ author.getInitial() + ")");
						}
					}
					if (publication.getKeywordCollection() != null) {
						for (Keyword keyword : publication
								.getKeywordCollection()) {
							p.println("           keyword:"
											+ keyword.getName());
						}
					}
				}
				p.println("\nPARSE COMPLETED");
				p.close();
			} catch (Exception e) {
				System.err.println("Error writing to file");
			}
			// END OF DEBUG
		}
		System.out.println("PARSE COMPLETED");
	}

	//NOT IN USED
	public static void replaceTokens(String filePath)
			throws java.io.IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			fileData.append(buf, 0, numRead);
		}
		reader.close();

		String[] tokens = {
				"<style face=\"normal\" font=\"default\" size=\"100%\">",
				"<style face=\"italic\" font=\"default\" size=\"100%\">",
				"<style face=\"bold\" font=\"default\" size=\"100%\">",
				"<style face=\"italic underline\" font=\"default\" size=\"100%\">",
				"</style>" };
		String fileString = fileData.toString();
		for (String token : tokens) {
			fileString = fileString.replaceAll(token, "");
		}

		FileOutputStream out = new FileOutputStream(filePath);
		PrintStream p = new PrintStream(out);
		p.print(fileString);
	}

}
