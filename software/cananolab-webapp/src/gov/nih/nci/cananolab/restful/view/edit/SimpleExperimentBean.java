package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.Technique;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;

import java.util.List;

public class SimpleExperimentBean {
	
	long id;
	String displayName;
	String abbreviation;
	String description;
	
	List<SimpleInstrumentBean> instruments;
	
	
	public void transferToExperimentConfigBean(ExperimentConfigBean expConfigBean) {
		if (id > 0)
			expConfigBean.getDomain().setId(id);
		
		expConfigBean.getDomain().setDescription(description);
		Technique tech = expConfigBean.getDomain().getTechnique();
		if (tech == null) {
			tech = new Technique();
			expConfigBean.getDomain().setTechnique(new Technique());
		}
		tech.setType(this.displayName);
		
		List<Instrument> instListInDomainBean = expConfigBean.getInstruments();
		instListInDomainBean.clear();
		
		for (SimpleInstrumentBean inst : instruments) {
			Instrument domainInst = new Instrument();
			domainInst.setManufacturer(inst.getManufacturer());
			domainInst.setModelName(inst.getModelName());
			domainInst.setType(inst.getType());
			
			instListInDomainBean.add(domainInst);
		}
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<SimpleInstrumentBean> getInstruments() {
		return instruments;
	}
	public void setInstruments(List<SimpleInstrumentBean> instruments) {
		this.instruments = instruments;
	}
	
}
