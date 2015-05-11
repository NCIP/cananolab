package gov.nih.nci.cananolab.restful.view;

import gov.nih.nci.cananolab.restful.bean.SimpleAdvancedResultCellUnitBean;

import java.util.ArrayList;
import java.util.List;

public class SimpleAdvancedResultRow {
	
	long sampleId;
	String sampleName;
	List<String> pointOfContact;
	
	List<String> nanomaterialEntity;

	List<String> functionalizaingEntity;
	List<String> function;
	
	//List<SimpleAdvancedResultCellUnitBean> physicalChemicalChar = new ArrayList<SimpleAdvancedResultCellUnitBean>();
	List<String> physicalChemicalChar;
	List<String> invivoChar;
	List<String> invitroChar;
	List<String> exvivoChar;
	String nanoEntityDesc;
	
	
	public String getNanoEntityDesc() {
		return nanoEntityDesc;
	}
	public void setNanoEntityDesc(String nanoEntityDesc) {
		this.nanoEntityDesc = nanoEntityDesc;
	}
	public long getSampleId() {
		return sampleId;
	}
	public void setSampleId(long sampleId) {
		this.sampleId = sampleId;
	}
	public String getSampleName() {
		return sampleName;
	}
	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}
	public List<String> getPointOfContact() {
		return pointOfContact;
	}
	public void setPointOfContact(List<String> pointOfContact) {
		this.pointOfContact = pointOfContact;
	}
	public List<String> getNanomaterialEntity() {
		return nanomaterialEntity;
	}
	public void setNanomaterialEntity(List<String> nanomaterialEntity) {
		this.nanomaterialEntity = nanomaterialEntity;
	}
	public List<String> getFunctionalizaingEntity() {
		return functionalizaingEntity;
	}
	public void setFunctionalizaingEntity(List<String> functionalizaingEntity) {
		this.functionalizaingEntity = functionalizaingEntity;
	}
	public List<String> getFunction() {
		return function;
	}
	public void setFunction(List<String> function) {
		this.function = function;
	}
	public List<String> getPhysicalChemicalChar() {
		return physicalChemicalChar;
	}
	public void setPhysicalChemicalChar(List<String> physicalChemicalChar) {
		this.physicalChemicalChar = physicalChemicalChar;
	}
	public List<String> getInvivoChar() {
		return invivoChar;
	}
	public void setInvivoChar(List<String> invivoChar) {
		this.invivoChar = invivoChar;
	}
	public List<String> getInvitroChar() {
		return invitroChar;
	}
	public void setInvitroChar(List<String> invitroChar) {
		this.invitroChar = invitroChar;
	}
	public List<String> getExvivoChar() {
		return exvivoChar;
	}
	public void setExvivoChar(List<String> exvivoChar) {
		this.exvivoChar = exvivoChar;
	}
	
}
