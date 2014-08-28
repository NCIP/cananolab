package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.restful.sample.InitCharacterizationSetup;

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
	
	
	public SimpleCharacterizationEditBean(HttpServletRequest request) 
	throws Exception {
		//create dummpy
		charTypesLookup = InitCharacterizationSetup
				.getInstance().getCharacterizationTypes(request);;
		
		characterizationNameLookup = new ArrayList<String>();
		
		SortedSet<String> charNames = InitCharacterizationSetup
				.getInstance().getCharNamesByCharType(request, "physico-chemical");
		
		characterizationNameLookup.addAll(charNames);
		
		AssayTypeLookup = new ArrayList<String>();
		protocolNameVersionLookup = new ArrayList<String>();
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
