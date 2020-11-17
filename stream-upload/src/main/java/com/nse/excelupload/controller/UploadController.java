package com.nse.excelupload.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nse.excelupload.model.Cell;
import com.nse.excelupload.model.ExcelDataVO;
import com.nse.excelupload.service.UploadService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/excel")
@Validated
@Api(value = "Excel Upload API", tags = "EXCEL")
public class UploadController {

	@Autowired
    private UploadService uploadService;

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Upload Excel file as attachment", response = ApiOperation.class, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Cell>> receive(@ApiParam(required = true) @RequestPart(value="file") MultipartFile file) {
    	
    	MultipartFile[] files = {file};
    	ExcelDataVO uploadData = new ExcelDataVO();
    	List<Cell> response=new ArrayList<Cell>();
    	
    	if(files.length > 0) {
    		response=uploadData.setAttachment(files);
    	}
    	//Collection<Cell> response = uploadService.receiveAndProcess(uploadData);
        
        if(response == null) { 
        	return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
		}
        
    	return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
}
