package gov.nih.nci.cananolab.restful.view;

import gov.nih.nci.cananolab.restful.bean.SimpleAdvancedResultCellUnitBean;

import java.util.ArrayList;
import java.util.List;

public class SimpleAdvancedResultRow {
	
	long sampleId;
	String sampleName;
	String pointOfContact;
	
	String nanomaterialEntity;

	String functionalizaingEntity;
	String function;
	
	List<SimpleAdvancedResultCellUnitBean> physicalChemicalChar = new ArrayList<SimpleAdvancedResultCellUnitBean>();
	String invivoChar;
	String invitroChar;
	String exvivoChar;
	
	
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
	public String getPointOfContact() {
		return pointOfContact;
	}
	public void setPointOfContact(String pointOfContact) {
		this.pointOfContact = pointOfContact;
	}
	public String getNanomaterialEntity() {
		return nanomaterialEntity;
	}
	public void setNanomaterialEntity(String nanomaterialEntity) {
		this.nanomaterialEntity = nanomaterialEntity;
	}
	public String getFunctionalizaingEntity() {
		return functionalizaingEntity;
	}
	public void setFunctionalizaingEntity(String functionalizaingEntity) {
		this.functionalizaingEntity = functionalizaingEntity;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	
	
	public List<SimpleAdvancedResultCellUnitBean> getPhysicalChemicalChar() {
		return physicalChemicalChar;
	}
	public void setPhysicalChemicalChar(
			List<SimpleAdvancedResultCellUnitBean> physicalChemicalChar) {
		this.physicalChemicalChar = physicalChemicalChar;
	}
	public String getExvivoChar() {
		return exvivoChar;
	}
	public void setExvivoChar(String exvivoChar) {
		this.exvivoChar = exvivoChar;
	}
	public String getInvitroChar() {
		return invitroChar;
	}
	public void setInvitroChar(String invitroChar) {
		this.invitroChar = invitroChar;
	}
	public String getInvivoChar() {
		return invivoChar;
	}
	public void setInvivoChar(String invivoChar) {
		this.invivoChar = invivoChar;
	}
	
	public class CellUnit {
		String displayName;
		long dataId;
		public String getDisplayName() {
			return displayName;
		}
		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
		public long getDataId() {
			return dataId;
		}
		public void setDataId(long dataId) {
			this.dataId = dataId;
		}
		
		
	}
	
}
