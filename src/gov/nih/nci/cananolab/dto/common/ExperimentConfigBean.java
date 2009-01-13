package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.Technique;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * View bean for technique and associated instruments;
 *
 * @author pansu, tanq
 *
 */
public class ExperimentConfigBean {
	private ExperimentConfig domain;
	private String displayName;
	private List<Instrument> instruments = new ArrayList<Instrument>(20);

	public ExperimentConfigBean() {
		domain = new ExperimentConfig();
		domain.setTechnique(new Technique());
		instruments.add(new Instrument());
	}

	public ExperimentConfigBean(ExperimentConfig config) {
		domain = config;
		for (Instrument instrument : config.getInstrumentCollection()) {
			instruments.add(instrument);
		}
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

	public void addInstrument() {
		instruments.add(new Instrument());
	}

	public void removeInstrument(int ind) {
		instruments.remove(ind);
	}

	/**
	 * @return the instruments
	 */
	public List<Instrument> getInstruments() {
		return instruments;
	}

	/**
	 * @param instruments
	 *            the instruments to set
	 */
	public void setInstruments(List<Instrument> instruments) {
		this.instruments = instruments;
	}

	public void setupDomain(String createdBy) throws Exception {
		if (domain.getId() == null) {
			domain.setCreatedBy(createdBy);
			domain.setCreatedDate(new Date());
		}
		if (domain.getInstrumentCollection() != null) {
			domain.getInstrumentCollection().clear();
		} else {
			domain.setInstrumentCollection(new HashSet<Instrument>());
		}
		for (Instrument instrument : instruments) {
			domain.getInstrumentCollection().add(instrument);
		}
	}
}
