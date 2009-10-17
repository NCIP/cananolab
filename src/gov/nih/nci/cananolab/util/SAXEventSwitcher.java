package gov.nih.nci.cananolab.util;

import java.util.Hashtable;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXEventSwitcher extends DefaultHandler {

		private Hashtable<String, SAXElementHandler> rules = new Hashtable<String, SAXElementHandler>();
		private Stack<SAXElementHandler> stack = new Stack<SAXElementHandler>();
		
		public void setElementHandler(String name, SAXElementHandler handler) {
			rules.put(name, handler);
		}
		
		public void startElement(String uri, String localName, String qname, Attributes atts) throws SAXException
		{
			SAXElementHandler handler = (SAXElementHandler) rules.get(qname.toLowerCase());
			stack.push(handler);
			if(handler != null) {
				handler.startElement(uri, localName, qname, atts);
			}
		}
		
		public void endElement(String uri, String localName, String qname) throws SAXException {
			SAXElementHandler handler = (SAXElementHandler) stack.pop();
			if(handler != null) {
				handler.endElement(uri, localName, qname);
			}
		}
		
		public void characters(char[] ch, int start, int length) throws SAXException {
			SAXElementHandler handler = (SAXElementHandler) stack.peek();
			if(handler != null) {
				handler.characters(ch, start, length);
			}
		}
}
