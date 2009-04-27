package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
	private List<Row> rows = new ArrayList<Row>();
	private List<FileBean> files = new ArrayList<FileBean>();
	private List<Column> columns = new ArrayList<Column>();
	private int numberOfColumns;
	private int numberOfRows;
	private FileBean theFile = new FileBean();

	public FindingBean() {
	}

	public FindingBean(Finding finding) {
		domain = finding;
		List<Datum> data = new ArrayList<Datum>(finding.getDatumCollection());
		Collections.sort(data, new Comparators.DatumDateComparator());

		if (finding.getFileCollection() != null) {
			for (File file : finding.getFileCollection()) {
				files.add(new FileBean(file));
			}
		}
		Collections.sort(files, new Comparators.FileBeanDateComparator());

		// get data matrix column headers in order and generate a map based on
		// headers
		Map<Column, List<Datum>> datumMap = new HashMap<Column, List<Datum>>();
		Map<Column, List<Condition>> conditionMap = new HashMap<Column, List<Condition>>();

		List<Datum> datumList = new ArrayList<Datum>();
		List<Condition> conditionList = new ArrayList<Condition>();
		for (Datum datum : data) {
			List<Condition> conditions = new ArrayList<Condition>(datum
					.getConditionCollection());
			Collections.sort(conditions,
					new Comparators.ConditionDateComparator());
			for (Condition condition : conditions) {
				Column conditionColumn = new Column(condition);
				if (!columns.contains(conditionColumn)) {
					columns.add(conditionColumn);
				}
				if (conditionMap.get(conditionColumn) != null) {
					conditionList = conditionMap.get(conditionColumn);
				} else {
					conditionList = new ArrayList<Condition>();
					conditionMap.put(conditionColumn, conditionList);
				}
				conditionList.add(condition);
			}
			Column datumColumn = new Column(datum);
			if (!columns.contains(datumColumn)) {
				columns.add(datumColumn);
			}
			if (datumMap.get(datumColumn) != null) {
				datumList = datumMap.get(datumColumn);
			} else {
				datumList = new ArrayList<Datum>();
				datumMap.put(datumColumn, datumList);
			}
			datumList.add(datum);
		}
		numberOfRows = datumMap.get(columns.get(0)).size();
		numberOfColumns = columns.size();

		// generate matrix
		if (data != null && !data.isEmpty()) {
			for (int i = 0; i < numberOfRows; i++) {
				Row row = new Row();
				for (int j = 0; j < numberOfColumns; j++) {
					Condition condition = conditionMap.get(columns.get(j)).get(
							0);
					row.getCells().add(new TableCell(condition));
					Datum datum = datumMap.get(columns.get(j)).get(0);
					row.getCells().add(new TableCell(datum));
				}
				rows.add(row);
			}
		}
	}

	public List<Row> getRows() {
		return rows;
	}

	public void setRows(List<Row> rows) {
		this.rows = rows;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public int getNumberOfColumns() {
		return numberOfColumns;
	}

	public void setNumberOfColumns(int numberOfColumns) {
		this.numberOfColumns = numberOfColumns;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public void initializeMatrix(int numberOfColumns, int numberOfRows) {
		this.numberOfColumns = numberOfColumns;
		this.numberOfRows = numberOfRows;
		columns = new ArrayList<Column>();
		for (int i = 0; i < numberOfColumns; i++) {
			columns.add(new Column());
		}
		rows = new ArrayList<Row>();
		for (int i = 0; i < numberOfRows; i++) {
			Row row = new Row();
			List<TableCell> cells = new ArrayList<TableCell>();
			for (int j = 0; j < numberOfColumns; j++) {
				cells.add(new TableCell());
			}
			row.setCells(cells);
			rows.add(row);
		}
	}

	public void setupDomain(String createdBy) throws Exception {
		int i = 0;
		if (domain.getId() != null && domain.getId() <= 0) {
			domain.setId(null);
		}
		if (domain.getId() == null
				|| domain.getCreatedBy() != null
				&& domain.getCreatedBy().equals(
						Constants.AUTO_COPY_ANNOTATION_PREFIX)) {
			domain.setCreatedBy(createdBy);
			domain.setCreatedDate(new Date());
		}
		if (domain.getDatumCollection() != null) {
			domain.getDatumCollection().clear();
		} else {
			domain.setDatumCollection(new HashSet<Datum>());
		}
		if (domain.getFileCollection() != null) {
			domain.getFileCollection().clear();
		} else {
			domain.setFileCollection(new HashSet<File>());
		}
		int j = 0;
		for (FileBean file : files) {
			domain.getFileCollection().add(file.getDomainFile());
			j++;
		}

		for (Row row : rows) {
			int cInd = 0;
			List<Condition> rowConditions = new ArrayList<Condition>();
			List<Datum> rowData = new ArrayList<Datum>();
			for (TableCell cell : row.getCells()) {
				Column column = columns.get(cInd);
				if (cell.getDatumOrCondition().equals("Datum")) {
					Datum datum = cell.getDatum();
					datum.setValue(cell.getValue());
					datum.setValueType(column.getValueType());
					datum.setValueUnit(column.getValueUnit());
					datum.setName(column.getName());
					rowData.add(datum);
				} else if (cell.getDatumOrCondition().equals("Condition")) {
					Condition condition = cell.getCondition();
					condition.setValue(cell.getValue());
					condition.setValueType(column.getValueType());
					condition.setValueUnit(column.getValueUnit());
					condition.setName(column.getName());
					condition.setProperty(column.getProperty());
					rowConditions.add(condition);
				}
				cInd++;
			}
			// associate conditions to each datum on each row
			for (Datum datum : rowData) {
				if (datum.getId() != null && datum.getId() <= 0) {
					datum.setId(null);
				}
				// if new
				if (datum.getId() == null) {
					datum.setCreatedBy(createdBy);
					datum.setCreatedDate(DateUtils.addSecondsToCurrentDate(i));
				}

				if (datum.getConditionCollection() == null) {
					datum.setConditionCollection(new HashSet<Condition>());
				} else {
					datum.getConditionCollection().clear();
				}
				for (Condition condition : rowConditions) {
					if (condition.getId() != null && condition.getId() <= 0) {
						condition.setId(null);
					}
					// if new
					if (condition.getId() == null) {
						condition.setCreatedBy(createdBy);
						condition.setCreatedDate(DateUtils
								.addSecondsToCurrentDate(i));
					}
					datum.getConditionCollection().add(condition);
				}

				domain.getDatumCollection().add(datum);
				datum.setFinding(domain);
				i++;
			}
		}
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

	public FileBean getTheFile() {
		return theFile;
	}

	public void setTheFile(FileBean theFile) {
		this.theFile = theFile;
	}

	public List<FileBean> getFiles() {
		return files;
	}

	public void setFiles(List<FileBean> files) {
		this.files = files;
	}

	public Finding getDomain() {
		return domain;
	}

	public void addFile(FileBean file) {
		if (files.contains(file)) {
			files.remove(file);
		}
		files.add(file);
	}

	public void removeFile(FileBean file) {
		files.remove(file);
	}
}
