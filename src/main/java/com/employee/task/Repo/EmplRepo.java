package com.employee.task.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.employee.task.entity.Employee;
import com.employee.task.entity.Task;

public interface EmplRepo extends JpaRepository<Employee, Integer> {

	@Query("SELECT t from Task t JOIN t.employee e WHERE e=:employ")
	List<Task>findTask(Employee employ);
}
