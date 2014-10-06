package gov.nih.nci.cananolab.restful.view;

import gov.nih.nci.cananolab.dto.common.LinkableItem;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleSearchBean;
import gov.nih.nci.cananolab.restful.bean.LabelValueBean;
import gov.nih.nci.cananolab.restful.bean.SimpleAdvancedResultCellBean;
import gov.nih.nci.cananolab.restful.bean.SimpleAdvancedResultCellUnitBean;
import gov.nih.nci.cananolab.service.security.UserBean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class SimpleAdvancedSearchSampleBean extends SimpleSearchSampleBean {
	private Logger logger = Logger.getLogger(SimpleAdvancedSearchSampleBean.class);

	
	List<SimpleAdvancedResultCellBean> columns = new ArrayList<SimpleAdvancedResultCellBean>();
	
	List<Map<String, Object>> rowCells = new ArrayList<Map<String, Object>>();

	Map<String, List<SimpleAdvancedResultCellUnitBean>> rowCellRefs = new HashMap<String, List<SimpleAdvancedResultCellUnitBean>>();
	
	SimpleAdvancedResultRow dataRow = new SimpleAdvancedResultRow();
	
	
	public List<SimpleAdvancedResultCellBean> getColumns() {
		return columns;
	}

	public void setColumns(List<SimpleAdvancedResultCellBean> columns) {
		this.columns = columns;
	}

	public void transferAdvancedSampleBeanForResultView(AdvancedSampleBean sampleBean,
			UserBean user, AdvancedSampleSearchBean searchBean, List<LabelValueBean> colNames) {

		if (sampleBean == null)
			return;
		
		for (LabelValueBean col : colNames) {
			Map<String, String> data = new HashMap<String, String>();
			String colName = col.getLabel();
			
			if (colName.contains("Sample"))
				data.put(colName, sampleBean.getDomainSample().getName());
			
			
		}
		
		setSampleId(sampleBean.getDomainSample().getId());
		setSampleName(sampleBean.getDomainSample().getName());
		
		List<String> sampleIdName = new ArrayList<String>();
		sampleIdName.add(this.sampleName);
		sampleIdName.add(String.valueOf(this.sampleId));
		
		
		
		
		
		this.columns.add(new SimpleAdvancedResultCellBean("Sample", sampleIdName));
		
		List<String> columnNames = searchBean.getQueryAsColumnNames();
		Map<String, List<LinkableItem>> dataMap = sampleBean.getAttributeMap();
		
//		for (String columnName : columnNames) {
//			List<LinkableItem> items = dataMap.get(columnName);
//			populateColumnContent(columnName, items);
//		}
//		
		
		for (LabelValueBean col : colNames) {
			Map<String, Object> data = new HashMap<String, Object>();
			String colName = col.getLabel();
			
			if (colName.contains("Sample")) {
				data.put(colName, sampleBean.getDomainSample().getName());
				this.rowCells.add(data);
				
				this.dataRow.setSampleId(this.sampleId);
				this.dataRow.setSampleName(this.sampleName);
			} else {
				List<LinkableItem> items = dataMap.get(colName);
				populateColumnContent(colName, items, this.rowCells);
			}
			
		}
	}
	
	public static String getHtmlFieldType(String colName) {
		//map to the type value the html is using.
		if (colName.contains("point of contact"))
			return "pointOfContact";
		else if (colName.contains("nanomaterial entity"))
			return "nanomaterialEntity";
		else if (colName.contains("functionalizing entity"))
			return "functionalizaingEntity";
		else if (colName.equals("function"))
			return "function";
		else if (colName.contains("physico"))
			return "physicalChemicalChar";
		else if (colName.contains("in vivo"))
			return "invivoChar";
		else if (colName.contains("in vitro"))
			return "invitroChar";
		else if (colName.contains("ex vivo"))
			return "exvivoChar";
		
		return "";
	}
	
	protected void populateColumnContent(String colName, List<LinkableItem> items, List<Map<String, Object>> displayableData) {
		
		List<SimpleAdvancedResultCellUnitBean> units = transferFromLinkableItems(items, colName, displayableData);
		
		String type = getHtmlFieldType(colName);
		this.columns.add(new SimpleAdvancedResultCellBean(type, units));
		this.rowCellRefs.put(colName, units);
		
		List<String> formattedDisplayables = new ArrayList<String>();
		for (SimpleAdvancedResultCellUnitBean unit :units) {
			String formatted = unit.getDisplayName() + "|" + unit.getDataId();
			formattedDisplayables.add(unit.getDisplayName() + "|" + unit.getDataId());
			
			if (type.equals("function"))
				formatted += "|" + unit.getRelatedEntityType();
			
			formattedDisplayables.add(formatted);
		}
		
		String methodName = "set" + type.substring(0, 1).toUpperCase() + type.substring(1);
		
		//this.setProperty(this.dataRow, methodName, formattedDisplayables);
		
//		//map to the type value the html is using.
		if (colName.contains("point of contact"))
			this.dataRow.setPointOfContact(formattedDisplayables);
		else if (colName.contains("nanomaterial entity"))
			this.dataRow.setNanomaterialEntity(formattedDisplayables);
		else if (colName.contains("functionalizing entity"))
			this.dataRow.setFunctionalizaingEntity(formattedDisplayables);
		else if (colName.equals("function"))
			this.dataRow.setFunction(formattedDisplayables);
		else if (colName.contains("physico")) 
			this.dataRow.setPhysicalChemicalChar(formattedDisplayables);
		else if (colName.contains("vivo"))
			this.dataRow.setInvivoChar(formattedDisplayables);
		else if (colName.contains("vitro"))
			this.dataRow.setInvitroChar(formattedDisplayables);
		else if (colName.contains("ex"))
			this.dataRow.setExvivoChar(formattedDisplayables);
			
//			
//			
//			List<String> r = new ArrayList<String>();
//			for (SimpleAdvancedResultCellUnitBean unit :units) {
//				r.add(unit.getDisplayName() + "|" + unit.getDataId());
//			}
//			
//			this.dataRow.setPhysicalChemicalChar(r);;
//		}
//		
	}
	
	protected void setProperty(Object targetObj, String methodName, List<String> value) {

		try {
			Class[] paramString = new Class[1];
			paramString[0] = String.class;
			Class currClass = targetObj.getClass();
			Method method = currClass.getMethod(methodName, paramString);

			method.invoke(targetObj, value);

		} catch (SecurityException se) {
			logger.debug(se);
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		} catch (InvocationTargetException ex) {
			ex.printStackTrace();
			return;
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
			return;
		} catch (Exception ex) {
			logger.debug(ex);
		} catch (Throwable t) {
			logger.debug(t);
		}

	}
	
	/**
	 * 
	 * @param items
	 * @param colName
	 * @param dataMap
	 * @return
	 */
	protected List<SimpleAdvancedResultCellUnitBean> transferFromLinkableItems(List<LinkableItem> items, 
			String colName, List<Map<String, Object>> dataMap) {
		List<SimpleAdvancedResultCellUnitBean> units = new ArrayList<SimpleAdvancedResultCellUnitBean>();
		List<String> displayables = new ArrayList<String>();
		Map<String, Object> displayMap = new HashMap<String, Object>();
		
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
			displayables.add(names);
			
			if (dataId != null) {
				String [] relatedItems = dataId.split(":");
				if (relatedItems.length == 2) { //a hack to handle Function data
					unit.setDataId(Long.parseLong(relatedItems[1]));
					unit.setRelatedEntityType(relatedItems[0]);
				} else
					unit.setDataId(Long.parseLong(dataId));
			}
			
			units.add(unit);
			
		}
		
		displayMap.put(colName, displayables);
		dataMap.add(displayMap);
		
		return units;
	}

	public List<Map<String, Object>> getRowCells() {
		return rowCells;
	}

	public void setRowCells(List<Map<String, Object>> rowCells) {
		this.rowCells = rowCells;
	}

	public Map<String, List<SimpleAdvancedResultCellUnitBean>> getRowCellRefs() {
		return rowCellRefs;
	}

	public void setRowCellRefs(
			Map<String, List<SimpleAdvancedResultCellUnitBean>> rowCellRefs) {
		this.rowCellRefs = rowCellRefs;
	}

	public SimpleAdvancedResultRow getDataRow() {
		return dataRow;
	}

	public void setDataRow(SimpleAdvancedResultRow dataRow) {
		this.dataRow = dataRow;
	}

	
}
