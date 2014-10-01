package gov.nih.nci.cananolab.restful.view;

import gov.nih.nci.cananolab.domain.characterization.OtherCharacterization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.nanomaterial.OtherNanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.dto.common.LinkableItem;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleSearchBean;
import gov.nih.nci.cananolab.dto.particle.CharacterizationQueryBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.restful.bean.SimpleAdvancedResultCellBean;
import gov.nih.nci.cananolab.restful.bean.SimpleAdvancedResultCellUnitBean;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class SimpleAdvancedSearchSampleBean extends SimpleSearchSampleBean {
	private Logger logger = Logger.getLogger(SimpleAdvancedSearchSampleBean.class);

	
	List<SimpleAdvancedResultCellBean> columns = new ArrayList<SimpleAdvancedResultCellBean>();
	
	boolean showPOC;
	boolean showNanomaterialEntity;
	boolean showFunctionalizingEntity;
	boolean showFunction;

	public boolean isShowPOC() {
		return showPOC;
	}



	public List<SimpleAdvancedResultCellBean> getColumns() {
		return columns;
	}



	public void setColumns(List<SimpleAdvancedResultCellBean> columns) {
		this.columns = columns;
	}



	public void setShowPOC(boolean showPOC) {
		this.showPOC = showPOC;
	}


	public boolean isShowNanomaterialEntity() {
		return showNanomaterialEntity;
	}


	public void setShowNanomaterialEntity(boolean showNanomaterialEntity) {
		this.showNanomaterialEntity = showNanomaterialEntity;
	}



	public boolean isShowFunctionalizingEntity() {
		return showFunctionalizingEntity;
	}


	public void setShowFunctionalizingEntity(boolean showFunctionalizingEntity) {
		this.showFunctionalizingEntity = showFunctionalizingEntity;
	}

	public boolean isShowFunction() {
		return showFunction;
	}


	public void setShowFunction(boolean showFunction) {
		this.showFunction = showFunction;
	}


	public void transferAdvancedSampleBeanForResultView(AdvancedSampleBean sampleBean,
			UserBean user, AdvancedSampleSearchBean searchBean) {

		if (sampleBean == null)
			return;
		
		setSampleId(sampleBean.getDomainSample().getId());
		setSampleName(sampleBean.getDomainSample().getName());
		
		List<String> sampleIdName = new ArrayList<String>();
		sampleIdName.add(this.sampleName);
		sampleIdName.add(String.valueOf(this.sampleId));
		
		
		this.columns.add(new SimpleAdvancedResultCellBean("Sample", sampleIdName));
		
		List<String> columnNames = searchBean.getQueryAsColumnNames();
		Map<String, List<LinkableItem>> dataMap = sampleBean.getAttributeMap();
		
		populatePOCs(dataMap, columnNames);
		populateNanomaterialEntity(dataMap, columnNames);
		populateFunctionalizingEntity(dataMap, columnNames);
		populateFunctionNames(dataMap, columnNames);
		
		populateCharacterizations(sampleBean, searchBean);
	}
	
	protected String getKeyFromColumnNames(List<String> columnNames, String token) {
		for (String columnName : columnNames) {
			if (columnName.contains(token))
				return columnName;
		}
		
		return "";
	}
	
	protected String getKeyFromColumnNamesExactMatch(List<String> columnNames, String token) {
		for (String columnName : columnNames) {
			if (columnName.equals(token))
				return columnName;
		}
		
		return "";
	}
	
	protected List<SimpleAdvancedResultCellUnitBean> transferFromLinkableItems(List<LinkableItem> items) {
		List<SimpleAdvancedResultCellUnitBean> units = new ArrayList<SimpleAdvancedResultCellUnitBean>();
		
		for (LinkableItem item :items) {
			String dn = item.getDisplayName();
			List<String> dnStrs = item.getDisplayStrings();
			String dataId = item.getAction();
			
			logger.debug(dn + "||" + dataId);
			
			SimpleAdvancedResultCellUnitBean unit = new SimpleAdvancedResultCellUnitBean();
			
			String names = "";
			for (String dnStr : dnStrs) {
				if (names.length() > 0)
					names += "<br>";
				names += dnStr;
			}
			
			unit.setDisplayName(names);
			
			if (dataId != null) {
				String [] relatedItems = dataId.split(":");
				if (relatedItems.length == 2) { //a hack to handle Function data
					unit.setDataId(Long.parseLong(relatedItems[1]));
					unit.setRelatedEntityType(relatedItems[0]);
				} else
					unit.setDataId(Long.parseLong(dataId));
			}
			
			unit.setDataId(Long.parseLong(dataId));
			units.add(unit);
			
		}
		
		return units;
	}
	
	protected void populatePOCs(Map<String, List<LinkableItem>> dataMap, List<String> columnNames) {
		
		String key = getKeyFromColumnNames(columnNames, "point of contact");
		List<LinkableItem> pocs = dataMap.get(key);
		
		if (pocs == null) return;
		
		List<SimpleAdvancedResultCellUnitBean> units = this.transferFromLinkableItems(pocs);
		
		this.columns.add(new SimpleAdvancedResultCellBean("POC", units));
	}
	
	protected void populateNanomaterialEntity( Map<String, List<LinkableItem>> dataMap, List<String> columnNames) {
		String key = getKeyFromColumnNames(columnNames, "nanomaterial entity");
		List<LinkableItem> nanoItems = dataMap.get(key);
		if (nanoItems == null) return;
		
		List<SimpleAdvancedResultCellUnitBean> units = this.transferFromLinkableItems(nanoItems);
		
		this.columns.add(new SimpleAdvancedResultCellBean("Nanomaterial", units));
	}
	
	protected void populateFunctionalizingEntity(Map<String, List<LinkableItem>> dataMap, List<String> columnNames) {
		String key = getKeyFromColumnNames(columnNames, "functionalizing entity");
		List<LinkableItem> items = dataMap.get(key);
		if (items == null) return;
		
		List<SimpleAdvancedResultCellUnitBean> units = this.transferFromLinkableItems(items);
		this.columns.add(new SimpleAdvancedResultCellBean("FunctionalizaingEntity", units));
	}
	
	protected void populateFunctionNames(Map<String, List<LinkableItem>> dataMap, List<String> columnNames) {
		String key = getKeyFromColumnNamesExactMatch(columnNames, "function");
		List<LinkableItem> items = dataMap.get(key);
		if (items == null) return;
		
		List<SimpleAdvancedResultCellUnitBean> units = this.transferFromLinkableItems(items);
		this.columns.add(new SimpleAdvancedResultCellBean("Function", units));
	}
	
	protected void populateCharacterizations(AdvancedSampleBean sampleBean, AdvancedSampleSearchBean searchBean) {
		if (searchBean.getCharacterizationQueries().size() == 0)
			return;
		
		List<Characterization> chars = sampleBean.getCharacterizations();

		if (chars == null) 
			return;
		
		Map<String, List<SimpleAdvancedResultCellUnitBean>> charsByType = 
				new HashMap<String, List<SimpleAdvancedResultCellUnitBean>>();
		
		for (Characterization achar : chars) {
			
			long id = achar.getId();
			String type = "";
			String name = "";
			
			if (achar instanceof OtherCharacterization) {
//				type = ((OtherCharacterization) achar)
//						.getAssayCategory();
				type = ((OtherCharacterization) achar).getName();
				logger.debug("Type: " + type + " Name: " + name);
			} else {
				String superClassShortName = ClassUtils
						.getShortClassName(achar.getClass().getSuperclass()
								.getName());
				type = ClassUtils
						.getDisplayName(superClassShortName);
				
				name = ClassUtils.getDisplayName(ClassUtils.getShortClassName(achar.getClass().getName()));
				
				logger.debug("Type: " + type + " Name: " + name);
			}
			
			
			String assayType = achar.getAssayType();
			
			List<SimpleAdvancedResultCellUnitBean> currentTypeList = charsByType.get(type);
			
			if (currentTypeList == null) {
				currentTypeList = new ArrayList<SimpleAdvancedResultCellUnitBean>();
				charsByType.put(type, currentTypeList);
			}
			
			SimpleAdvancedResultCellUnitBean simpleChar = new SimpleAdvancedResultCellUnitBean();
			simpleChar.setDataId(id);
			
			if (assayType != null && assayType.length() > 0)
				name += ":" + assayType;
			simpleChar.setDisplayName(name);
			
			currentTypeList.add(simpleChar);
			
		}
		
		List<CharacterizationQueryBean> charQueries = searchBean.getCharacterizationQueries();
		
		for (CharacterizationQueryBean cqBean : charQueries) {
			List<SimpleAdvancedResultCellUnitBean> cc = charsByType.get(cqBean.getCharacterizationType());
			if (cc == null)
				cc = new ArrayList<SimpleAdvancedResultCellUnitBean>();
			
//			for (SimpleCharacterizationAdvancedSearchResultBean result : cc) {
//				List<String> cell = new ArrayList<String>();
//				cell.add("Characterization");
//				cell.add(result.getDisplayName());
//				cell.add(String.valueOf(result.getCharId()));
//			}
			
			
			//this.columns.add(cc);
			this.columns.add(new SimpleAdvancedResultCellBean("Characterization", cc));
		}

	}
	
	
}
