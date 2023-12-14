package com.employee.task.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.employee.task.entity.Employee;
import com.employee.task.entity.Task;

public interface TaskRepo extends JpaRepository<Task, Integer>{

	@Query("SELECT e FROM Employee e JOIN e.tasks t WHERE t = :task")
	List<Employee>findByTask(Task task);
}
