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
import com.employee.task.Service.EmployeeService;
import com.employee.task.entity.Task;
import com.employee.task.exception.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/emp")
@Slf4j
public class EmployeeController {
	@Autowired
	private EmployeeService empser;

	@PostMapping("/")
	public ResponseEntity<EmployeeDto>create(@RequestBody EmployeeDto emploDto){
		EmployeeDto emp=this.empser.createEmpl(emploDto);
		return new ResponseEntity<EmployeeDto>(emp,HttpStatus.CREATED);
	}
	@PutMapping("/{id}")
	public ResponseEntity<EmployeeDto>update(@RequestBody EmployeeDto empDto,@PathVariable Integer id){
		EmployeeDto updates=this.empser.updateEmpl(empDto, id);
		return ResponseEntity.ok(updates);
	}
	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDto>getId(@PathVariable Integer id){
		EmployeeDto data=this.empser.getId(id);
		return ResponseEntity.ok(data);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<?>delete(@PathVariable Integer id){
		this.empser.deleteEmplo(id);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Successfully deleted", true),HttpStatus.OK);
	}
	@GetMapping("/")
	public ResponseEntity<List<EmployeeDto>>getAll(){
		return ResponseEntity.ok(this.empser.getAll());
	}
	@GetMapping("/task/{id}")
	public ResponseEntity<List<TaskDto>>getAlltask(@PathVariable Integer id){
		return ResponseEntity.ok(this.empser.getAllTask(id));
	}
	@PostMapping("/enroll/{uid}/{tid}")
	public ResponseEntity<EmployeeDto>enrolltask(@PathVariable Integer uid,@PathVariable Integer tid){
		log.info("I am here..............................");
		return ResponseEntity.ok(this.empser.Enrolltask(uid, tid));
	}
}




