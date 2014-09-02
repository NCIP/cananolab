package gov.nih.nci.cananolab.restful.view.edit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.function.ImagingFunction;
import gov.nih.nci.cananolab.domain.nanomaterial.Biopolymer;
import gov.nih.nci.cananolab.domain.nanomaterial.CarbonNanotube;
import gov.nih.nci.cananolab.domain.nanomaterial.Dendrimer;
import gov.nih.nci.cananolab.domain.nanomaterial.Emulsion;
import gov.nih.nci.cananolab.domain.nanomaterial.Fullerene;
import gov.nih.nci.cananolab.domain.nanomaterial.Liposome;
import gov.nih.nci.cananolab.domain.nanomaterial.Polymer;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;

import javax.servlet.http.HttpServletRequest;

public class SimpleNanomaterialEntityBean {

	SimpleComposingElementBean simpleCompBean;
	
	SimpleFileBean fileBean;
	
	String type = "";
	
	Long id = 0L;
	
	String description = "";
	
	String sampleId = "";
	
	boolean userDeletable = false;
	
	boolean userUpdatable = false;
	
	String createdBy = "";
	
	Date createdDate;

	Map<Object, Object> domainEntity;

	List<SimpleComposingElementBean> composingElements;

	List<SimpleFileBean> files;

	boolean withProperties = false;

	String isPolymerized;

	String isCrossLinked;
	
	List<String> errors;
	
	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public SimpleComposingElementBean getSimpleCompBean() {
		return simpleCompBean;
	}

	public void setSimpleCompBean(SimpleComposingElementBean simpleCompBean) {
		this.simpleCompBean = simpleCompBean;
	}

	public SimpleFileBean getFileBean() {
		return fileBean;
	}

	public void setFileBean(SimpleFileBean fileBean) {
		this.fileBean = fileBean;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSampleId() {
		return sampleId;
	}

	public Map<Object, Object> getDomainEntity() {
		return domainEntity;
	}

	public void setDomainEntity(Map<Object, Object> domainEntity) {
		this.domainEntity = domainEntity;
	}

	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}

	public boolean isUserDeletable() {
		return userDeletable;
	}

	public void setUserDeletable(boolean userDeletable) {
		this.userDeletable = userDeletable;
	}

	public boolean isUserUpdatable() {
		return userUpdatable;
	}

	public void setUserUpdatable(boolean userUpdatable) {
		this.userUpdatable = userUpdatable;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public boolean isWithProperties() {
		return withProperties;
	}

	public void setWithProperties(boolean withProperties) {
		this.withProperties = withProperties;
	}

	public String getIsPolymerized() {
		return isPolymerized;
	}

	public void setIsPolymerized(String isPolymerized) {
		this.isPolymerized = isPolymerized;
	}

	public String getIsCrossLinked() {
		return isCrossLinked;
	}

	public void setIsCrossLinked(String isCrossLinked) {
		this.isCrossLinked = isCrossLinked;
	}

	public void transferNanoMaterialEntityBeanToSimple(
			NanomaterialEntityBean bean, HttpServletRequest httpRequest) {
		simpleCompBean = new SimpleComposingElementBean();
		fileBean = new SimpleFileBean();
		NanomaterialEntity nanoEntity = bean.getDomainEntity();
		setType(bean.getType());
		setDescription(bean.getDescription());
		setCreatedBy(nanoEntity.getCreatedBy());
		setCreatedDate(nanoEntity.getCreatedDate());
		setDomainEntityInfo(bean);
}

	private void setDomainEntityInfo(NanomaterialEntityBean bean) {
		// TODO Auto-generated method stub
		NanomaterialEntity nanoEntity = bean.getDomainEntity();
		SampleComposition sampleComp =  new SampleComposition();
				
		domainEntity = new HashMap<Object, Object>();
		if(bean.getType().equalsIgnoreCase("dendrimer")){
			domainEntity = new HashMap<Object, Object>();
			domainEntity.put("branch", bean.getDendrimer().getBranch());
			domainEntity.put("generation", bean.getDendrimer().getGeneration());
			domainEntity.put("createdDate", bean.getDendrimer().getCreatedBy());
			domainEntity.put("createdBy", bean.getDendrimer().getCreatedBy());
			domainEntity.put("id", bean.getDendrimer().getId());
		}
		if(bean.getType().equalsIgnoreCase("polymer")){
			domainEntity = new HashMap<Object, Object>();
			domainEntity.put("crossLinked", bean.getPolymer().getCrossLinked());
			domainEntity.put("crossLinkDegree", bean.getPolymer().getCrossLinkDegree());
			domainEntity.put("initiator", bean.getPolymer().getInitiator());
			domainEntity.put("createdDate", bean.getPolymer().getCreatedBy());
			domainEntity.put("createdBy", bean.getPolymer().getCreatedBy());
			domainEntity.put("id", bean.getPolymer().getId());

		}
		if(bean.getType().equalsIgnoreCase("biopolymer")){
			domainEntity = new HashMap<Object, Object>();

			domainEntity.put("name", bean.getBiopolymer().getName());
			domainEntity.put("type", bean.getBiopolymer().getType());
			domainEntity.put("sequence", bean.getBiopolymer().getSequence());
			domainEntity.put("createdDate", bean.getBiopolymer().getCreatedBy());
			domainEntity.put("createdBy", bean.getBiopolymer().getCreatedBy());
			domainEntity.put("id", bean.getBiopolymer().getId());

		}
		if(bean.getType().equalsIgnoreCase("CarbonNanotube")){
			domainEntity = new HashMap<Object, Object>();

			domainEntity.put("averageLength", bean.getCarbonNanotube().getAverageLength());
			domainEntity.put("averageLengthUnit", bean.getCarbonNanotube().getAverageLengthUnit());
			domainEntity.put("chirality", bean.getCarbonNanotube().getChirality());
			domainEntity.put("diameter", bean.getCarbonNanotube().getDiameter());
			domainEntity.put("diameterUnit",bean.getCarbonNanotube().getDiameterUnit());
			domainEntity.put("wallType", bean.getCarbonNanotube().getWallType());
			domainEntity.put("createdDate", bean.getCarbonNanotube().getCreatedBy());
			domainEntity.put("createdBy", bean.getCarbonNanotube().getCreatedBy());
			domainEntity.put("id", bean.getCarbonNanotube().getId());

		}
		if(bean.getType().equalsIgnoreCase("Liposome")){
			domainEntity = new HashMap<Object, Object>();

			domainEntity.put("IsPolymarized",bean.getLiposome().getPolymerized());
			domainEntity.put("PolymerName",	bean.getLiposome().getPolymerName());
			domainEntity.put("createdDate", bean.getLiposome().getCreatedBy());
			domainEntity.put("createdBy", bean.getLiposome().getCreatedBy());
			domainEntity.put("id", bean.getLiposome().getId());

		}
		if(bean.getType().equalsIgnoreCase("Emulsion")){
			domainEntity = new HashMap<Object, Object>();

			domainEntity.put("IsPolymarized",bean.getEmulsion().getPolymerized());
			domainEntity.put("PolymerName",	bean.getEmulsion().getPolymerName());
			domainEntity.put("createdDate", bean.getEmulsion().getCreatedBy());
			domainEntity.put("createdBy", bean.getEmulsion().getCreatedBy());
			domainEntity.put("id", bean.getEmulsion().getId());


		}
		if(bean.getType().equalsIgnoreCase("Fullerene")){
			domainEntity = new HashMap<Object, Object>();

			domainEntity.put("AverageDiameter",	bean.getFullerene().getAverageDiameter());
			domainEntity.put("AverageDiameterUnit",bean.getFullerene().getAverageDiameterUnit());
			domainEntity.put("NoOfCarbons",	bean.getFullerene().getNumberOfCarbon());
			domainEntity.put("createdDate", bean.getFullerene().getCreatedBy());
			domainEntity.put("createdBy", bean.getFullerene().getCreatedBy());
			domainEntity.put("id", bean.getFullerene().getId());

		}
		
		domainEntity.put("id", bean.getDomainEntity().getId());

		if(bean.getDomainEntity().getComposingElementCollection()!=null){
			composingElements = new ArrayList<SimpleComposingElementBean>();
			for(ComposingElementBean comp : bean.getComposingElements()){
				simpleCompBean = new SimpleComposingElementBean();
				simpleCompBean.setDescription(comp.getDescription());
				simpleCompBean.setId(comp.getDomain().getId());
				simpleCompBean.setMolecularFormula(comp.getDomain().getMolecularFormula());
				simpleCompBean.setMolecularFormulaType(comp.getDomain().getMolecularFormulaType());
				simpleCompBean.setName(comp.getDomain().getName());
				simpleCompBean.setPubChemDataSourceName(comp.getDomain().getPubChemDataSourceName());
				simpleCompBean.setPubChemId(comp.getDomain().getPubChemId());
				simpleCompBean.setType(comp.getDomain().getType());
				simpleCompBean.setValue(comp.getDomain().getValue());
				simpleCompBean.setValueUnit(comp.getDomain().getValueUnit());
				Map<String, Object> function = new HashMap<String, Object>();
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				if(comp.getInherentFunctions().size()>0){
					for(FunctionBean func : comp.getInherentFunctions()){
						
						function.put("description", func.getDescription());
						function.put("type", func.getType());
						function.put("modality",func.getImagingFunction().getModality());
						function.put("id", func.getDomainFunction().getId());
						list.add(function);
				//	simpleCompBean.setFunctionDescription(func.getDescription());
				//	simpleCompBean.setFunctionType(func.getType());
				//	simpleCompBean.setImagingModality(func.getImagingFunction().getModality());
					}
					simpleCompBean.setInherentFunction(list);
				}
				composingElements.add(simpleCompBean);
			}
			domainEntity.put("composingElementCollection", composingElements);
			
		}
			if(bean.getDomainEntity().getFileCollection()!=null){
				files = new ArrayList<SimpleFileBean>();
			for(FileBean file : bean.getFiles()){
				fileBean = new SimpleFileBean();
				fileBean.setDescription(file.getDescription());
				fileBean.setType(file.getDomainFile().getType());
				fileBean.setTitle(file.getDomainFile().getTitle());
				fileBean.setUri(file.getDomainFile().getUri());
				fileBean.setUriExternal(file.getDomainFile().getUriExternal());
				files.add(fileBean);
			}
			domainEntity.put("fileCollection", files);
		}
			domainEntity.put("sampleComposition", new SampleComposition());
			setDomainEntity(domainEntity);
	}
}
