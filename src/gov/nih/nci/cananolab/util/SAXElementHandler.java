package gov.nih.nci.cananolab.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class SAXElementHandler {

	/**
	 * start of an element
	 */
	public void startElement (String uri, String localName, String qname, Attributes atts)
		throws SAXException {}
	
	/*
	 * end of an element
	 */
	public void endElement (String uri, String localName, String qname)
		throws SAXException {}
	
	/*
	 * character data
	 */
	public void characters(char[] ch, int start, int length)throws SAXException {}
}
