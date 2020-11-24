package com.nse.excelupload.model;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.kie.api.KieServices;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.api.runtime.ExecutionResults;
import org.kie.internal.command.CommandFactory;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.RuleServicesClient;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.nse.excelupload.model.Cell;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "Excel Data")
public class ExcelDataVO {

	@ApiModelProperty(value = "Attachment", example = "file", hidden = true)
	@JsonIgnore
	private MultipartFile[] attachment;

	public MultipartFile[] getAttachment() {
		return attachment;
	}

	public List<Cell> setAttachment(MultipartFile[] attachment) {
		this.attachment = attachment;
		
		InputStream inputFromExcel;
		XSSFWorkbook wb;
		try {
			inputFromExcel = attachment[0].getInputStream();
			if (null == inputFromExcel) {
				throw new FileNotFoundException("Cannot find file:");
			} else {
				System.out.println("found file:");
			}
			System.out.println("TEST1");
			wb = new XSSFWorkbook(inputFromExcel);
			System.out.println("TEST2 text"+wb.toString());
			// Convert the cell
			RangeHolder ranges ;
			try {
				//
				/*
				 * final FormulaEvaluator evaluator =
				 * wb.getCreationHelper().createFormulaEvaluator(); for (int i = 0; i <
				 * wb.getNumberOfNames(); i++) { Name name = wb.getNameAt(i); try { if
				 * (!ranges.contains(name.getNameName()) &&
				 * !name.getRefersToFormula().equals("#REF!") &&
				 * name.getSheetName().equals(name.getSheetName())) ; } catch
				 * (IllegalArgumentException e) { // TODO: Add logging. } }
				 */

				//
				ranges = RangeConvertor.convertExcelToCells(wb);
			
			return fireRules(ranges.getAllRangesAndCells());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("exception TEST1 e="+e);
			e.printStackTrace();
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("exception TEST2 e="+e);

			e.printStackTrace();
		} // at this line we are getting an error

return null;
}
	
	public List<Cell> fireRules(Collection<Object> coll)
	{
		
		  String url = "http://localhost:8080/kie-server/services/rest/server";
	        String username = "wbadmin";
	        String password = "wbadmin";
	        String container = "ExcelRules1_1.0.0";
	        KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(url, username, password);
	        Set<Class<?>> allClasses = new HashSet<Class<?>>();
	        allClasses.add(Cell.class);
	        //allClasses.add(Product.class);
	        config.addExtraClasses(allClasses);
	        config.setMarshallingFormat(MarshallingFormat.JSON);
	        KieServicesClient client  = KieServicesFactory.newKieServicesClient(config);
	        RuleServicesClient ruleClient = client.getServicesClient(RuleServicesClient.class);
	        
	        Command<?>[] commands = {
	                CommandFactory.newInsertElements(coll, "cells", true, null),
	                CommandFactory.newFireAllRules("fire-identifier"),
	                //CommandFactory.newGetObjects("com.java.excel.Cell"),
	                CommandFactory.newDispose()
	        };
	        
	        BatchExecutionCommand batchCommand = KieServices.Factory.get().getCommands().newBatchExecution(Arrays.asList(commands));
	        ServiceResponse<ExecutionResults> response = ruleClient.executeCommandsWithResults(container, batchCommand);
	        List<?> list=(List) response.getResult().getValue("cells");
	        System.out.println(list);
	        List<Cell> cellList=new ArrayList<Cell>();
	        for(int i=0;i<list.size();i++)
	        {
	        	if(list.get(i).getClass().getName()!="java.util.LinkedHashMap"){
	        		if(!((Cell)list.get(i)).isValid()){
	        			cellList.add((Cell)list.get(i));
	        		}
	        	}
	        
	        }
	        return cellList;
	       
	        
	             
		
	}


}
