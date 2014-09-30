package gov.nih.nci.cananolab.restful.view;

import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

public class SimpleAdvacedSampleCompositionBean {

	Map<String, Object> properties;
	Map<String, Object> nanoentitiy;
	Map<String, Object> composingElement;
	Map<String, Object> files;
	MultiMap nanomaterialentity;

	List<Map<String, Object>> composingElements;
	List<Map<String, Object>> fileList;
	
	Map<String, Object> Functions;
	List<Map<String, Object>> functionsList;
	Map<String, Object> function;
	
	MultiMap functionalizingentity;
	
	public MultiMap getFunctionalizingentity() {
		return functionalizingentity;
	}

	public void setFunctionalizingentity(MultiMap functionalizingentity) {
		this.functionalizingentity = functionalizingentity;
	}

	public MultiMap getNanomaterialentity() {
		return nanomaterialentity;
	}

	public void setNanomaterialentity(MultiMap nanomaterialentity) {
		this.nanomaterialentity = nanomaterialentity;
	}

	public void transferNanomaterialEntityForAdvancedSampleSearch(NanomaterialEntityBean nanoMaterialEntity, HttpServletRequest request){
		nanomaterialentity = new MultiValueMap();
		nanoentitiy = new HashMap<String, Object>();
		String entityType = nanoMaterialEntity.getType();
		nanoentitiy.put("Description",
				nanoMaterialEntity.getDescriptionDisplayName());
		nanoentitiy.put("dataId", nanoMaterialEntity.getDomainEntity().getId());

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
					properties.put("Branch", nanoMaterialEntity
							.getDendrimer().getBranch());
					properties.put("Generation", nanoMaterialEntity
							.getDendrimer().getGeneration());
				}
				if (detailPage.contains("Polymer")) {
					properties = new HashMap<String, Object>();

					properties.put("Initiator", nanoMaterialEntity
							.getPolymer().getInitiator());
					properties.put("isCrossLinked",
							nanoMaterialEntity.getPolymer()
									.getCrossLinked());
					properties.put("CrossLinkDegree",
							nanoMaterialEntity.getPolymer()
									.getCrossLinkDegree());

				}
				if (detailPage.contains("Biopolymer")) {
					properties = new HashMap<String, Object>();
					properties.put("Name", nanoMaterialEntity
							.getBiopolymer().getName());
					properties.put("Type", nanoMaterialEntity
							.getBiopolymer().getType());
					properties.put("Sequence", nanoMaterialEntity
							.getBiopolymer().getSequence());

				}
				if (detailPage.contains("CarbonNanotube")) {
					properties = new HashMap<String, Object>();

					properties.put("AverageLength",
							nanoMaterialEntity.getCarbonNanotube()
									.getAverageLength());
					properties.put("AverageLengthUnit",
							nanoMaterialEntity.getCarbonNanotube()
									.getAverageLengthUnit());
					properties.put("chirality", nanoMaterialEntity
							.getCarbonNanotube().getChirality());
					properties.put("Diameter", nanoMaterialEntity
							.getCarbonNanotube().getDiameter());
					properties.put("DiameterUnit",
							nanoMaterialEntity.getCarbonNanotube()
									.getDiameterUnit());
					properties.put("WallType", nanoMaterialEntity
							.getCarbonNanotube().getWallType());
				}
				if (detailPage.contains("Liposome")) {
					properties = new HashMap<String, Object>();

					properties.put("isPolymerized",
							nanoMaterialEntity.getLiposome()
									.getPolymerized());
					properties.put("polymerName",
							nanoMaterialEntity.getLiposome()
									.getPolymerName());

				}
				if (detailPage.contains("Emulsion")) {
					properties = new HashMap<String, Object>();

					properties.put("isPolymerized",
							nanoMaterialEntity.getEmulsion()
									.getPolymerized());
					properties.put("polymerName",
							nanoMaterialEntity.getEmulsion()
									.getPolymerName());

				}
				if (detailPage.contains("Fullerene")) {
					properties = new HashMap<String, Object>();

					properties.put("averageDiameter",
							nanoMaterialEntity.getFullerene()
									.getAverageDiameter());
					properties.put("averageDiameterUnit",
							nanoMaterialEntity.getFullerene()
									.getAverageDiameterUnit());
					properties.put("numberOfCarbon",
							nanoMaterialEntity.getFullerene()
									.getNumberOfCarbon());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			nanoentitiy.put("Properties", properties);
		}
		composingElements = new ArrayList<Map<String, Object>>();
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
				composingElement.put("MolecularFormulaDisplayName",
						compElement
								.getMolecularFormulaDisplayName());
				composingElement.put("PubChemDataSourceName",
						compElement.getDomain()
								.getPubChemDataSourceName());
				composingElement.put("PubChemId", compElement
						.getDomain().getPubChemId());
				composingElements.add(composingElement);
			}
			nanoentitiy.put("ComposingElements", composingElements);
		}
		fileList = new ArrayList<Map<String, Object>>();
		if (nanoMaterialEntity.getFiles() != null) {

			for (FileBean file : nanoMaterialEntity.getFiles()) {
				files = new HashMap<String, Object>();

				files.put("fileId", file.getDomainFile().getId());
				files.put("isImage", file.isImage());
				files.put("ExternalURI", file.getDomainFile()
						.getUriExternal().toString());
				files.put("Title", file.getDomainFile().getTitle());

				files.put("URI", file.getDomainFile().getUri());

				files.put("KeywordStr", file.getKeywordsStr());

				files.put("KeyWordDisplayName",
						file.getKeywordsDisplayName());

				files.put("Description", file.getDomainFile()
						.getDescription());
				fileList.add(files);
			}

			nanoentitiy.put("Files", fileList);
		}
		nanomaterialentity.put(entityType, nanoentitiy);

	}

	public void transferFunctionalizingEntityForAdvancedSampleSearch(
			FunctionalizingEntityBean funcBean, HttpServletRequest httpRequest) {

		functionalizingentity = new MultiValueMap();
		function = new HashMap<String, Object>();
		String entityType = funcBean.getType();
		function.put("dataId", funcBean.getDomainEntity().getId());
		function.put("Name", funcBean.getName());
		function.put("pubChemID", funcBean.getPubChemId());
		function.put("pubChemLink", funcBean.getPubChemLink());
		function.put("pubChemDS",
				funcBean.getPubChemDataSourceName());
		function.put("value", funcBean.getValue());
		function.put("valueUnit", funcBean.getValueUnit());
		function.put("MolecularFormulaDisplayName",
				funcBean.getMolecularFormulaDisplayName());
		function.put("withImagingFunction",
				funcBean.isWithImagingFunction());
		function.put("withTargettingFunction",
				funcBean.isWithTargetingFunction());
		function.put("description",
				funcBean.getDescription());
		if (funcBean.isWithProperties()) {
			properties = new HashMap<String, Object>();
			properties.put("isWithProperties",
					funcBean.isWithProperties());
			System.out.println("****** Is WIth Properties*****"
					+ funcBean.isWithProperties());
			try {
				String detailPage = gov.nih.nci.cananolab.restful.sample.InitCompositionSetup
						.getInstance().getDetailPage(
								entityType,
								"functionalizingentity");
				System.out.println("**** Deatils Page *****"
						+ detailPage);
				function.put("detailsPage", detailPage);
				if (detailPage.contains("SmallMolecule")) {
					properties.put("alternateName", funcBean
							.getSmallMolecule()
							.getAlternateName());
				}

				if (detailPage.contains("Biopolymer")) {
					properties = new HashMap<String, Object>();

					properties.put("type", funcBean
							.getBiopolymer().getType());
					properties.put("sequence", funcBean
							.getBiopolymer().getSequence());

				}
				if (detailPage.contains("Antibody")) {
					properties = new HashMap<String, Object>();

					properties.put("type", funcBean
							.getAntibody().getType());
					properties.put("isoType", funcBean
							.getAntibody().getIsotype());
					properties.put("species", funcBean
							.getAntibody().getSpecies());

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			//
			function.put("Properties", properties);
		}
		if (funcBean.getFunctions() != null) {
			Functions = new HashMap<String, Object>();
			functionsList = new ArrayList<Map<String, Object>>();
			for (FunctionBean func : funcBean.getFunctions()) {
				Functions = new HashMap<String, Object>();

				Functions.put("withImagingFunction",
						funcBean.isWithImagingFunction());
				Functions.put("withTargettingFunction",
						funcBean.isWithTargetingFunction());
				Functions.put("Modality", func
						.getImagingFunction().getModality());
				Functions.put("TargetDisplayNames",
						func.getTargetDisplayNames());
				Functions.put("FunctionDescription",
						func.getDescription());
				Functions.put("FunctionDescriptionDisplayName",
						func.getDescriptionDisplayName());
				Functions.put("Type", func.getType());
				// Functions.put("ActivationMethodDisplayName",
				// funcBean.getActivationMethodDisplayName());
				// Functions.put("Desc",
				// funcBean.getDescription());
				// Functions.put("Description",
				// funcBean.getDescriptionDisplayName());
				functionsList.add(Functions);
			}
			function.put("Functions", functionsList);
		}
		if (funcBean.getActivationMethodDisplayName() != null) {
			function.put("ActivationMethod",
					funcBean.getActivationMethodDisplayName());
		}
		if (funcBean.getDescription() != null) {
			function.put("Desc", funcBean.getDescription());
			function.put("Decription",
					funcBean.getDescriptionDisplayName());
		}
		fileList = new ArrayList<Map<String, Object>>();
		if (funcBean.getFiles() != null) {

			for (FileBean file : funcBean.getFiles()) {
				files = new HashMap<String, Object>();

				files.put("fileId", file.getDomainFile()
						.getId());
				files.put("isImage", file.isImage());
				files.put("ExternalURI", file.getDomainFile()
						.getUriExternal().toString());
				files.put("Title", file.getDomainFile()
						.getTitle());

				files.put("URI", file.getDomainFile().getUri());

				files.put("KeywordStr", file.getKeywordsStr());

				files.put("KeyWordDisplayName",
						file.getKeywordsDisplayName());

				files.put("Description", file.getDomainFile()
						.getDescription());
				fileList.add(files);
			}

			function.put("Files", fileList);
		}

		functionalizingentity.put(entityType, function);

	}
}
