package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.DateUtils;

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
public class FindingBean {
	private Finding domain = new Finding();
	private List<DataRowBean> dataRows = new ArrayList<DataRowBean>();
	private List<Datum> data = new ArrayList<Datum>();
	private List<FileBean> files = new ArrayList<FileBean>();
	private List<ColumnBean> datumColumnBeans = new ArrayList<ColumnBean>();
	private List<ColumnBean> conditionColumnBeans = new ArrayList<ColumnBean>();
	private List<ColumnBean> columnBeans = new ArrayList<ColumnBean>();
	private DataRowBean theDataRow = new DataRowBean();

	public FindingBean() {
	}

	public FindingBean(Finding finding) {
		domain = finding;
		data = new ArrayList<Datum>(finding.getDatumCollection());
		Collections.sort(data, new Comparators.DatumDateComparator());

		if (finding.getFileCollection() != null) {
			for (File file : finding.getFileCollection()) {
				files.add(new FileBean(file));
			}
		}
		Collections.sort(files, new Comparators.FileBeanDateComparator());
		// get data matrix column labels in order and generate a map based on
		// labels
		List<String> datumColumnLabels = new ArrayList<String>();
		List<String> conditionColumnLabels = new ArrayList<String>();
		Map<String, List<Datum>> datumMap = new HashMap<String, List<Datum>>();
		Map<String, List<Condition>> conditionMap = new HashMap<String, List<Condition>>();

		List<Datum> datumList = new ArrayList<Datum>();
		List<Condition> conditionList = new ArrayList<Condition>();
		for (Datum datum : data) {
			ColumnBean datumColumn = new ColumnBean(datum);
			String label = datumColumn.getColumnLabel();
			if (!datumColumnLabels.contains(label)) {
				datumColumnLabels.add(label);
			}
			if (datumMap.get(label) != null) {
				datumList = datumMap.get(label);
			} else {
				datumList = new ArrayList<Datum>();
				datumMap.put(label, datumList);
			}
			datumList.add(datum);

			List<Condition> conditions = new ArrayList<Condition>(datum
					.getConditionCollection());
			Collections.sort(conditions,
					new Comparators.ConditionDateComparator());
			for (Condition condition : conditions) {
				ColumnBean conditionColumn = new ColumnBean(condition);
				String clabel = conditionColumn.getColumnLabel();
				if (conditionColumnLabels.contains(clabel)) {
					conditionColumnLabels.add(clabel);
				}
				if (conditionMap.get(clabel) != null) {
					conditionList = conditionMap.get(clabel);
				} else {
					conditionList = new ArrayList<Condition>();
					conditionMap.put(clabel, conditionList);
				}
				conditionList.add(condition);
			}
		}

		// generate dataMatrix
		String firstDatumLabel = datumColumnLabels.get(0);
		int numberOfRows = datumMap.get(firstDatumLabel).size();

		for (int i = 0; i < numberOfRows; i++) {
			DataRowBean row = new DataRowBean();
			for (String label : datumColumnLabels) {
				row.getData().add(datumMap.get(label).get(i));
			}
			for (String label : conditionColumnLabels) {
				row.getConditions().add(conditionMap.get(label).get(i));
			}
			row.setRowNumber(i);
			dataRows.add(row);
		}

		for (Condition condition : dataRows.get(0).getConditions()) {
			// TODO add other info to column
			ColumnBean conditionColumnBean = new ColumnBean(condition);
			columnBeans.add(conditionColumnBean);
			conditionColumnBeans.add(conditionColumnBean);
		}
		// get column information from first data row
		for (Datum datum : dataRows.get(0).getData()) {
			// TODO add other info to column
			ColumnBean dataColumnBean = new ColumnBean(datum);
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
					datum.setCreatedDate(DateUtils.addSecondsToCurrentDate(i));
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
						condition.setCreatedDate(DateUtils
								.addSecondsToCurrentDate(i));
					}

					setDatumColumnValuesToCondition(condition);
					datum.getConditionCollection().add(condition);
				}
				domain.getDatumCollection().add(datum);
				datum.setFinding(domain);
				i++;
			}
		}
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

	public void addDatumColumnBean(ColumnBean columnBean) {
		if (datumColumnBeans.contains(columnBean)) {
			for (ColumnBean thisColumnBean : datumColumnBeans) {
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

	public void removeDatumColumnBean(ColumnBean columnBean) {
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

	public void removeConditionColumnBean(ColumnBean columnBean) {
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

	public void addConditionColumnBean(ColumnBean columnBean) {
		if (conditionColumnBeans.contains(columnBean)) {
			for (ColumnBean thisColumnBean : conditionColumnBeans) {
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
	public List<ColumnBean> getDatumColumnBeans() {
		return datumColumnBeans;
	}

	/**
	 * @param datumColumnBeans
	 *            the datumColumnBeans to set
	 */
	public void setDatumColumnBeans(List<ColumnBean> datumColumnBeans) {
		this.datumColumnBeans = datumColumnBeans;
	}

	/**
	 * @return the conditionColumnBeans
	 */
	public List<ColumnBean> getConditionColumnBeans() {
		return conditionColumnBeans;
	}

	/**
	 * @param conditionColumnBeans
	 *            the conditionColumnBeans to set
	 */
	public void setConditionColumnBeans(
			List<ColumnBean> conditionColumnBeans) {
		this.conditionColumnBeans = conditionColumnBeans;
	}

	/**
	 * @return the columnBeans
	 */
	public List<ColumnBean> getColumnBeans() {
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
		if (obj instanceof FindingBean) {
			FindingBean findingBean = (FindingBean) obj;
			if (domain.getId() != null
					&& domain.getId().equals(findingBean.getDomain().getId()))
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
		for (ColumnBean dataColumnBean : datumColumnBeans) {
			if (dataColumnBean != null
					&& dataColumnBean.getId().equals(datum.getId())) {
				datum.setName(dataColumnBean.getName());
				datum.setValueType(dataColumnBean.getValueType());
				datum.setValueUnit(dataColumnBean.getValueUnit());
			}
		}
	}

	private void setDatumColumnValuesToCondition(Condition condition) {
		for (ColumnBean dataColumnBean : conditionColumnBeans) {
			if (dataColumnBean != null
					&& dataColumnBean.getId().equals(condition.getId())) {
				condition.setName(dataColumnBean.getName());
				condition.setProperty(dataColumnBean.getProperty());
				condition.setValueType(dataColumnBean.getValueType());
				condition.setValueUnit(dataColumnBean.getValueUnit());
			}
		}
		// private List<ColumnBean> conditionColumnBeans = new
		// ArrayList<ColumnBean>();
	}

	public Finding getDomain() {
		return domain;
	}
}
