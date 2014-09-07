package gov.nih.nci.cananolab.restful.view.edit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;

public class SimpleChemicalAssociationBean {

	SimpleFileBean simpleFile;
	
	String type = "";
	String bondType = "";
	String description = "";
	SimpleAssociatedElement associatedElementA;
	SimpleAssociatedElement associatedElementB;
	String sampleId = "";
	List<String> errors;
	List<SimpleFileBean> files;
	
	public List<SimpleFileBean> getFiles() {
		return files;
	}
	public void setFiles(List<SimpleFileBean> files) {
		this.files = files;
	}
	public String getSampleId() {
		return sampleId;
	}
	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}
	public List<String> getErrors() {
		return errors;
	}
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	public SimpleFileBean getSimpleFile() {
		return simpleFile;
	}
	public void setSimpleFile(SimpleFileBean simpleFile) {
		this.simpleFile = simpleFile;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBondType() {
		return bondType;
	}
	public void setBondType(String bondType) {
		this.bondType = bondType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public SimpleAssociatedElement getAssociatedElementA() {
		return associatedElementA;
	}
	public void setAssociatedElementA(SimpleAssociatedElement associatedElementA) {
		this.associatedElementA = associatedElementA;
	}
	public SimpleAssociatedElement getAssociatedElementB() {
		return associatedElementB;
	}
	public void setAssociatedElementB(SimpleAssociatedElement associatedElementB) {
		this.associatedElementB = associatedElementB;
	}
	
	public void trasferToSimpleChemicalAssociation(ChemicalAssociationBean chemBean, HttpServletRequest request){
		
		setType(chemBean.getType());
		setDescription(chemBean.getDescription());
		setBondType(chemBean.getAttachment().getBondType());
		associatedElementA = new SimpleAssociatedElement();
		
		associatedElementA.setClassName(chemBean.getAssociatedElementA().getClassName());
		associatedElementA.setCompositionType(chemBean.getAssociatedElementA().getCompositionType());
		associatedElementA.setEntityDisplayName(chemBean.getAssociatedElementA().getEntityDisplayName());
		associatedElementA.setEntityId(chemBean.getAssociatedElementA().getEntityId());
		
		SimpleComposingElementBean comp = new SimpleComposingElementBean();
		comp.setId(chemBean.getAssociatedElementA().getComposingElement().getId());
		comp.setPubChemDataSourceName(chemBean.getAssociatedElementA().getComposingElement().getPubChemDataSourceName());
		comp.setPubChemId(chemBean.getAssociatedElementA().getComposingElement().getPubChemId());
		comp.setDescription(chemBean.getAssociatedElementA().getComposingElement().getDescription());
		comp.setCreatedBy(chemBean.getAssociatedElementA().getComposingElement().getCreatedBy());
		comp.setMolecularFormula(chemBean.getAssociatedElementA().getComposingElement().getMolecularFormula());
		comp.setMolecularFormulaType(chemBean.getAssociatedElementA().getComposingElement().getMolecularFormulaType());
		comp.setName(chemBean.getAssociatedElementA().getComposingElement().getName());
		comp.setType(chemBean.getAssociatedElementA().getComposingElement().getType());
		comp.setValue(chemBean.getAssociatedElementA().getComposingElement().getValue());
		comp.setValueUnit(chemBean.getAssociatedElementA().getComposingElement().getValueUnit());
		comp.setId(chemBean.getAssociatedElementA().getComposingElement().getId());
		
//		Map<String, Object> inherentFunction = new HashMap<String, Object>();
//		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
//		for(Function fun : chemBean.getAssociatedElementA().getComposingElement().getInherentFunctionCollection()){
//			inherentFunction.put("description", fun.getDescription());
//			inherentFunction.put("id", fun.getId());
//			list.add(inherentFunction);
//		}
//		comp.setInherentFunction(list);
		associatedElementA.setComposingElement(comp);
		setAssociatedElementA(associatedElementA);
		
		associatedElementB = new SimpleAssociatedElement();
		
		associatedElementB.setClassName(chemBean.getAssociatedElementB().getClassName());
		associatedElementB.setCompositionType(chemBean.getAssociatedElementB().getCompositionType());
		associatedElementB.setEntityDisplayName(chemBean.getAssociatedElementB().getEntityDisplayName());
		associatedElementB.setEntityId(chemBean.getAssociatedElementB().getEntityId());
		
		comp = new SimpleComposingElementBean();
		comp.setId(chemBean.getAssociatedElementB().getComposingElement().getId());
		comp.setPubChemDataSourceName(chemBean.getAssociatedElementB().getComposingElement().getPubChemDataSourceName());
		comp.setPubChemId(chemBean.getAssociatedElementB().getComposingElement().getPubChemId());
		comp.setDescription(chemBean.getAssociatedElementB().getComposingElement().getDescription());
		comp.setCreatedBy(chemBean.getAssociatedElementB().getComposingElement().getCreatedBy());
		comp.setMolecularFormula(chemBean.getAssociatedElementB().getComposingElement().getMolecularFormula());
		comp.setMolecularFormulaType(chemBean.getAssociatedElementB().getComposingElement().getMolecularFormulaType());
		comp.setName(chemBean.getAssociatedElementB().getComposingElement().getName());
		comp.setType(chemBean.getAssociatedElementB().getComposingElement().getType());
		comp.setValue(chemBean.getAssociatedElementB().getComposingElement().getValue());
		comp.setValueUnit(chemBean.getAssociatedElementB().getComposingElement().getValueUnit());
		comp.setId(chemBean.getAssociatedElementB().getComposingElement().getId());
		
//		inherentFunction = new HashMap<String, Object>();
//		list = new ArrayList<Map<String,Object>>();
//		for(Function fun : chemBean.getAssociatedElementB().getComposingElement().getInherentFunctionCollection()){
//			inherentFunction.put("description", fun.getDescription());
//			inherentFunction.put("id", fun.getId());
//			list.add(inherentFunction);
//		}
//		comp.setInherentFunction(list);
		associatedElementB.setComposingElement(comp);
		
		setAssociatedElementB(associatedElementB);
	}
}
