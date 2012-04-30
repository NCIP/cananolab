package gov.nih.nci.cananolab.util;

import org.hibernate.criterion.MatchMode;

/**
 * For use in searching objects by the given text through Hibernate MatchMode.
 * 
 * @author pansu
 */
public class TextMatchMode {
	private String originalText;

	private String updatedText;

	private MatchMode matchMode = MatchMode.EXACT;

	public TextMatchMode(String originalText) {
		this.originalText = originalText;
		updatedText = originalText;
		if (originalText.equals("*")){
			matchMode = MatchMode.ANYWHERE;
			updatedText = "";
		}else if (originalText.startsWith("*") && originalText.endsWith("*")) {
			matchMode = MatchMode.ANYWHERE;
			updatedText = originalText.substring(1, originalText.length() - 1);
		} else if (originalText.startsWith("*")) {
			matchMode = MatchMode.END;
			updatedText = originalText.substring(1, originalText.length());
		} else if (originalText.endsWith("*")) {
			matchMode = MatchMode.START;
			updatedText = originalText.substring(0, originalText.length() - 1);
		}
	}

	public MatchMode getMatchMode() {
		return matchMode;
	}

	public String getOriginalText() {
		return originalText;
	}

	public String getUpdatedText() {
		return updatedText;
	}

}
