package gov.nih.nci.cananolab.service.publication.helper;

import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.cananolab.domain.common.DocumentAuthor;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.util.SAXElementHandler;
import gov.nih.nci.cananolab.util.SAXEventSwitcher;

import org.xml.sax.*;

import javax.xml.parsers.*;

public class PubMedXMLHandler {

	private static final String PUBMED_URL = "http://www.ncbi.nlm.nih.gov/entrez/utils/pmfetch.fcgi?db=PubMed&report=abstract&mode=xml&id=";
	
	private static PublicationBean publicationBean = null;
	private Publication publication = null;
	private static PubMedXMLHandler onlyInstance = null;
	
    private StringBuffer journal;
    private StringBuffer volume;
    private StringBuffer articleTitle;
    private StringBuffer year;
    private StringBuffer abstractText;
    private StringBuffer pageStr;
    private long startPage;
    private long endPage;
    private List<DocumentAuthor> authorList = null;
    private DocumentAuthor author = null;
    private StringBuffer firstName;
    private StringBuffer middleInitial;
    private StringBuffer lastName;
    private boolean inPubDate;
    //private String pubmedId;
    
    public static synchronized PubMedXMLHandler getInstance() {
        if (onlyInstance == null)
            onlyInstance = new PubMedXMLHandler();
        
        return onlyInstance;
    }
    
    private PubMedXMLHandler(){}
	
	public PublicationBean parsePubMedXML (Long pubMedId,
			PublicationBean pubBean) {
			publicationBean = pubBean;
			publication = (Publication) pubBean.getDomainFile();
			
 			publication.setPubMedId(pubMedId);
			String uri = PUBMED_URL + pubMedId;
	        try {
	        	go(uri);
	        	publicationBean.setDomainPublication(publication);
	        } catch(Exception ex) {
	        	System.out.println("exception in parsePubMedXML, ");
	        	ex.printStackTrace();
	        }
	       // pubmedId = pubMedId.toString();
	        //publicationBean = new PublicationBean(publication);
	        
	        
	        return publicationBean;
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
	
	private class JournalTitleHandler extends SAXElementHandler
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
	
	private class PubDateHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			inPubDate = true;
		}
		
		public void endElement(String uri, String localName, String qname) {
			inPubDate = false;
		}
	}
	
	private class YearHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			if(inPubDate) year = new StringBuffer();
		}
		
		public void characters(char[] ch, int start, int length) {
			if(inPubDate) year.append(new String(ch, start, length));
		}
		
		public void endElement(String uri, String localName, String qname) {
			if(inPubDate) publication.setYear(Integer.parseInt(year.toString()));
		}
	}
	
	private class ArticleTitleHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			articleTitle = new StringBuffer();
		}
		
		public void characters(char[] ch, int start, int length) {
			articleTitle.append(new String(ch, start, length));
		}
		
		public void endElement(String uri, String localName, String qname) {
			publication.setTitle(articleTitle.toString());
		}
	}
	
	private class AbstractHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			abstractText = new StringBuffer();
		}
		
		public void characters(char[] ch, int start, int length) {
			abstractText.append(new String(ch, start, length));
		}
		
		public void endElement(String uri, String localName, String qname) {
			publicationBean.setAbstractText(abstractText.toString());
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
			String [] pages = pageStr.toString().split("-");
			startPage = Long.parseLong(pages[0]);
			int endPagePrefixLength = pages[0].length() - pages[1].length();
			String endPagePrefix = pages[0].substring(0, endPagePrefixLength);
			endPage = Long.parseLong(endPagePrefix + pages[1]);

			publication.setStartPage(startPage);
			publication.setEndPage(endPage);
		}
	}
	
	private class AuthorListHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			authorList = new ArrayList<DocumentAuthor>();
		}
		
		public void endElement(String uri, String localName, String qname) {
			publicationBean.setAuthors(authorList);
		}
	}
	
	private class AuthorHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			author = new DocumentAuthor();
		}
		
		public void endElement(String uri, String localName, String qname) {
			author.setFirstName(firstName.toString());
			author.setLastName(lastName.toString());
			author.setMiddleInitial(middleInitial.toString());
			authorList.add(author);
		}
	}
	
	private class LastNameHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			lastName = new StringBuffer();
		}
		
		public void characters(char[] ch, int start, int length) {
			lastName.append(new String(ch, start, length));
		}
	}
	
	private class ForeNameHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			firstName = new StringBuffer();
		}
		
		public void characters(char[] ch, int start, int length) {
			firstName.append(new String(ch, start, length));
		}
	}
	
	private class MiddleInitialHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			middleInitial = new StringBuffer();
		}
		
		public void characters(char[] ch, int start, int length) {
			middleInitial.append(new String(ch, start, length));
		}
	}
	
	public void go(String xmlinput) throws Exception
	{
		SAXEventSwitcher s = new SAXEventSwitcher();
		s.setElementHandler("volume", new VolumeHandler());
		s.setElementHandler("pubdate", new PubDateHandler());
		s.setElementHandler("year", new YearHandler());
		s.setElementHandler("title", new JournalTitleHandler());
		s.setElementHandler("articletitle", new ArticleTitleHandler());
		s.setElementHandler("abstracttext", new AbstractHandler());
		s.setElementHandler("medlinepgn", new PageHandler());
		s.setElementHandler("authorlist", new AuthorListHandler());
		s.setElementHandler("author", new AuthorHandler());
		s.setElementHandler("lastname", new LastNameHandler());
		s.setElementHandler("forename", new ForeNameHandler());
		s.setElementHandler("initials", new MiddleInitialHandler());
		
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sparser = spf.newSAXParser();
        sparser.parse(xmlinput, s);
	}
	
	public static void main(String[] args) {
		PubMedXMLHandler phandler = PubMedXMLHandler.getInstance();
		phandler.parsePubMedXML(Long.valueOf("18294836"), new PublicationBean());
		
		System.out.println("bean journal:" + publicationBean.getJournal());
		System.out.println("abstract:" + publicationBean.getAbstractText());
		System.out.println("year:" + publicationBean.getYear());
		System.out.println("title:" + publicationBean.getTitle());
		System.out.println("start page:" + publicationBean.getStartPage());
		System.out.println("end page:" + publicationBean.getEndPage());

	}
}
