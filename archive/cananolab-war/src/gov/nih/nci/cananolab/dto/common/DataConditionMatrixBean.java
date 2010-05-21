package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class DataConditionMatrixBean {
	public static final String DATUM_TYPE = "datum";
	public static final String CONDITION_TYPE = "condition";
	private List<Row> rows = new ArrayList<Row>();
	private List<ColumnHeader> columnHeaders = new ArrayList<ColumnHeader>();
	private int numberOfColumns;
	private int numberOfRows;

	public DataConditionMatrixBean() {
	}

	/**
	 * Constructor for CharBean copying & "findFindingById()".
	 * 
	 * @param finding
	 */
	public DataConditionMatrixBean(List<Datum> data) {
		Collections.sort(data, new Comparators.DatumDateComparator());

		// generate matrix
		if (data != null && !data.isEmpty()) {
			// get data matrix column headers in order and generate a map based
			// on headers.
			Map<ColumnHeader, List<Datum>> datumMap = new HashMap<ColumnHeader, List<Datum>>();
			Map<ColumnHeader, List<Condition>> conditionMap = new HashMap<ColumnHeader, List<Condition>>();

			List<Datum> datumList = new ArrayList<Datum>();
			List<Condition> conditionList = new ArrayList<Condition>();
			for (Datum datum : data) {
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
						if (!conditionList.contains(condition)) {
							conditionList.add(condition);
						}
					}
				}
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
			}

			int numRows = -1;
			// iterate through all condition columns and find the biggest list
			// size as the
			// number of rows
			for (Map.Entry<ColumnHeader, List<Condition>> entry : conditionMap
					.entrySet()) {
				int numConditions = entry.getValue().size();
				if (numConditions > numRows) {
					numRows = numConditions;
				}
			}
			// iterate through all datum columns and find the biggest list size
			// as the number of rows
			for (Map.Entry<ColumnHeader, List<Datum>> entry : datumMap
					.entrySet()) {
				int numData = entry.getValue().size();
				if (numData > numRows) {
					numRows = numData;
				}
			}
			numberOfRows = numRows;
			numberOfColumns = columnHeaders.size();

			for (int i = 0; i < numberOfRows; i++) {
				Row row = new Row();
				for (int j = 0; j < numberOfColumns; j++) {
					if (!conditionMap.isEmpty()) {
						ColumnHeader theHeader = columnHeaders.get(j);
						if (conditionMap.get(theHeader) != null
								&& !conditionMap.get(theHeader).isEmpty()) {
							try {
								Condition condition = conditionMap.get(
										theHeader).get(i);
								row.getCells().add(new TableCell(condition));
							} catch (IndexOutOfBoundsException e) {
								row.getCells().add(new TableCell());
							}
						}
					}
				}
				for (int j = 0; j < numberOfColumns; j++) {
					if (!datumMap.isEmpty()) {
						ColumnHeader theHeader = columnHeaders.get(j);
						if (datumMap.get(theHeader) != null
								&& !datumMap.get(theHeader).isEmpty()) {
							try {
								Datum datum = datumMap.get(theHeader).get(i);
								row.getCells().add(new TableCell(datum));
							} catch (IndexOutOfBoundsException e) {
								row.getCells().add(new TableCell());
							}
						}
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
		columnHeaders = new ArrayList<ColumnHeader>();
		columnHeaders.addAll(newColumns);
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

//	public void setupDomain(String createdBy) throws Exception {
//		for (Row row : rows) {
//			int cInd = 0;
//			List<Condition> rowConditions = new ArrayList<Condition>();
//			List<Datum> rowData = new ArrayList<Datum>();
//			for (TableCell cell : row.getCells()) {
//				ColumnHeader columnHeader = columnHeaders.get(cInd);
//				if (CharacterizationResultBean.DATUM_TYPE.equals(columnHeader
//						.getColumnType())) {
//					Datum datum = cell.getDatum();
//					if (StringUtils.isEmpty(cell.getValue())) {
//						datum.setValue(new Double(-1));
//						datum
//								.setCreatedBy(Constants.PLACEHOLDER_DATUM_CONDITION_CREATED_BY);
//					} else {
//						datum.setValue(Double.valueOf(cell.getValue()));
//					}
//					datum.setValueType(columnHeader.getValueType());
//					datum.setValueUnit(columnHeader.getValueUnit());
//					datum.setName(columnHeader.getColumnName());
//					rowData.add(datum);
//
//				} else if (CharacterizationResultBean.CONDITION_TYPE
//						.equals(columnHeader.getColumnType())) {
//					Condition condition = cell.getCondition();
//					if (StringUtils.isEmpty(cell.getValue())) {
//						condition
//								.setValue(Constants.PLACEHOLDER_DATUM_CONDITION_CREATED_BY);
//						condition
//								.setCreatedBy(Constants.PLACEHOLDER_DATUM_CONDITION_CREATED_BY);
//					} else {
//						condition.setValue(cell.getValue());
//					}
//					condition.setValueType(columnHeader.getValueType());
//					condition.setValueUnit(columnHeader.getValueUnit());
//					condition.setName(columnHeader.getColumnName());
//					condition.setProperty(columnHeader.getConditionProperty());
//					rowConditions.add(condition);
//				}
//				cInd++;
//			}
//			// associate conditions to each datum on each row
//			for (Datum datum : rowData) {
//				if (datum.getId() != null && datum.getId() <= 0) {
//					datum.setId(null);
//				}
//				// if new or copy or bogus empty
//				if (datum.getId() == null
//						|| !StringUtils.isEmpty(datum.getCreatedBy())
//						&& Constants.AUTO_COPY_ANNOTATION_PREFIX.equals(datum
//								.getCreatedBy())
//						|| !StringUtils.isEmpty(datum.getCreatedBy())
//						|| Constants.PLACEHOLDER_DATUM_CONDITION_CREATED_BY
//								.equals(datum.getCreatedBy())
//						&& datum.getValue() == -1) {
//					// keep the bogus place holder created by for empty datum
//					// entered
//					if (StringUtils.isEmpty(datum.getCreatedBy())
//							|| !Constants.PLACEHOLDER_DATUM_CONDITION_CREATED_BY
//									.equals(datum.getCreatedBy())
//							&& datum.getValue() != -1) {
//						datum.setCreatedBy(createdBy);
//					}
//					datum.setCreatedDate(DateUtils.addSecondsToCurrentDate(i));
//				}
//
//				if (datum.getConditionCollection() == null) {
//					datum.setConditionCollection(new HashSet<Condition>());
//				} else {
//					datum.getConditionCollection().clear();
//				}
//				for (Condition condition : rowConditions) {
//					if (condition.getId() != null && condition.getId() <= 0) {
//						condition.setId(null);
//					}
//					// if new or copy or bogus empty
//					if (condition.getId() == null
//							|| !StringUtils.isEmpty(condition.getCreatedBy())
//							&& Constants.AUTO_COPY_ANNOTATION_PREFIX
//									.equals(condition.getCreatedBy())
//							|| !StringUtils.isEmpty(condition.getCreatedBy())
//							&& Constants.PLACEHOLDER_DATUM_CONDITION_CREATED_BY
//									.equals(condition.getCreatedBy())
//							&& condition
//									.getValue()
//									.equals(
//											Constants.PLACEHOLDER_DATUM_CONDITION_CREATED_BY)) {
//						// keep the bogus place holder created by for empty
//						// condition entered
//						if (StringUtils.isEmpty(condition.getCreatedBy())
//								|| !Constants.PLACEHOLDER_DATUM_CONDITION_CREATED_BY
//										.equals(condition.getCreatedBy())
//								&& !condition
//										.getValue()
//										.equals(
//												Constants.PLACEHOLDER_DATUM_CONDITION_CREATED_BY)) {
//							condition.setCreatedBy(createdBy);
//						}
//						condition.setCreatedDate(DateUtils
//								.addSecondsToCurrentDate(i));
//					}
//					datum.getConditionCollection().add(condition);
//				}
//				domain.getDatumCollection().add(datum);
//				datum.setFinding(domain);
//				i++;
//			}
//		}
//	}
}
