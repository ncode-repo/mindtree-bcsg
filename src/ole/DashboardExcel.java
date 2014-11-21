package ole;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import util.ConfigManager;
import util.Constant;

public class DashboardExcel {
	static ConfigManager cm=ConfigManager.getInstance();
	static SXSSFWorkbook wb;
	static XSSFWorkbook workbook;
	static Sheet sh;

	Integer count_RELEASE_HEADER = 0;
	Integer count_MONTH_HEADER = 5;

	/**
	 * This method demonstrates how to Auto resize Excel column
	 */
	private static void autoResizeColumns() {
		for (int colIndex = 0; colIndex < cm.getProperty(Constant.HEADER_RELEASE_DATA).split(",").length; colIndex++) {
			sh.setColumnWidth(colIndex, 5000);//autoSizeColumn(colIndex);
		}
	}

	/**
	 * This method will return Style of Header Cell
	 * 
	 * @return
	 */
	private static CellStyle getHeaderStyle() {
		CellStyle style = wb.createCellStyle();
		style.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setWrapText(true);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style.setAlignment(CellStyle.ALIGN_CENTER);
		return style;
	}

	/**
	 * This method will return style for Normal Cell
	 * 
	 * @return
	 */
	private static CellStyle getNormalStyle() {
		CellStyle style = wb.createCellStyle();

		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style.setAlignment(CellStyle.ALIGN_CENTER);

		return style;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
/*
		wb = new SXSSFWorkbook(100); // keep 100 rows in memory, exceeding rows
										// will be flushed to disk
		sh = wb.createSheet("Sample sheet");

		CellStyle headerStle = getHeaderStyle();
		CellStyle normalStyle = getNormalStyle();

		for (int rownum = 0; rownum < 1000; rownum++) {
			Row row = sh.createRow(rownum);
			for (int cellnum = 0; cellnum < 10; cellnum++) {

				Cell cell = row.createCell(cellnum);
				String address = new CellReference(cell).formatAsString();
				cell.setCellValue(address);

				if (rownum == 0) {

					cell.setCellStyle(headerStle);
				} else {
					cell.setCellStyle(normalStyle);
				}
			}

		}

		// Below code Shows how to merge Cell
		sh.addMergedRegion(new CellRangeAddress(0, // first row (0-based)
				0, // last row (0-based)
				0, // first column (0-based)
				5 // last column (0-based)
		));

		autoResizeColumns();

		/**
		 * To Auto-resize Row, We have to follow two steps 1. Set WordWrap
		 * property in CellStyle to true 2. Set setHeightInPoints of row likw
		 * this : row.setHeightInPoints((totalHtmlLineBreak *
		 * sh.getDefaultRowHeightInPoints())); Where totalHtmlLineBreak is total
		 * lines for auto height
		 *

		File f = new File("c:/DeleteThis/2/Example2.xlsx");

		if (!f.exists()) {
			// If directories are not available then create it
			File parent_directory = f.getParentFile();
			if (null != parent_directory) {
				parent_directory.mkdirs();
			}

			f.createNewFile();
		}

		FileOutputStream out = new FileOutputStream(f, false);
		wb.write(out);
		out.close();

		// dispose of temporary files backing this workbook on disk
		wb.dispose();
		System.out.println("File is created");
		// Launch Excel File Created
		Desktop.getDesktop().open(f);
		*/
		new DashboardExcel().writeDataInExcel("Me.xls",null);
	}

	private void createReleaseDataHeader() {
		createHeader(count_RELEASE_HEADER, cm.getProperty(Constant.HEADER_RELEASE), cm.getProperty(Constant.HEADER_RELEASE_DATA).split(","));
		count_RELEASE_HEADER++;
	}

	private void createMonthDataHeader() {
		createHeader(count_MONTH_HEADER, cm.getProperty(Constant.HEADER_MONTH), cm.getProperty(Constant.HEADER_MONTHLY_DATA).split(","));
		count_MONTH_HEADER++;
	}

	private void createHeader(Integer count, String type, String[] header) {
		// Heading
		CellStyle headerStyle = getHeaderStyle();
		Row row = sh.createRow(count);
		Cell cell = row.createCell(0);
		cell.setCellValue(type);
		cell.setCellStyle(headerStyle);
		sh.addMergedRegion(new CellRangeAddress(count, count, 0, header.length-1));
		// Data heading
		row = sh.createRow(++count);
		List<String> headerData = Arrays.asList(header);
		for (int i = 0; i < headerData.size(); i++) {
			cell = row.createCell(i);
			cell.setCellValue(headerData.get(i));
			cell.setCellStyle(headerStyle);
		}
	}

	private void fillData(Row row, List<String> lstData) {
		CellStyle normalStyle = getNormalStyle();
		for (int cellnum = 0; cellnum < lstData.size(); cellnum++) {
			Cell cell = row.createCell(cellnum);
			cell.setCellValue(lstData.get(cellnum).toString());
			cell.setCellStyle(normalStyle);
		}
	}

	public void writeDataInExcel(String fileName, List<List<String>> lstData) {
		wb = new SXSSFWorkbook(100); // keep 100 rows in memory, exceeding rows
		// will be flushed to disk
		sh = wb.createSheet("Dashboard data");

		// Release wise
		createReleaseDataHeader();
		Row row = sh.createRow(++count_RELEASE_HEADER);
		if (lstData != null) {
			fillData(row, lstData.get(0));
		}
		// Month wise
		createMonthDataHeader();
		row = sh.createRow(++count_MONTH_HEADER);
		if (lstData != null) {
			fillData(row, lstData.get(1));
		}

		autoResizeColumns();

		File f = getFile(fileName);
		try {
			FileOutputStream out = new FileOutputStream(f, false);
			wb.write(out);
			out.close();
			// dispose of temporary files backing this workbook on disk
			wb.dispose();
			System.out.println("File is created");
			// Launch Excel File Created
			//Desktop.getDesktop().open(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private File getFile(String fileName) {
		File f = new File(fileName);
		if (!f.exists()) {
			// If directories are not available then create it
			// File parent_directory = f.getParentFile();
			// if (null != parent_directory) {
			// parent_directory.mkdirs();
			// }
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return f;
	}
}
