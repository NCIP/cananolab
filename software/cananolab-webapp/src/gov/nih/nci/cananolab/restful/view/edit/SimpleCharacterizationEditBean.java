package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.restful.core.InitSetup;
import gov.nih.nci.cananolab.restful.sample.InitCharacterizationSetup;
import gov.nih.nci.cananolab.service.sample.SampleService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

public class SimpleCharacterizationEditBean {
	
	String type;
	String name;
	long parentSampleId;
	
	String assayType;
	String protocolNameVersion;
	String characterizationSource;
	Date characterizationDate;
	
	List<String> charTypesLookup;
	List<String> characterizationNameLookup;
	List<String> AssayTypeLookup;
	List<String> protocolNameVersionLookup;
	List<String> charSourceLookup;
	
	public void transferCharacterizationEditData(HttpServletRequest request, CharacterizationBean charBean, String sampleId) 
	throws Exception {
		
		setupLookups(request, charBean, sampleId);
	}
	
	protected void setupLookups(HttpServletRequest request, CharacterizationBean charBean, String sampleId) 
			throws Exception {
		//create dummpy
		charTypesLookup = InitCharacterizationSetup
				.getInstance().getCharacterizationTypes(request);

		characterizationNameLookup = new ArrayList<String>();
		SortedSet<String> charNames = InitCharacterizationSetup
				.getInstance().getCharNamesByCharType(request, charBean.getCharacterizationType());
		characterizationNameLookup.addAll(charNames);

		//This is empty for new char
		AssayTypeLookup = new ArrayList<String>();
		// setup Assay Type drop down.
//				InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
//						"charNameAssays", charBean.getCharacterizationName(),
//						"assayType", "otherAssayType", true);
		
		
		
		//Proto needs:
		//property="achar.protocolBean.fileBean.domainFile.uri" 
		//property="achar.protocolBean.domain.id"
		//characterizationForm.map.achar.protocolBean.fileBean.domainFile.id
		//characterizationForm.map.achar.protocolBean.fileBean.domainFile.uri
		
		protocolNameVersionLookup = new ArrayList<String>();
		
		SampleService service = (SampleService)request.getSession().getAttribute("sampleService");
		List<PointOfContactBean> pocs = service
				.findPointOfContactsBySampleId(sampleId);
		//source = poc
		//needs:
		//achar.pocBean.domain.id
		//poc display name: 
		charSourceLookup = new ArrayList<String>();
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
	public String getAssayType() {
		return assayType;
	}
	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}
	public String getProtocolNameVersion() {
		return protocolNameVersion;
	}
	public void setProtocolNameVersion(String protocolNameVersion) {
		this.protocolNameVersion = protocolNameVersion;
	}
	public String getCharacterizationSource() {
		return characterizationSource;
	}
	public void setCharacterizationSource(String characterizationSource) {
		this.characterizationSource = characterizationSource;
	}
	public Date getCharacterizationDate() {
		return characterizationDate;
	}
	public void setCharacterizationDate(Date characterizationDate) {
		this.characterizationDate = characterizationDate;
	}
	public long getParentSampleId() {
		return parentSampleId;
	}
	public void setParentSampleId(long parentSampleId) {
		this.parentSampleId = parentSampleId;
	}
	public List<String> getCharTypesLookup() {
		return charTypesLookup;
	}
	public void setCharTypesLookup(List<String> charTypesLookup) {
		this.charTypesLookup = charTypesLookup;
	}
	public List<String> getCharacterizationNameLookup() {
		return characterizationNameLookup;
	}
	public void setCharacterizationNameLookup(
			List<String> characterizationNameLookup) {
		this.characterizationNameLookup = characterizationNameLookup;
	}
	public List<String> getAssayTypeLookup() {
		return AssayTypeLookup;
	}
	public void setAssayTypeLookup(List<String> assayTypeLookup) {
		AssayTypeLookup = assayTypeLookup;
	}
	public List<String> getProtocolNameVersionLookup() {
		return protocolNameVersionLookup;
	}
	public void setProtocolNameVersionLookup(List<String> protocolNameVersionLookup) {
		this.protocolNameVersionLookup = protocolNameVersionLookup;
	}
	public List<String> getCharSourceLookup() {
		return charSourceLookup;
	}
	public void setCharSourceLookup(List<String> charSourceLookup) {
		this.charSourceLookup = charSourceLookup;
	}
	
	

}
