package com.employee.task.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.employee.task.Dto.EmployeeDto;
import com.employee.task.Dto.TaskDto;
import com.employee.task.Repo.EmplRepo;
import com.employee.task.Repo.TaskRepo;
import com.employee.task.Service.EmployeeService;
import com.employee.task.entity.Employee;
import com.employee.task.entity.Task;
import com.employee.task.exception.ResourceNotFoundException;

@Service
public class EmployeeImpl implements EmployeeService{
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private PasswordEncoder passwordEncode;
    @Autowired
	private EmplRepo empRepo;
    @Autowired
    private TaskRepo taskRepo;
    
	@Override
	public EmployeeDto createEmpl(EmployeeDto employDto) {
	     Employee empl=this.modelMapper.map(employDto, Employee.class);
	     empl.setPassword(this.passwordEncode.encode(employDto.getPassword()));
	     Employee saved=this.empRepo.saveAndFlush(empl);
		return this.modelMapper.map(saved, EmployeeDto.class);
	}

	@Override
	public EmployeeDto updateEmpl(EmployeeDto employDto, Integer empId) {
		Employee empl=this.empRepo.findById(empId).orElseThrow(()->new ResourceNotFoundException("Employee", "EmployeeId", empId));
		if(employDto.getEmail()!=null) {
		empl.setEmail(employDto.getEmail());
		}
		if(employDto.getAddress()!=null) {
		empl.setAddress(employDto.getAddress());
		}
		Employee updateEmpl=this.empRepo.save(empl);
		return this.modelMapper.map(updateEmpl, EmployeeDto.class);
	}

	@Override
	public void deleteEmplo(Integer empId) {
		Employee empl=this.empRepo.findById(empId).orElseThrow(()->new ResourceNotFoundException("Employee", "EmployeeId", empId));
		this.empRepo.delete(empl);
	}

	@Override
	public EmployeeDto getId(Integer empId) {
		Employee empl=this.empRepo.findById(empId).orElseThrow(()->new ResourceNotFoundException("Employee", "EmployeeId", empId));
		return this.modelMapper.map(empl, EmployeeDto.class);
	}

	@Override
	public List<EmployeeDto> getAll() {
		List<Employee>list=this.empRepo.findAll();
		List<EmployeeDto>ret=list.stream().map((lis)->this.modelMapper.map(lis, EmployeeDto.class)).collect(Collectors.toList());
		return ret;
	}

	@Override
	public List<TaskDto> getAllTask(Integer empId) {
		Employee empl=this.empRepo.findById(empId).orElseThrow(()->new ResourceNotFoundException("Employee", "EmployeeId", empId));
		List<Task>get=this.empRepo.findTask(empl);
		List<TaskDto>ret=get.stream().map((lis)->this.modelMapper.map(lis, TaskDto.class)).collect(Collectors.toList());
		return ret;
	}

	@Override
	public EmployeeDto Enrolltask(Integer empId,Integer id) {
		Task task=this.taskRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Task", "TaskId", id));
		Employee empl=this.empRepo.findById(empId).orElseThrow(()->new ResourceNotFoundException("Employee", "EmployeeId", empId));
		List<Task>tasks=empl.getTasks();
		if(tasks.contains(task)) {
			//no add in list
		}else if(task!=null) {
		tasks.add(task);
		}
		empl.setTasks(tasks);
		empl=this.empRepo.saveAndFlush(empl);
		return this.modelMapper.map(empl, EmployeeDto.class);
	}

	@Override
	public EmployeeDto forgetPassword(String Email, String numb,String newPass) {
		Employee emp=this.empRepo.findByEmail(Email).orElseThrow(()->new ResourceNotFoundException("Employee", "emailID", 0));
		if(emp.getPhone().equals(numb)) {
			emp.setPassword(this.passwordEncode.encode(newPass));	
		}
		Employee savePas=this.empRepo.saveAndFlush(emp);
		return this.modelMapper.map(savePas, EmployeeDto.class);
	}

}
