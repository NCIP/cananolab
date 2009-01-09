package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.ExperimentConfig;

/**
 * View bean for technique and associated instruments;
 *
 * @author pansu, tanq
 *
 */
public class ExperimentConfigBean {
	private ExperimentConfig domain;
	private String displayName;

	public ExperimentConfigBean() {
		super();
	}

	public ExperimentConfigBean(ExperimentConfig config) {
		domain = config;
	}

	public ExperimentConfig getDomain() {
		return domain;
	}

	public void setDomain(ExperimentConfig domain) {
		this.domain = domain;
	}

	public String getDisplayName() {
		if (domain.getTechnique().getAbbreviation() != null) {
			displayName = domain.getTechnique().getType() + "("
					+ domain.getTechnique().getAbbreviation() + ")";
		} else {
			displayName = domain.getTechnique().getType();
		}
		return displayName;
	}
}
