package com.java.spring.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.java.spring.dto.DsrDto;
import com.java.spring.dto.Emp;
import com.java.spring.model.PersonalImage;
import com.java.spring.service.EmpService;

/**
 * Handles requests for the Employee service.
 */
@Controller
public class EmployeeController {
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
@Autowired
private EmpService empService;

	
	@RequestMapping(value = URIConstants.DUMMY_EMP, method = RequestMethod.GET)
	public @ResponseBody Emp getDummyEmployee() {
		logger.info("Start getDummyEmployee");
		Emp emp = new Emp();
//		emp.setId(9999l);
//		emp.setName("Dummy");
		return emp;
	}
	
	@RequestMapping(value = URIConstants.GET_EMP, method = RequestMethod.GET)
	public @ResponseBody Emp getEmployee(@PathVariable("id") int empId) {
		logger.info("Start getEmployee. ID="+empId);
		return empService.getUser(new Long(empId));
	}
	

	@RequestMapping(value = URIConstants.CREATE_EMP, method = RequestMethod.POST)
	public @ResponseBody Emp createEmployee(@RequestBody Emp emp) {
		logger.info("Start createEmployee.");
//		emp.setCreatedDate(new Date());
//		empData.put(emp.getId(), emp);
		empService.addNewUser(emp);
		System.out.println(emp.toString());
		return emp;
	}
	
	@RequestMapping(value = URIConstants.DELETE_EMP, method = RequestMethod.PUT)
	public @ResponseBody Emp deleteEmployee(@PathVariable("id") int empId) {
		logger.info("Start deleteEmployee.");
		Emp emp = new Emp();
		return emp;
	}
	
	@RequestMapping(value = URIConstants.AUTH_EMP, method = RequestMethod.POST)
	public @ResponseBody Emp loginEmployee(@RequestBody Emp emp) {
		logger.info("Start deleteEmployee.");
		emp=empService.getLoginUser(emp);
		return emp;
	}
	
	@RequestMapping(value = "asdasdsa", method = RequestMethod.POST)
	public @ResponseBody DsrDto createUpdateTimesheet(@RequestBody DsrDto dsrdto) {
		logger.info("Start createEmployee.");
		
		return dsrdto;
	}

	@RequestMapping(value = "/upload")
	public @ResponseBody Long uploadFile(@RequestParam("file") MultipartFile file) {
		PersonalImage personalImage=new PersonalImage();
		Emp emp=new Emp();
		try {
			byte[] b =new byte[file.getBytes().length];
			b = file.getBytes();
			System.out.println("bytesRead " + b.length);
			personalImage.setImage(file.getBytes());
			personalImage.setImageName(file.getName());
			personalImage=empService.saveOrUpdate(personalImage);
			
			emp.setDpid(personalImage.getPiId());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return personalImage.getPiId();
	}
	@RequestMapping(value = "/download/{id}")
	public @ResponseBody String downloadFile(@PathVariable("id") int id,HttpServletResponse response,HttpServletRequest request) {
		PersonalImage personalImage=new PersonalImage();
		Emp emp=new Emp();
		String base64Encoded = "";
		try {

			FileOutputStream fos = new FileOutputStream("C:/test/output.jpeg");
			personalImage = empService.getImage(new Long(id));
			fos.write(personalImage.getImage());
			fos.close();
			emp.setFetchImage(personalImage.getImage());
			System.out.println(personalImage.getImage().toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return base64Encoded;
	}
	
}
