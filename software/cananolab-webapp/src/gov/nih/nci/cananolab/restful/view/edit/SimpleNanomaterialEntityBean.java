package gov.nih.nci.cananolab.restful.view.edit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.nanomaterial.Biopolymer;
import gov.nih.nci.cananolab.domain.nanomaterial.CarbonNanotube;
import gov.nih.nci.cananolab.domain.nanomaterial.Dendrimer;
import gov.nih.nci.cananolab.domain.nanomaterial.Emulsion;
import gov.nih.nci.cananolab.domain.nanomaterial.Fullerene;
import gov.nih.nci.cananolab.domain.nanomaterial.Liposome;
import gov.nih.nci.cananolab.domain.nanomaterial.Polymer;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
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
	
	Map<Object, Object> polymer;

	Map<Object, Object> biopolymer;

	Map<Object, Object> dendrimer;

	Map<Object, Object> carbonNanotube;

	Map<Object, Object> liposome;

	Map<Object, Object> emulsion;

	Map<Object, Object> fullerene;

	List<SimpleComposingElementBean> composingElements;

	List<SimpleFileBean> files;
	NanomaterialEntity domainEntity;

	boolean withProperties = false;

	String isPolymerized;

	String isCrossLinked;
	
	public Map<Object, Object> getPolymer() {
		return polymer;
	}

	public void setPolymer(Map<Object, Object> polymer) {
		this.polymer = polymer;
	}

	public Map<Object, Object> getBiopolymer() {
		return biopolymer;
	}

	public void setBiopolymer(Map<Object, Object> biopolymer) {
		this.biopolymer = biopolymer;
	}

	public Map<Object, Object> getDendrimer() {
		return dendrimer;
	}

	public void setDendrimer(Map<Object, Object> dendrimer) {
		this.dendrimer = dendrimer;
	}

	public Map<Object, Object> getCarbonNanotube() {
		return carbonNanotube;
	}

	public void setCarbonNanotube(Map<Object, Object> carbonNanotube) {
		this.carbonNanotube = carbonNanotube;
	}

	public Map<Object, Object> getLiposome() {
		return liposome;
	}

	public void setLiposome(Map<Object, Object> liposome) {
		this.liposome = liposome;
	}

	public Map<Object, Object> getEmulsion() {
		return emulsion;
	}

	public void setEmulsion(Map<Object, Object> emulsion) {
		this.emulsion = emulsion;
	}

	public Map<Object, Object> getFullerene() {
		return fullerene;
	}

	public void setFullerene(Map<Object, Object> fullerene) {
		this.fullerene = fullerene;
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

	public NanomaterialEntity getDomainEntity() {
		return domainEntity;
	}

	public void setDomainEntity(NanomaterialEntity domainEntity) {
		this.domainEntity = domainEntity;
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
		
		for(FileBean file : bean.getFiles()){
			fileBean.setDescription(file.getDescription());
			fileBean.setId(file.getDomainFile().getId());
			fileBean.setKeywordsStr(file.getKeywordsStr());
			fileBean.setTitle(file.getDomainFile().getTitle());
			fileBean.setType(file.getDomainFile().getType());
			fileBean.setUri(file.getDomainFile().getUri());
			fileBean.setUriExternal(file.getDomainFile().getUriExternal());
			setFileBean(fileBean);
		}
		setType(bean.getType());
		setDescription(bean.getDescription());
		setCreatedBy(nanoEntity.getCreatedBy());
		setCreatedDate(nanoEntity.getCreatedDate());
		if(bean.getDendrimer()!=null){
		dendrimer = new HashMap<Object, Object>();
		dendrimer.put("branch", bean.getDendrimer().getBranch());
		dendrimer.put("generation", bean.getDendrimer().getGeneration());
		dendrimer.put("createdBy", bean.getDendrimer().getCreatedBy());
		dendrimer.put("createdDate", bean.getDendrimer().getCreatedDate());
		composingElements = new ArrayList<SimpleComposingElementBean>();
		if(bean.getDendrimer().getComposingElementCollection()!=null){
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
			simpleCompBean.setFunctionDescription(comp.getInherentFunctions().get(0).getDescription());
			simpleCompBean.setFunctionType(comp.getInherentFunctions().get(0).getType());
			simpleCompBean.setImagingModality(comp.getInherentFunctions().get(0).getImagingFunction().getModality());
			composingElements.add(simpleCompBean);
		}
		dendrimer.put("composingElementCollection", composingElements);
		
	}
		if(bean.getDendrimer().getFileCollection()!=null){
			files = new ArrayList<SimpleFileBean>();
		for(File file : bean.getDendrimer().getFileCollection()){
			fileBean = new SimpleFileBean();
			fileBean.setDescription(file.getDescription());
			fileBean.setType(file.getType());
			fileBean.setTitle(file.getTitle());
			fileBean.setUri(file.getUri());
			fileBean.setUriExternal(file.getUriExternal());
			files.add(fileBean);
		}
		dendrimer.put("fileCollection", files);
	}
		}
		
		if(bean.getPolymer()!=null){
			polymer = new HashMap<Object, Object>();
			polymer.put("description", bean.getPolymer().getDescription());
			polymer.put("crossLinked", bean.getPolymer().getCrossLinked());
			polymer.put("crossLinkDegree", bean.getPolymer().getCrossLinkDegree());
			polymer.put("createdDate", bean.getPolymer().getCreatedDate());
			polymer.put("createdBy", bean.getPolymer().getCreatedBy());
			polymer.put("initiator", bean.getPolymer().getInitiator());
			if(bean.getPolymer().getComposingElementCollection()!=null){
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
				simpleCompBean.setFunctionDescription(comp.getInherentFunctions().get(0).getDescription());
				simpleCompBean.setFunctionType(comp.getInherentFunctions().get(0).getType());
				simpleCompBean.setImagingModality(comp.getTheFunction().getImagingFunction().getModality());
				composingElements.add(simpleCompBean);
			}
			polymer.put("composingElementCollection", composingElements);
			
		}
			if(bean.getPolymer().getFileCollection()!=null){
				files = new ArrayList<SimpleFileBean>();
			for(File file : bean.getPolymer().getFileCollection()){
				fileBean = new SimpleFileBean();
				fileBean.setDescription(file.getDescription());
				fileBean.setType(file.getType());
				fileBean.setTitle(file.getTitle());
				fileBean.setUri(file.getUri());
				fileBean.setUriExternal(file.getUriExternal());
				files.add(fileBean);
			}
			polymer.put("fileCollection", files);
		}
	}
}
}
