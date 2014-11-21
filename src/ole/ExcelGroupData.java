package ole;

import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import util.ConfigManager;

public class ExcelGroupData {

	static ConfigManager cm= ConfigManager.getInstance();
    public static void main(String[] args)
    {

        String excelFilename = null;
       
        ExcelGroupData myExcel = new ExcelGroupData();
//        if (args.length < 1)
//        {
//            System.err.println("Usage: java "+ myExcel.getClass().getName()+
//            " Excel_Filename");
//            System.exit(1);
//        }

        //excelFilename = args[0].trim();
        excelFilename = "Test.xls";
        myExcel.generateExcel(excelFilename);

    }

    public void  generateExcel(String excelFilename){

        try {

            //New Workbook
            Workbook wb = new HSSFWorkbook();

            Cell c = null;

            //Cell style for header row
            CellStyle cs = wb.createCellStyle();
            cs.setFillForegroundColor(IndexedColors.LIME.getIndex());
            //cs.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
            Font f = wb.createFont();
            f.setBoldweight(Font.BOLDWEIGHT_BOLD);
            f.setFontHeightInPoints((short) 12);
            cs.setFont(f);
           
            //Cell style for summary row
            CellStyle css = wb.createCellStyle();
            f = wb.createFont();
            f.setBoldweight(Font.BOLDWEIGHT_BOLD);
            f.setFontHeightInPoints((short) 10);
            css.setFont(f);

            //New Sheet
            HSSFSheet sheet1 = null;
            sheet1 = (HSSFSheet) wb.createSheet("myData");

            // Row and column indexes
            int idx = 0;
            int idy = 0;

            //Generate column headings
            Row row = sheet1.createRow(idx);
            c = row.createCell(idy);
            c.setCellValue("Customer");
            c.setCellStyle(cs);
            sheet1.setColumnWidth(idy, 10 * 500);
            idy++;
            c = row.createCell(idy);
            c.setCellValue("Order Number");
            c.setCellStyle(cs);
            sheet1.setColumnWidth(idy, 10 * 500);
            idy++;
            c = row.createCell(idy);
            c.setCellValue("Order Total");
            c.setCellStyle(cs);
            sheet1.setColumnWidth(idy, 10 * 500);
            idy++;
           
            //Skip 2 rows and reset column 
            idx = idx + 3;
            idy = 0;
       
            //Populate detail row data
            int firstRow = idx + 2;
            row = sheet1.createRow(idx);
            c = row.createCell(idy);
            c.setCellValue("ABC");
            idy++;
            c = row.createCell(idy);
            c.setCellValue("101");
            idy++;
            c = row.createCell(idy);
            c.setCellValue(10.99);
            idy++;
           
            //Next row and reset column 
            idx = idx + 1;
            idy = 0;
           
            //Populate detail row data
            row = sheet1.createRow(idx);
            c = row.createCell(idy);
            c.setCellValue("ABC");
            idy++;
            c = row.createCell(idy);
            c.setCellValue("102");
            idy++;
            c = row.createCell(idy);
            c.setCellValue(22.23);
            idy++;
           
            //Next row and reset column 
            idx = idx + 1;
            idy = 0;
           
            //Populate detail row data
            int lastRow = idx + 1;
            row = sheet1.createRow(idx);
            c = row.createCell(idy);
            c.setCellValue("ABC");
            idy++;
            c = row.createCell(idy);
            c.setCellValue("103");
            idy++;
            c = row.createCell(idy);
            c.setCellValue(100.33);
            idy++;
           
            //Next row and reset column 
            idx = idx + 1;
            idy = 0;
           
            //Populate summary row data
            row = sheet1.createRow(idx);
            c = row.createCell(idy);
            c.setCellValue("ABC");
            c.setCellStyle(css);
            idy++;
            c = row.createCell(idy);
            c.setCellValue("");
            c.setCellStyle(css);
            idy++;
            c = row.createCell(idy);
            String myFormula = "SUM(" + getColumnName(idy) + firstRow + ":" + 
                                        getColumnName(idy) + lastRow + ")";
            c.setCellType(Cell.CELL_TYPE_FORMULA);
            c.setCellFormula(myFormula);
            c.setCellStyle(css);
            idy++;
           
///////////////
            //Next row and reset column 
            idx = idx + 1;
            idy = 0;
           
            //Populate summary row data
            row = sheet1.createRow(idx);
            c = row.createCell(idy);
            c.setCellValue("NNN");
            c.setCellStyle(css);
            idy++;
            c = row.createCell(idy);
            c.setCellValue("");
            c.setCellStyle(css);
            idy++;
            c = row.createCell(idy);
            //c.setCellType(Cell.CELL_TYPE_FORMULA);
            //c.setCellFormula(myFormula);
            c.setCellStyle(css);
            idy++;
//////////////            
            //Group the Rows together
            sheet1.groupRow(firstRow-1,lastRow-1);
            sheet1.setRowGroupCollapsed(firstRow-1, true);
           
            FileOutputStream fileOut = new FileOutputStream(excelFilename.trim());

            wb.write(fileOut);
            fileOut.close();

        }
        catch (Exception e) {
            System.out.println(e);
        }

    }
   
    private String getColumnName(int columnNumber) {
       
        String columnName = "";
        int dividend = columnNumber + 1;
        int modulus;

        while (dividend > 0){
            modulus = (dividend - 1) % 26;
            columnName = (char)(65 + modulus) + columnName;
            dividend = (int)((dividend - modulus) / 26);
        } 

        return columnName;
    }


}