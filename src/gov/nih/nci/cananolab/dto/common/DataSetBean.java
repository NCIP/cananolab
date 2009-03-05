package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.DataRow;
import gov.nih.nci.cananolab.domain.common.DataSet;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.cananolab.util.DateUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
	private List<DataColumnBean> datumColumnBeans = new ArrayList<DataColumnBean>();
	private List<DataColumnBean> conditionColumnBeans = new ArrayList<DataColumnBean>();
	private List<DataColumnBean> columnBeans = new ArrayList<DataColumnBean>();

	public DataSetBean() {
	}

	public DataSetBean(DataSet dataSet) {
		domain = dataSet;
		data = new ArrayList<Datum>(dataSet.getDatumCollection());
		Collections.sort(data, new CaNanoLabComparators.DatumDateComparator());
		if (dataSet.getFile() != null) {
			file = new FileBean(dataSet.getFile());
		}
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

		for (Condition condition : dataRows.get(0).getConditions()) {
			// TODO add other info to column
			DataColumnBean conditionColumnBean = new DataColumnBean(condition);
			columnBeans.add(conditionColumnBean);
			conditionColumnBeans.add(conditionColumnBean);
		}
		// get column information from first data row
		for (Datum datum : dataRows.get(0).getData()) {
			// TODO add other info to column
			DataColumnBean dataColumnBean = new DataColumnBean(datum);
			columnBeans.add(dataColumnBean);
			datumColumnBeans.add(dataColumnBean);
		}
	}

	public DataSetBean(List<Datum> data) {
		domain = data.get(0).getDataSet();
		if (domain.getFile() != null) {
			file = new FileBean(domain.getFile());
		}
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
		for (Condition condition : dataRows.get(0).getConditions()) {
			// TODO add other info to column
			DataColumnBean conditionColumnBean = new DataColumnBean(condition);
			columnBeans.add(conditionColumnBean);
			conditionColumnBeans.add(conditionColumnBean);
		}
		for (Datum datum : dataRows.get(0).getData()) {
			// TODO add other info to column
			DataColumnBean dataColumnBean = new DataColumnBean(datum);
			columnBeans.add(dataColumnBean);
			datumColumnBeans.add(dataColumnBean);
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
		int index = dataRows.indexOf(dataRow);
		if (index != -1) {
			dataRows.remove(dataRow);
			dataRows.add(index, dataRow);
		} else {
			dataRows.add(dataRow);
		}
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
		if (domain.getId() != null && domain.getId() <= 0) {
			domain.setId(null);
		}
		if (domain.getDatumCollection() != null) {
			domain.getDatumCollection().clear();
		} else {
			domain.setDatumCollection(new HashSet<Datum>());
		}
		for (DataRowBean dataRowBean : dataRows) {
			for (Datum datum : dataRowBean.getData()) {
				if (datum.getId() != null && datum.getId() <= 0) {
					datum.setId(null);
				}
				// if new
				if (datum.getId() == null) {
					datum.setCreatedBy(createdBy);
					datum.setCreatedDate(DateUtil.addSecondsToCurrentDate(i));
				}
				if (datum.getDataRow().getId() != null
						&& datum.getDataRow().getId() <= 0) {
					datum.getDataRow().setId(null);
				}
				setDatumColumnValuesToDatum(datum);

				if (datum.getConditionCollection() == null) {
					datum.setConditionCollection(new HashSet<Condition>());
				} else {
					datum.getConditionCollection().clear();
				}
				for (Condition condition : dataRowBean.getConditions()) {
					if (condition.getId() != null && condition.getId() <= 0) {
						condition.setId(null);
					}
					// if new
					if (condition.getId() == null) {
						condition.setCreatedBy(createdBy);
						condition.setCreatedDate(DateUtil
								.addSecondsToCurrentDate(i));
					}

					setDatumColumnValuesToCondition(condition);
					datum.getConditionCollection().add(condition);
				}
				domain.getDatumCollection().add(datum);
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

	public void addDatumColumnBean(DataColumnBean columnBean) {
		if (datumColumnBeans.contains(columnBean)) {
			for (DataColumnBean thisColumnBean : datumColumnBeans) {
				if (thisColumnBean.getId().equals(columnBean.getId())) {
					thisColumnBean.setName(columnBean.getName());
					thisColumnBean.setValueType(columnBean.getValueType());
					thisColumnBean.setValueUnit(columnBean.getValueUnit());
					thisColumnBean.setDatumOrCondition(columnBean
							.getDatumOrCondition());
					thisColumnBean.setProperty(columnBean.getProperty());
				}
			}
		} else {
			datumColumnBeans.add(columnBean);
		}
	}

	public void removeDatumColumnBean(DataColumnBean columnBean) {
		Datum dummyDatum = new Datum();
		dummyDatum.setId(columnBean.getId());
		int index = -1;
		for (DataRowBean dataRowBean : dataRows) {
			List<Datum> tempDataList = new ArrayList(dataRowBean.getData());
			index = tempDataList.indexOf(dummyDatum);
			if (index != -1) {
				break;
			}
		}
		if (index >= 0) {
			for (DataRowBean dataRowBean : dataRows) {
				dataRowBean.removeDatumColumn(index);
			}
		}
		datumColumnBeans.remove(columnBean);
	}

	public void removeConditionColumnBean(DataColumnBean columnBean) {
		Condition dummyCondition = new Condition();
		dummyCondition.setId(columnBean.getId());
		int index = -1;
		for (DataRowBean dataRowBean : dataRows) {
			List<Condition> tempConditionList = new ArrayList(dataRowBean
					.getConditions());
			index = tempConditionList.indexOf(dummyCondition);
			if (index != -1) {
				break;
			}
		}
		if (index >= 0) {
			for (DataRowBean dataRowBean : dataRows) {
				dataRowBean.removeConditionColumn(index);
			}
		}
		conditionColumnBeans.remove(columnBean);
	}

	public void addConditionColumnBean(DataColumnBean columnBean) {
		if (conditionColumnBeans.contains(columnBean)) {
			for (DataColumnBean thisColumnBean : conditionColumnBeans) {
				if (thisColumnBean.getId().equals(columnBean.getId())) {
					thisColumnBean.setName(columnBean.getName());
					thisColumnBean.setValueType(columnBean.getValueType());
					thisColumnBean.setValueUnit(columnBean.getValueUnit());
					thisColumnBean.setDatumOrCondition(columnBean
							.getDatumOrCondition());
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
	 * @param datumColumnBeans
	 *            the datumColumnBeans to set
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
	 * @param conditionColumnBeans
	 *            the conditionColumnBeans to set
	 */
	public void setConditionColumnBeans(
			List<DataColumnBean> conditionColumnBeans) {
		this.conditionColumnBeans = conditionColumnBeans;
	}

	/**
	 * @return the columnBeans
	 */
	public List<DataColumnBean> getColumnBeans() {
		columnBeans.clear();
		columnBeans.addAll(conditionColumnBeans);
		columnBeans.addAll(datumColumnBeans);
		return columnBeans;
	}

	/**
	 * Compares <code>obj</code> to it self and returns true if they both are
	 * same
	 *
	 * @param obj
	 */
	public boolean equals(Object obj) {
		if (obj instanceof DataSetBean) {
			DataSetBean dataSetBean = (DataSetBean) obj;
			if (getDomain().getId() != null
					&& getDomain().getId().equals(
							dataSetBean.getDomain().getId()))
				return true;
		}
		return false;
	}

	/**
	 * Returns hash code for the primary key of the object
	 */
	public int hashCode() {
		if (getDomain().getId() != null)
			return getDomain().getId().hashCode();
		return 0;
	}

	private void setDatumColumnValuesToDatum(Datum datum) {
		for (DataColumnBean dataColumnBean : datumColumnBeans) {
			if (dataColumnBean != null
					&& dataColumnBean.getId().equals(datum.getId())) {
				datum.setName(dataColumnBean.getName());
				datum.setValueType(dataColumnBean.getValueType());
				datum.setValueUnit(dataColumnBean.getValueUnit());
			}
		}
	}

	private void setDatumColumnValuesToCondition(Condition condition) {
		for (DataColumnBean dataColumnBean : conditionColumnBeans) {
			if (dataColumnBean != null
					&& dataColumnBean.getId().equals(condition.getId())) {
				condition.setName(dataColumnBean.getName());
				condition.setProperty(dataColumnBean.getProperty());
				condition.setValueType(dataColumnBean.getValueType());
				condition.setValueUnit(dataColumnBean.getValueUnit());
			}
		}
		// private List<DataColumnBean> conditionColumnBeans = new
		// ArrayList<DataColumnBean>();
	}
}
