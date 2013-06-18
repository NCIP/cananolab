/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Custom tag for context sensitive help.
 */
public class ContextSensitiveHelpTag implements Tag, Serializable {

	private static final long serialVersionUID = 5618297483211863400L;
	
	static private final Log log = LogFactory.getLog(ContextSensitiveHelpTag.class);	

	private PageContext myPageContext = null;

	private Tag myParent = null;

	// Tag attributes
	private String myKey = null;

	private String myImage = null;

	private String myLabelName = null;

	private String myHref = null;

	private String myTopic = null;

	private String myBundle = "ContextSensitiveHelp";

	private String myStyleClass = "helpText";

	private String myJavascriptKey = "help_javascript";
	
	private String wikiSiteBeginKey = "wiki_help_main";

	public void setPageContext(PageContext inPageContext) {
		myPageContext = inPageContext;
	}

	public void setParent(Tag inParent) {
		myParent = inParent;
	}

	public Tag getParent() {
		return myParent;
	}

	public void setTopic(String inTopic) {
		myTopic = inTopic;
	}

	public String getTopic() {
		return myTopic;
	}

	/**
	 * Sets the key attribute. This is included in the tld file.
	 * 
	 * @jsp.attribute description="The key attribute used to look up the value
	 *                in the properties file"
	 * 
	 * required="true"
	 * 
	 * rtexprvalue="false"
	 */
	public void setKey(String inKey) {
		myKey = inKey;
	}

	public String getKey() {
		return myKey;
	}

	/**
	 * Sets the Image attribute. This is included in the tld file.
	 * 
	 * @jsp.attribute description="The key attribute used to look up the value
	 *                in the properties file"
	 * 
	 * required="false"
	 * 
	 * rtexprvalue="false"
	 */
	public void setImage(String inKey) {
		myImage = inKey;
	}

	public String getImage() {
		return myImage;
	}

	/**
	 * Sets the text attribute. This is included in the tld file.
	 * 
	 * @jsp.attribute description="The text the CS help will be for"
	 * 
	 * required="true"
	 * 
	 * rtexprvalue="false"
	 */
	public void setText(String inLabelName) {
		myLabelName = inLabelName;
	}

	public String getText() {
		return myLabelName;
	}

	public String getHref() {
		return myHref;
	}

	/**
	 * Sets the href attribute. This is included in the tld file.
	 * 
	 * @jsp.attribute description="Where to go when the text is clicked.
	 *                Currently not implemented"
	 * 
	 * required="false"
	 * 
	 * rtexprvalue="false"
	 */
	public void setHref(String inHref) {
		myHref = inHref;
	}

	public String getBundle() {
		return myBundle;
	}

	/**
	 * Sets the bundle attribute. This is included in the tld file.
	 * 
	 * @jsp.attribute description="What bundle to use for the key lookup.
	 *                Currently defaults to ContextSensitiveHelp.properties"
	 * 
	 * required="false"
	 * 
	 * rtexprvalue="false"
	 */
	public void setBundle(String inBundle) {
		myBundle = inBundle;
	}

	public String getStyleClass() {
		return myStyleClass;
	}

	/**
	 * Sets the styleClass. This is included in the tld file.
	 * 
	 * @jsp.attribute description="What style to use for the popup. Currently
	 *                defaults to style_0"
	 * 
	 * required="false"
	 * 
	 * rtexprvalue="false"
	 */
	public void setStyleClass(String inStyleClass) {
		myStyleClass = inStyleClass;
	}

	public int doStartTag() throws JspException {

		try {
			String theHref = "";
			String wikiSiteBegin = "";
			String theJavascript = "";
			String theText = "";
			String theStyleClass = "";
			try {
				// Get the text
//				ResourceBundle theBundle = ResourceBundle.getBundle(myBundle);

				// Process optional attributes
				if (myTopic != null) {
					String theTopic = "";
//					String theJavascript = theBundle.getString(myJavascriptKey);
					
					Properties wikihelpProperties = new Properties();
					try {

						String wikihelpPropertiesFileName = null;

						wikihelpPropertiesFileName = System.getProperty("gov.nih.nci.cananolab.wikihelpProperties");
						
						try {
						
						FileInputStream in = new FileInputStream(wikihelpPropertiesFileName);
						wikihelpProperties.load(in);
				
						} 
						catch (FileNotFoundException e) {
							log.error("Caught exception finding file for properties: ", e);
							e.printStackTrace();			
						} catch (IOException e) {
							log.error("Caught exception finding file for properties: ", e);
							e.printStackTrace();			
						}
						theJavascript = wikihelpProperties.getProperty(myJavascriptKey);
						theText = wikihelpProperties.getProperty(myKey);
						theStyleClass = wikihelpProperties.getProperty(myStyleClass);
						wikiSiteBegin =  wikihelpProperties.getProperty(wikiSiteBeginKey);
						theTopic = wikihelpProperties.getProperty(myTopic);
					}

					// Default to 100 on an exception
					catch (Exception e) {
						System.err.println("Error loading system.properties file");
						e.printStackTrace();
					}
					theHref = "href=\"" + theJavascript + wikiSiteBegin + theTopic + "')\"";
				}

				if (myImage != null) {
					myPageContext.getOut().write(
							"<a " + theHref + " onMouseOver=\"stm(" + theText
									+ "," + theStyleClass
									+ ")\" onMouseOut=\"htm();\"><img alt=\"Help\" src=\""
									+ myImage + "\" border=\"0\"/>" + "</a>");

				} else {
					myPageContext.getOut().write(
							"<a class=\"" + getStyleClass() + "\"" + theHref + ">"
									+ myLabelName + "</a>");
				}

			} catch (Exception e) {
				e.printStackTrace();

				System.out.println("Can't get bundle. Ignore tooltip");
				// Can't get bundle. Ignore tooltip
				myPageContext.getOut().write(
						"<a " + theHref + " \">" + myLabelName + "</a>");
			}

		} catch (IOException e) {
			throw new JspTagException("An IOException occurred.");
		} catch (Exception e) {
			throw new JspTagException("An unknown exception occurred.");
		}

		return SKIP_BODY;

	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public void release() {
		myPageContext = null;
		myParent = null;
		myKey = null;
	}
}

