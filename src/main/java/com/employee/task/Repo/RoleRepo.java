package com.employee.task.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.employee.task.entity.Role;

public interface RoleRepo extends JpaRepository<Role, Integer>{

}
