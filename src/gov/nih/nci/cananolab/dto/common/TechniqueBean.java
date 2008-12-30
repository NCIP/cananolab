package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Technique;

/**
 * This class represents attributes of a technique to be viewed in a view page.
 *
 * @author pansu
 *
 */
public class TechniqueBean {
	private Technique domain;
	private String displayName;

	public TechniqueBean() {
		super();
		domain=new Technique();
	}

	public TechniqueBean(Technique technique) {
		domain = technique;
	}

	public Technique getDomain() {
		return domain;
	}

	public String getDisplayName() {
		if (domain != null) {
			displayName = domain.getType();
		}
		return displayName;
	}
}
