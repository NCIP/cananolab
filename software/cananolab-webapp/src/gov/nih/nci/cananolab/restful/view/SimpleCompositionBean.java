package gov.nih.nci.cananolab.restful.view;

import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.restful.sample.InitCompositionSetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class SimpleCompositionBean {

	List<String> compositionSections = new ArrayList<String>();
	
	String nanomaterialEntityDescription;
	Boolean nanomaterialEntityWithProperties;
	Map <String, String> properties;
	Map <String, String> description;
	Map <String, String> composingElements;
	Map <String, String> files;
	Map <String, Map <String, String>> dendrimer;
	Map <String, Object> nanomaterialentity;
	
		
	public Map<String, Object> getNanomaterialentity() {
		return nanomaterialentity;
	}


	public void setNanomaterialentity(Map<String, Object> nanomaterialentity) {
		this.nanomaterialentity = nanomaterialentity;
	}


/*	public Map<String, String> getDescription() {
		return description;
	}


	public void setDescription(Map<String, String> description) {
		this.description = description;
	}


	public Map<String, Map<String, String>> getDendrimer() {
		return dendrimer;
	}


	public void setDendrimer(Map<String, Map<String, String>> dendrimer) {
		this.dendrimer = dendrimer;
	}


	public Map<String, String> getFiles() {
		return files;
	}


	public void setFiles(Map<String, String> files) {
		this.files = files;
	}


	public Map<String, String> getComposingElements() {
		return composingElements;
	}


	public void setComposingElements(Map<String, String> composingElements) {
		this.composingElements = composingElements;
	}


	
	public Map<String, String> getProperties() {
		return properties;
	}


	public void setProperties(Map<String, String> properties) {
		properties = properties;
	}
*/

	public List<String> getCompositionSections() {
		return compositionSections;
	}


	public void setCompositionSections(List<String> compositionSections) {
		this.compositionSections = compositionSections;
	}


	public Boolean getNanomaterialEntityWithProperties() {
		return nanomaterialEntityWithProperties;
	}

	public void setNanomaterialEntityWithProperties(
			Boolean nanomaterialEntityWithProperties) {
		this.nanomaterialEntityWithProperties = nanomaterialEntityWithProperties;
	}

	public void transferCompositionBeanForSummaryView(CompositionBean compBean) {
		
		properties = new HashMap<String, String>();
		composingElements = new HashMap<String, String>();
		description = new HashMap<String, String>();
		setCompositionSections(compBean.getCompositionSections());
	//	setNanomaterialEntityDescription(compBean.getNanomaterialEntities().get(0).getDescriptionDisplayName());
		setNanomaterialEntityWithProperties(compBean.getNanomaterialEntities().get(0).isWithProperties());
		properties.put("Branch", compBean.getNanomaterialEntities().get(0).getDendrimer().getBranch());
		properties.put("Generation", compBean.getNanomaterialEntities().get(0).getDendrimer().getGeneration().toString());
	//	setProperties(properties);
		
		description.put("Description", compBean.getNanomaterialEntities().get(0).getDescriptionDisplayName());
//		description.put("withProperties", compBean.getNanomaterialEntities().get(0).isWithProperties());
		
		composingElements.put("Description",compBean.getNanomaterialEntities().get(0).getComposingElements().get(0).getDescription());
		composingElements.put("DisplayName",compBean.getNanomaterialEntities().get(0).getComposingElements().get(0).getDisplayName());
		composingElements.put("PubChemLink",compBean.getNanomaterialEntities().get(0).getComposingElements().get(0).getPubChemLink());
	//	composingElements.put("Function",compBean.getNanomaterialEntities().get(0).getComposingElements().get(0).getFunctionDisplayNames());
		composingElements.put("MolecularFormulaDisplayName",compBean.getNanomaterialEntities().get(0).getComposingElements().get(0).getMolecularFormulaDisplayName());
		composingElements.put("PubChemDataSourceName",compBean.getNanomaterialEntities().get(0).getComposingElements().get(0).getDomain().getPubChemDataSourceName());
	//	composingElements.put("PubChemId",compBean.getNanomaterialEntities().get(0).getComposingElements().get(0).getDomain().getPubChemId().toString());
	//	setComposingElements(composingElements);
		
		files = new HashMap<String, String>();
		files.put("ExternalURI", compBean.getNanomaterialEntities().get(0).getFiles().get(0).getDomainFile().getUriExternal().toString());
		files.put("Title", compBean.getNanomaterialEntities().get(0).getFiles().get(0).getDomainFile().getTitle());
		files.put("URI", compBean.getNanomaterialEntities().get(0).getFiles().get(0).getDomainFile().getUri());
		files.put("KeywordStr", compBean.getNanomaterialEntities().get(0).getFiles().get(0).getKeywordsStr());
		files.put("KeyWordDisplayName", compBean.getNanomaterialEntities().get(0).getFiles().get(0).getKeywordsDisplayName());
		files.put("Description", compBean.getNanomaterialEntities().get(0).getFiles().get(0).getDomainFile().getDescription());
	//	setFiles(files);
		
		dendrimer = new HashMap<String, Map<String,String>>();
		dendrimer.put("Description", description);
		dendrimer.put("Properties", properties);
		dendrimer.put("Composing Elements", composingElements);
		dendrimer.put("Files", files);
	//	setDendrimer(dendrimer);
		
		nanomaterialentity = new HashMap<String, Object>();
		nanomaterialentity.put("dendrimer", dendrimer);
		
	}

	
}
