package gov.nih.nci.cananolab.restful.view;

import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
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
	String sampleName;
	Boolean nanomaterialEntityWithProperties;
	Map<String, Object> properties;
	Map<String, Object> nanoentitiy;
	Map<String, Object> composingElement;
	Map<String, Object> files;
	Map<String, Object> type2NanoEntities;
	Map<String, Object> nanomaterialentity;

	List<Map<String, Object>> composingElements;
	Map<String, Object> functionalizingentity;
	Map<String, Object> smallMolecule;

	Map<String, Object> chemicalassociation;
	Map<String, Object> Association;

	Map<String, Object> compositionfile;
	Map<String, Object> image;

	private SortedSet<String> nanoEntityTypes = new TreeSet<String>();

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

	public void setFunctionalizingentity(
			Map<String, Object> functionalizingentity) {
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

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}

	public void transferCompositionBeanForSummaryView(CompositionBean compBean) {

		// NanoMaterial Entity
		setCompositionSections(compBean.getCompositionSections());
		setSampleName(compBean.getDomain().getSample().getName());
		nanomaterialentity = new HashMap<String, Object>();
		
		
		nanoentitiy = new HashMap<String, Object>();
		if(compBean.getNanomaterialEntities()!=null){

		nanomaterialentity.put("NanoMaterialEntitySize", compBean.getNanomaterialEntities().size());
		
		for (String entityType : compBean.getNanoEntityTypes()) {
			nanoentitiy = new HashMap<String, Object>();
			for(NanomaterialEntityBean nanoMaterialEntity : compBean.getType2NanoEntities().get(entityType)){
				nanoentitiy.put("Description", nanoMaterialEntity.getDescriptionDisplayName());
				
				if (nanoMaterialEntity.isWithProperties()) {

					System.out.println("****** Is WIth Properties*****"
							+ nanoMaterialEntity.isWithProperties());
					nanoentitiy.put("isWithProperties",
							nanoMaterialEntity.isWithProperties());

					try {
						String detailPage = gov.nih.nci.cananolab.restful.sample.InitCompositionSetup
								.getInstance().getDetailPage(entityType,
										"nanomaterialEntity");
						System.out.println("**** Deatils Page *****"
								+ detailPage);
						nanoentitiy.put("detailsPage", detailPage);
						if (detailPage.contains("Dendrimer")) {
							properties = new HashMap<String, Object>();
							properties.put("Branch", compBean
									.getNanomaterialEntities().get(0)
									.getDendrimer().getBranch());
							properties.put("Generation", compBean
									.getNanomaterialEntities().get(0)
									.getDendrimer().getGeneration()
									.toString());
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					nanoentitiy.put("Properties", properties);
				}
				composingElements = new ArrayList<Map<String,Object>>();
                  if (nanoMaterialEntity.getComposingElements() != null) {
						
						for (ComposingElementBean compElement : nanoMaterialEntity
								.getComposingElements()) {
							composingElement = new HashMap<String, Object>();

							composingElement.put("Description",
									compElement.getDescription());
							composingElement.put("DisplayName",
									compElement.getDisplayName());
							composingElement.put("PubChemLink",
									compElement.getPubChemLink());
							composingElement.put("Function",
									compElement.getFunctionDisplayNames());
							composingElement.put(
									"MolecularFormulaDisplayName", compElement
											.getMolecularFormulaDisplayName());
							composingElement.put("PubChemDataSourceName",
									compElement.getDomain()
											.getPubChemDataSourceName());
							composingElement.put("PubChemId", compElement
									.getDomain().getPubChemId());
							composingElements.add(composingElement);
						}
						nanoentitiy.put("Composing Elements", composingElements);
			}
                  
                  if (nanoMaterialEntity.getFiles() != null) {
						composingElement.put("FilesSize", nanoMaterialEntity
								.getFiles().size());
						for (FileBean file : nanoMaterialEntity
								.getFiles()) {
							files = new HashMap<String, Object>();

							files = new HashMap<String, Object>(); 
							files.put("ExternalURI",file.getDomainFile
									  ().getUriExternal().toString()); 
							files.put("Title", file.getDomainFile().getTitle());
									 
							files.put("URI", file.getDomainFile().getUri()); 
							
							files.put("KeywordStr", file.getKeywordsStr());
									 
							files.put("KeyWordDisplayName",file.getKeywordsDisplayName());
									 
							files.put("Description",file.getDomainFile().getDescription());
						}

						nanoentitiy.put("Files",files);
                  }
			nanomaterialentity.put(entityType, nanoentitiy);
				
		}
	
		}

		//Functionalizing Entity
		if(compBean.getFunctionalizingEntities()!=null){
			functionalizingentity = new HashMap<String, Object>();
			Map<String, Object> function;
			functionalizingentity.put("FuncEntitySize",compBean.getFunctionalizingEntities().size());
			
			for(String entityType : compBean.getFuncEntityTypes()){
				function = new HashMap<String, Object>();
				
				for(FunctionalizingEntityBean funcBean : compBean.getType2FuncEntities().get(entityType)){
				
					function.put("Name", funcBean.getName());
					function.put("pubChemID", funcBean.getPubChemId());
					function.put("pubChemLink", funcBean.getPubChemLink());
					function.put("pubChemDS", funcBean.getPubChemDataSourceName());
					function.put("Value", funcBean.getValue());
					function.put("ValueUnit", funcBean.getValueUnit());
					function.put("Molecular Formula", funcBean.getMolecularFormulaDisplayName());
				    if(funcBean.isWithProperties()){
				    	properties = new HashMap<String, Object>();
							  properties.put("With Properties", funcBean.isWithProperties());
							  System.out.println("****** Is WIth Properties*****"
										+ funcBean.isWithProperties());
								try {
									String detailPage = gov.nih.nci.cananolab.restful.sample.InitCompositionSetup
											.getInstance().getDetailPage(entityType,
													"functionalizingEntity");
									System.out.println("**** Deatils Page *****"
											+ detailPage);
									function.put("detailsPage", detailPage);
									if (detailPage.contains("SmallMolecule")) {
										properties.put("Alternate Name",
													  funcBean.getSmallMolecule().getAlternateName());
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						//	  
							  function.put("Properties", properties);
				}  
				    if(funcBean.getFunctions()!=null){
				    	Map <String, Object> Functions = new HashMap<String, Object>();
						  Functions.put("functionList",
						  funcBean.getFunctions().size());
						  for(FunctionBean func : funcBean.getFunctions()){
							  
						  Functions.put("withImagingFunction", funcBean.isWithImagingFunction());
						  Functions.put("withTargettingFunction", funcBean.isWithTargetingFunction());
						  Functions.put("Modality", func.getImagingFunction().getModality());
						  Functions.put("Target DisplayNames",func.getTargetDisplayNames());
						  Functions.put("Function Description", func.getDescription());
						  Functions.put("Function Description DisplayName", func.getDescriptionDisplayName());
						  Functions.put("Type", func.getType()); 
						  Functions.put("ActivationMethodDisplayName", funcBean.getActivationMethodDisplayName());
						  Functions.put("Desc", funcBean.getDescription());
						  Functions.put("Description", funcBean.getDescriptionDisplayName());
						  }
						  function.put("Functions", Functions);
				    }
						if(funcBean.getFiles()!=null){
							composingElement.put("FilesSize", funcBean
									.getFiles().size());
							for (FileBean file : funcBean
									.getFiles()) {
								files = new HashMap<String, Object>();

								files = new HashMap<String, Object>(); 
								files.put("ExternalURI",file.getDomainFile
										  ().getUriExternal().toString()); 
								files.put("Title", file.getDomainFile().getTitle());
										 
								files.put("URI", file.getDomainFile().getUri()); 
								
								files.put("KeywordStr", file.getKeywordsStr());
										 
								files.put("KeyWordDisplayName",file.getKeywordsDisplayName());
										 
								files.put("Description",file.getDomainFile().getDescription());
							}

							function.put("Files",files);
						}
							  
				functionalizingentity.put(entityType, function);
				
				}
			}
		}
		
		//Chemical Association
		if(compBean.getChemicalAssociations()!=null){
			chemicalassociation = new HashMap<String, Object>();
			Map<String, Object> association= new HashMap<String, Object>();
			Map <String, Object> AssociatedElements = null;
			
			chemicalassociation.put("ChemAssocSize", compBean.getChemicalAssociations().size());
			for(String entityType : compBean.getAssocTypes()){
				for(ChemicalAssociationBean chemBean : compBean.getType2Assocs().get(entityType)){
					chemicalassociation.put("AssociationTypes", compBean.getAssocTypes());
					chemicalassociation.put("AttachmentId",
					  chemBean.getAttachment().getId());
					chemicalassociation.put("Bond Type",
					  chemBean.getAttachment().getBondType());
					chemicalassociation.put("Description",
					  chemBean.getDescription());
					  
					  AssociatedElements = new HashMap<String, Object>();
					  AssociatedElements.put("CompositiontypeA",
					  chemBean.getAssociatedElementA().getCompositionType());
					  AssociatedElements.put("EntityDisplayNameA",
							  chemBean.getAssociatedElementA().getEntityDisplayName());
					  AssociatedElements.put("ComposingElemetIdA",
							  chemBean.getAssociatedElementA().getComposingElement().getId());
					  AssociatedElements.put("ComposingElementTypeA",
							  chemBean.getAssociatedElementA().getComposingElement().getType());
					  AssociatedElements.put("ComposingElementNameA",
							  chemBean.getAssociatedElementA().getComposingElement().getName());
					  AssociatedElements.put("DomainElementNameA",
							  chemBean.getAssociatedElementA().getDomainElement().getName());
					  AssociatedElements.put("DomainAssociationId",
							  chemBean.getDomainAssociation().getId());
					  
					  //Associated with
					  
					  AssociatedElements.put("CompositiontypeB",
							  chemBean.getAssociatedElementB().getCompositionType());
					  AssociatedElements.put("EntityDisplayNameB",
							  chemBean.getAssociatedElementB().getEntityDisplayName());
					  AssociatedElements.put("ComposingElemetIdB",
							  chemBean.getAssociatedElementB().getComposingElement().getId());
					  AssociatedElements.put("ComposingElementTypeB",
							  chemBean.getAssociatedElementB().getComposingElement().getType());
					  AssociatedElements.put("ComposingElementNameB",
							  chemBean.getAssociatedElementB().getComposingElement().getName());
					  AssociatedElements.put("DomainElementNameB",
							  chemBean.getAssociatedElementB().getDomainElement().getName());
				
					  chemicalassociation.put("Associted Elements", AssociatedElements);
						
						if(chemBean.getFiles()!=null){
							chemicalassociation.put("FilesSize", chemBean
									.getFiles().size());
							for (FileBean file : chemBean
									.getFiles()) {
								files = new HashMap<String, Object>();

								files = new HashMap<String, Object>(); 
								files.put("ExternalURI",file.getDomainFile
										  ().getUriExternal().toString()); 
								files.put("Title", file.getDomainFile().getTitle());
										 
								files.put("URI", file.getDomainFile().getUri()); 
								
								files.put("KeywordStr", file.getKeywordsStr());
										 
								files.put("KeyWordDisplayName",file.getKeywordsDisplayName());
										 
								files.put("Description",file.getDomainFile().getDescription());
								chemicalassociation.put("Files",files);
							}

							
						chemicalassociation.put(entityType, association);
					}
						
				}	
				
		}
		
	}
		
		if(compBean.getFiles()!=null){
			compositionfile = new HashMap<String, Object>();
			
			Map <String, Object> comFile = new HashMap<String, Object>();
			comFile.put("Files Size", compBean.getFiles().size());
			for(String entityType : compBean.getFileTypes()){
				for(FileBean file : compBean.getType2Files().get(entityType)){
				comFile.put("ExternalURI",file.getDomainFile
						  ().getUriExternal().toString()); 
				comFile.put("Title", file.getDomainFile().getTitle());
						 
				comFile.put("URI", file.getDomainFile().getUri()); 
				
				comFile.put("KeywordStr", file.getKeywordsStr());
						 
				comFile.put("KeyWordDisplayName",file.getKeywordsDisplayName());
						 
				comFile.put("Description",file.getDomainFile().getDescription());
					
				}
				compositionfile.put(entityType, comFile);
			}
			
		}
		}

	
	}
}
