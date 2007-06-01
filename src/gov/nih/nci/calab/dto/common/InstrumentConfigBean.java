package gov.nih.nci.calab.dto.common;

import gov.nih.nci.calab.domain.InstrumentConfiguration;
import gov.nih.nci.calab.service.util.StringUtils;

/**
 * This class represents an instrument configuration used for characterization
 * 
 * @author pansu
 * 
 */
public class InstrumentConfigBean {
	private String id;

	private String description;

	private InstrumentBean instrumentBean = new InstrumentBean();

	public InstrumentConfigBean() {
	}

	public InstrumentConfigBean(InstrumentConfiguration config) {
		this.id = StringUtils.convertToString(config.getId());
		this.description = (config.getDescription() != null) ? config
				.getDescription() : "";
		this.instrumentBean = new InstrumentBean(config.getInstrument());
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public InstrumentBean getInstrumentBean() {
		return instrumentBean;
	}

	public void setInstrumentBean(InstrumentBean instrumentBean) {
		this.instrumentBean = instrumentBean;
	}

}
