package gov.nih.nci.cananolab.restful.view.edit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
	
	Polymer polymer;

	Biopolymer biopolymer;

	Dendrimer dendrimer;

	CarbonNanotube carbonNanotube;

	Liposome liposome;

	Emulsion emulsion;

	Fullerene fullerene;

	List<ComposingElementBean> composingElements;

	NanomaterialEntity domainEntity;

	boolean withProperties = false;

	String isPolymerized;

	String isCrossLinked;
	
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

	public Polymer getPolymer() {
		return polymer;
	}

	public void setPolymer(Polymer polymer) {
		this.polymer = polymer;
	}

	public Biopolymer getBiopolymer() {
		return biopolymer;
	}

	public void setBiopolymer(Biopolymer biopolymer) {
		this.biopolymer = biopolymer;
	}

	public Dendrimer getDendrimer() {
		return dendrimer;
	}

	public void setDendrimer(Dendrimer dendrimer) {
		this.dendrimer = dendrimer;
	}

	public CarbonNanotube getCarbonNanotube() {
		return carbonNanotube;
	}

	public void setCarbonNanotube(CarbonNanotube carbonNanotube) {
		this.carbonNanotube = carbonNanotube;
	}

	public Liposome getLiposome() {
		return liposome;
	}

	public void setLiposome(Liposome liposome) {
		this.liposome = liposome;
	}

	public Emulsion getEmulsion() {
		return emulsion;
	}

	public void setEmulsion(Emulsion emulsion) {
		this.emulsion = emulsion;
	}

	public Fullerene getFullerene() {
		return fullerene;
	}

	public void setFullerene(Fullerene fullerene) {
		this.fullerene = fullerene;
	}

	public List<ComposingElementBean> getComposingElements() {
		return composingElements;
	}

	public void setComposingElements(List<ComposingElementBean> composingElements) {
		this.composingElements = composingElements;
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
		for(ComposingElement comp : bean.getDendrimer().getComposingElementCollection()){
			simpleCompBean.setDescription(comp.getDescription());
			simpleCompBean.setId(comp.getId());
			simpleCompBean.setMolecularFormula(comp.getMolecularFormula());
			simpleCompBean.setMolecularFormulaType(comp.getMolecularFormulaType());
			simpleCompBean.setName(comp.getName());
			simpleCompBean.setPubChemDataSourceName(comp.getPubChemDataSourceName());
			simpleCompBean.setPubChemId(comp.getPubChemId());
			simpleCompBean.setType(comp.getType());
			simpleCompBean.setValue(comp.getValue());
			simpleCompBean.setValueUnit(comp.getValueUnit());
		}
		
		
	}
	
}
