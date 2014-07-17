package gov.nih.nci.cananolab.restful.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.log4j.Logger;

import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.dto.common.ColumnHeader;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.FindingBean;
import gov.nih.nci.cananolab.dto.common.Row;
import gov.nih.nci.cananolab.dto.common.TableCell;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryViewBean;

public class SimpleCharacterizationSummaryViewBean {
	
	private Logger logger = Logger.getLogger(SimpleCharacterizationSummaryViewBean.class);
	
	List<SimpleCharacterizationsByTypeBean> charByTypeBeans = new ArrayList<SimpleCharacterizationsByTypeBean>();
	
	
	
	//Map<String, Map<String, Object>> viewMap = new HashMap<String, Map<String, Object>>();
	
	
	
	//Map<String, Map<String, Object>> charMapByType = new HashMap<String, Map<String, Object>>();
	
	
	
	public List<SimpleCharacterizationsByTypeBean> transferData(CharacterizationSummaryViewBean viewBean) {
		
		logger.info("============ SimpleCharacterizationSummaryViewBean.transferData ==================");
		if (viewBean == null) return null;
		
		Map<String, SortedSet<CharacterizationBean>> type2CharsBeans = viewBean.getType2Characterizations();
		
		Map<String, SortedSet<CharacterizationBean>> name2CharBeans = viewBean.getCharName2Characterizations();
		
		Set<String> charTypes = viewBean.getCharacterizationTypes();
		
		Map<String, SortedSet<String>> charNames = viewBean.getType2CharacterizationNames();
		
		//for the outter most map
		
		
		
		for (String type : charTypes) {
			logger.info("Processing type: " + type);
			
			
			SortedSet<String> namesOfType = charNames.get(type);
			
			//List<SortedSet<CharacterizationBean>> charBeanMapOfType = new ArrayList<SortedSet<CharacterizationBean>>();
			
			SortedMap<String, Object> charsByAssayType = new TreeMap<String, Object>();
			
			
			for (String charname : namesOfType) {
				logger.info("Char Name for type: " + charname + " | " + type);
				
				SortedSet<CharacterizationBean> charBeans = name2CharBeans.get(charname);
				//Map<String, Object> charBeans = new HashMap<String, Object>();
				
				//List<Object> charBeansByCharName = new ArrayList<Object>();
				List<List<SimpleCharacterizationUnitBean>> charBeansByCharName = new ArrayList<List<SimpleCharacterizationUnitBean>>();
				
				for (CharacterizationBean charBean : charBeans) {
					logger.info("Proccessing char bean: " + charBean.getCharacterizationName());
					//Map<String, Object> aBeanMap = tranferCharacterizationBeanData(charBean);
					List<SimpleCharacterizationUnitBean> aBeanUnitList = tranferCharacterizationBeanData(charBean);
					charBeansByCharName.add(aBeanUnitList);
					logger.info("End Proccessing char bean: " + charBean.getCharacterizationName());
				}
				charsByAssayType.put(charname, charBeansByCharName);
				
			}
			
			SimpleCharacterizationsByTypeBean charByTypeBean = new SimpleCharacterizationsByTypeBean();
			charByTypeBean.setType(type);
			charByTypeBean.setCharsByAssayType(charsByAssayType);
			charByTypeBeans.add(charByTypeBean);
			
			//charMapByType.put(type, charsByAssayType);
			
			
			logger.info("End of Processing type: " + type);
		}		
		
//		viewMap = charMapByType;
//		
//		return viewMap;
		
		return charByTypeBeans;
	}
	
	/**
	 * Mimick the logic in bodySingleCharacterizationSummaryView.jsp
	 * 
	 * @param charObj
	 */
	public List<SimpleCharacterizationUnitBean> tranferCharacterizationBeanData(CharacterizationBean charBean) {
		if (charBean == null)
			return null;
		
		Map<String, Object> charBeanMap = new HashMap<String, Object>();
		
		List<SimpleCharacterizationUnitBean> charBeanUnits = new ArrayList<SimpleCharacterizationUnitBean>();
		
		Characterization charObj = charBean.getDomainChar();
		String charName = charBean.getCharacterizationName();
		String charType = charBean.getCharacterizationType();
	
		//Assay Type
		
		SimpleCharacterizationUnitBean aUnit;
		if (charObj.getAssayType() != null && charObj.getAssayType().length() > 0) {
			logger.info(charObj.getAssayType());
			charBeanMap.put("Assay Type", charObj.getAssayType());
			
			//aUnit.setName("Assay Type");
			//aUnit.setValue(charObj.getAssayType());
			aUnit = new SimpleCharacterizationUnitBean("Assay Type", charObj.getAssayType());
			charBeanUnits.add(aUnit);
			
		} else if (charBean.getCharacterizationType().equals("physico chemical characterization")) {
			logger.info("Assay Type: " + charName);
			charBeanMap.put("Assay Type", charName);
			
			aUnit = new SimpleCharacterizationUnitBean("Assay Type", charName);
			charBeanUnits.add(aUnit);
		}
		
		//Point of Contacts
		String pocName = charBean.getPocBean().getDisplayName();

		if (pocName != null && pocName.length() > 0) {
			logger.info("Point of Contact: " + pocName);
			charBeanMap.put("Point of Contact", pocName);
			
			aUnit = new SimpleCharacterizationUnitBean("Point of Contact", pocName);
			charBeanUnits.add(aUnit);
		}
		
		//Characterization Date
		if (charBean.getDateString().length() > 0) {
			logger.info("Characterization Date: " + charBean.getDateString());
			charBeanMap.put("Characterization Date", charBean.getDateString());
			aUnit = new SimpleCharacterizationUnitBean("Characterization Date", charBean.getDateString());
			charBeanUnits.add(aUnit);
		}
		
		//Protocol		
		if (charBean.getProtocolBean().getDisplayName().length() > 0) {
			logger.info("Protocol: " + charBean.getProtocolBean().getDisplayName());
			charBeanMap.put("Protocol", charBean.getProtocolBean().getDisplayName());
			
			aUnit = new SimpleCharacterizationUnitBean("Protocol", charBean.getProtocolBean().getDisplayName());
			charBeanUnits.add(aUnit);
		}
		
		//What is this?
		if (charBean.isWithProperties()) {
			
//			logger.info("======== Details go here ================ ");
//			String detailPage = gov.nih.nci.cananolab.restful.sample.InitCharacterizationSetup
//					.getInstance().getDetailPage(charType, charName);
//			logger.info(detailPage);
			
			String phyStateType = charBean.getPhysicalState().getType();
			
			
			if (phyStateType != null && phyStateType.length() > 0) {
				charBeanMap.put("Properties", phyStateType);
			
				aUnit = new SimpleCharacterizationUnitBean("Properties", phyStateType);
				charBeanUnits.add(aUnit);
			}else {
				charBeanMap.put("Properties", "N/A");
				
				aUnit = new SimpleCharacterizationUnitBean("Properties", "N/A");
				charBeanUnits.add(aUnit);
			}
		}
		
		//Design Description
		String desigMethodsDesc = charObj.getDesignMethodsDescription().trim();
		if (desigMethodsDesc.length() > 0) {
			logger.info("Design Description: " + desigMethodsDesc);
			charBeanMap.put("Design Description", desigMethodsDesc);
			
			aUnit = new SimpleCharacterizationUnitBean("Design Description", desigMethodsDesc);
			charBeanUnits.add(aUnit);
		} 
		
		//Experiment Configurations
		transferExperimentConfigurations(charBeanMap, charBeanUnits, charBean);
		
		//Characterization Results
		transferCharacterizationResults(charBeanMap, charBeanUnits, charBean);
		
		//Analysis and Conclusion
		if (charBean.getConclusion() != null && charBean.getConclusion().length() > 0) {
			logger.info("Analysis and Conclusion: " + charBean.getConclusion());
			charBeanMap.put("Analysis and Conclusion", charBean.getConclusion());
			
			aUnit = new SimpleCharacterizationUnitBean("Analysis and Conclusion", charBean.getConclusion());
			charBeanUnits.add(aUnit);
		}
		
		
		//return charBeanMap;
		return charBeanUnits;
	}
	
	protected void transferExperimentConfigurations(Map<String, Object> charBeanMap, List<SimpleCharacterizationUnitBean> charBeanUnits, CharacterizationBean charBean) {
		if (charBean.getExperimentConfigs().size() == 0) 
			return;

		logger.info("Experiment Configurations size: " + charBean.getExperimentConfigs().size());
		List<ExperimentConfigBean> expConfigBeans = charBean.getExperimentConfigs();

		MultiMap technique = new MultiValueMap();
		MultiMap instruments = new MultiValueMap();
		MultiMap description = new MultiValueMap();

		List<MultiMap> expConfigTable = new ArrayList<MultiMap>();

		for (ExperimentConfigBean aBean : expConfigBeans) {
			String techDisplayName = (aBean.getTechniqueDisplayName() == null) ? "" : aBean.getTechniqueDisplayName();
			String instrDisplayNames = (aBean.getInstrumentDisplayNames() == null) ? "" : aBean.getInstrumentDisplayNames().toString();
			String desc = (aBean.getDomain().getDescription() == null) ? "" : aBean.getDomain().getDescription();

			technique.put("Technique", techDisplayName);
			instruments.put("Instruments", instrDisplayNames);
			description.put("Description", desc);

			logger.info("Tech display name: " + aBean.getTechniqueDisplayName());
		}

		expConfigTable.add(technique);
		expConfigTable.add(instruments);
		expConfigTable.add(description);

		charBeanMap.put("Experiment Configurations", expConfigTable);
		
		SimpleCharacterizationUnitBean aUnit = new SimpleCharacterizationUnitBean("Experiment Configurations", expConfigTable);
		charBeanUnits.add(aUnit);

	}
	
	protected List<Object> transferCharacterizationResultsDataAndCondition(FindingBean findingBean) {

		List<Object> findingTables = new ArrayList<Object>();


		List<Row> rows = findingBean.getRows();

		if (rows == null || rows.size() == 0)
			return findingTables;

		logger.info("Data and Conditions:");
		List<MultiMap> colsOfTable = new ArrayList<MultiMap>();

		List<ColumnHeader>  colHeaders = findingBean.getColumnHeaders();
		int idx = 0;
		for (ColumnHeader colHeader : colHeaders) {
			String colTitle = colHeader.getDisplayName();

			logger.info("==Header: " + colTitle);

			MultiMap aColMap = new MultiValueMap();

			for (Row aRow : rows) {
				List<TableCell> cells = aRow.getCells();
				aColMap.put(colTitle, cells.get(idx).getValue());
			}

			idx++;
			colsOfTable.add(aColMap);
		}

		findingTables.add(colsOfTable);

		return findingTables;
	}
	
	protected List<Object> transferCharacterizationResultsFiles (FindingBean findingBean) {

		List<Object> files = new ArrayList<Object>();
		List<FileBean> fileBeans = findingBean.getFiles(); 

		if (fileBeans == null || fileBeans.size() ==0)
			return files;
		
		logger.debug("Files: ");
		
		for (FileBean fileBean : fileBeans) {
			
			Map<String, Object> aFile = new HashMap<String, Object>();
			
			aFile.put("fileId", fileBean.getDomainFile().getId());
			if (fileBean.getDomainFile().getUriExternal()) {
				logger.info("uriExternal: " + fileBean.getDomainFile().getId());
				//aFile.put("fileId", fileBean.getDomainFile().getId());
				aFile.put("uri", fileBean.getDomainFile().getUri());
			} else if (fileBean.isImage()){
				logger.info("Is image: " + fileBean.getDomainFile().getTitle());
				aFile.put("title", fileBean.getDomainFile().getTitle());
				//aFile.put("fileId", fileBean.getDomainFile().getId());
			} else {
				logger.info("Have download link here");
				aFile.put("title", fileBean.getDomainFile().getTitle());
			}
			
			if (fileBean.getKeywordsStr() != null && fileBean.getKeywordsStr().length() > 0) {
				logger.info("keyword displayname: " + fileBean.getKeywordsDisplayName());
				aFile.put("keywordsString", fileBean.getKeywordsDisplayName());
			}

			if (fileBean.getDomainFile().getDescription().length() > 0) {
				logger.info("File description: " + fileBean.getDescription());
				aFile.put("description", fileBean.getDescription());
			}
			
			files.add(aFile);
		}
		
		return files;
	}
	
	protected void transferCharacterizationResults(Map<String, Object> charBeanMap, List<SimpleCharacterizationUnitBean> charBeanUnits, CharacterizationBean charBean) {
		
		List<Object> charResults = new ArrayList<Object>();
		
		List<FindingBean> findings = charBean.getFindings();
		
		
		for (FindingBean findingBean : findings) {
			
			Map<String, List<Object>> oneCharResult = new HashMap<String, List<Object>>();
			
			List<Object> dataCondition = transferCharacterizationResultsDataAndCondition(findingBean);
			if (dataCondition != null && dataCondition.size() > 0)
				oneCharResult.put("Data and Conditions", dataCondition);
			
			List<Object> files = transferCharacterizationResultsFiles(findingBean);
			if (files != null && files.size() > 0)
				oneCharResult.put("Files", files);
			

			charResults.add(oneCharResult);
		}

		charBeanMap.put("Characterization Results", charResults);
		SimpleCharacterizationUnitBean aUnit = new SimpleCharacterizationUnitBean("Characterization Results", charResults);
		charBeanUnits.add(aUnit);
	}

	public List<SimpleCharacterizationsByTypeBean> getCharByTypeBeans() {
		return charByTypeBeans;
	}

	public void setCharByTypeBeans(
			List<SimpleCharacterizationsByTypeBean> charByTypeBeans) {
		this.charByTypeBeans = charByTypeBeans;
	}

	
}
