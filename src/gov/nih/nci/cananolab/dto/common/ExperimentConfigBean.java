package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.Technique;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
	private ExperimentConfig domain = new ExperimentConfig();
	private List<Instrument> instruments = new ArrayList<Instrument>(20);

	public ExperimentConfigBean() {
		domain = new ExperimentConfig();
		domain.setTechnique(new Technique());
	}

	public ExperimentConfigBean(ExperimentConfig config) {
		domain = config;
		if (config.getInstrumentCollection() != null) {
			for (Instrument instrument : config.getInstrumentCollection()) {
				instruments.add(instrument);
			}
			Collections.sort(instruments,
					new Comparators.InstrumentDateComparator());
		}
	}

	public ExperimentConfig getDomain() {
		return domain;
	}

	public void setDomain(ExperimentConfig domain) {
		this.domain = domain;
	}

	public String getTechniqueDisplayName() {
		String techniqueDisplayName = "";
		if (domain.getTechnique() == null) {
			return techniqueDisplayName;
		}
		if (!StringUtils.isEmpty(domain.getTechnique().getAbbreviation())) {
			techniqueDisplayName = domain.getTechnique().getType() + "("
					+ domain.getTechnique().getAbbreviation() + ")";
		} else {
			techniqueDisplayName = domain.getTechnique().getType();
		}
		return techniqueDisplayName;
	}

	public void addInstrument(Instrument instrument) {
		int index = instruments.indexOf(instrument);
		if (index != -1) {
			instruments.remove(instrument);
			// retain the original order
			instruments.add(index, instrument);
		} else {
			instruments.add(instrument);
		}
	}

	public void removeInstrument(Instrument instrument) {
		instruments.remove(instrument);
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
		if (domain.getId() != null && domain.getId() == 0) {
			domain.setId(null);
		}
		//updated created_date and created_by if id is null
		if (domain.getId() == null) {
			domain.setCreatedBy(createdBy);
			domain.setCreatedDate(Calendar.getInstance().getTime());
		}
		//updated created_by if created_by contains copy, but keep the original created_date
		if (domain.getId() != null
				|| !StringUtils.isEmpty(domain.getCreatedBy())
				&& domain.getCreatedBy().contains(
						Constants.AUTO_COPY_ANNOTATION_PREFIX)) {
			domain.setCreatedBy(createdBy);
		}
		if (domain.getInstrumentCollection() != null) {
			domain.getInstrumentCollection().clear();
		} else {
			domain.setInstrumentCollection(new HashSet<Instrument>());
		}
		int i = 0;
		for (Instrument instrument : instruments) {
			if (!StringUtils.isEmpty(instrument.getType())
					|| !StringUtils.isEmpty(instrument.getManufacturer())
					|| !StringUtils.isEmpty(instrument.getModelName())) {
				instrument.setCreatedBy(createdBy);
				instrument.setCreatedDate(DateUtils.addSecondsToCurrentDate(i));
				domain.getInstrumentCollection().add(instrument);
				i++;
			}
		}
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof ExperimentConfigBean) {
			ExperimentConfigBean c = (ExperimentConfigBean) obj;
			Long thisId = this.domain.getId();
			if (thisId != null && thisId.equals(c.getDomain().getId())) {
				eq = true;
			}
		}
		return eq;
	}

	public int hashCode() {
		return domain.hashCode();
	}

	private String getInstrumentDisplayName(Instrument instrument) {
		StringBuffer sb = new StringBuffer();
		if (!StringUtils.isEmpty(instrument.getType())) {
			sb.append(instrument.getType());
			sb.append(" ");
		}
		if (!StringUtils.isEmpty(instrument.getManufacturer())) {
			sb.append("(");
			sb.append(instrument.getManufacturer());
			if (!StringUtils.isEmpty(instrument.getModelName())) {
				sb.append(", ");
				sb.append(instrument.getModelName());

			}
			sb.append(")");
		}
		return sb.toString();
	}

	public String[] getInstrumentDisplayNames() {
		List<String> displayNames = new ArrayList<String>();
		for (Instrument instrument : instruments) {
			displayNames.add(getInstrumentDisplayName(instrument));
		}
		if (displayNames.isEmpty()) {
			return null;
		}
		return displayNames.toArray(new String[displayNames.size()]);
	}

	public void resetDomainCopy(ExperimentConfig copy) {
		copy.setId(null);
		copy.setCreatedBy(Constants.AUTO_COPY_ANNOTATION_PREFIX);
		// don't need to set instrument and technique ID's to null
		// because they are reused across different characterizations
	}
}
