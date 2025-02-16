package com.rest.springbootkata.repository;

import com.rest.springbootkata.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaEmployeeRepository extends JpaRepository<Employee, Integer> {
    List<Employee> findByGender(String gender);
}
