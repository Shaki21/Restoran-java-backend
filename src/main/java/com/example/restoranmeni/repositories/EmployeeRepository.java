package com.example.restoranmeni.repositories;
import java.util.Optional;

import com.example.restoranmeni.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUserUsername(String username);
}