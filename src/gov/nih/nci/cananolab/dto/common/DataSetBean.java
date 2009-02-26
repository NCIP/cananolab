package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.DataRow;
import gov.nih.nci.cananolab.domain.common.DataSet;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.util.DateUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * View bean for Datum
 *
 * @author pansu, tanq
 *
 */
public class DataSetBean {
	private DataSet domain = new DataSet();
	private DataRowBean theDataRow = new DataRowBean();
	private List<DataRowBean> dataRows = new ArrayList<DataRowBean>();
	private List<Datum> data = new ArrayList<Datum>();
	private FileBean file = new FileBean();
	private List<String> columns = new ArrayList<String>();
	private List<DataColumnBean> datumColumnBeans = new ArrayList<DataColumnBean>();
	private List<DataColumnBean> conditionColumnBeans = new ArrayList<DataColumnBean>();
	private List<DataColumnBean> columnBeans = new ArrayList<DataColumnBean>();
	
	public DataSetBean() {

	}

	public DataSetBean(List<Datum> data) {
		domain = data.get(0).getDataSet();
		Map<DataRow, List<Datum>> dataMap = new HashMap<DataRow, List<Datum>>();
		List<DataRow> rows = new ArrayList<DataRow>();
		List<Datum> dataPerRow = null;
		for (Datum datum : data) {
			if (!rows.contains(datum.getDataRow())) {
				rows.add(datum.getDataRow());
			}
			if (dataMap.containsKey(datum.getDataRow())) {
				dataPerRow = dataMap.get(datum.getDataRow());
			} else {
				dataPerRow = new ArrayList<Datum>();
				dataMap.put(datum.getDataRow(), dataPerRow);
			}
			dataPerRow.add(datum);
		}
		for (DataRow row : rows) {
			DataRowBean rowBean = new DataRowBean(dataMap.get(row));
			dataRows.add(rowBean);
		}

		// get column information from first data row
		for (Datum datum : dataRows.get(0).getData()) {
			// TODO add other info to column
			columns.add(datum.getName());
		}
		for (Condition condition : dataRows.get(0).getConditions()) {
			// TODO add other info to column
			columns.add(condition.getName());
		}
	}

	/**
	 * @return the theDataRow
	 */
	public DataRowBean getTheDataRow() {
		return theDataRow;
	}

	/**
	 * @param theDataRow
	 *            the theDataRow to set
	 */
	public void setTheDataRow(DataRowBean theDataRow) {
		this.theDataRow = theDataRow;
	}

	/**
	 * @return the dataRows
	 */
	public List<DataRowBean> getDataRows() {
		return dataRows;
	}

	/**
	 * @param dataRows
	 *            the dataRows to set
	 */

	public void addDataRow() {
		dataRows.add(this.theDataRow);
	}

	public void addDataRow(DataRowBean dataRow) {
		if (dataRows.contains(dataRow)) {
			dataRows.remove(dataRow);
		}
		dataRows.add(dataRow);
	}

	public void removeDataRow(DataRowBean dataRow) {
		dataRows.remove(dataRow);
	}

	public List<Datum> getData() {
		for (DataRowBean row : dataRows) {
			data.addAll(row.getData());
		}
		return data;
	}

	public void setupDomain(String createdBy) {
		int i = 0;
		for (DataRowBean dataRowBean : dataRows) {
			for (Datum datum : dataRowBean.getData()) {
				datum.setCreatedBy(createdBy);
				datum.setCreatedDate(DateUtil.addSecondsToCurrentDate(i));
				datum.setDataSet(domain);
				i++;
			}
		}
	}

	/**
	 * @return the domain
	 */
	public DataSet getDomain() {
		return domain;
	}

	public FileBean getFile() {
		return file;
	}

	public void setFile(FileBean file) {
		this.file = file;
	}

	public DataRowBean getDataRowBean(Datum myDatum) {
		DataRowBean dataRowBean = null;
		for (DataRowBean drb : this.dataRows) {
			Collection<Datum> datumColletion = drb.getData();
			if (datumColletion.contains(myDatum)) {
				dataRowBean = drb;
				break;
			}
		}
		return dataRowBean;
	}

	public List<String> getColumns() {
		return columns;
	}


	
	public void addDatumColumnBean(DataColumnBean columnBean) {		
		if (datumColumnBeans.contains(columnBean)) {
			for (DataColumnBean thisColumnBean : datumColumnBeans) {
				if (thisColumnBean.getId().equals(columnBean.getId())) {
					thisColumnBean.setName(columnBean.getName());
					thisColumnBean.setValueType(columnBean.getValueType());
					thisColumnBean.setValueUnit(columnBean.getValueUnit());
					thisColumnBean.setDatumOrCondition(columnBean.getDatumOrCondition());
					thisColumnBean.setProperty(columnBean.getProperty());
				}
			}
		} else {
			datumColumnBeans.add(columnBean);
		}
	}
	
	
	public void addConditionColumnBean(DataColumnBean columnBean) {		
		if (conditionColumnBeans.contains(columnBean)) {
			for (DataColumnBean thisColumnBean : conditionColumnBeans) {
				if (thisColumnBean.getId().equals(columnBean.getId())) {
					thisColumnBean.setName(columnBean.getName());
					thisColumnBean.setValueType(columnBean.getValueType());
					thisColumnBean.setValueUnit(columnBean.getValueUnit());
					thisColumnBean.setDatumOrCondition(columnBean.getDatumOrCondition());
					thisColumnBean.setProperty(columnBean.getProperty());
				}
			}
		} else {
			conditionColumnBeans.add(columnBean);
		}
	}

	/**
	 * @return the datumColumnBeans
	 */
	public List<DataColumnBean> getDatumColumnBeans() {
		return datumColumnBeans;
	}

	/**
	 * @param datumColumnBeans the datumColumnBeans to set
	 */
	public void setDatumColumnBeans(List<DataColumnBean> datumColumnBeans) {
		this.datumColumnBeans = datumColumnBeans;
	}

	/**
	 * @return the conditionColumnBeans
	 */
	public List<DataColumnBean> getConditionColumnBeans() {
		return conditionColumnBeans;
	}

	/**
	 * @param conditionColumnBeans the conditionColumnBeans to set
	 */
	public void setConditionColumnBeans(List<DataColumnBean> conditionColumnBeans) {
		this.conditionColumnBeans = conditionColumnBeans;
	}

	/**
	 * @return the columnBeans
	 */
	public List<DataColumnBean> getColumnBeans() {
		columnBeans.clear();
		columnBeans.addAll(datumColumnBeans);
		columnBeans.addAll(conditionColumnBeans);
		//System.out.println("####### columnBeans="+columnBeans.size()+" "+columnBeans);
		return columnBeans;
	}

}
