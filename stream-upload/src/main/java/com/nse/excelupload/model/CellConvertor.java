package com.nse.excelupload.model;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import io.swagger.annotations.ApiModel;

//Convert from POI Cell into our type of cell

@ApiModel(value = "Cell Converter")
public class CellConvertor {

	private static final String EXCEL_DATE_STYLE = "m/d/yy";
	private static Log log = LogFactory.getLog(CellConvertor.class);

	
	/**
	 * Convert from Excel to Standard JavaBea
	 * @param rangeName
	 * @param poiCell
	 * @return
	 */
	public static Cell convertExcelToCell(String uniqueCellHandle, XSSFCell poiCell) {
		Cell redCell = new Cell();
		redCell.setCellName(uniqueCellHandle);
		Object value = null;

		if (poiCell != null) {
			int cellType = poiCell.getCellType();

			switch (cellType) {

			case XSSFCell.CELL_TYPE_BLANK:
				value = "blank";
				break;
			case XSSFCell.CELL_TYPE_BOOLEAN:
				value = poiCell.getBooleanCellValue();
				break;
			case XSSFCell.CELL_TYPE_ERROR:
				value = "error";
				break;
			case XSSFCell.CELL_TYPE_FORMULA:
				value = "formula";
			case XSSFCell.CELL_TYPE_NUMERIC:
				value = poiCell.getNumericCellValue();
				break;
			case XSSFCell.CELL_TYPE_STRING:
				XSSFRichTextString hssfValue = poiCell.getRichStringCellValue();
				if(hssfValue!=null){
					value = hssfValue.getString();
				}
				break;
			default:
				value = "default";// do nothing
			}
		} else {
			value = null;
		}

		redCell.setValue(value);
		
		//Reset the modified flag
		//redCell.setModified(false);
		
		return redCell;
	}
	
	/**
	 * Convert from Standard JavaBean to Excel
	 * @param rangeName
	 * @param poiCell
	 * @param fact
	 */
	public static void convertCellToExcel(XSSFWorkbook wb, XSSFCell poiCell,Cell fact) {
		
		//If the cell has no value , then it is null
		//We should create the cell, as we have a value to update into it
		// but for now we just ignfore the update
		if(poiCell==null){
			return;
		}
		
		XSSFCellStyle style = getExcelCellStyle(wb);
	    
		/*if(fact.isModified()){
			
			//Set the generic updated style
			poiCell.setCellStyle(style);
			
			Object value= fact.getValue();
			
			//Ugly, but we can't switch on objects and
			//operator overloading breaks down as we have a handle to a generic 'object'
			if(value!=null &&value instanceof String){
				log.debug("UpdatingCell:"+fact.getCellName()+" value:"+value+" as String");
				HSSFRichTextString textValue=new HSSFRichTextString(value.toString());
				poiCell.setCellValue(textValue);
				poiCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				
			} else if(value!=null &&value instanceof Boolean){
				log.debug("UpdatingCell:"+fact.getCellName()+" value:"+value+" as Boolean");
				poiCell.setCellValue((Boolean)fact.getValue());
				poiCell.setCellType(HSSFCell.CELL_TYPE_BOOLEAN);
				
				
			} else if(value!=null &&value instanceof Number){
				log.debug("UpdatingCell:"+fact.getCellName()+" value:"+value+" as Number");
				Double number = ((Number)value).doubleValue();
				poiCell.setCellValue(number);
				poiCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				
				
			} else if(value!=null &&value instanceof Date){
				
				//Excel dates are numbers with a special style
				log.debug("UpdatingCell:"+fact.getCellName()+" value:"+value+" as Date");
				Double number = ((Number)value).doubleValue();
				poiCell.setCellValue(number);
				poiCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				
				poiCell.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat(EXCEL_DATE_STYLE));

				
			} else if(value!=null){
				
				//Treat as object, use toString() method
				log.debug("UpdatingCell:"+fact.getCellName()+" value:"+value+" as Generic Object");
				HSSFRichTextString textValue=new HSSFRichTextString(value.toString());
				
				poiCell.setCellValue(textValue);
				poiCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
			} else {
				
				//value is null, blank cell
				log.debug("UpdatingCell:"+fact.getCellName()+" value is null");
				poiCell.setCellValue("");
				poiCell.setCellType(HSSFCell.CELL_TYPE_BLANK);
				
			}*/
						
			
		}	
	
	
	
	
	
	
	
	/**
	 * Get the 'updated' style that we use to show that a cell value has been changed
	 * @return
	 */
	protected static XSSFCellStyle getExcelCellStyle(XSSFWorkbook wb) {
		XSSFCellStyle style = wb.createCellStyle();
	    style.setFillForegroundColor(HSSFColor.ORANGE.index);
	    style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	    
	    return style;

	}

}
