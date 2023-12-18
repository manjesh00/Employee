package com.employee.task.ServiceImpl;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

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
    
    @Autowired
    private JavaMailSender mailSender;
    
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

	@Override
	public void register(Employee employee, String siteURL) throws UnsupportedEncodingException,MessagingException{
		employee.setPassword(passwordEncode.encode(employee.getPassword()));
		employee.setVerificationCode(RandomString.make(64));
		employee.setStatus(false);
		Employee registerEmployee=this.empRepo.save(employee);
		sendVerification(registerEmployee, siteURL);
	}

	@Override
	public void sendVerification(Employee employee, String siteURL) throws MessagingException, UnsupportedEncodingException{
		String toAddress=employee.getEmail();
		String fromAddress="rayamajhimanjesh41@gmail.com";
		String senderName="Employee Management System";
		String subject="Please verify your email";
		String content="Dear "+employee.getFullName()+"<br>"
		                +"Please click the link below to verify your registration:<br>"
		                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
		                + "Thank you,<br>"
		                + "Employee Management.";
		
		MimeMessage message=mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
	     
	    helper.setFrom(fromAddress, senderName);
	    helper.setTo(toAddress);
	    helper.setSubject(subject);
	     
	    content = content.replace("[[name]]", employee.getUsername());
	    String verifyURL = siteURL + "/verify?code=" + employee.getVerificationCode();
	     
	    content = content.replace("[[URL]]", verifyURL);
	     
	    helper.setText(content, true);
	     
	    mailSender.send(message);
	}

	@Override
	public boolean verify(String code) {
		Employee employee =this.empRepo.findByVerificationCode(code);
		if(employee==null) {
			return false;
		}else {
			employee.setVerificationCode(null);
			employee.setStatus(true);
			this.empRepo.save(employee);
			
			return true;
		}
		
	}

}












