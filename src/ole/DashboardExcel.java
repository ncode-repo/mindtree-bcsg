package ole;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import util.ConfigManager;
import util.Constant;
import util.Log;

public class DashboardExcel {
	static ConfigManager cm=ConfigManager.getInstance();
	static SXSSFWorkbook wb;
	static XSSFWorkbook workbook;
	static Sheet sh;

	//Row numbers for header
	Integer count_RELEASE_HEADER = 0;
	Integer count_MONTH_HEADER = 7;
	Integer count_PRIORITY_HEADER = 10;
	//

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
	
	private void createPriorityDataHeader() {
		createHeader(count_PRIORITY_HEADER, cm.getProperty(Constant.HEADER_PRIORITY), cm.getProperty(Constant.HEADER_PRIORITY_DATA).split(","));
		count_PRIORITY_HEADER++;
	}
	
	/**
	 * This method is used create Header
	 *  @param Header Row no
	 *  @param Header data
	 *  @param array of sub headers
	 * 
	 */
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
	
	/**
	 * This method will fill Release wise and Month wise data from specified Row
	 *  @param row
	 *  @param List
	 * 
	 */
	private void fillData(Row row, List<?> lstData) {
		CellStyle normalStyle = getNormalStyle();
		for (int cellnum = 0; cellnum < lstData.size(); cellnum++) {
			Cell cell = row.createCell(cellnum);
			cell.setCellValue(lstData.get(cellnum).toString());
			cell.setCellStyle(normalStyle);
		}
	}
	
	/**
	 * This method will fill priority wise data from specified Row
	 * @param row
	 * @param List<Map>lstData
	 * 
	 */
	private void fillPriorityData(Row row, List<Map<String, Integer>> lstData) {
		CellStyle normalStyle = getNormalStyle();
		//This is for to print 1st col i.e. Priority type {P1,P2,P3,Other}
		final String priortyArr[]={"P1","P2","P3","Other","Total"};
		try{
			for (int rownum = count_PRIORITY_HEADER, i = 0; rownum <=count_PRIORITY_HEADER
					+ lstData.get(0).size(); rownum++, i++) {
				Cell cell = row.createCell(0);
				cell.setCellValue(priortyArr[i]);
				cell.setCellStyle(normalStyle);
				row = sh.createRow(rownum + 1);
			}
			//This will be for -{Raised, Closed, Re-opened} vals
			//used to iterate data column wise
			for(int colnum = 1;colnum<=lstData.size();colnum++){
				int count =0;
				Map<String, Integer> map = lstData.get(colnum-1);
				//fill data column wise
				for (int rownum = count_PRIORITY_HEADER,i=0; rownum <=count_PRIORITY_HEADER+lstData.get(0).size(); rownum++,i++) {
					row = sh.getRow(rownum);
					Cell cell = row.createCell(colnum);
					//set data in cell and calculating total
					if(i<priortyArr.length-1){
						cell.setCellValue(map.get(priortyArr[i].toLowerCase()));
						count+=map.get(priortyArr[i].toLowerCase());
					}
					cell.setCellStyle(normalStyle);
				}
				row = sh.getRow(count_PRIORITY_HEADER+lstData.get(0).size());
				Cell cell = row.createCell(colnum);
				// set Total count data in cell 
				cell.setCellValue(count);
				cell.setCellStyle(normalStyle);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * This method will write data in Excel
	 * @param fileName
	 * @param List<List<?>> lstData
	 * 
	 */
	public void writeDataInExcel(String fileName, List<List<?>> lstData) {
		wb = new SXSSFWorkbook(100); // keep 100 rows in memory, exceeding rows
		// will be flushed to disk
		sh = wb.createSheet("Dashboard data");

		// Release wise
		createReleaseDataHeader();
		Row row = sh.createRow(++count_RELEASE_HEADER);
		if (lstData != null) {
			List<List<String>> lstRelesewiseData =(List<List<String>>) lstData.get(0);
			Iterator <List<String>> itrRelesewiseData = lstRelesewiseData.iterator(); 
			while(itrRelesewiseData.hasNext()){
				row = sh.createRow(++count_RELEASE_HEADER);
				fillData(row, itrRelesewiseData.next());
			}
		}
		
		count_MONTH_HEADER = count_RELEASE_HEADER +2;
		// Month wise
		createMonthDataHeader();
		row = sh.createRow(++count_MONTH_HEADER);
		if (lstData != null) {
			fillData(row, lstData.get(1));
		}
		count_PRIORITY_HEADER = count_MONTH_HEADER+2;
		
		// Priority wise Month data
		createPriorityDataHeader();
		row = sh.createRow(++count_PRIORITY_HEADER);
		if(lstData!=null){
			List<Map<String, Integer>> mapData = new ArrayList<Map<String, Integer>>();
			mapData=(List<Map<String, Integer>>) lstData.get(2);
			fillPriorityData(row, mapData);
		}
		
		autoResizeColumns();

		File f = getFile(fileName);
		try {
			FileOutputStream out = new FileOutputStream(f, false);
			wb.write(out);
			out.close();
			// dispose of temporary files backing this workbook on disk
			wb.dispose();
			Log.info("File is created");
			// Launch Excel File Created
			//Desktop.getDesktop().open(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This method will return file name
	 * @param fileName
	 * 
	 * @return filename
	 */
	private File getFile(String fileName) {
		File f = new File(fileName);
		if (!f.exists()) {
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
