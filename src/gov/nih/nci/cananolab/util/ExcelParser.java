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
	 * Parse the Excel file into a 2-D matrix represented as a map of map. Key
	 * is column header, value is a map, whose key is row header and value is
	 * the cell.
	 * 
	 * @return
	 * @throws IOException
	 */
	public SortedMap<String, SortedMap<String, Double>> parse()
			throws IOException {
		Workbook wb = null;

		InputStream inputStream = new BufferedInputStream(new FileInputStream(
				fileName));
		POIFSFileSystem fs = new POIFSFileSystem(inputStream);
		wb = new HSSFWorkbook(fs);

		Sheet sheet1 = wb.getSheetAt(0);
		// printSheet(sheet1);
		SortedMap<String, SortedMap<String, Double>> dataMatrix = new TreeMap<String, SortedMap<String, Double>>();
		Row firstRow = sheet1.getRow(0);
		int i = 0;
		for (Row row : sheet1) {
			String rowHeader = row.getCell(0).getStringCellValue();
			int j = 0;
			for (Cell cell : row) {
				if (i > 0 && j > 0) {
					String columnHeader = firstRow.getCell(j)
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
				j++;
			}
			i++;
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

	public void printMatrix(
			SortedMap<String, SortedMap<String, Double>> dataMatrix) {
		for (String key : dataMatrix.keySet()) {
			System.out.println("column header:" + key);
			Map<String, Double> colData = dataMatrix.get(key);
			for (Map.Entry<String, Double> entry : colData.entrySet()) {
				System.out.println("row header:" + entry.getValue());
			}
		}
	}

	public static void main(String[] args) {
		if (args != null && args.length == 1) {
			String inputFileName = args[0];
			try {
				ExcelParser parser = new ExcelParser(inputFileName);
				SortedMap<String, SortedMap<String, Double>> matrix = parser
						.parse();
				parser.printMatrix(matrix);
			} catch (IOException e) {
				System.out.println("Input file not found.");
				e.printStackTrace();
				System.exit(0);
			}
		} else {
			System.out.println("Invalid argument!");
			System.out.println("java ExcelParser <inputFileName>");
		}
		System.exit(1);
	}
}
