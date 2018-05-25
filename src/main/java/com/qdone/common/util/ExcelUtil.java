package com.qdone.common.util;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * excel导出类
 */
public class ExcelUtil {
	
	
	private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

	// 默认单元格内容为数字时格式
	private static DecimalFormat df = new DecimalFormat("0");
	// 默认单元格格式化日期字符串
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// 格式化数字
	private static DecimalFormat nf = new DecimalFormat("0.00");

	public static ArrayList<ArrayList<Object>> readExcel(MultipartFile file) {
		if (file == null) {
			return null;
		}
		if (file.getOriginalFilename().endsWith("xlsx")) {
			// 处理ecxel2007
			return readExcel2007(file);
		} else {
			// 处理ecxel2003
			return readExcel2003(file);
		}
	}

	/*
	 * @return 将返回结果存储在ArrayList内，存储结构与二位数组类似
	 * lists.get(0).get(0)表示过去Excel中0行0列单元格
	 */
	public static ArrayList<ArrayList<Object>> readExcel2003(MultipartFile file) {
		try {
			ArrayList<ArrayList<Object>> rowList = new ArrayList<ArrayList<Object>>();
			ArrayList<Object> colList;
			HSSFWorkbook wb = new HSSFWorkbook(file.getInputStream());
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row;
			HSSFCell cell;
			Object value;
			for (int i = sheet.getFirstRowNum(), rowCount = 0; rowCount < sheet.getPhysicalNumberOfRows(); i++) {
				row = sheet.getRow(i);
				colList = new ArrayList<Object>();
				if (row == null) {
					// 当读取行为空时
					if (i != sheet.getPhysicalNumberOfRows()) {// 判断是否是最后一行
						rowList.add(colList);
					}
					continue;
				} else {
					rowCount++;
				}
				for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
					cell = row.getCell(j);
					if (cell == null || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
						// 当该单元格为空
						if (j != row.getLastCellNum()) {// 判断是否是该行中最后一个单元格
							colList.add("");
						}
						continue;
					}
					switch (cell.getCellType()) {
					case XSSFCell.CELL_TYPE_STRING:
						System.out.println(i + "行" + j + " 列 is String type");
						value = cell.getStringCellValue();
						break;
					case XSSFCell.CELL_TYPE_NUMERIC:
						if ("@".equals(cell.getCellStyle().getDataFormatString())) {
							value = df.format(cell.getNumericCellValue());
						} else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
							value = nf.format(cell.getNumericCellValue());
						} else {
							value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
						}
						System.out.println(i + "行" + j + " 列 is Number type ; DateFormt:" + value.toString());
						break;
					case XSSFCell.CELL_TYPE_BOOLEAN:
						System.out.println(i + "行" + j + " 列 is Boolean type");
						value = Boolean.valueOf(cell.getBooleanCellValue());
						break;
					case XSSFCell.CELL_TYPE_BLANK:
						System.out.println(i + "行" + j + " 列 is Blank type");
						value = "";
						break;
					default:
						System.out.println(i + "行" + j + " 列 is default type");
						value = cell.toString();
					}// end switch
					colList.add(value);
				} // end for j
				rowList.add(colList);
			} // end for i

			return rowList;
		} catch (Exception e) {
			logger.error("异常"+e);
			return null;
		}
	}

	public static ArrayList<ArrayList<Object>> readExcel2007(MultipartFile file) {
		try {
			ArrayList<ArrayList<Object>> rowList = new ArrayList<ArrayList<Object>>();
			ArrayList<Object> colList;
			XSSFWorkbook wb = new XSSFWorkbook(file.getInputStream());
			XSSFSheet sheet = wb.getSheetAt(0);
			XSSFRow row;
			XSSFCell cell;
			Object value;
			for (int i = sheet.getFirstRowNum(), rowCount = 0; rowCount < sheet.getPhysicalNumberOfRows(); i++) {
				row = sheet.getRow(i);
				colList = new ArrayList<Object>();
				if (row == null) {
					// 当读取行为空时
					if (i != sheet.getPhysicalNumberOfRows()) {// 判断是否是最后一行
						rowList.add(colList);
					}
					continue;
				} else {
					rowCount++;
				}
				for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
					cell = row.getCell(j);
					if (cell == null || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
						// 当该单元格为空
						if (j != row.getLastCellNum()) {// 判断是否是该行中最后一个单元格
							colList.add("");
						}
						continue;
					}
					switch (cell.getCellType()) {
					case XSSFCell.CELL_TYPE_STRING:
						// System.out.println(i + "行" + j + " 列 is String
						// type");
						value = cell.getStringCellValue();
						break;
					case XSSFCell.CELL_TYPE_NUMERIC:
						if ("@".equals(cell.getCellStyle().getDataFormatString())) {
							value = df.format(cell.getNumericCellValue());
						} else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
							value = nf.format(cell.getNumericCellValue());
						} else {
							value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
						}
						// System.out.println(i + "行" + j + " 列 is Number type ;
						// DateFormt:" + value.toString());
						break;
					case XSSFCell.CELL_TYPE_BOOLEAN:
						// System.out.println(i + "行" + j + " 列 is Boolean
						// type");
						value = Boolean.valueOf(cell.getBooleanCellValue());
						break;
					case XSSFCell.CELL_TYPE_BLANK:
						// System.out.println(i + "行" + j + " 列 is Blank type");
						value = "";
						break;
					default:
						// System.out.println(i + "行" + j + " 列 is default
						// type");
						value = cell.toString();
					}// end switch
					colList.add(value);
				} // end for j
				rowList.add(colList);
			} // end for i

			return rowList;
		} catch (Exception e) {
			logger.error("readExcel2007异常"+e);
			return null;
		}
	}

	public static void writeExcel(ArrayList<ArrayList<Object>> result, String path) {
		if (result == null) {
			return;
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("sheet1");
		for (int i = 0; i < result.size(); i++) {
			HSSFRow row = sheet.createRow(i);
			if (result.get(i) != null) {
				for (int j = 0; j < result.get(i).size(); j++) {
					HSSFCell cell = row.createCell(j);
					cell.setCellValue(result.get(i).get(j).toString());
				}
			}
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		File file = new File(path);// Excel文件生成后存储的位置。
		OutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(content);
			os.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static DecimalFormat getDf() {
		return df;
	}

	public static void setDf(DecimalFormat df) {
		ExcelUtil.df = df;
	}

	public static SimpleDateFormat getSdf() {
		return sdf;
	}

	public static void setSdf(SimpleDateFormat sdf) {
		ExcelUtil.sdf = sdf;
	}

	public static DecimalFormat getNf() {
		return nf;
	}

	public static void setNf(DecimalFormat nf) {
		ExcelUtil.nf = nf;
	}

    /***
     * 设置下载EXCEL头文件
     */
	public static void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response,
			String fileName) {
		String userAgent = request.getHeader("USER-AGENT").toLowerCase();
		String finalFileName = fileName;
		try {
			if (StringUtils.contains(userAgent, "firefox")) {// 火狐浏览器
				finalFileName = new String(fileName.getBytes(), "ISO8859-1");
			} else {
				finalFileName = URLEncoder.encode(fileName, "UTF8");// 其他浏览器
			}
			response.setHeader("Content-Disposition", "attachment; filename=\"" + finalFileName + "\"");// 这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
		} catch (Exception e) {
			logger.error("setFileDownloadHeader error", e);
		}
	}

	/**
	 * 多sheet导出
	 * 
	 * @param workbook
	 * @param mainTitle
	 * @param titles
	 * @param contents
	 * @param sheetNum
	 * @param totalSheet
	 */
	public final static void buildExcel(SXSSFWorkbook wb, String mainTitle, String[] titles, List<Object[]> contents,
			int sheetNum, int totalSheet, OutputStream os) {
		try {
			/** **********创建工作表************ */
			Sheet sheet = wb.createSheet("Sheet" + sheetNum);

			/** ************设置单元格字体************** */
			Font font = wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示

			/** ************以下设置三种单元格样式************ */
			CellStyle cellStyle = wb.createCellStyle();
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			cellStyle.setFont(font);
			Row row = sheet.createRow(0);
			for (short i = 0; i < titles.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(titles[i]);
			}
			/** ***************以下是EXCEL正文数据********************* */
			for (int i = 0; i < contents.size(); i++) {// row
				Object[] rowContent = contents.get(i);
				Row row2 = sheet.createRow(i + 1);
				for (int j = 0; j < titles.length; j++) { // cell
					Cell cell = row2.createCell(j);
					cell.setCellValue(String.valueOf(rowContent[j]));
				}
			}
			if (totalSheet == sheetNum) {
				wb.write(os);
				os.close();
			}

		} catch (Exception e) {
			logger.error("buildExcel error,", e);
		}
	}

	/**
	 * 多sheet导出
	 *   创建excel存储本地
	 * @param workbook
	 * @param mainTitle
	 * @param titles
	 * @param contents
	 * @param sheetNum
	 * @param path
	 */
	public final static void buildExcel(SXSSFWorkbook wb, String mainTitle, String[] titles, List<Object[]> contents,
			int sheetNum, int totalSheet, String path) {
		try {
			/** **********创建工作表************ */
			Sheet sheet = wb.createSheet("Sheet" + sheetNum);

			/** ************设置单元格字体************** */
			Font font = wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示

			/** ************以下设置三种单元格样式************ */
			CellStyle cellStyle = wb.createCellStyle();
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			cellStyle.setFont(font);
			Row row = sheet.createRow(0);
			for (short i = 0; i < titles.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(titles[i]);
			}
			/** ***************以下是EXCEL正文数据********************* */
			for (int i = 0; i < contents.size(); i++) {// row
				Object[] rowContent = contents.get(i);
				Row row2 = sheet.createRow(i + 1);
				for (int j = 0; j < titles.length; j++) { // cell
					Cell cell = row2.createCell(j);
					cell.setCellValue(String.valueOf(rowContent[j]));
				}
			}
			if (totalSheet == sheetNum) {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				wb.write(os);
				byte[] content = os.toByteArray();
				File file = new File(path);// Excel文件生成后存储的位置。
				OutputStream fos = new FileOutputStream(file);
				fos.write(content);
				os.close();
				fos.close();
			}
		} catch (Exception e) {
			logger.error("buildExcel error", e);
		}
	}

}
