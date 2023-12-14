package com.employee.task.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.task.Dto.EmployeeDto;
import com.employee.task.Dto.TaskDto;
import com.employee.task.Service.TaskService;
import com.employee.task.exception.ApiResponse;
@RestController
@RequestMapping("/task")
public class TaskController {
	@Autowired
	private TaskService testService;
	
	@PostMapping("/")
	public ResponseEntity<TaskDto>create(@RequestBody TaskDto taskDto){
		TaskDto task=this.testService.create(taskDto);
		return new ResponseEntity<TaskDto>(task,HttpStatus.CREATED);
	}
	@PutMapping("/{id}")
	public ResponseEntity<TaskDto>update(@RequestBody TaskDto taskDto,@PathVariable Integer id){
		TaskDto task=this.testService.update(taskDto, id);
		return ResponseEntity.ok(task);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<?>delete(@PathVariable Integer id){
		this.testService.delete(id);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Delete successfully", true),HttpStatus.OK);
	}
	@GetMapping("/{id}")
	public ResponseEntity<TaskDto>getId(@PathVariable Integer id){
		TaskDto task=this.testService.getId(id);
		return ResponseEntity.ok(task);
	}
	@GetMapping("/")
	public ResponseEntity<List<TaskDto>>getAll(){
		return ResponseEntity.ok(this.testService.getAll());
	}
	@GetMapping("/enroll/{id}")
	public ResponseEntity<List<EmployeeDto>>gerEnroll(@PathVariable Integer id){
		return ResponseEntity.ok(this.testService.getEnrollEmployee(id));
	}
	
	

}
