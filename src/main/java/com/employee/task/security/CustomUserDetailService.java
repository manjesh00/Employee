package com.employee.task.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.employee.task.Repo.EmplRepo;
import com.employee.task.entity.Employee;
import com.employee.task.exception.ResourceNotFoundException;
@Service
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
	private EmplRepo empRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Employee empl=this.empRepo.findByEmail(username).orElseThrow(()->new ResourceNotFoundException("Employee", "Email :"+username, 0));
	    return empl;
	}

}
