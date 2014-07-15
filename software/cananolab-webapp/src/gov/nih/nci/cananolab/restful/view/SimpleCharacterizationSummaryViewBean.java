package gov.nih.nci.cananolab.restful.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

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
	
	Map<String, Object> viewMap = new HashMap<String, Object>();
	
	
	public Map<String, Object> transferData(CharacterizationSummaryViewBean viewBean) {
		
		logger.info("============ SimpleCharacterizationSummaryViewBean.transferData ==================");
		if (viewBean == null) return null;
		
		Map<String, SortedSet<CharacterizationBean>> type2CharsBeans = viewBean.getType2Characterizations();
		
		Map<String, SortedSet<CharacterizationBean>> name2CharBeans = viewBean.getCharName2Characterizations();
		
		Set<String> charTypes = viewBean.getCharacterizationTypes();
		Map<String, SortedSet<String>> charNames = viewBean.getType2CharacterizationNames();
		
		for (String type : charTypes) {
			logger.info("Processing type: " + type);
			SortedSet<String> namesOfType = charNames.get(type);
			
			List<Map<String, Object>> charBeanMapOfType = new ArrayList<Map<String, Object>>();
			
			for (String charname : namesOfType) {
				logger.info("Char Name for type: " + charname + " | " + type);
				
				SortedSet<CharacterizationBean> charBeans = name2CharBeans.get(charname);
				
				for (CharacterizationBean charBean : charBeans) {
					logger.info("Proccessing char bean: " + charBean.getCharacterizationName());
					Map<String, Object> aBeanMap = tranferCharacterizationBeanData(charBean);
					charBeanMapOfType.add(aBeanMap);
					logger.info("End Proccessing char bean: " + charBean.getCharacterizationName());
				}
				
				//add charName - data object to map
				//charBeanMapOfType.
				
			}
			
			viewMap.put(type, charBeanMapOfType);
			
			logger.info("End of Processing type: " + type);
		}		
		
		return viewMap;
	}
	
	/**
	 * Mimick the logic in bodySingleCharacterizationSummaryView.jsp
	 * 
	 * @param charObj
	 */
	public Map<String, Object> tranferCharacterizationBeanData(CharacterizationBean charBean) {
		if (charBean == null)
			return null;
		
		Map<String, Object> charBeanMap = new HashMap<String, Object>();
		
		Characterization charObj = charBean.getDomainChar();
		String charName = charBean.getCharacterizationName();
		String charType = charBean.getCharacterizationType();
	
		if (charObj.getAssayType() != null && charObj.getAssayType().length() > 0) {
			logger.info(charObj.getAssayType());
			charBeanMap.put("Assay Type", charObj.getAssayType());
		} else if (charBean.getCharacterizationType().equals("physico chemical characterization")) {
			logger.info("Assay Type: " + charName);
			charBeanMap.put("Assay Type", charName);
		}
		
		String pocName = charBean.getPocBean().getDisplayName();
		if (pocName != null && pocName.length() > 0) {
			logger.info("Point of Contact: " + pocName);
			charBeanMap.put("Point of Contact", pocName);
		}
		
		if (charBean.getDateString().length() > 0) {
			logger.info("Characterization Date: " + charBean.getDateString());
			charBeanMap.put("Characterization Date", charBean.getDateString());
		}
		
		if (charBean.getProtocolBean().getDisplayName().length() > 0) {
			logger.info("Protocol: " + charBean.getProtocolBean().getDisplayName());
			charBeanMap.put("Protocol", charBean.getProtocolBean().getDisplayName());
		}
		
		if (charBean.isWithProperties()) {
			
			logger.info("======== Details go here ================ ");
			String detailPage = gov.nih.nci.cananolab.restful.sample.InitCharacterizationSetup
					.getInstance().getDetailPage(charType, charName);
			logger.info(detailPage);	
			charBeanMap.put("detailPage", detailPage);
		}
		
		String desigMethodsDesc = charObj.getDesignMethodsDescription().trim();
		if (desigMethodsDesc.length() > 0) {
			logger.info("Design Description: " + desigMethodsDesc);
			charBeanMap.put("Design Description", desigMethodsDesc);
		} else {
			logger.info("Design Description: N/A");
			charBeanMap.put("Design Description", "N/A");
		}
		
		if (charBean.getExperimentConfigs().size() > 0) {
			logger.info("Experiment Configurations size: " + charBean.getExperimentConfigs().size());
			List<ExperimentConfigBean> expConfigBeans = charBean.getExperimentConfigs();
			
			MultiMap technique = new MultiValueMap();
			MultiMap instruments = new MultiValueMap();
			MultiMap description = new MultiValueMap();
			
			SimpleTableBean expConfigTable = new SimpleTableBean();
			expConfigTable.addHeader("Technique");
			expConfigTable.addHeader("Instruments");
			expConfigTable.addHeader("Description");
			
			for (ExperimentConfigBean aBean : expConfigBeans) {
				String techDisplayName = (aBean.getTechniqueDisplayName() == null) ? "" : aBean.getTechniqueDisplayName();
				String instrDisplayNames = (aBean.getInstrumentDisplayNames() == null) ? "" : aBean.getInstrumentDisplayNames().toString();
				String desc = (aBean.getDomain().getDescription() == null) ? "" : aBean.getDomain().getDescription();
				
				
				
				
				List<String> row = new ArrayList<String>();
				row.add(techDisplayName);
				row.add(instrDisplayNames);
				row.add(desc);
				expConfigTable.addRow(row);
				
				logger.info("Tech display name: " + aBean.getTechniqueDisplayName());
				
				
				String[] instDisName = aBean.getInstrumentDisplayNames();
				
				if (instDisName != null && instDisName.length > 0) {
					logger.info("Instrument dis name: " + instDisName.toString());
					
				} 
				
				if (aBean.getDomain().getDescription().length() > 0) {
					logger.info("Domain desc: " + aBean.getDomain().getDescription());
					
				}
				
			}
			
			charBeanMap.put("Experiment Configurations", expConfigTable);
		}
		
		if (charBean.getFindings().size() > 0) {
			logger.info("Characterization Results: " + charBean.getFindings().size());
			List<FindingBean> findings = charBean.getFindings();
			
			for (FindingBean fBean : findings) {
				List<Row> rows = fBean.getRows();
				if (rows != null && rows.size() > 0) {
					logger.info("Data and Conditions:");
					
					List<ColumnHeader>  colHeaders = fBean.getColumnHeaders();
					for (ColumnHeader aH : colHeaders) {
						logger.info("==Header: " + aH.getDisplayName());
					}
					
					for (Row aRow : rows) {
						List<TableCell> cells = aRow.getCells();
						for (TableCell cell : cells) {
							logger.info("==Val: " + cell.getValue());
						}
					}
				}
					
			}
			
			for (FindingBean fBean : findings) {
				List<FileBean> fileBeans = fBean.getFiles(); 
				if (fileBeans != null && fileBeans.size() > 0) {
					logger.info("Files: ");
					
					for (FileBean fileBean : fileBeans) {
						if (fileBean.getDomainFile().getUriExternal()) {
							logger.info("uriExternal: " + fileBean.getDomainFile().getId());
						} else if (fileBean.isImage()){
							logger.info("Is image: " + fileBean.getDomainFile().getTitle());
							
						} else {
							logger.info("Have download link here");
						}
					}
					
					for (FileBean fileBean : fileBeans) {
						if (fileBean.getKeywordsStr() != null && fileBean.getKeywordsStr().length() > 0) 
							logger.info("keyword displayname: " + fileBean.getKeywordsDisplayName());
						
						if (fileBean.getDomainFile().getDescription().length() > 0)
							logger.info("File description: " + fileBean.getDescription());
					}
				}
				
			}
			
			
		
		}
		if (charBean.getConclusion() != null && charBean.getConclusion().length() > 0)
			logger.info("Analysis and Conclusion: " + charBean.getConclusion());
		
		
		return charBeanMap;
	}

}
