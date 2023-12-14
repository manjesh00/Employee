package com.employee.task.Service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.employee.task.Dto.EmployeeDto;
import com.employee.task.Dto.TaskDto;
import com.employee.task.entity.Task;

public interface TaskService {

	TaskDto create(TaskDto taskDto);
	TaskDto  update(TaskDto taskDto,Integer id);
	void delete(Integer id);
	TaskDto  getId(Integer id);
	List<EmployeeDto>getEnrollEmployee(Integer id);
	List<TaskDto>getAll();
}
