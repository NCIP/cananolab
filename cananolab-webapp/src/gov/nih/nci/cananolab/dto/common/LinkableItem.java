package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a string displayed as a link
 *
 * @author pansu
 *
 */
public class LinkableItem {
	private List<String> displayStrings = new ArrayList<String>();
	private String action;
	private String displayName;

	public LinkableItem() {
	}

	public List<String> getDisplayStrings() {
		return displayStrings;
	}

	public void setDisplayStrings(List<String> displayStrings) {
		this.displayStrings = displayStrings;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDisplayName() {
		String str = StringUtils.join(displayStrings, "\r\n");
		//per app scan
		displayName = StringUtils.escapeXmlButPreserveLineBreaks(str);
		return displayName;
	}
}
