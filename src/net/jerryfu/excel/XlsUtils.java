package net.jerryfu.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * excel处理工具类
 * @author jerryfu
 *
 */
public class XlsUtils {

	/**
	 * 解析excel文件
	 * @param is
	 * @param suffix
	 * @param startRow
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static List<String[]> rosolveFile(InputStream is, String suffix, int startRow) throws IOException, FileNotFoundException {
		Workbook xssfWorkbook = null;
		if ("xls".equals(suffix)) {
			xssfWorkbook = new HSSFWorkbook(is);
		} else if ("xlsx".equals(suffix)) {
			xssfWorkbook = new XSSFWorkbook(is);
		}
		Sheet xssfSheet = xssfWorkbook.getSheetAt(0);
		if (xssfSheet == null) {
			return null;
		}
		ArrayList<String[]> list = new ArrayList<String[]>();
		int lastRowNum = xssfSheet.getLastRowNum();
		for (int rowNum = startRow; rowNum <= lastRowNum; rowNum++) {
			if (xssfSheet.getRow(rowNum) != null) {
				Row xssfRow = xssfSheet.getRow(rowNum);
				short firstCellNum = xssfRow.getFirstCellNum();
				short lastCellNum = xssfRow.getLastCellNum();
				if (firstCellNum != lastCellNum) {
					String[] values = new String[lastCellNum];
					for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
						Cell xssfCell = xssfRow.getCell(cellNum);
						if (xssfCell == null) {
							values[cellNum] = "";
						} else {
							values[cellNum] = parseCell(xssfCell);
						}
					}
					list.add(values);
				}
			}
		}
		return list;
	}

	private static String parseCell(Cell cell) {
		String result = new String();
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:// 数字类型
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				// 处理日期格式、时间格式
				SimpleDateFormat sdf = null;
				if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
					sdf = new SimpleDateFormat("HH:mm");
				} else {// 日期
					sdf = new SimpleDateFormat("yyyy-MM-dd");
				}
				Date date = cell.getDateCellValue();
				result = sdf.format(date);
			} else if (cell.getCellStyle().getDataFormat() == 58) {
				// 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				double value = cell.getNumericCellValue();
				Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
				result = sdf.format(date);
			} else {
				Double value = cell.getNumericCellValue();
				CellStyle style = cell.getCellStyle();
				result = styleFormat(style, value);
			}
			break;
		case Cell.CELL_TYPE_STRING:// String类型
			result = cell.getRichStringCellValue().toString();
			break;
		case Cell.CELL_TYPE_FORMULA:
			Double value = cell.getNumericCellValue();
			CellStyle style = cell.getCellStyle();
			result = styleFormat(style, value);
			break;
		case Cell.CELL_TYPE_BLANK:
			result = "";
			break;
		default:
			result = cell.toString();
			break;
		}
		return result;
	}

	private static String styleFormat(CellStyle style, Double value) {
		DecimalFormat format = new DecimalFormat();
		String temp = style.getDataFormatString();
		// 单元格设置成常规
		if (temp.equals("General") || temp.equals("0_);[Red]\\(0\\)")) {
			format.applyPattern("#");
		} else {
			format.applyPattern(temp);
		}
		return format.format(value);
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		for (String[] str : rosolveFile(new FileInputStream(new File("D://ttt.xls")), "xls", 3)) {
			for (String s : str) {
				// if (StringUtils.hasText(s))
				System.out.println(s);
			}
		}
	}

}
