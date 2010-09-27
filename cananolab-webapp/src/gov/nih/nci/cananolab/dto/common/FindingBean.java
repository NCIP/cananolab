package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
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
	public static final String DATUM_TYPE = "datum";
	public static final String CONDITION_TYPE = "condition";
	private Finding domain = new Finding();
	private List<Row> rows = new ArrayList<Row>();
	private List<FileBean> files = new ArrayList<FileBean>();
	private List<ColumnHeader> columnHeaders = new ArrayList<ColumnHeader>();
	private int numberOfColumns;
	private int numberOfRows;
	private FileBean theFile = new FileBean();
	private int theFileIndex;

	public FindingBean() {
	}

	/**
	 * Constructor for CharBean copying & "findFindingById()".
	 *
	 * @param finding
	 */
	public FindingBean(Finding finding) {
		domain = finding;
		List<Datum> data = null;
		if (finding.getDatumCollection() != null) {
			data = new ArrayList<Datum>(finding.getDatumCollection());
			Collections.sort(data, new Comparators.DatumDateComparator());
		}

		if (finding.getFileCollection() != null
				&& !finding.getFileCollection().isEmpty()) {
			for (File file : finding.getFileCollection()) {
				files.add(new FileBean(file));
			}
			Collections.sort(files, new Comparators.FileBeanDateComparator());
		}

		// generate matrix
		if (data != null && !data.isEmpty()) {
			// get data matrix column headers and generate a map based
			// on headers.
			Map<ColumnHeader, List<Datum>> datumMap = new HashMap<ColumnHeader, List<Datum>>();
			Map<ColumnHeader, List<Condition>> conditionMap = new HashMap<ColumnHeader, List<Condition>>();

			List<Datum> datumList = new ArrayList<Datum>();
			List<Condition> conditionList = new ArrayList<Condition>();
			for (Datum datum : data) {
				// add datum column
				ColumnHeader datumColumn = new ColumnHeader(datum);
				if (!columnHeaders.contains(datumColumn)) {
					columnHeaders.add(datumColumn);
				}
				if (datumMap.get(datumColumn) != null) {
					datumList = datumMap.get(datumColumn);
				} else {
					datumList = new ArrayList<Datum>();
					datumMap.put(datumColumn, datumList);
				}
				datumList.add(datum);
				// add condition columns
				if (datum.getConditionCollection() != null) {
					List<Condition> conditions = new ArrayList<Condition>(datum
							.getConditionCollection());
					Collections.sort(conditions,
							new Comparators.ConditionDateComparator());
					for (Condition condition : conditions) {
						ColumnHeader conditionColumn = new ColumnHeader(
								condition);
						if (!columnHeaders.contains(conditionColumn)) {
							columnHeaders.add(conditionColumn);
						}
						if (conditionMap.get(conditionColumn) != null) {
							conditionList = conditionMap.get(conditionColumn);
						} else {
							conditionList = new ArrayList<Condition>();
							conditionMap.put(conditionColumn, conditionList);
						}
						// in case of copied Finding, ids are all null before
						// persisting
						if (condition.getId() != null) {
							if (!conditionList.contains(condition)) {
								conditionList.add(condition);
							}
						}
						// use created_by field that contains the original ID to
						// test whether condition is already in the list
						else {
							boolean existed = false;
							for (Condition cond : conditionList) {
								if (cond.getCreatedBy().equals(
										condition.getCreatedBy())) {
									existed = true;
									break;
								}
							}
							if (!existed) {
								conditionList.add(condition);
							}
						}
					}
				}
			}
			// sort column headers by created date and set column orders
			setupColumnOrder();

			int numRows = -1;
			// iterate through all datum columns and find the biggest list size
			// as the number of rows
			for (Map.Entry<ColumnHeader, List<Datum>> entry : datumMap
					.entrySet()) {
				int numData = entry.getValue().size();
				if (numData > numRows) {
					numRows = numData;
				}
			}
			// iterate through all condition columns and find the biggest list
			// size as the number of rows
			for (Map.Entry<ColumnHeader, List<Condition>> entry : conditionMap
					.entrySet()) {
				int numConditions = entry.getValue().size();
				if (numConditions > numRows) {
					numRows = numConditions;
				}
			}
			numberOfRows = numRows;
			numberOfColumns = columnHeaders.size();

			for (int i = 0; i < numberOfRows; i++) {
				Row row = new Row();
				for (int j = 0; j < numberOfColumns; j++) {
					ColumnHeader theHeader = columnHeaders.get(j);
					if (theHeader.getColumnType()
							.equals(FindingBean.DATUM_TYPE)) {
						Datum datum = new Datum();
						if (datumMap.get(theHeader) != null
								&& datumMap.get(theHeader).size() > i) {
							datum = datumMap.get(theHeader).get(i);
						}
						row.getCells().add(new TableCell(datum));
					} else if (theHeader.getColumnType().equals(
							FindingBean.CONDITION_TYPE)) {
						Condition condition = new Condition();
						if (conditionMap.get(theHeader) != null
								&& conditionMap.get(theHeader).size() > i) {
							condition = conditionMap.get(theHeader).get(i);
						}
						row.getCells().add(new TableCell(condition));
					}
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

	public List<ColumnHeader> getColumnHeaders() {
		return columnHeaders;
	}

	public void setColumnHeaders(List<ColumnHeader> columnHeaders) {
		this.columnHeaders = columnHeaders;
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

	public void updateMatrix(int numberOfColumns, int numberOfRows) {
		this.numberOfColumns = numberOfColumns;
		this.numberOfRows = numberOfRows;
		List<ColumnHeader> newColumns = new ArrayList<ColumnHeader>();
		if (columnHeaders.size() <= numberOfColumns) {
			newColumns.addAll(columnHeaders);
			for (int i = columnHeaders.size(); i < numberOfColumns; i++) {
				newColumns.add(new ColumnHeader());
			}
		}
		// remove the columnHeaders from the end
		else {
			for (int i = 0; i < numberOfColumns; i++) {
				newColumns.add(columnHeaders.get(i));
			}
		}

		List<Row> newRows = new ArrayList<Row>();
		if (rows.size() <= numberOfRows) {
			newRows.addAll(rows);
			for (int i = rows.size(); i < numberOfRows; i++) {
				newRows.add(new Row());
			}
		}
		// remove the rows from the end
		else {
			for (int i = 0; i < numberOfRows; i++) {
				newRows.add(rows.get(i));
			}
		}
		for (int i = 0; i < numberOfRows; i++) {
			Row row = newRows.get(i);
			List<TableCell> cells = row.getCells();
			List<TableCell> newCells = new ArrayList<TableCell>();
			if (cells.size() <= numberOfColumns) {
				newCells.addAll(cells);
				for (int j = cells.size(); j < numberOfColumns; j++) {
					newCells.add(new TableCell());
				}
			}
			// remove the columnHeaders from the end
			else {
				for (int j = 0; j < numberOfColumns; j++) {
					newCells.add(cells.get(i));
				}
			}
			row.setCells(newCells);
		}
		columnHeaders = new ArrayList<ColumnHeader>(newColumns);
		rows = new ArrayList<Row>();
		rows.addAll(newRows);
	}

	public void removeColumn(int colIndex) {
		columnHeaders.remove(colIndex);
		for (int i = 0; i < rows.size(); i++) {
			List<TableCell> cells = rows.get(i).getCells();
			cells.remove(colIndex);
		}
		numberOfColumns--;
	}

	public void removeRow(int rowIndex) {
		rows.remove(rowIndex);
		numberOfRows--;
	}

	public void setupDomain(String internalFileUriPath, String createdBy)
			throws Exception {
		if (domain.getId() != null && domain.getId() <= 0) {
			domain.setId(null);
		}
		// updated created_date and created_by if id is null
		if (domain.getId() == null) {
			domain.setCreatedBy(createdBy);
			domain.setCreatedDate(Calendar.getInstance().getTime());
		}
		// updated created_by if created_by contains copy, but keep the original
		// created_date
		if (domain.getId() != null
				|| !StringUtils.isEmpty(domain.getCreatedBy())
				&& domain.getCreatedBy().contains(
						Constants.AUTO_COPY_ANNOTATION_PREFIX)) {
			domain.setCreatedBy(createdBy);
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

		for (FileBean fileBean : files) {
			fileBean.setupDomainFile(internalFileUriPath, createdBy);
			domain.getFileCollection().add(fileBean.getDomainFile());
		}
		int rInd = 0;
		for (Row row : rows) {
			int cInd = 0;
			List<Condition> rowConditions = new ArrayList<Condition>();
			List<Datum> rowData = new ArrayList<Datum>();
			for (TableCell cell : row.getCells()) {
				ColumnHeader columnHeader = columnHeaders.get(cInd);
				if (FindingBean.DATUM_TYPE.equals(columnHeader.getColumnType())) {
					Datum datum = cell.getDatum();
					// set bogus empty cell
					if (StringUtils.isEmpty(cell.getValue())) {
						datum.setValue(Float.valueOf(-1));
						datum
								.setCreatedBy(Constants.PLACEHOLDER_DATUM_CONDITION_CREATED_BY);
					} else {
						datum.setValue(Float.valueOf(cell.getValue()));
					}
					datum.setValueType(columnHeader.getValueType());
					datum.setValueUnit(columnHeader.getValueUnit());
					datum.setName(columnHeader.getColumnName());
					rowData.add(datum);
					if (datum.getId() != null && datum.getId() <= 0) {
						datum.setId(null);
					}
					// Update createdBy if createdBy is empty or if copy
					// or if bogus empty when the cell is not empty
					if (StringUtils.isEmpty(datum.getCreatedBy())
							|| Constants.AUTO_COPY_ANNOTATION_PREFIX
									.equals(datum.getCreatedBy())
							|| Constants.PLACEHOLDER_DATUM_CONDITION_CREATED_BY
									.equals(datum.getCreatedBy())
							&& !StringUtils.isEmpty(cell.getValue())) {
						datum.setCreatedBy(createdBy);
					}
					// Update createdDate if id is null and created_date is
					// null.
					// When user updated order, created_date is reset according
					// to the order
					if (datum.getId() == null) {
						datum.setCreatedDate(DateUtils
								.addSecondsToCurrentDate(rInd * 100 + cInd));
					}

				} else if (FindingBean.CONDITION_TYPE.equals(columnHeader
						.getColumnType())) {
					Condition condition = cell.getCondition();
					if (StringUtils.isEmpty(cell.getValue())) {
						condition
								.setValue(Constants.PLACEHOLDER_DATUM_CONDITION_CREATED_BY);
						condition
								.setCreatedBy(Constants.PLACEHOLDER_DATUM_CONDITION_CREATED_BY);
					} else {
						condition.setValue(cell.getValue());
					}
					condition.setValueType(columnHeader.getValueType());
					condition.setValueUnit(columnHeader.getValueUnit());
					condition.setName(columnHeader.getColumnName());
					condition.setProperty(columnHeader.getConditionProperty());
					rowConditions.add(condition);
					if (condition.getId() != null && condition.getId() <= 0) {
						condition.setId(null);
					}
					// Update createdBy if createdBy is empty or if copy
					if (StringUtils.isEmpty(condition.getCreatedBy())
							|| Constants.AUTO_COPY_ANNOTATION_PREFIX
									.equals(condition.getCreatedBy())
							|| Constants.PLACEHOLDER_DATUM_CONDITION_CREATED_BY
									.equals(condition.getCreatedBy())
							&& !StringUtils.isEmpty(cell.getValue())) {
						condition.setCreatedBy(createdBy);
					}
					// Update createdDate if id is null and created_date is
					// null.
					// When user updated order, created_date is reset according
					// to the order
					if (condition.getId() == null) {
						condition.setCreatedDate(DateUtils
								.addSecondsToCurrentDate(rInd * 100 + cInd));
					}
				}
				cInd++;
			}
			// associate conditions to each datum on each row
			for (Datum datum : rowData) {
				if (datum.getConditionCollection() == null) {
					datum.setConditionCollection(new HashSet<Condition>());
				} else {
					datum.getConditionCollection().clear();
				}
				for (Condition condition : rowConditions) {
					datum.getConditionCollection().add(condition);
				}
				domain.getDatumCollection().add(datum);
				datum.setFinding(domain);
				rInd++;
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

	public void addFile(FileBean file, int index) {
		if (index == -1) {
			files.add(file);
			return;
		}
		if (!files.isEmpty()) {
			files.remove(index);
		}
		files.add(index, file);
	}

	public void removeFile(int index) {
		files.remove(index);
	}

	public int getTheFileIndex() {
		return theFileIndex;
	}

	public void setTheFileIndex(int theFileIndex) {
		this.theFileIndex = theFileIndex;
	}

	public void resetDomainCopy(Finding copy, Boolean copyData) {
		copy.setCreatedBy(Constants.AUTO_COPY_ANNOTATION_PREFIX + ":"
				+ copy.getId());
		copy.setId(null);

		// copy data and condition
		if (!copyData) {
			copy.setDatumCollection(null);
		} else {
			Collection<Datum> oldDatums = copy.getDatumCollection();
			if (oldDatums == null || oldDatums.isEmpty()) {
				copy.setDatumCollection(null);
			} else {
				copy.setDatumCollection(new HashSet<Datum>(oldDatums));
				for (Datum datum : copy.getDatumCollection()) {
					String originalDatumId = datum.getId().toString();
					datum.setId(null);
					// keep the bogus place holder if empty datum
					if (StringUtils.isEmpty(datum.getCreatedBy())
							|| !datum
									.getCreatedBy()
									.equals(
											Constants.PLACEHOLDER_DATUM_CONDITION_CREATED_BY)
							&& datum.getValue() != -1) {
						datum
								.setCreatedBy(Constants.AUTO_COPY_ANNOTATION_PREFIX
										+ ":" + originalDatumId);
					}
					// conditions
					Collection<Condition> oldConditions = datum
							.getConditionCollection();
					if (oldConditions == null || oldConditions.isEmpty()) {
						datum.setConditionCollection(null);
					} else {
						datum.setConditionCollection(new HashSet<Condition>(
								oldConditions));
						for (Condition condition : datum
								.getConditionCollection()) {
							String originalCondId = null;
							// condition ID could have been set to null for the
							// previous datum if the same condition is
							// associated with multiple datum
							if (condition.getId() != null) {
								originalCondId = condition.getId().toString();
							}
							condition.setId(null);
							// keep the bogus place holder if empty
							// condition
							if (StringUtils.isEmpty(condition.getCreatedBy())
									|| !Constants.PLACEHOLDER_DATUM_CONDITION_CREATED_BY
											.equals(condition.getCreatedBy())
									&& !condition
											.getValue()
											.equals(
													Constants.PLACEHOLDER_DATUM_CONDITION_CREATED_BY)) {
								if (originalCondId != null)
									condition
											.setCreatedBy(Constants.AUTO_COPY_ANNOTATION_PREFIX
													+ ":" + originalCondId);
							}
						}
					}
				}
			}
		}

		// copy file
		Collection<File> oldFiles = copy.getFileCollection();
		if (oldFiles == null || oldFiles.isEmpty()) {
			copy.setFileCollection(null);
		} else {
			copy.setFileCollection(new HashSet<File>(oldFiles));
			for (File file : copy.getFileCollection()) {
				FileBean fileBean = new FileBean(file);
				fileBean.resetDomainCopy(file);
			}
		}
	}

	// FR# 26194, datum matrix column order.
	public void setupColumnOrder() {
		// sort columnHeaders by createdDate and set column orders
		Collections.sort(columnHeaders,
				new Comparators.ColumnHeaderCreatedDateComparator());
		for (int i = 0; i < columnHeaders.size(); i++) {
			columnHeaders.get(i).setColumnOrder(i + 1);
		}
	}

	// FR# 26194, datum matrix column order.
	public void updateColumnOrder() {
		if (!rows.isEmpty()) {
			Comparators.TableCellComparator cellComparator = new Comparators.TableCellComparator();

			for (Row row : rows) {
				int cInd = 0;
				for (TableCell cell : row.getCells()) {
					ColumnHeader columnHeader = columnHeaders.get(cInd++);
					cell.setColumnOrder(columnHeader.getColumnOrder());
				}
				Collections.sort(row.getCells(), cellComparator);

			}
			Collections.sort(columnHeaders,
					new Comparators.ColumnHeaderComparator());

			// update created_date based on column order
			// update the created date regardless whether created date already
			// existed
			int rInd = 0;
			for (Row row : rows) {
				int cInd = 0;
				for (TableCell cell : row.getCells()) {
					ColumnHeader columnHeader = columnHeaders.get(cInd);
					if (FindingBean.DATUM_TYPE.equals(columnHeader
							.getColumnType())) {
						Datum datum = cell.getDatum();
						datum.setCreatedDate(DateUtils
								.addSecondsToCurrentDate(rInd * 100 + cInd));
					} else if (FindingBean.CONDITION_TYPE.equals(columnHeader
							.getColumnType())) {
						Condition condition = cell.getCondition();
						condition.setCreatedDate(DateUtils
								.addSecondsToCurrentDate(rInd * 100 + cInd));
					}
					cInd++;
				}
				rInd++;
			}
		}
	}
}
