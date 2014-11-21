package ole;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import pmr.beans.DevDataBean;
import util.ConfigManager;
import util.Constant;

public class ExportToXls {
	WritableWorkbook workbook;
	static ConfigManager cm= ConfigManager.getInstance();
	private WritableSheet formatExcel(WritableSheet writablesheet1) {
		String head=Constant.HEADER_RELEASE;
		List<String> header = Arrays.asList(Constant.HEADER_RELEASE_DATA);
		try {
			// Setting Background colour for Cells
			Colour bckcolor = Colour.BLUE_GREY;
			WritableCellFormat cellFormat = new WritableCellFormat();
			cellFormat.setBackground(bckcolor);
			// Setting Colour & Font for the Text
			WritableFont font = new WritableFont(WritableFont.ARIAL);
			font.setColour(Colour.WHITE);
			cellFormat.setFont(font);
			// Write the Header to the excel file
			Label label = new Label(0, 0, head);
			writablesheet1.addCell(label);
			writablesheet1.mergeCells(0, 0, 0, 0);
			for (int i = 0; i < header.size(); i++) {
				Label label1 = new Label(i, 1, header.get(i));
				//Label label2 = new Label(i, 1, "");
				writablesheet1.addCell(label1);
				//writablesheet1.addCell(label2);
				//writablesheet1.mergeCells(i, 0, 0, 1);
				
				WritableCell cell = writablesheet1.getWritableCell(i, 0);
				cell.setCellFormat(cellFormat);
			}
		} catch (Exception e) {

		}
		return writablesheet1;
	}

	public void writeDataInExcel(String fileName, List<DevDataBean> lstDevDataBeans) {
		try {
			try {
				workbook = Workbook.createWorkbook(new File(fileName));
				WritableSheet writablesheet1 = workbook.createSheet("SG", 0);
//				WritableSheet writablesheet0 = workbook.createSheet("NN", 3);
//				WritableSheet writablesheet2 = workbook.createSheet("SS", 2);
//				WritableSheet writablesheet3 = workbook.createSheet("N", 1);
				// Set Header
				writablesheet1 = formatExcel(writablesheet1);
				int counter = 1;
				for (DevDataBean devDataBean : lstDevDataBeans) {
					List<String> lstFields = devDataBean.getFields();
					for (int i = 0; i < lstFields.size(); i++) {
						Label label = new Label(i, counter,lstFields.get(i));
						writablesheet1.addCell(label);
					}
					counter++;
				}
				workbook.write();
				workbook.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (WriteException e) {

		}
	}
}
