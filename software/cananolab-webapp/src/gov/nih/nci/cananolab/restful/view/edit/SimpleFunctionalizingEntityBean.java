package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleFunctionalizingEntityBean {

	SimpleFileBean fileBean;
	SimpleFunctionBean simpleFunctionBean;
	String type = "";
	String name = "";
	String pubChemDataSourceName = "";
	String pubChemId = "";
	String value = "";
	String valueUnit = "";
	String molecularFormulaType = "";
	String molecularFormula = "";
	String activationMethodType = "";
	String activationEffect = "";
	String description = "";
	String sampleId = "";
	Map<Object, Object> domainEntity;
	List<String> errors;
	List<SimpleFunctionBean> functionList;
	List<SimpleFileBean> fileList;
	
	public String getSampleId() {
		return sampleId;
	}
	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}
	public List<SimpleFunctionBean> getFunctionList() {
		return functionList;
	}
	public void setFunctionList(List<SimpleFunctionBean> functionList) {
		this.functionList = functionList;
	}
	public List<SimpleFileBean> getFileList() {
		return fileList;
	}
	public void setFileList(List<SimpleFileBean> fileList) {
		this.fileList = fileList;
	}
	public Map<Object, Object> getDomainEntity() {
		return domainEntity;
	}
	public void setDomainEntity(Map<Object, Object> domainEntity) {
		this.domainEntity = domainEntity;
	}
	public SimpleFunctionBean getSimpleFunctionBean() {
		return simpleFunctionBean;
	}
	public void setSimpleFunctionBean(SimpleFunctionBean simpleFunctionBean) {
		this.simpleFunctionBean = simpleFunctionBean;
	}
	public List<String> getErrors() {
		return errors;
	}
	public void setErrors(List<String> errors) {
		this.errors = errors;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPubChemDataSourceName() {
		return pubChemDataSourceName;
	}
	public void setPubChemDataSourceName(String pubChemDataSourceName) {
		this.pubChemDataSourceName = pubChemDataSourceName;
	}
	public String getPubChemId() {
		return pubChemId;
	}
	public void setPubChemId(String pubChemId) {
		this.pubChemId = pubChemId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValueUnit() {
		return valueUnit;
	}
	public void setValueUnit(String valueUnit) {
		this.valueUnit = valueUnit;
	}
	public String getMolecularFormulaType() {
		return molecularFormulaType;
	}
	public void setMolecularFormulaType(String molecularFormulaType) {
		this.molecularFormulaType = molecularFormulaType;
	}
	public String getMolecularFormula() {
		return molecularFormula;
	}
	public void setMolecularFormula(String molecularFormula) {
		this.molecularFormula = molecularFormula;
	}
	public String getActivationMethodType() {
		return activationMethodType;
	}
	public void setActivationMethodType(String activationMethodType) {
		this.activationMethodType = activationMethodType;
	}
	public String getActivationEffect() {
		return activationEffect;
	}
	public void setActivationEffect(String activationEffect) {
		this.activationEffect = activationEffect;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void tranferSimpleFunctionalizingBean(FunctionalizingEntityBean bean) {
		domainEntity = new HashMap<Object, Object>();
		this.setDescription(bean.getDescription());
		this.setName(bean.getName());
		this.setMolecularFormula(bean.getMolecularFormula());
		
		this.setType(bean.getType());
		this.setMolecularFormulaType(bean.getMolecularFormulaType());
		
		List<SimpleFileBean> fileList = new ArrayList<SimpleFileBean>();
		for(FileBean files : bean.getFiles()){
			fileBean = new SimpleFileBean();

			fileBean.setDescription(files.getDescription());
			fileBean.setId(files.getDomainFile().getId());
			fileBean.setKeywordsStr(files.getKeywordsStr());
			fileBean.setTitle(files.getDomainFile().getTitle());
			fileBean.setType(files.getDomainFile().getType());
			fileBean.setUri(files.getDomainFile().getUri());
			fileBean.setUriExternal(files.getDomainFile().getUriExternal());
			fileList.add(fileBean);
		}
		setFileList(fileList);
		
		List<SimpleFunctionBean> funcList = new ArrayList<SimpleFunctionBean>();
		for(FunctionBean funcBean : bean.getFunctions()){
			simpleFunctionBean = new SimpleFunctionBean();
			
			simpleFunctionBean.setDescription(funcBean.getDescription());
			simpleFunctionBean.setId(funcBean.getId());
			simpleFunctionBean.setType(funcBean.getType());
			simpleFunctionBean.setTargetDescription(funcBean.getTheTarget().getDescription());
			simpleFunctionBean.setTargetId(funcBean.getTheTarget().getId());
			simpleFunctionBean.setTargetName(funcBean.getTheTarget().getName());
			simpleFunctionBean.setTargetType(funcBean.getTheTarget().getType());
			simpleFunctionBean.setModality(funcBean.getImagingFunction().getModality());
			funcList.add(simpleFunctionBean);
		}
		setFunctionList(funcList);
		
		
		if(bean.getType().equalsIgnoreCase("SmallMolecule")){
			domainEntity.put("alternateName", bean.getSmallMolecule().getAlternateName());
		}
		
		if(bean.getType().equalsIgnoreCase("Biopolymer")){
			domainEntity.put("type", bean.getBiopolymer().getType());
			domainEntity.put("sequence", bean.getBiopolymer().getSequence());
		}
		
		if(bean.getType().equalsIgnoreCase("Antibody")){
			domainEntity.put("type", bean.getAntibody().getType());
			domainEntity.put("isoType", bean.getAntibody().getIsotype());
			domainEntity.put("species", bean.getAntibody().getSpecies());
		}
		
	}
	
}
