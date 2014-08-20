package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;

import javax.servlet.http.HttpServletRequest;

public class SimpleNanomaterialEntityBean {

	SimpleComposingElementBean simpleCompBean;
	SimpleFileBean fileBean;
	String type = "";
	String id = "";
	String description = "";
	String sampleId = "";
	boolean userDeletable = false;
	boolean userUpdatable = false;
	
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	public void transferNanoMaterialEntityBeanToSimple(
			NanomaterialEntityBean bean, HttpServletRequest httpRequest) {
		simpleCompBean = new SimpleComposingElementBean();
		fileBean = new SimpleFileBean();
		
		simpleCompBean.setDescription(bean.getTheComposingElement().getDescription());
		simpleCompBean.setFunctionDescription(bean.getTheComposingElement().getTheFunction().getDescription());
		simpleCompBean.setFunctionId(bean.getTheComposingElement().getTheFunction().getId());
		simpleCompBean.setFunctionType(bean.getTheComposingElement().getTheFunction().getType());
		simpleCompBean.setId(bean.getTheComposingElement().getDomainId());
		simpleCompBean.setImagingModality(bean.getTheComposingElement().getTheFunction().getImagingFunction().getModality());
		simpleCompBean.setMolecularFormula(bean.getTheComposingElement().getDomain().getMolecularFormula());
		simpleCompBean.setMolecularFormulaType(bean.getTheComposingElement().getDomain().getMolecularFormulaType());
		simpleCompBean.setName(bean.getTheComposingElement().getDomain().getName());
		simpleCompBean.setPubChemDataSourceName(bean.getTheComposingElement().getDomain().getPubChemDataSourceName());
		simpleCompBean.setPubChemId(bean.getTheComposingElement().getDomain().getPubChemId());
		simpleCompBean.setType(bean.getTheComposingElement().getDomain().getType());
		simpleCompBean.setValue(bean.getTheComposingElement().getDomain().getValue());
		simpleCompBean.setValueUnit(bean.getTheComposingElement().getDomain().getValueUnit());
		
		setSimpleCompBean(simpleCompBean);
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
		
		
		
	}
	
;

}
