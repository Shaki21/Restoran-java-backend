package com.example.restoranmeni.repositories;

import com.example.restoranmeni.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
