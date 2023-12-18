package com.employee.task.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.employee.task.Dto.EmployeeDto;
import com.employee.task.Dto.TaskDto;
import com.employee.task.entity.Employee;
import com.employee.task.entity.Task;

import jakarta.mail.MessagingException;

public interface EmployeeService {
	
	EmployeeDto createEmpl(EmployeeDto employDto);
	EmployeeDto updateEmpl(EmployeeDto employDto,Integer empId);
	void deleteEmplo(Integer empId);
	EmployeeDto getId(Integer empId);
	List<EmployeeDto>getAll();
	List<TaskDto>getAllTask(Integer id);
	EmployeeDto Enrolltask(Integer eid,Integer tid);
	EmployeeDto forgetPassword(String Email , String numb,String newPass);
	
	void register(Employee employee,String siteURL) throws UnsupportedEncodingException,MessagingException;
	void sendVerification(Employee employee,String siteURL)throws MessagingException, UnsupportedEncodingException;
	
	boolean verify(String code);
	
}
