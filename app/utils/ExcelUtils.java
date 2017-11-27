package utils;

import org.apache.commons.lang.BooleanUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ExcelUtils {
    
    public static List<List<String>> read(File file) {
        List<List<String>> lists = new ArrayList<>();
        try {
            InputStream input = new FileInputStream(file);
            Workbook wb = file.getName().endsWith("xlsx") ? new XSSFWorkbook(input) : new HSSFWorkbook(input);
            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> rows = sheet.rowIterator();
            int column = sheet.getRow(0).getLastCellNum();
            while (rows.hasNext()) {
                List<String> list = new ArrayList<>();
                Row row = rows.next();
                for (int i = 0; i < column; i++) {
                    Cell cell = row.getCell(i);
                    if (cell == null) {
                        list.add("");
                        continue;
                    }
                    switch (cell.getCellType()) {
                        case HSSFCell.CELL_TYPE_STRING:
                            list.add(cell.getStringCellValue());
                            break;
                        case HSSFCell.CELL_TYPE_NUMERIC:
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                Date date = cell.getDateCellValue();
                                if (date == null) {
                                    list.add("");
                                } else {
                                    list.add(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date));
                                }
                            } else {
                                list.add(new DecimalFormat("#").format(cell.getNumericCellValue()));
                            }
                            break;
                        case HSSFCell.CELL_TYPE_BOOLEAN:
                            list.add(BooleanUtils.toStringTrueFalse(cell.getBooleanCellValue()));
                            break;
                        case HSSFCell.CELL_TYPE_FORMULA:
                            list.add(cell.getCellFormula());
                            break;
                        default:
                            list.add("");
                            break;
                    }
                }
                lists.add(list);
            }
            wb.close();
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return lists;
    }
    
    public static void export(List<List<String>> lists, File file) {
        try {
            Workbook wb = file.getName().endsWith("xlsx") ? new XSSFWorkbook() : new HSSFWorkbook();
            Sheet sheet = wb.createSheet("data");
            for (int i = 0; i < lists.size(); i++) {
                List<String> list = lists.get(i);
                Row row = sheet.createRow(i);
                for (int j = 0; j < list.size(); j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(list.get(j));
                }
            }
            // 自动调整列宽
            for (int i = 0; i < lists.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            FileOutputStream fos = new FileOutputStream(file);
            wb.write(fos);
            fos.flush();
            wb.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}