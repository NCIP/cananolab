/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

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
