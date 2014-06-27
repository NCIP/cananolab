package gov.nih.nci.cananolab.ui.form;

import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;

public class CompositionForm {
	
	CompositionBean comp;
	NanomaterialEntityBean nanomaterialEntity;
	FunctionalizingEntityBean functionalizingEntity;
	ChemicalAssociationBean assoc;
	
	String sampleId;
	String dataId;
	String[] otherSamples;
	int page;
	String dispatch;
	public CompositionBean getComp() {
		return comp;
	}
	public void setComp(CompositionBean comp) {
		this.comp = comp;
	}
	public NanomaterialEntityBean getNanomaterialEntity() {
		return nanomaterialEntity;
	}
	public void setNanomaterialEntity(NanomaterialEntityBean nanomaterialEntity) {
		this.nanomaterialEntity = nanomaterialEntity;
	}
	public FunctionalizingEntityBean getFunctionalizingEntity() {
		return functionalizingEntity;
	}
	public void setFunctionalizingEntity(
			FunctionalizingEntityBean functionalizingEntity) {
		this.functionalizingEntity = functionalizingEntity;
	}
	public ChemicalAssociationBean getAssoc() {
		return assoc;
	}
	public void setAssoc(ChemicalAssociationBean assoc) {
		this.assoc = assoc;
	}
	public String getSampleId() {
		return sampleId;
	}
	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	public String[] getOtherSamples() {
		return otherSamples;
	}
	public void setOtherSamples(String[] otherSamples) {
		this.otherSamples = otherSamples;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public String getDispatch() {
		return dispatch;
	}
	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}

	
}
