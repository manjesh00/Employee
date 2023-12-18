package com.employee.task.Repo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.employee.task.entity.Employee;
import com.employee.task.entity.Task;


public interface EmplRepo extends JpaRepository<Employee, Integer> {

	@Query("SELECT t from Task t JOIN t.employee e WHERE e=:employ")
	List<Task>findTask(Employee employ);
	
	@Query("SELECT e FROM Employee e WHERE e.email=:Eemail")
	Optional<Employee> findByEmail(String Eemail);

	@Query("SELECT e FROM Employee e WHERE e.verificationCode=:code")
    Employee findByVerificationCode(String code);
}
