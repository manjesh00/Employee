package com.employee.task.Dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.employee.task.entity.Task;

import lombok.Data;

@Data
public class EmployeeDto {

   private String username;
	
	private String password;
	
	private String address;  
	
	private List<Task>tasks=new ArrayList<>();
}
