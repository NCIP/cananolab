package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.restful.protocol.InitProtocolSetup;
import gov.nih.nci.cananolab.restful.sample.InitCharacterizationSetup;
import gov.nih.nci.cananolab.restful.sample.InitSampleSetup;
import gov.nih.nci.cananolab.service.sample.SampleService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

public class SimpleCharacterizationEditBean {
	
	String type;
	String name;
	long parentSampleId;
	
	long charId;	
	
	String assayType;
	String protocolNameVersion;
	long protocolId;
	String characterizationSource;
	Date characterizationDate;
	
	List<String> charNamesForCurrentType;
	
	String designMethodsDescription;
	
	
	//for Edit
	
	SimpleTechniqueAndInstrument techniqueInstruments = new SimpleTechniqueAndInstrument();
	
	
	//When saving, could propagate to other samples
	List<String> selectedOtherSampleNames;
	boolean copyToOtherSamples;
	
	///

	
	
	List<String> charTypesLookup;
	//List<String> characterizationNameLookup;
	//List<String> AssayTypeLookup;
	List<SimpleProtocol> protocolLookup;
	List<SimplePOC> charSourceLookup;
	List<String> otherSampleNameLookup;
	
	Map<String, List<String>> assayTypesByCharNameLookup = new HashMap<String, List<String>>();
	
	List<String> errors = new ArrayList<String>();
	List<String> messages;
	
	public void transferCharacterizationEditData(HttpServletRequest request, CharacterizationBean charBean, String sampleId) 
	throws Exception {
		
		//TODO: handle type=other than in the list
		this.type = charBean.getCharacterizationType();
		this.parentSampleId = Long.parseLong(sampleId);
		
		transferCharBeanData(charBean);
		
		setupLookups(request, charBean, sampleId);
	}
	
	protected void transferCharBeanData(CharacterizationBean charBean) {
		if (charBean.getDomainChar() == null) 
			return;

		Long id = charBean.getDomainChar().getId();
		if (id != null) {
			this.charId = id.longValue();
			this.assayType = charBean.getAssayType();
		}
		
		transferExperimentConfigs(charBean.getExperimentConfigs());
			
		
	}
	
	protected void transferExperimentConfigs(List<ExperimentConfigBean> expConfigs) {
		if (expConfigs == null) return;
	
		for (ExperimentConfigBean expConfig : expConfigs) {
			//SimpleExperiment
			expConfig.getDescription();
			expConfig.getTechniqueDisplayName();
			expConfig.getDomain().getId();
			
			expConfig.getInstruments();
		}
	}
	
	protected void setupLookups(HttpServletRequest request, CharacterizationBean charBean, String sampleId) 
			throws Exception {
	
		String charType = charBean.getCharacterizationType();
		
		charTypesLookup = InitCharacterizationSetup
				.getInstance().getCharacterizationTypes(request);
		charTypesLookup.add("[other]");

		charNamesForCurrentType = new ArrayList<String>();
		SortedSet<String> charNames = InitCharacterizationSetup
				.getInstance().getCharNamesByCharType(request, charType);
		charNamesForCurrentType.addAll(charNames);
		charNamesForCurrentType.add("[other]");
		
		setProtocolLookup(request, charType);
		setPOCLookup(request, sampleId);
		otherSampleNameLookup = InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId);
		
		this.techniqueInstruments.setupLookups(request);
	}
	
	protected void setPOCLookup(HttpServletRequest request, String sampleId) 
	throws Exception {
		charSourceLookup = new ArrayList<SimplePOC>();
		SampleService service = (SampleService)request.getSession().getAttribute("sampleService");
		List<PointOfContactBean> pocs = service
				.findPointOfContactsBySampleId(sampleId);
		
		if (pocs == null) return;
			
		for (PointOfContactBean poc : pocs) {
			SimplePOC simplePOC = new SimplePOC();
			simplePOC.transferFromPointOfContactBean(poc);
			charSourceLookup.add(simplePOC);
		}
		
		SimplePOC other = new SimplePOC();
		other.setDisplayName("[other]");
		charSourceLookup.add(other);
	}

	protected void setProtocolLookup(HttpServletRequest request, String charType) 
	throws Exception {
		protocolLookup = new ArrayList<SimpleProtocol>();
		List<ProtocolBean> protoBeans = InitProtocolSetup.getInstance()
				.getProtocolsByChar(request, charType);
		
		if (protoBeans == null)
			return;
		
		for (ProtocolBean protoBean : protoBeans) {
			SimpleProtocol simpleProto = new SimpleProtocol();
			simpleProto.transferFromProtocolBean(protoBean);
			protocolLookup.add(simpleProto);
		}
	}
	
	
	public long getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(long protocolId) {
		this.protocolId = protocolId;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public boolean isCopyToOtherSamples() {
		return copyToOtherSamples;
	}

	public void setCopyToOtherSamples(boolean copyToOtherSamples) {
		this.copyToOtherSamples = copyToOtherSamples;
	}

	public List<String> getSelectedOtherSampleNames() {
		return selectedOtherSampleNames;
	}

	public void setSelectedOtherSampleNames(List<String> selectedOtherSampleNames) {
		this.selectedOtherSampleNames = selectedOtherSampleNames;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public Map<String, List<String>> getAssayTypesByCharNameLookup() {
		return assayTypesByCharNameLookup;
	}

	public void setAssayTypesByCharNameLookup(
			Map<String, List<String>> assayTypesByCharNameLookup) {
		this.assayTypesByCharNameLookup = assayTypesByCharNameLookup;
	}

	public long getCharId() {
		return charId;
	}

	public void setCharId(long charId) {
		this.charId = charId;
	}

	public List<SimpleProtocol> getProtocolLookup() {
		return protocolLookup;
	}

	public void setProtocolLookup(List<SimpleProtocol> protocolLookup) {
		this.protocolLookup = protocolLookup;
	}

	public List<String> getOtherSampleNameLookup() {
		return otherSampleNameLookup;
	}

	public void setOtherSampleNameLookup(List<String> otherSampleNameLookup) {
		this.otherSampleNameLookup = otherSampleNameLookup;
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
	
	

//	public List<String> getAssayTypeLookup() {
//		return AssayTypeLookup;
//	}
//	public void setAssayTypeLookup(List<String> assayTypeLookup) {
//		AssayTypeLookup = assayTypeLookup;
//	}	
	
	

	public String getDesignMethodsDescription() {
		return designMethodsDescription;
	}



	public List<String> getCharNamesForCurrentType() {
		return charNamesForCurrentType;
	}



	public void setCharNamesForCurrentType(List<String> charNamesForCurrentType) {
		this.charNamesForCurrentType = charNamesForCurrentType;
	}



	public void setDesignMethodsDescription(String designMethodsDescription) {
		this.designMethodsDescription = designMethodsDescription;
	}



	public List<SimplePOC> getCharSourceLookup() {
		return charSourceLookup;
	}

	public void setCharSourceLookup(List<SimplePOC> charSourceLookup) {
		this.charSourceLookup = charSourceLookup;
	}

	public SimpleTechniqueAndInstrument getTechniqueInstruments() {
		return techniqueInstruments;
	}

	public void setTechniqueInstruments(
			SimpleTechniqueAndInstrument techniqueInstruments) {
		this.techniqueInstruments = techniqueInstruments;
	}

	public class SimpleProtocol {
		//Proto needs:
				//property="achar.protocolBean.fileBean.domainFile.uri" 
				//property="achar.protocolBean.domain.id"
				//characterizationForm.map.achar.protocolBean.fileBean.domainFile.id
				//characterizationForm.map.achar.protocolBean.fileBean.domainFile.uri
		
		long domainId;
		long domainFileId;
		String domainFileUri;
		String displayName;
		
		public long getDomainId() {
			return domainId;
		}

		public void setDomainId(long domainId) {
			this.domainId = domainId;
		}

		public long getDomainFileId() {
			return domainFileId;
		}

		public void setDomainFileId(long domainFileId) {
			this.domainFileId = domainFileId;
		}

		public String getDomainFileUri() {
			return domainFileUri;
		}

		public void setDomainFileUri(String domainFileUri) {
			this.domainFileUri = domainFileUri;
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		
		
		public void transferFromProtocolBean(ProtocolBean protoBean) {
			if (protoBean == null) return;
			
			domainId = protoBean.getDomain().getId();
			FileBean domainFile = protoBean.getFileBean();
			if (domainFile != null && domainFile.getDomainFile() != null) {
				if (domainFile.getDomainFile().getId() != null)
					domainFileId = domainFile.getDomainFile().getId();
				domainFileUri = domainFile.getDomainFile().getUri();
			}
			displayName = protoBean.getDisplayName();
		}
	}
	
	public class SimplePOC {
		long id;
		String displayName;
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getDisplayName() {
			return displayName;
		}
		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
		
		public void transferFromPointOfContactBean(PointOfContactBean pocBean) {
			id = pocBean.getDomain().getId();
			displayName = pocBean.getDisplayName();
		}
	}
}
