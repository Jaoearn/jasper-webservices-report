package com.jasper.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jasper.models.ResumeModels;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


@RestController
public class ApiController {
	@PostMapping("/getmodel")
	public ResumeModels GetModel(@RequestBody ResumeModels data) {
		return data; 
	}
	@PostMapping("/getresume")
	public ResponseEntity<byte[]>  GetResume(@RequestBody ResumeModels resumeModels )throws JRException, IOException{
		
		Map<String, Object> parameter = new HashMap<String, Object>();
		
		JRBeanCollectionDataSource ResumeDataSource = new JRBeanCollectionDataSource(Arrays.asList(resumeModels));
		
	
		parameter.put("ResumeDataSource", ResumeDataSource);
		
		JasperReport jasperDesign = JasperCompileManager
				.compileReport(new FileInputStream("src/main/resources/resume.jrxml"));
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperDesign, parameter, new JREmptyDataSource());
		
		byte data[] = JasperExportManager.exportReportToPdf(jasperPrint);
		System.err.println(data);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=demoSI.pdf");

		System.out.println("Report Generated!");
		
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(data);
	}
}
