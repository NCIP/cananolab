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
	Map <String, Object> properties;
	Map <String, Object> description;
	Map <String, Object> composingElements;
	Map <String, Object> files;
	Map <String, Object> dendrimer;
	Map <String, Object> nanomaterialentity;
	
	Map <String, Object> functionalizingentity;
	Map <String, Object> smallMolecule;
	
	Map <String, Object> chemicalassociation;
	Map <String, Object> Association;
	
	Map <String, Object> compositionfile;
	Map <String, Object> image;
		
	public Map<String, Object> getNanomaterialentity() {
		return nanomaterialentity;
	}

	public void setNanomaterialentity(Map<String, Object> nanomaterialentity) {
		this.nanomaterialentity = nanomaterialentity;
	}

	
	public Map<String, Object> getChemicalassociation() {
		return chemicalassociation;
	}

	public void setChemicalassociation(Map<String, Object> chemicalassociation) {
		this.chemicalassociation = chemicalassociation;
	}

	public Map<String, Object> getFunctionalizingentity() {
		return functionalizingentity;
	}

	public void setFunctionalizingentity(Map<String, Object> functionalizingentity) {
		this.functionalizingentity = functionalizingentity;
	}

	public Map<String, Object> getCompositionfile() {
		return compositionfile;
	}

	public void setCompositionfile(Map<String, Object> compositionfile) {
		this.compositionfile = compositionfile;
	}

	public List<String> getCompositionSections() {
		return compositionSections;
	}

	public void setCompositionSections(List<String> compositionSections) {
		this.compositionSections = compositionSections;
	}

	public void transferCompositionBeanForSummaryView(CompositionBean compBean) {
		
		//NanoMaterial Entity
		
		properties = new HashMap<String, Object>();
		composingElements = new HashMap<String, Object>();
		description = new HashMap<String, Object>();
		setCompositionSections(compBean.getCompositionSections());
		properties.put("Branch", compBean.getNanomaterialEntities().get(0).getDendrimer().getBranch());
		properties.put("Generation", compBean.getNanomaterialEntities().get(0).getDendrimer().getGeneration().toString());
		
		description.put("Description", compBean.getNanomaterialEntities().get(0).getDescriptionDisplayName());
		description.put("withProperties", compBean.getNanomaterialEntities().get(0).isWithProperties());
		
		composingElements.put("Description",compBean.getNanomaterialEntities().get(0).getComposingElements().get(0).getDescription());
		composingElements.put("DisplayName",compBean.getNanomaterialEntities().get(0).getComposingElements().get(0).getDisplayName());
		composingElements.put("PubChemLink",compBean.getNanomaterialEntities().get(0).getComposingElements().get(0).getPubChemLink());
		composingElements.put("Function",compBean.getNanomaterialEntities().get(0).getComposingElements().get(0).getFunctionDisplayNames());
		composingElements.put("MolecularFormulaDisplayName",compBean.getNanomaterialEntities().get(0).getComposingElements().get(0).getMolecularFormulaDisplayName());
		composingElements.put("PubChemDataSourceName",compBean.getNanomaterialEntities().get(0).getComposingElements().get(0).getDomain().getPubChemDataSourceName());
		composingElements.put("PubChemId",compBean.getNanomaterialEntities().get(0).getComposingElements().get(0).getDomain().getPubChemId());
		
		files = new HashMap<String, Object>();
		files.put("ExternalURI", compBean.getNanomaterialEntities().get(0).getFiles().get(0).getDomainFile().getUriExternal().toString());
		files.put("Title", compBean.getNanomaterialEntities().get(0).getFiles().get(0).getDomainFile().getTitle());
		files.put("URI", compBean.getNanomaterialEntities().get(0).getFiles().get(0).getDomainFile().getUri());
		files.put("KeywordStr", compBean.getNanomaterialEntities().get(0).getFiles().get(0).getKeywordsStr());
		files.put("KeyWordDisplayName", compBean.getNanomaterialEntities().get(0).getFiles().get(0).getKeywordsDisplayName());
		files.put("Description", compBean.getNanomaterialEntities().get(0).getFiles().get(0).getDomainFile().getDescription());
	
		dendrimer = new HashMap<String, Object>();
		dendrimer.put("Description", description);
		dendrimer.put("Properties", properties);
		dendrimer.put("Composing Elements", composingElements);
		dendrimer.put("Files", files);
		
		nanomaterialentity = new HashMap<String, Object>();
		nanomaterialentity.put("dendrimer", dendrimer);
		
		//functionalizingentity
		
		smallMolecule = new HashMap<String, Object>();
		functionalizingentity = new HashMap<String, Object>();
		properties = new HashMap<String, Object>();
		smallMolecule.put("Name", compBean.getFunctionalizingEntities().get(0).getName());
		smallMolecule.put("pubChemID", compBean.getFunctionalizingEntities().get(0).getPubChemId());
		smallMolecule.put("pubChemLink", compBean.getFunctionalizingEntities().get(0).getPubChemLink());
		smallMolecule.put("pubChemDS", compBean.getFunctionalizingEntities().get(0).getPubChemDataSourceName());
		smallMolecule.put("Value", compBean.getFunctionalizingEntities().get(0).getValue());
		smallMolecule.put("ValueUnit", compBean.getFunctionalizingEntities().get(0).getValueUnit());
		smallMolecule.put("Molecular Formula", compBean.getFunctionalizingEntities().get(0).getMolecularFormulaDisplayName());
		
		properties.put("With Properties", compBean.getFunctionalizingEntities().get(0).isWithProperties());
		properties.put("Alternate Name", compBean.getFunctionalizingEntities().get(0).getSmallMolecule().getAlternateName());
		smallMolecule.put("Properties", properties);
		
		Map <String, Object> Functions = new HashMap<String, Object>();
		Functions.put("functionList", compBean.getFunctionalizingEntities().get(0).getFunctions().size());
		Functions.put("withImagingFunction", compBean.getFunctionalizingEntities().get(0).isWithImagingFunction());
		Functions.put("withTargettingFunction", compBean.getFunctionalizingEntities().get(0).isWithTargetingFunction());
		Functions.put("Modality", compBean.getFunctionalizingEntities().get(0).getFunctions().get(0).getImagingFunction().getModality());
		Functions.put("Target DisplayNames", compBean.getFunctionalizingEntities().get(0).getFunctions().get(0).getTargetDisplayNames());
		Functions.put("Function Description", compBean.getFunctionalizingEntities().get(0).getFunctions().get(0).getDescription());
		Functions.put("Function Description DisplayName", compBean.getFunctionalizingEntities().get(0).getFunctions().get(0).getDescriptionDisplayName());
		Functions.put("Type", compBean.getFunctionalizingEntities().get(0).getFunctions().get(0).getType());
		Functions.put("ActivationMethodDisplayName", compBean.getFunctionalizingEntities().get(0).getActivationMethodDisplayName());
		Functions.put("Desc", compBean.getFunctionalizingEntities().get(0).getDescription());
		Functions.put("Description", compBean.getFunctionalizingEntities().get(0).getDescriptionDisplayName());


		files = new HashMap<String, Object>();
		files.put("ExternalURI", compBean.getFunctionalizingEntities().get(0).getFiles().get(0).getDomainFile().getUriExternal().toString());
		files.put("Title", compBean.getFunctionalizingEntities().get(0).getFiles().get(0).getDomainFile().getTitle());
		files.put("URI", compBean.getFunctionalizingEntities().get(0).getFiles().get(0).getDomainFile().getUri());
		files.put("KeywordStr", compBean.getFunctionalizingEntities().get(0).getFiles().get(0).getKeywordsStr());
		files.put("KeyWordDisplayName", compBean.getFunctionalizingEntities().get(0).getFiles().get(0).getKeywordsDisplayName());
		files.put("Description", compBean.getFunctionalizingEntities().get(0).getFiles().get(0).getDomainFile().getDescription());
		
		smallMolecule.put("Files", files);
		smallMolecule.put("Functions", Functions);
		functionalizingentity.put("small molecule", smallMolecule);
		
		//Chemical Association
		
		Association = new HashMap<String, Object>();
		Association.put("AssociationTypes", compBean.getAssocTypes());
		Association.put("AttachmentId", compBean.getChemicalAssociations().get(0).getAttachment().getId());
		Association.put("Bond Type", compBean.getChemicalAssociations().get(0).getAttachment().getBondType());
		Association.put("Description", compBean.getChemicalAssociations().get(0).getDescription());
		
		Map <String, Object> AssociatedElements = new HashMap<String, Object>();
		AssociatedElements.put("CompositiontypeA", compBean.getChemicalAssociations().get(0).getAssociatedElementA().getCompositionType());
		AssociatedElements.put("EntityDisplayNameA", compBean.getChemicalAssociations().get(0).getAssociatedElementA().getEntityDisplayName());
		AssociatedElements.put("ComposingElemetIdA", compBean.getChemicalAssociations().get(0).getAssociatedElementA().getComposingElement().getId());
		AssociatedElements.put("ComposingElementTypeA", compBean.getChemicalAssociations().get(0).getAssociatedElementA().getComposingElement().getType());
		AssociatedElements.put("ComposingElementNameA", compBean.getChemicalAssociations().get(0).getAssociatedElementA().getComposingElement().getName());
		AssociatedElements.put("DomainElementNameA", compBean.getChemicalAssociations().get(0).getAssociatedElementA().getDomainElement().getName());
		AssociatedElements.put("DomainAssociationId", compBean.getChemicalAssociations().get(0).getDomainAssociation().getId());
		
		//Associated with
		
		AssociatedElements.put("CompositiontypeB", compBean.getChemicalAssociations().get(0).getAssociatedElementB().getCompositionType());
		AssociatedElements.put("EntityDisplayNameB", compBean.getChemicalAssociations().get(0).getAssociatedElementB().getEntityDisplayName());
		AssociatedElements.put("ComposingElemetIdB", compBean.getChemicalAssociations().get(0).getAssociatedElementB().getComposingElement().getId());
		AssociatedElements.put("ComposingElementTypeB", compBean.getChemicalAssociations().get(0).getAssociatedElementB().getComposingElement().getType());
		AssociatedElements.put("ComposingElementNameB", compBean.getChemicalAssociations().get(0).getAssociatedElementB().getComposingElement().getName());
		AssociatedElements.put("DomainElementNameB", compBean.getChemicalAssociations().get(0).getAssociatedElementB().getDomainElement().getName());
				
		//Files data
		
		chemicalassociation = new HashMap<String, Object>();
		chemicalassociation.put("Association", Association);
		chemicalassociation.put("Associated Elelments", AssociatedElements);
		chemicalassociation.put("Files", files); 
		
		
		//composition file
		
	//	files = new HashMap<String, Object>();
		System.out.println("compBean.getFiles().get(0).getDomainFile().getType()"+compBean.getFiles().get(0).getDomainFile().getTitle());
		files.put("Type", compBean.getFiles().get(0).getDomainFile().getType());
		files.put("ExternalURI", compBean.getFiles().get(0).getDomainFile().getUriExternal().toString());
		files.put("Title", compBean.getFiles().get(0).getDomainFile().getTitle());
		files.put("URI", compBean.getFiles().get(0).getDomainFile().getUri());
		files.put("KeywordStr", compBean.getFiles().get(0).getKeywordsStr());
		files.put("KeyWordDisplayName", compBean.getFiles().get(0).getKeywordsDisplayName());
		files.put("Description", compBean.getFiles().get(0).getDomainFile().getDescription());
		
		compositionfile = new HashMap<String, Object>();
		compositionfile.put("Image", files);
		
	}

	
}
