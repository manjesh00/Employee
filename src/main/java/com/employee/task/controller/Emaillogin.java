package com.employee.task.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.task.Service.EmployeeService;
import com.employee.task.entity.Employee;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class Emaillogin {
	
	@Autowired
	private EmployeeService employeeService;
	
	 @PostMapping("register")
	    public String processRegister(@RequestBody Employee employee,HttpServletRequest request) throws UnsupportedEncodingException,MessagingException{
		 employeeService.register(employee,getSiteURL(request));
		return "Register Successfull";
	}
	 private String getSiteURL(HttpServletRequest request) {
	        String siteURL = request.getRequestURL().toString();
	        return siteURL.replace(request.getServletPath(), "");
	    }
	 @GetMapping("verify")
	 public String verifyEmployee(@Param("code") String code) {
		 if(employeeService.verify(code)) {
			 return "Verify sucessfully";
		 }else {
			 return "verify denied";
		 }
	 }

}
