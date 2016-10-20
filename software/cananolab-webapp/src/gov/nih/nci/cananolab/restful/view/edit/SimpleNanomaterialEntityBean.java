package gov.nih.nci.cananolab.restful.view.edit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.security.enums.SecureClassesEnum;
import gov.nih.nci.cananolab.security.service.SpringSecurityAclService;

import javax.servlet.http.HttpServletRequest;

public class SimpleNanomaterialEntityBean {

	SimpleComposingElementBean simpleCompBean;
	
	SimpleSampleComposition sampleComposition;
	
	SimpleFileBean fileBean;
	
	String type = "";
	
	Long id = 0L;
	
	String description = "";
	
	String sampleId = "";
	
	boolean userDeletable = false;
	
	boolean userUpdatable = false;
	
	String createdBy = "";
	
	Date createdDate;

	Map<String, Object> domainEntity;

	List<SimpleComposingElementBean> composingElements;

	List<SimpleFileBean> files;

	boolean withProperties = false;

	String isPolymerized;

	String isCrossLinked;
	
	List<String> errors;
	
	List<String> otherSampleNames;
	
	public List<String> getOtherSampleNames() {
		return otherSampleNames;
	}

	public void setOtherSampleNames(List<String> otherSampleNames) {
		this.otherSampleNames = otherSampleNames;
	}

	public SimpleSampleComposition getSampleComposition() {
		return sampleComposition;
	}

	public void setSampleComposition(SimpleSampleComposition sampleComposition) {
		this.sampleComposition = sampleComposition;
	}

	public List<SimpleComposingElementBean> getComposingElements() {
		return composingElements;
	}

	public void setComposingElements(
			List<SimpleComposingElementBean> composingElements) {
		this.composingElements = composingElements;
	}

	public List<SimpleFileBean> getFiles() {
		return files;
	}

	public void setFiles(List<SimpleFileBean> files) {
		this.files = files;
	}

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

	public Map<String, Object> getDomainEntity() {
		return domainEntity;
	}

	public void setDomainEntity(Map<String, Object> domainEntity) {
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

	public void transferNanoMaterialEntityBeanToSimple(NanomaterialEntityBean bean, HttpServletRequest httpRequest, SpringSecurityAclService springSecurityAclService)
	{
		NanomaterialEntity nanoEntity = bean.getDomainEntity();
		setSampleId((String) httpRequest.getSession().getAttribute("sampleId"));
		setType(bean.getType());
		setDescription(bean.getDescription());
		setCreatedBy(nanoEntity.getCreatedBy());
		setCreatedDate(nanoEntity.getCreatedDate());
		setWithProperties(bean.isWithProperties());
		if(bean.getDomainEntity().getComposingElementCollection()!=null){
			composingElements = new ArrayList<SimpleComposingElementBean>();
			for(ComposingElementBean comp : bean.getComposingElements()){
				SimpleComposingElementBean sCompBean = new SimpleComposingElementBean();
				sCompBean.setDescription(comp.getDescription());
				sCompBean.setId(comp.getDomain().getId());
				sCompBean.setMolecularFormula(comp.getDomain().getMolecularFormula());
				sCompBean.setMolecularFormulaType(comp.getDomain().getMolecularFormulaType());
				sCompBean.setName(comp.getDomain().getName());
				sCompBean.setPubChemDataSourceName(comp.getDomain().getPubChemDataSourceName());
				sCompBean.setPubChemId(comp.getDomain().getPubChemId());
				sCompBean.setType(comp.getDomain().getType());
				sCompBean.setValue(comp.getDomain().getValue());
				sCompBean.setValueUnit(comp.getDomain().getValueUnit());
				sCompBean.setCreatedBy(comp.getDomain().getCreatedBy());
				sCompBean.setCreatedDate(comp.getDomain().getCreatedDate());
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				if(comp.getInherentFunctions().size()>0){
					for(FunctionBean func : comp.getInherentFunctions()){
						Map<String, Object> function = new HashMap<String, Object>();
						
						function.put("description", func.getDescription());
						function.put("type", func.getType());
						function.put("modality",func.getImagingFunction().getModality());
						function.put("id", func.getId());
						function.put("createdBy", func.getDomainFunction().getCreatedBy());
						function.put("createdDate", func.getDomainFunction().getCreatedDate());
						list.add(function);
					}
					sCompBean.setInherentFunction(list);
				}
				composingElements.add(sCompBean);
			}
			//domainEntity.put("composingElementCollection", composingElements);
			
		}
		
		if(bean.getFiles()!=null){
			files = new ArrayList<SimpleFileBean>();
		for(FileBean file : bean.getFiles()){
			SimpleFileBean fBean = new SimpleFileBean();
			fBean.setDescription(file.getDescription());
			fBean.setType(file.getDomainFile().getType());
			fBean.setTitle(file.getDomainFile().getTitle());
			fBean.setUri(file.getDomainFile().getUri());
			fBean.setUriExternal(file.getDomainFile().getUriExternal());
			fBean.setExternalUrl(file.getExternalUrl());
			fBean.setKeywordsStr(file.getKeywordsStr());
			fBean.setId(file.getDomainFile().getId());
			fBean.setCreatedBy(file.getDomainFile().getCreatedBy());
			fBean.setCreatedDate(file.getDomainFile().getCreatedDate());
			fBean.setTheAccess(file.getTheAccess());
			boolean isPublic = springSecurityAclService.checkObjectPublic(Long.valueOf(getSampleId()), SecureClassesEnum.SAMPLE.getClazz());
			fBean.setIsPublic(isPublic);
			files.add(fBean);
		}
		setFiles(files);
	}
		
		setDomainEntityInfo(bean);
}

	private void setDomainEntityInfo(NanomaterialEntityBean bean) {
		// TODO Auto-generated method stub
		NanomaterialEntity nanoEntity = bean.getDomainEntity();
		SampleComposition sampleComp =  new SampleComposition();
				
		domainEntity = new HashMap<String, Object>();
		if(bean.getType().equalsIgnoreCase("dendrimer")){
			domainEntity = new HashMap<String, Object>();
			domainEntity.put("branch", bean.getDendrimer().getBranch());
			domainEntity.put("generation", bean.getDendrimer().getGeneration());
			domainEntity.put("createdDate", bean.getDendrimer().getCreatedDate());
			domainEntity.put("createdBy", bean.getDendrimer().getCreatedBy());
			domainEntity.put("id", bean.getDendrimer().getId());
		}else if(bean.getType().equalsIgnoreCase("polymer")){
			domainEntity = new HashMap<String, Object>();
			domainEntity.put("isCrossLinked", bean.getPolymer().getCrossLinked());
			domainEntity.put("crossLinkDegree", bean.getPolymer().getCrossLinkDegree());
			domainEntity.put("initiator", bean.getPolymer().getInitiator());
			domainEntity.put("createdDate", bean.getPolymer().getCreatedDate());
			domainEntity.put("createdBy", bean.getPolymer().getCreatedBy());
			domainEntity.put("id", bean.getPolymer().getId());

		}else if(bean.getType().equalsIgnoreCase("biopolymer")){
			domainEntity = new HashMap<String, Object>();

			domainEntity.put("name", bean.getBiopolymer().getName());
			domainEntity.put("type", bean.getBiopolymer().getType());
			domainEntity.put("sequence", bean.getBiopolymer().getSequence());
			domainEntity.put("createdDate", bean.getBiopolymer().getCreatedDate());
			domainEntity.put("createdBy", bean.getBiopolymer().getCreatedBy());
			domainEntity.put("id", bean.getBiopolymer().getId());

		}else if(bean.getType().equalsIgnoreCase("carbon nanotube")){
			domainEntity = new HashMap<String, Object>();

			domainEntity.put("averageLength", bean.getCarbonNanotube().getAverageLength());
			domainEntity.put("averageLengthUnit", bean.getCarbonNanotube().getAverageLengthUnit());
			domainEntity.put("chirality", bean.getCarbonNanotube().getChirality());
			domainEntity.put("diameter", bean.getCarbonNanotube().getDiameter());
			domainEntity.put("diameterUnit",bean.getCarbonNanotube().getDiameterUnit());
			domainEntity.put("wallType", bean.getCarbonNanotube().getWallType());
			domainEntity.put("createdDate", bean.getCarbonNanotube().getCreatedDate());
			domainEntity.put("createdBy", bean.getCarbonNanotube().getCreatedBy());
			domainEntity.put("id", bean.getCarbonNanotube().getId());

		}else if(bean.getType().equalsIgnoreCase("Liposome")){
			domainEntity = new HashMap<String, Object>();

			domainEntity.put("isPolymerized",bean.getLiposome().getPolymerized());
			domainEntity.put("polymerName",	bean.getLiposome().getPolymerName());
			domainEntity.put("createdDate", bean.getLiposome().getCreatedDate());
			domainEntity.put("createdBy", bean.getLiposome().getCreatedBy());
			domainEntity.put("id", bean.getLiposome().getId());

		}else if(bean.getType().equalsIgnoreCase("Emulsion")){
			domainEntity = new HashMap<String, Object>();

			domainEntity.put("isPolymerized",bean.getEmulsion().getPolymerized());
			domainEntity.put("polymerName",	bean.getEmulsion().getPolymerName());
			domainEntity.put("createdDate", bean.getEmulsion().getCreatedDate());
			domainEntity.put("createdBy", bean.getEmulsion().getCreatedBy());
			domainEntity.put("id", bean.getEmulsion().getId());


		}else if(bean.getType().equalsIgnoreCase("Fullerene")){
			domainEntity = new HashMap<String, Object>();

			domainEntity.put("averageDiameter",	bean.getFullerene().getAverageDiameter());
			domainEntity.put("averageDiameterUnit",bean.getFullerene().getAverageDiameterUnit());
			domainEntity.put("numberOfCarbon",	bean.getFullerene().getNumberOfCarbon());
			domainEntity.put("createdDate", bean.getFullerene().getCreatedDate());
			domainEntity.put("createdBy", bean.getFullerene().getCreatedBy());
			domainEntity.put("id", bean.getFullerene().getId());

		}else{
		
		domainEntity.put("id", bean.getDomainEntity().getId());

		}
			
			domainEntity.put("sampleComposition", new SampleComposition());
			setDomainEntity(domainEntity);
	}
}
