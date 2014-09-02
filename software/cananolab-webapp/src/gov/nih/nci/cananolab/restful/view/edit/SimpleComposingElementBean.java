package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;

import java.util.List;
import java.util.Map;

public class SimpleComposingElementBean {

	String type ="";
	String name = "";
	String pubChemDataSourceName = "";
	Long pubChemId = 0L;
	Float value;
	String valueUnit = "";
	String molecularFormulaType = "";
	String molecularFormula = "";
	String description = "";
	Long id = 0L;
	String sampleId = "";
	String modality ="";
	String createdBy="";
	List<Map<String, Object>> inherentFunction;
	

	public List<Map<String, Object>> getInherentFunction() {
		return inherentFunction;
	}
	public void setInherentFunction(List<Map<String, Object>> inherentFunction) {
		this.inherentFunction = inherentFunction;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getModality() {
		return modality;
	}
	public void setModality(String modality) {
		this.modality = modality;
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
	public Long getPubChemId() {
		return pubChemId;
	}
	public void setPubChemId(Long pubChemId) {
		this.pubChemId = pubChemId;
	}
	public Float getValue() {
		return value;
	}
	public void setValue(Float value) {
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSampleId() {
		return sampleId;
	}
	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}
	
	
}
