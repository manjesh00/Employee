package com.employee.task.Dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.employee.task.entity.Employee;

import lombok.Data;

@Data
public class TaskDto {
	
	private String name;
	
	private String describe;
	
	private List<Employee>employee=new ArrayList<>();
	

}
