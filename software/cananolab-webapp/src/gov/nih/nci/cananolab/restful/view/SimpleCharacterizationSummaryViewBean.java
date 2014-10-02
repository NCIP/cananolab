package gov.nih.nci.cananolab.restful.view;

import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.dto.common.ColumnHeader;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.FindingBean;
import gov.nih.nci.cananolab.dto.common.Row;
import gov.nih.nci.cananolab.dto.common.TableCell;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryViewBean;
import gov.nih.nci.cananolab.restful.view.characterization.properties.CharacterizationPropertyUtil;
import gov.nih.nci.cananolab.restful.view.characterization.properties.SimpleCharacterizationProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.log4j.Logger;

public class SimpleCharacterizationSummaryViewBean {
	
	long parentSampleId;
	
	private Logger logger = Logger.getLogger(SimpleCharacterizationSummaryViewBean.class);
	
	List<SimpleCharacterizationsByTypeBean> charByTypeBeans = new ArrayList<SimpleCharacterizationsByTypeBean>();
	List<String> errors = new ArrayList<String>();
	List<String> messages = new ArrayList<String>();
	
	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public long getParentSampleId() {
		return parentSampleId;
	}

	public void setParentSampleId(long parentSampleId) {
		this.parentSampleId = parentSampleId;
	}

	public List<SimpleCharacterizationsByTypeBean> transferData(HttpServletRequest request, 
			CharacterizationSummaryViewBean viewBean, String sampleId) 
	throws Exception {
		
		logger.debug("============ SimpleCharacterizationSummaryViewBean.transferData ==================");
		if (viewBean == null) return null;
		
		Map<String, SortedSet<CharacterizationBean>> name2CharBeans = viewBean.getCharName2Characterizations();
		Set<String> charTypes = viewBean.getCharacterizationTypes();
		Map<String, SortedSet<String>> charNames = viewBean.getType2CharacterizationNames();
		
		if (charTypes == null || charTypes.size() == 0) 
			return charByTypeBeans;
		
		for (String type : charTypes) {
			logger.debug("Processing type: " + type);
			
			SortedSet<String> namesOfType = charNames.get(type);
			SortedMap<String, Object> charsByAssayType = new TreeMap<String, Object>();
			
			for (String charname : namesOfType) {
				logger.debug("Char Name for type: " + charname + " | " + type);
				
				SortedSet<CharacterizationBean> charBeans = name2CharBeans.get(charname);
				List<SimpleCharacterizationViewBean> charBeansByCharName = new ArrayList<SimpleCharacterizationViewBean>();
				
				for (CharacterizationBean charBean : charBeans) {
					logger.debug("Proccessing char bean: " + charBean.getCharacterizationName());
					
					SimpleCharacterizationViewBean aView = new SimpleCharacterizationViewBean();
		
					List<SimpleCharacterizationUnitBean> aBeanUnitList = tranferCharacterizationBeanData(request, charBean);
					aView.transferData(charBean, aBeanUnitList, sampleId, type);
										
					charBeansByCharName.add(aView);
					logger.debug("End Proccessing char bean: " + charBean.getCharacterizationName());
				}
				charsByAssayType.put(charname, charBeansByCharName);
				
			}
			
			SimpleCharacterizationsByTypeBean charByTypeBean = new SimpleCharacterizationsByTypeBean();
			charByTypeBean.setType(type);
			charByTypeBean.setCharsByAssayType(charsByAssayType);
			charByTypeBeans.add(charByTypeBean);
			
			logger.debug("End of Processing type: " + type);
		}		

		return charByTypeBeans;
	}
	
	/**
	 * Mimick the logic in bodySingleCharacterizationSummaryView.jsp
	 * 
	 * @param charObj
	 */
	public List<SimpleCharacterizationUnitBean> tranferCharacterizationBeanData(HttpServletRequest request, 
			CharacterizationBean charBean) {
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
			
			aUnit = new SimpleCharacterizationUnitBean("Assay Type", charObj.getAssayType());
			
		} else 
			aUnit = new SimpleCharacterizationUnitBean("Assay Type", "N/A");
		charBeanUnits.add(aUnit);
		
//		else if (charBean.getCharacterizationType().equals("physico chemical characterization")) {
//			logger.info("Assay Type: " + charName);
//			charBeanMap.put("Assay Type", charName);
//			
//			aUnit = new SimpleCharacterizationUnitBean("Assay Type", charName);
//			charBeanUnits.add(aUnit);
//		}
		
		//Point of Contacts
		String pocName = charBean.getPocBean().getDisplayName();

		if (pocName != null && pocName.length() > 0) {
			logger.info("Point of Contact: " + pocName);
			charBeanMap.put("Point of Contact", pocName);
			
			aUnit = new SimpleCharacterizationUnitBean("Point of Contact", pocName);
			
		} else
			aUnit = new SimpleCharacterizationUnitBean("Point of Contact", "N/A");
		
		charBeanUnits.add(aUnit);
		
		//Characterization Date
		if (charBean.getDateString().length() > 0) {
			logger.info("Characterization Date: " + charBean.getDateString());
			charBeanMap.put("Characterization Date", charBean.getDateString());
			aUnit = new SimpleCharacterizationUnitBean("Characterization Date", charBean.getDateString());
			
		} else 
			aUnit = new SimpleCharacterizationUnitBean("Characterization Date", "N/A");
		charBeanUnits.add(aUnit);
		
		//Protocol		
		if (charBean.getProtocolBean().getDisplayName().length() > 0) {
			logger.info("Protocol: " + charBean.getProtocolBean().getDisplayName());
			charBeanMap.put("Protocol", charBean.getProtocolBean().getDisplayName());
			
			aUnit = new SimpleCharacterizationUnitBean("Protocol", charBean.getProtocolBean().getDisplayName());
			
		} else
			aUnit = new SimpleCharacterizationUnitBean("Protocol", "N/A");
		charBeanUnits.add(aUnit);
		
		//What is this?
		if (charBean.isWithProperties()) {
			
			List rows = this.transferCharacterizationProperties(charBean);
			
			aUnit = new SimpleCharacterizationUnitBean("Properties", rows);
			charBeanUnits.add(aUnit);
		}
		
		//Design Description
		String desigMethodsDesc = charObj.getDesignMethodsDescription();
		if (desigMethodsDesc != null && desigMethodsDesc.length() > 0) {
			logger.debug("Design Description: " + desigMethodsDesc);
			charBeanMap.put("Design Description", desigMethodsDesc);
			
			aUnit = new SimpleCharacterizationUnitBean("Design Description", desigMethodsDesc);
			
		} else
			aUnit = new SimpleCharacterizationUnitBean("Design Description", "N/A");
		charBeanUnits.add(aUnit);
		
		//Experiment Configurations
		transferExperimentConfigurations(charBeanMap, charBeanUnits, charBean);
		
		//Characterization Results
		transferCharacterizationResults(charBeanMap, charBeanUnits, charBean);
		
		//Analysis and Conclusion
		if (charBean.getConclusion() != null && charBean.getConclusion().length() > 0) {
			logger.debug("Analysis and Conclusion: " + charBean.getConclusion());
			charBeanMap.put("Analysis and Conclusion", charBean.getConclusion());
			
			aUnit = new SimpleCharacterizationUnitBean("Analysis and Conclusion", charBean.getConclusion());
		} else
			aUnit = new SimpleCharacterizationUnitBean("Analysis and Conclusion", "N/A");
		charBeanUnits.add(aUnit);
		
		return charBeanUnits;
	}
	
	protected void transferExperimentConfigurations(Map<String, Object> charBeanMap, List<SimpleCharacterizationUnitBean> charBeanUnits, CharacterizationBean charBean) {
		if (charBean.getExperimentConfigs().size() == 0) {
			//SimpleCharacterizationUnitBean aUnit = new SimpleCharacterizationUnitBean("Experiment Configurations", "N/A");
			//charBeanUnits.add(aUnit);
			return;
		}

		logger.debug("Experiment Configurations size: " + charBean.getExperimentConfigs().size());
		List<ExperimentConfigBean> expConfigBeans = charBean.getExperimentConfigs();

		MultiMap technique = new MultiValueMap();
		MultiMap instruments = new MultiValueMap();
		MultiMap description = new MultiValueMap();

		List<MultiMap> expConfigTable = new ArrayList<MultiMap>();

		for (ExperimentConfigBean aBean : expConfigBeans) {
			String techDisplayName = (aBean.getTechniqueDisplayName() == null) ? "" : aBean.getTechniqueDisplayName();
			
			//String instrDisplayNames = (aBean.getInstrumentDisplayNames() == null) ? "" : aBean.getInstrumentDisplayNames();
			String desc = (aBean.getDomain().getDescription() == null) ? "" : aBean.getDomain().getDescription();
			
			String[] instNames = aBean.getInstrumentDisplayNames();
			StringBuilder sb = new StringBuilder();
			if (instNames != null)  {

				for (String instName : instNames) {
					if (sb.length() > 0)
						sb.append(",");
					sb.append(instName);
				}
			}
				

			technique.put("Technique", techDisplayName);
			instruments.put("Instruments", sb.toString());
			description.put("Description", desc);

			logger.debug("Tech display name: " + aBean.getTechniqueDisplayName());
		}

		expConfigTable.add(technique);
		expConfigTable.add(instruments);
		expConfigTable.add(description);

		charBeanMap.put("Experiment Configurations", expConfigTable);
		
		SimpleCharacterizationUnitBean aUnit = new SimpleCharacterizationUnitBean("Experiment Configurations", expConfigTable);
		charBeanUnits.add(aUnit);

	}
	
	protected List transferCharacterizationProperties(CharacterizationBean charBean) {
		
		SimpleCharacterizationProperty simpleProp = 
				CharacterizationPropertyUtil.getPropertyClassByCharName(charBean.getCharacterizationName());
		try {
			if (simpleProp != null) {
				simpleProp.transferFromPropertyBean(null, charBean, false);
				
				List<String> titles = simpleProp.getPropertyViewTitles();
				List<String> vals = simpleProp.getPropertyViewValues();
				
				List<SimpleCharacterizationUnitBean> rowsOfTable = new ArrayList<SimpleCharacterizationUnitBean>();
				rowsOfTable.add(new SimpleCharacterizationUnitBean("colTitles", titles));
				rowsOfTable.add(new SimpleCharacterizationUnitBean("colValues", vals));
				
				return rowsOfTable;
				
			}
		} catch (Exception e) {
			logger.error("Error while transferring data from property");
		}
		
		return null;
	}
	
	protected List transferCharacterizationResultsDataAndCondition(FindingBean findingBean) {

		logger.debug("Data and Conditions:");
		List<SimpleCharacterizationUnitBean> rowsOfTable = new ArrayList<SimpleCharacterizationUnitBean>();
		
		List<Row> rows = findingBean.getRows();

		if (rows == null || rows.size() == 0)
			return rowsOfTable;

		List<ColumnHeader>  colHeaders = findingBean.getColumnHeaders();
		int colSize = colHeaders.size();
		
		List<String> rowVals = new ArrayList<String>();
		for (ColumnHeader colHeader : colHeaders) {
			String colTitle = colHeader.getDisplayName();

			logger.debug("==Header: " + colTitle);
			rowVals.add(colTitle);
		}
		
		rowsOfTable.add(new SimpleCharacterizationUnitBean("colTitles", rowVals));
		
		for (Row aRow : rows) {
			rowVals = new ArrayList<String>();
			List<TableCell> cells = aRow.getCells();
			for (TableCell cell : cells) {
				rowVals.add(cell.getValue());
			}
			
			if (rowVals.size() == colSize) 
				rowsOfTable.add(new SimpleCharacterizationUnitBean("colValues", rowVals));
			else
				logger.error("Size of Data and Conditions column values doesn't match column header size.");
		}
		
		return rowsOfTable;
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
				logger.debug("uriExternal: " + fileBean.getDomainFile().getId());
				//aFile.put("fileId", fileBean.getDomainFile().getId());
				aFile.put("uri", fileBean.getDomainFile().getUri());
			} else if (fileBean.isImage()){
				logger.debug("Is image: " + fileBean.getDomainFile().getTitle());
				aFile.put("imageTitle", fileBean.getDomainFile().getTitle());
			} else {
				logger.debug("Have download link here");
				aFile.put("title", fileBean.getDomainFile().getTitle());
			}
			
			if (fileBean.getKeywordsStr() != null && fileBean.getKeywordsStr().length() > 0) {
				logger.info("keyword displayname: " + fileBean.getKeywordsDisplayName());
				aFile.put("keywordsString", fileBean.getKeywordsDisplayName());
			}

			
			if (fileBean.getDomainFile().getDescription() != null && fileBean.getDomainFile().getDescription().length() > 0) {
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

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}



	public class SimpleCharacterizationViewBean {
		
		long charId;
		String charType;
		String charClassName;
		
		long parentSampleId;
		
		
		List<SimpleCharacterizationUnitBean> displayableItems;

		public long getCharId() {
			return charId;
		}

		public void setCharId(long charId) {
			this.charId = charId;
		}

		public String getCharClassName() {
			return charClassName;
		}

		public void setCharClassName(String charClassName) {
			this.charClassName = charClassName;
		}

		public List<SimpleCharacterizationUnitBean> getDisplayableItems() {
			return displayableItems;
		}

		public void setDisplayableItems(
				List<SimpleCharacterizationUnitBean> displayableItems) {
			this.displayableItems = displayableItems;
		}

		public String getCharType() {
			return charType;
		}

		public void setCharType(String charType) {
			this.charType = charType;
		}

		public long getParentSampleId() {
			return parentSampleId;
		}

		public void setParentSampleId(long parentSampleId) {
			this.parentSampleId = parentSampleId;
		}

		public void transferData(CharacterizationBean charBean, List<SimpleCharacterizationUnitBean> displayableItems,
				String sampleId, String charType) {
			if (charBean.getDomainChar().getId() != null)
				charId = charBean.getDomainChar().getId();
			
			charClassName = charBean.getClassName();
			
			if (sampleId != null)
				parentSampleId = Long.valueOf(sampleId);
			
			this.charType = charType;
			
			setDisplayableItems(displayableItems);
		}
	}
	
}
