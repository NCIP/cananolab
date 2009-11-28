package gov.nih.nci.cananolab.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
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

import sun.util.logging.resources.logging;

public class ExcelParser {
	/**
	 * Vertically parse the Excel file into a 2-D matrix represented as a map of map.
	 * Key is Column header, value is a map, whose key is Row header and value is
	 * the cell.
	 * 
	 * @return
	 * @throws IOException
	 */
	public SortedMap<String, SortedMap<String, Double>> verticalParse(String fileName)
			throws IOException {
		InputStream inputStream = null;
		SortedMap<String, SortedMap<String, Double>> dataMatrix = new TreeMap<String, SortedMap<String, Double>>();
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
	public SortedMap<String, SortedMap<String, Double>> horizontalParse(String fileName)
			throws IOException {
		InputStream inputStream = null;
		SortedMap<String, SortedMap<String, Double>> dataMatrix = new TreeMap<String, SortedMap<String, Double>>();
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

	/**
	 * Parse secondary StanShaw Excel spreadsheet and store data in a 3-layer map.
	 * 1st layer: sample map, key is sample name (261-13-4), value is the 2nd layer map.
	 * 2nd layer: assay map, key is assay name (Aorta 1), value is the 3rd layer map.
	 * 3rd layer: datum map, there are always 3 entries in this map, for example,  
	 * 			  key is datum name Median (M), value is 9.02194E-08.
	 * 			  key is datum name Mean (M), value is 7.96025E-08.
	 * 			  key is datum name SEM (M), value is 6.12968E-09.
	 *  
	 * @param fileName
	 * @return a 3-layer map
	 * @throws IOException
	 */
	public SortedMap<String, SortedMap<String, SortedMap<String, Double>>> twoWayParse(
			String fileName) throws IOException {
		InputStream inputStream = null;
		SortedMap<String, SortedMap<String, SortedMap<String, Double>>> dataMatrix = 
			new TreeMap<String, SortedMap<String, SortedMap<String, Double>>>();
		try {
			inputStream = new BufferedInputStream(new FileInputStream(fileName));
			POIFSFileSystem fs = new POIFSFileSystem(inputStream);
			Workbook wb = new HSSFWorkbook(fs);
			Sheet sheet1 = wb.getSheetAt(0);
			//printSheet(sheet1);
			// Sheet must contain >= 2 rows (header + data).
			if (sheet1.getLastRowNum() < 1) {
				return dataMatrix;
			}
			// Sheet must contain >= 5 columns (assay, sample + 3 datums).   
			Row firstRow = sheet1.getRow(0);
			if (firstRow.getLastCellNum() < 4) {
				return dataMatrix;
			}
			// Iterate sheet from 2nd row and populate the data matrix.
			for (int rowIndex = 1; rowIndex <= sheet1.getLastRowNum(); rowIndex++) {
				Row row = sheet1.getRow(rowIndex);
				
				//1.get sampleName key for 1st layer map, assayName key for 2 layer map.
				String sampleName = row.getCell(1).getStringCellValue();
				String assayName = row.getCell(0).getStringCellValue();
				
				//2.find sampleMap in dataMatrix, if null create & store new sampleMap.
				SortedMap<String, SortedMap<String, Double>> sampleMap = 
					dataMatrix.get(sampleName);
				if (sampleMap == null) {
					sampleMap = new TreeMap<String, SortedMap<String, Double>>();
					dataMatrix.put(sampleName, sampleMap);
				}
				
				//3.find assayMap in sampleMap, if null create & store new assayMap.
				SortedMap<String, Double> assayMap = sampleMap.get(assayName);
				if (assayMap == null) {
					assayMap = new TreeMap<String, Double>();
					sampleMap.put(assayName, assayMap);
				}
				
				//4.iterate row from col-2 to last column, store datum value.
				for (int colIndex = 2; colIndex <= row.getLastCellNum(); colIndex++) {
					Cell cell = row.getCell(colIndex);
					if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						String datumName = firstRow.getCell(colIndex).getStringCellValue();
						assayMap.put(datumName,	cell.getNumericCellValue());
					}
				}
			}
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {}
			}
			this.print2ndMatrix(dataMatrix);
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

	public void printMatrix(SortedMap<String, SortedMap<String, Double>> dataMatrix) {
		for (String key : dataMatrix.keySet()) {
			System.out.println("key:" + key);
			SortedMap<String, Double> data = dataMatrix.get(key);
			for (Map.Entry<String, Double> entry : data.entrySet()) {
				System.out.println("key-" + entry.getKey()+": "+entry.getValue());
			}
		}
	}
	
	public void print2ndMatrix(SortedMap<String, SortedMap<String, SortedMap<String, Double>>> dataMatrix) {
		int sampleCount = 1;
		for (String sampleName : dataMatrix.keySet()) {
			int assayCount = 1;
			System.out.println("NP" + sampleCount + ": " + sampleName);
			SortedMap<String, SortedMap<String, Double>> assayMap = dataMatrix.get(sampleName);
			for (String assayName : assayMap.keySet()) {
				System.out.println("    Assay" + assayCount + ": " + assayName);
				SortedMap<String, Double> datumMap = assayMap.get(assayName);
				for (String datumName : datumMap.keySet()) {
					Double datumValue = datumMap.get(datumName);
					System.out.println("      Datum: " + datumName + " | " + datumValue);
				}
				assayCount++;
			}
			sampleCount++;
		}
	}

	public static void main(String[] args) {
		if (args != null && args.length == 1) {
			String inputFileName = args[0];
			try {
				ExcelParser parser = new ExcelParser();
				parser.twoWayParse(inputFileName);
				/**
				SortedMap<String, SortedMap<String, Double>> matrix1 = 
					parser.verticalParse(inputFileName);
				parser.printMatrix(matrix1);
				SortedMap<String, SortedMap<String, Double>> matrix2 = 
					parser.horizontalParse(inputFileName);
				parser.printMatrix(matrix2);
				*/
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
