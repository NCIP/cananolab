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
	private List<RowBean> rows = new ArrayList<RowBean>();
	private List<Datum> data = new ArrayList<Datum>();
	private List<FileBean> files = new ArrayList<FileBean>();
	private List<ColumnBean> datumColumnBeans = new ArrayList<ColumnBean>();
	private List<ColumnBean> conditionColumnBeans = new ArrayList<ColumnBean>();
	private List<ColumnBean> columnBeans = new ArrayList<ColumnBean>();
	private RowBean theRow = new RowBean();

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
			RowBean row = new RowBean();
			for (String label : datumColumnLabels) {
				row.getData().add(datumMap.get(label).get(i));
			}
			for (String label : conditionColumnLabels) {
				row.getConditions().add(conditionMap.get(label).get(i));
			}
			row.setRowNumber(i);
			rows.add(row);
		}

		for (Condition condition : rows.get(0).getConditions()) {
			// TODO add other info to column
			ColumnBean conditionColumnBean = new ColumnBean(condition);
			columnBeans.add(conditionColumnBean);
			conditionColumnBeans.add(conditionColumnBean);
		}
		// get column information from first data row
		for (Datum datum : rows.get(0).getData()) {
			// TODO add other info to column
			ColumnBean columnBean = new ColumnBean(datum);
			columnBeans.add(columnBean);
			datumColumnBeans.add(columnBean);
		}
	}

	/**
	 * @return the theRow
	 */
	public RowBean getTheRow() {
		return theRow;
	}

	/**
	 * @param theRow
	 *            the theRow to set
	 */
	public void setTheRow(RowBean theRow) {
		this.theRow = theRow;
	}

	/**
	 * @return the rows
	 */
	public List<RowBean> getRows() {
		return rows;
	}

	/**
	 * @param rows
	 *            the rows to set
	 */

	public void addRow() {
		rows.add(this.theRow);
	}

	public void addRow(RowBean row) {
		int index = rows.indexOf(row);
		if (index != -1) {
			rows.remove(row);
			rows.add(index, row);
		} else {
			rows.add(row);
		}
	}

	public void removeRow(RowBean row) {
		rows.remove(row);
	}

	public List<Datum> getData() {
		for (RowBean row : rows) {
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
		for (RowBean rowBean : rows) {
			for (Datum datum : rowBean.getData()) {
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
				for (Condition condition : rowBean.getConditions()) {
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

	public RowBean getRowBean(Datum myDatum) {
		RowBean rowBean = null;
		for (RowBean drb : this.rows) {
			Collection<Datum> datumColletion = drb.getData();
			if (datumColletion.contains(myDatum)) {
				rowBean = drb;
				break;
			}
		}
		return rowBean;
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
		for (RowBean rowBean : rows) {
			List<Datum> tempDataList = new ArrayList(rowBean.getData());
			index = tempDataList.indexOf(dummyDatum);
			if (index != -1) {
				break;
			}
		}
		if (index >= 0) {
			for (RowBean rowBean : rows) {
				rowBean.removeDatumColumn(index);
			}
		}
		datumColumnBeans.remove(columnBean);
	}

	public void removeConditionColumnBean(ColumnBean columnBean) {
		Condition dummyCondition = new Condition();
		dummyCondition.setId(columnBean.getId());
		int index = -1;
		for (RowBean rowBean : rows) {
			List<Condition> tempConditionList = new ArrayList(rowBean
					.getConditions());
			index = tempConditionList.indexOf(dummyCondition);
			if (index != -1) {
				break;
			}
		}
		if (index >= 0) {
			for (RowBean rowBean : rows) {
				rowBean.removeConditionColumn(index);
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
		for (ColumnBean columnBean : datumColumnBeans) {
			if (columnBean != null
					&& columnBean.getId().equals(datum.getId())) {
				datum.setName(columnBean.getName());
				datum.setValueType(columnBean.getValueType());
				datum.setValueUnit(columnBean.getValueUnit());
			}
		}
	}

	private void setDatumColumnValuesToCondition(Condition condition) {
		for (ColumnBean columnBean : conditionColumnBeans) {
			if (columnBean != null
					&& columnBean.getId().equals(condition.getId())) {
				condition.setName(columnBean.getName());
				condition.setProperty(columnBean.getProperty());
				condition.setValueType(columnBean.getValueType());
				condition.setValueUnit(columnBean.getValueUnit());
			}
		}
		// private List<ColumnBean> conditionColumnBeans = new
		// ArrayList<ColumnBean>();
	}

	public List<FileBean> getFiles() {
		return files;
	}

	public void setRows(List<RowBean> rows) {
		this.rows = rows;
	}

	public void setData(List<Datum> data) {
		this.data = data;
	}

	public void setFiles(List<FileBean> files) {
		this.files = files;
	}

	public void setColumnBeans(List<ColumnBean> columnBeans) {
		this.columnBeans = columnBeans;
	}

	public Finding getDomain() {
		return domain;
	}
}
