package com.employee.task.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employee.task.Dto.EmployeeDto;
import com.employee.task.Dto.TaskDto;
import com.employee.task.Repo.TaskRepo;
import com.employee.task.Service.TaskService;
import com.employee.task.entity.Employee;
import com.employee.task.entity.Task;
import com.employee.task.exception.ResourceNotFoundException;
@Service
public class TaskImpl implements TaskService{
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private TaskRepo taskrepo;

	@Override
	public TaskDto  create(TaskDto taskDto) {
		Task tas=this.modelMapper.map(taskDto, Task.class);
		Task saves=this.taskrepo.save(tas);
		return this.modelMapper.map(saves,TaskDto.class);
	}

	@Override
	public TaskDto  update(TaskDto taskDto, Integer id) {
		Task task=this.taskrepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Task", "TaskId", id));
		if(taskDto.getName()!=null) {
			task.setName(taskDto.getName());
		}
		if(taskDto.getDescribe()!=null) {
			task.setDescribe(taskDto.getDescribe());
		}
		if(taskDto.getEmployee()==null) {
			task.setEmployee(task.getEmployee());
		}
		Task update=this.taskrepo.save(task);
		return this.modelMapper.map(update, TaskDto.class);
	}

	@Override
	public void delete(Integer id) {
		Task task=this.taskrepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Task", "TaskId", id));
		this.taskrepo.delete(task);
		
	}

	@Override
	public TaskDto  getId(Integer id) {
		Task task=this.taskrepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Task", "TaskId", id));
		return this.modelMapper.map(task, TaskDto.class);
	}

	@Override
	public List<EmployeeDto> getEnrollEmployee(Integer id) {
		Task task=this.taskrepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Task", "TaskId", id));
		List<Employee>list=task.getEmployee();
		List<EmployeeDto>lis=list.stream().map((liss)->this.modelMapper.map(liss, EmployeeDto.class)).collect(Collectors.toList());
		return lis;
	}

	@Override
	public List<TaskDto> getAll() {
		List<Task>list=this.taskrepo.findAll();
		List<TaskDto>lis=list.stream().map((liss)->this.modelMapper.map(liss, TaskDto.class)).collect(Collectors.toList());
		return lis;
	}

}
