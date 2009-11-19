package gov.nih.nci.cananolab.service.publication;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.util.SAXElementHandler;
import gov.nih.nci.cananolab.util.SAXEventSwitcher;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;

public class PubMedXMLHandler {
	private static final String PUBMED_URL = 
		"http://www.ncbi.nlm.nih.gov/entrez/utils/pmfetch.fcgi?db=PubMed&report=abstract&mode=xml&id=";
	private static PubMedXMLHandler onlyInstance = null;
	
	private PublicationBean publicationBean = null;
	private Publication publication = null;
	
    private StringBuilder journal;
    private StringBuilder volume;
    private StringBuilder articleTitle;
    private StringBuilder year;
    private StringBuilder abstractText;
    private StringBuilder pageStr;
    private String startPage;
    private String endPage;
    private List<Author> authorList = null;
    private Author author = null;
	private StringBuilder keywordsStr;
    private StringBuilder keywordName;
    private StringBuilder firstName;
    private StringBuilder initial;
    private StringBuilder lastName;
    private StringBuilder doi;
    private boolean inPubDate;
    private boolean isDoi = false;
    private boolean foundDoi = false;
    private boolean foundPubmedArticle = false;
    
    public static synchronized PubMedXMLHandler getInstance() {
        if (onlyInstance == null)
            onlyInstance = new PubMedXMLHandler();
        
        return onlyInstance;
    }
    
    private PubMedXMLHandler(){
    }
	
	public boolean parsePubMedXML(Long pubMedId, PublicationBean pubBean) {
		publicationBean = pubBean;
		publication = (Publication) publicationBean.getDomainFile();
        try {
        	go(PUBMED_URL + pubMedId);
        } catch(Exception ex) {
        	System.out.println("Exception in parsePubMedXML, ");
        	ex.printStackTrace();
        }
        return !StringUtils.isEmpty(publication.getTitle());
	}
	
	private class PubmedArticleHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			foundPubmedArticle = true;
		}
		
		public void endElement(String uri, String localName, String qname) {
			if(!foundDoi)
				publication.setDigitalObjectId("");
			
			foundDoi = false;
		}

	}
	
	private class VolumeHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			volume = new StringBuilder();
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
			journal = new StringBuilder();
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
			if(inPubDate) year = new StringBuilder();
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
			articleTitle = new StringBuilder();
		}
		
		public void characters(char[] ch, int start, int length) {
			articleTitle.append(new String(ch, start, length));
		}
		
		public void endElement(String uri, String localName, String qname) {
			publication.setTitle(articleTitle.toString());
		}
	}
	
	private class ArticleIdHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			doi = new StringBuilder();
			String idType = atts.getValue("IdType");
			//System.out.println("start doi, idtype:" + idType);
			if(idType != null && idType.equalsIgnoreCase("doi")) {
				isDoi = true;
				foundDoi = true;
			}
		}
		
		public void characters(char[] ch, int start, int length) {
			if(isDoi)
				doi.append(new String(ch, start, length));
		}
		
		public void endElement(String uri, String localName, String qname) {
			if(isDoi) {
				publication.setDigitalObjectId(doi.toString());
				//System.out.println("doi:" + doi.toString());
				isDoi = false;
			}
		}
	}
	
	private class AbstractHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			abstractText = new StringBuilder();
		}
		
		public void characters(char[] ch, int start, int length) {
			abstractText.append(new String(ch, start, length));
		}
		
		public void endElement(String uri, String localName, String qname) {
			publication.setDescription(abstractText.toString());
		}
	}
	
	private class PageHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			pageStr = new StringBuilder();
		}
		
		public void characters(char[] ch, int start, int length) {
			pageStr.append(new String(ch, start, length));
		}
		
		public void endElement(String uri, String localName, String qname) {
			String page = pageStr.toString().trim();
			if (page.length() > 0) {
				String[] pages = page.split("-");
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
				try {
					publication.setStartPage(Integer.valueOf(startPage).toString());
					publication.setEndPage(Integer.valueOf(endPage).toString());
				} catch (NumberFormatException nfe) {
					publication.setStartPage(page);
					publication.setEndPage(null);
					//System.out.println(
						//"publication page number format exception:" + pageStr.toString());
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
			publicationBean.setAuthors(authorList);
		}
	}
	
	private class AuthorHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			author = new Author();
			lastName = new StringBuilder();
			firstName = new StringBuilder();
			initial = new StringBuilder();
		}
		
		public void endElement(String uri, String localName, String qname) {
			author.setFirstName(firstName.toString());
			author.setLastName(lastName.toString());
			author.setInitial(initial.toString());
			authorList.add(author);
		}
	}
	
	private class LastNameHandler extends SAXElementHandler
	{
		public void characters(char[] ch, int start, int length) {
			lastName.append(new String(ch, start, length));
		}
	}
	
	private class ForeNameHandler extends SAXElementHandler
	{
		public void characters(char[] ch, int start, int length) {
			firstName.append(new String(ch, start, length));
		}
	}
	
	private class InitialHandler extends SAXElementHandler
	{
		public void characters(char[] ch, int start, int length) {
			initial.append(new String(ch, start, length));
		}
	}
	
	private class KeywordListHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			keywordsStr = new StringBuilder();
		}
		
		public void endElement(String uri, String localName, String qname) {
			publicationBean.setKeywordsStr(keywordsStr.toString());
		}
	}
	
	private class KeywordHandler extends SAXElementHandler
	{
		public void startElement(String uri, String localName, String qname, Attributes atts) {
			keywordName = new StringBuilder();
		}
		
		public void endElement(String uri, String localName, String qname) {
			String name = keywordName.toString();
			if (keywordName.length() > 0 && 
				keywordName.charAt(keywordName.length() - 1) == '/') {
				name = keywordName.substring(0, keywordName.length() - 1);
			}
			keywordsStr.append(name).append("\r\n");
		}
	}
	
	private class DescriptorNameHandler extends SAXElementHandler
	{
		public void characters(char[] ch, int start, int length) {
			keywordName.append(new String(ch, start, length));
		}
		
		public void endElement(String uri, String localName, String qname) {
			keywordName.append('/');
		}
	}
	
	private class QualifierNameHandler extends SAXElementHandler
	{
		//Note: this will be called 3 times for "antagonists & inhibitors".
		public void characters(char[] ch, int start, int length) {
			keywordName.append(new String(ch, start, length));
		}
		
		public void endElement(String uri, String localName, String qname) {
			keywordName.append('/');
		}
	}
	
	public void go(String xmlinput) throws Exception
	{
		SAXEventSwitcher s = new SAXEventSwitcher();
		s.setElementHandler("pubmedarticle", new PubmedArticleHandler());
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
		s.setElementHandler("firstname", new ForeNameHandler());
		s.setElementHandler("initials", new InitialHandler());
		s.setElementHandler("articleid", new ArticleIdHandler());
		
		// Retrieve keyword information from PubMed (must be all lower case).
		s.setElementHandler("meshheadinglist", new KeywordListHandler());
		s.setElementHandler("meshheading", new KeywordHandler());
		s.setElementHandler("descriptorname", new DescriptorNameHandler());
		s.setElementHandler("qualifiername", new QualifierNameHandler());
		
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sparser = spf.newSAXParser();
        sparser.parse(xmlinput, s);
	}
	
	// for testing
	public static void main(String[] args) {
		PubMedXMLHandler phandler = PubMedXMLHandler.getInstance();
		PublicationBean pubBean = new PublicationBean();
		phandler.parsePubMedXML(Long.valueOf("19420561"), pubBean); //16642514
		
		Publication pub = (Publication) pubBean.getDomainFile();
		System.out.println("=========================================");
		System.out.println("title: " + pub.getTitle());
		System.out.println("url: " + pub.getUri());
		System.out.println("pubmed id: " + pub.getPubMedId());
		System.out.println("doi: " + pub.getDigitalObjectId());
		System.out.println("journal: " + pub.getJournalName());
		System.out.println("year: " + pub.getYear());
		System.out.println("createdBy: " + pub.getCreatedBy());
		System.out.println("createdDate: " + pub.getCreatedDate());
		System.out.println("start page: " + pub.getStartPage());
		System.out.println("end page: " + pub.getEndPage());
		System.out.println("volume: " + pub.getVolume());
		System.out.println("Abstract: " + pub.getDescription());
		for (Author author : pubBean.getAuthors()) {
			System.out.println("       Authors: " + author.getLastName() + ","
					+ author.getFirstName() + "(" + author.getInitial()
					+ ")");
		}
		System.out.println("Keywords:");
		System.out.println(pubBean.getKeywordsStr());
	}
}
