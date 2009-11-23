package gov.nih.nci.cananolab.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelParser {
	private String fileName;

	public ExcelParser(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Vertically parse the Excel file into a 2-D matrix represented as a map of map.
	 * Key is Column header, value is a map, whose key is Row header and value is
	 * the cell.
	 * 
	 * @return
	 * @throws IOException
	 */
	public Map<String, SortedMap<String, Double>> verticalParse() throws IOException {
		InputStream inputStream = null;
		Map<String, SortedMap<String, Double>> dataMatrix = new TreeMap<String, SortedMap<String, Double>>();
		try {
			inputStream = new BufferedInputStream(new FileInputStream(fileName));
			POIFSFileSystem fs = new POIFSFileSystem(inputStream);
			Workbook wb = new HSSFWorkbook(fs);
			Sheet sheet1 = wb.getSheetAt(0);
			//printSheet(sheet1);
			Row firstRow = sheet1.getRow(0);
			int rowIndex = 0;
			for (Row row : sheet1) {
				int colIndex = 0;
				String rowHeader = row.getCell(0).getStringCellValue();
				for (Cell cell : row) {
					if (rowIndex > 0 && colIndex > 0) { //skipping first row/column
						String columnHeader = firstRow.getCell(colIndex)
								.getStringCellValue();
						SortedMap<String, Double> columnData = null;
						if (dataMatrix.get(columnHeader) != null) {
							columnData = dataMatrix.get(columnHeader);
						} else {
							columnData = new TreeMap<String, Double>();
						}
						if (cell != null) {
							columnData.put(rowHeader, cell.getNumericCellValue());
							dataMatrix.put(columnHeader, columnData);
						}
					}
					colIndex++;
				}
				rowIndex++;
			}
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
				}
			}
		}
		return dataMatrix;
	}

	/**
	 * Horizontally parse the Excel file into a 2-D matrix represented as a map of map.
	 * Key is Row header, value is a map, whose key is Column header and value is
	 * the cell.
	 * 
	 * @return
	 * @throws IOException
	 */
	public Map<String, SortedMap<String, Double>> horizontalParse()	throws IOException {
		InputStream inputStream = null;
		Map<String, SortedMap<String, Double>> dataMatrix = new TreeMap<String, SortedMap<String, Double>>();
		try {
			inputStream = new BufferedInputStream(new FileInputStream(fileName));
			POIFSFileSystem fs = new POIFSFileSystem(inputStream);
			Workbook wb = new HSSFWorkbook(fs);
			Sheet sheet1 = wb.getSheetAt(0);
			//printSheet(sheet1);
			Row firstRow = sheet1.getRow(0);
			int rowIndex = 0;
			for (Row row : sheet1) {
				int colIndex = 0;
				String rowHeader = row.getCell(0).getStringCellValue();
				for (Cell cell : row) {
					if (rowIndex > 0 && colIndex > 0) { //skipping first row/column
						String columnHeader = firstRow.getCell(colIndex)
								.getStringCellValue();
						SortedMap<String, Double> rowData = null;
						if (dataMatrix.get(rowHeader) != null) {
							rowData = dataMatrix.get(rowHeader);
						} else {
							rowData = new TreeMap<String, Double>();
						}
						if (cell != null) {
							rowData.put(columnHeader, cell.getNumericCellValue());
							dataMatrix.put(rowHeader, rowData);
						}
					}
					colIndex++;
				}
				rowIndex++;
			}
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
				}
			}
		}
		return dataMatrix;
	}

	public void printSheet(Sheet sheet) {
		for (Row row : sheet) {
			for (Cell cell : row) {
				CellReference cellRef = new CellReference(cell.getRowIndex(),
						cell.getColumnIndex());
				System.out.print(cellRef.formatAsString());
				System.out.print(" - ");

				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					System.out.println(cell.getRichStringCellValue()
							.getString());
					break;
				case Cell.CELL_TYPE_NUMERIC:
					if (DateUtil.isCellDateFormatted(cell)) {
						System.out.println(cell.getDateCellValue());
					} else {
						System.out.println(cell.getNumericCellValue());
					}
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					System.out.println(cell.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_FORMULA:
					System.out.println(cell.getCellFormula());
					break;
				default:
					System.out.println();
				}
			}
		}
	}

	public void printMatrix(Map<String, SortedMap<String, Double>> dataMatrix) {
		for (String key : dataMatrix.keySet()) {
			System.out.println("key:" + key);
			Map<String, Double> data = dataMatrix.get(key);
			for (Map.Entry<String, Double> entry : data.entrySet()) {
				System.out.println("key-" + entry.getKey()+": "+entry.getValue());
			}
		}
	}

	public static void main(String[] args) {
		if (args != null && args.length == 1) {
			String inputFileName = args[0];
			try {
				ExcelParser parser = new ExcelParser(inputFileName);
				Map<String, SortedMap<String, Double>> matrix1 = 
					parser.verticalParse();
				parser.printMatrix(matrix1);
				Map<String, SortedMap<String, Double>> matrix2 = 
					parser.horizontalParse();
				parser.printMatrix(matrix2);
			} catch (IOException e) {
				System.out.println("Input file not found.");
				e.printStackTrace();
				System.exit(1);
			}
		} else {
			System.out.println("Invalid argument!");
			System.out.println("java ExcelParser <inputFileName>");
		}
		System.exit(0);
	}
}
