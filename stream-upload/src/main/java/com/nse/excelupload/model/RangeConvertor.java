package com.nse.excelupload.model;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFName;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import io.swagger.annotations.ApiModel;


 
@ApiModel(value = "Range Converter")
public class RangeConvertor {

	private static Log log = LogFactory.getLog(RangeConvertor.class);

	/**
	 * Read an excel file and spit out what we find.
	 * 
	 * @param args
	 *            Expect one argument that is the file to read.
	 * @throws IOException
	 *             When there is an error processing the file.
	 */
	public static RangeHolder convertExcelToCells(XSSFWorkbook wb)
			throws IOException {

		RangeHolder returnValues = new RangeHolder();

		// retrieve the named range
		int numberOfNames = wb.getNumberOfNames();

		// Get all the named ranges in our spreadsheet
		for (int namedRangeIdx = 0; namedRangeIdx < numberOfNames; namedRangeIdx++) {
			XSSFName aNamedRage = wb.getNameAt(namedRangeIdx);
			// retrieve the cell at the named range and test its contents
			AreaReference aref = new AreaReference(aNamedRage.getRefersToFormula());
			CellReference[] crefs = aref.getAllReferencedCells();

			// A Range that we will put the new cells into
			Range redRange = new Range(aNamedRage.getNameName());

			for (int thisCellinRange = 0; thisCellinRange < crefs.length; thisCellinRange++) {
				XSSFSheet sheet = wb.getSheet(crefs[thisCellinRange]
						.getSheetName());
				XSSFRow r = sheet.getRow(crefs[thisCellinRange].getRow());

				XSSFCell thisExcelCell = null;
				if (r != null) {
					thisExcelCell = r.getCell(crefs[thisCellinRange].getCol());
					// extract the cell contents based on cell type etc.
				}

				// Create our JavaBean representing the cell
				String cellHandle = redRange.getUniqueCellName(thisCellinRange);
				Cell redCell = CellConvertor
						.convertExcelToCell(cellHandle, thisExcelCell);

				//Give the cell information about who is holding it
				//and that it should pass on property change events to it
				//redCell.setHoldingRange(redRange);
				//redCell.addPropertyChangeListener(redRange);
				
				// Add the list of cells to a range
				redRange.put(cellHandle, redCell);

			}

			returnValues.add(redRange);
		}

		return returnValues;

	}

	/**
	 * Update an excel file with our new values
	 * 
	 */
	public static void convertCellsToExcel(XSSFWorkbook wb,	RangeHolder updatedValues) throws IOException {

		// retrieve the named range
		int numberOfNames = wb.getNumberOfNames();
		
		//Get all names of *all* the cells in *all* the ranges
		Map<String,Cell> allCells =updatedValues.getAllCells();

		for (int namedCellIdx = 0; namedCellIdx < numberOfNames; namedCellIdx++) {
			XSSFName aNamedCell = wb.getNameAt(namedCellIdx);

			// retrieve the cell at the named range and test its contents
			AreaReference aref = new AreaReference(aNamedCell.getRefersToFormula());
			CellReference[] crefs = aref.getAllReferencedCells();

			for (int thisCellinRange = 0; thisCellinRange < crefs.length; thisCellinRange++) {
				XSSFSheet sheet = wb.getSheet(crefs[thisCellinRange]
						.getSheetName());

				XSSFRow r = sheet.getRow(crefs[thisCellinRange].getRow());

				// Get the cell that is referred to
				XSSFCell excelCell = null;
				if (r != null) {
					excelCell = r.getCell(crefs[thisCellinRange].getCol());

					// Check that the range name is on our list
					String cellHandle = Range.getUniqueCellName(aNamedCell
							.getNameName(), thisCellinRange);

					
					if (allCells.containsKey(cellHandle)) {
						CellConvertor.convertCellToExcel(wb, excelCell,
								allCells.get(cellHandle));

					} else {
						log.debug("Name not found in facts:" + cellHandle);
					}

				} else {
					log.info("Null");
				}

			}
		}

	}

}
