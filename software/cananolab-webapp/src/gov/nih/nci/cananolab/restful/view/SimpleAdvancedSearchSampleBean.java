package gov.nih.nci.cananolab.restful.view;

import gov.nih.nci.cananolab.dto.common.LinkableItem;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleSearchBean;
import gov.nih.nci.cananolab.restful.bean.SimpleAdvancedResultCellBean;
import gov.nih.nci.cananolab.restful.bean.SimpleAdvancedResultCellUnitBean;
import gov.nih.nci.cananolab.service.security.UserBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class SimpleAdvancedSearchSampleBean extends SimpleSearchSampleBean {
	private Logger logger = Logger.getLogger(SimpleAdvancedSearchSampleBean.class);

	
	List<SimpleAdvancedResultCellBean> columns = new ArrayList<SimpleAdvancedResultCellBean>();
	
	public List<SimpleAdvancedResultCellBean> getColumns() {
		return columns;
	}

	public void setColumns(List<SimpleAdvancedResultCellBean> columns) {
		this.columns = columns;
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
		
		for (String columnName : columnNames) {
			List<LinkableItem> items = dataMap.get(columnName);
			populateColumnContent(columnName, items);
		}
	}
	
	
	protected void populateColumnContent(String colName, List<LinkableItem> items) {
		List<SimpleAdvancedResultCellUnitBean> units = transferFromLinkableItems(items);
		
		String type = "";
		
		//map to the type value the html is using.
		if (colName.contains("point of contact"))
			type = "POC";
		else if (colName.contains("nanomaterial entity"))
			type = "Nanomaterial";
		else if (colName.contains("functionalizing entity"))
			type = "FunctionalizaingEntity";
		else if (colName.equals("function"))
			type = "Function";
		else if (colName.contains("characterization"))
			type = "Characterization";
		
		
		this.columns.add(new SimpleAdvancedResultCellBean(type, units));
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
			
			units.add(unit);
			
		}
		
		return units;
	}
}
