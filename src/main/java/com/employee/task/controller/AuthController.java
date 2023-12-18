package com.employee.task.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.task.Dto.EmployeeDto;
import com.employee.task.Service.EmployeeService;
import com.employee.task.entity.Employee;
import com.employee.task.exception.ApiException;
import com.employee.task.payload.JwtAuthRequest;
import com.employee.task.payload.JwtAuthResponse;
import com.employee.task.security.JwtTokenHelper;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private EmployeeService employeeService;
	
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse>createToken(@RequestBody JwtAuthRequest request) throws Exception{
		this.authenticate(request.getUsername(),request.getPassword());
		 UserDetails userDetails=this.userDetailsService.loadUserByUsername(request.getUsername());
		String generateToken=this.jwtTokenHelper.generateToken(userDetails);
		JwtAuthResponse response=new JwtAuthResponse();
		response.setToken(generateToken);
		return new ResponseEntity<JwtAuthResponse>(response,HttpStatus.OK);
	}

	private void authenticate(String username, String password) throws Exception {
		UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(username, password);
		try {
			this.authenticationManager.authenticate(authenticationToken);
		}catch (BadCredentialsException e) {
			throw new ApiException("Invalid username or Password");
		}	
	}
	
	@PostMapping("/reset/{email}/{numb}/{word}")
	public ResponseEntity<EmployeeDto>resetPassword(@PathVariable String email,@PathVariable String numb,@PathVariable String word){
		EmployeeDto emps=this.employeeService.forgetPassword(email, numb, word);
		return ResponseEntity.ok(emps);
	}
	 @PostMapping("/register")
	    public String processRegister(@RequestBody Employee employee,HttpServletRequest request) throws UnsupportedEncodingException,MessagingException{
		 employeeService.register(employee,getSiteURL(request));
		return "Register Successfull";
	}
	 private String getSiteURL(HttpServletRequest request) {
	        String siteURL = request.getRequestURL().toString();
	        return siteURL.replace(request.getServletPath(), "");
	    }
	 @GetMapping("/verify")
	 public String verifyEmployee(@Param("code") String code) {
		 if(employeeService.verify(code)) {
			 return "Verify sucessfully";
		 }else {
			 return "verify denied";
		 }
	 }

}






