package com.example.restoranmeni.controller;

import com.example.restoranmeni.model.Employee;
import com.example.restoranmeni.model.Position;
import com.example.restoranmeni.model.User;
import com.example.restoranmeni.services.EmployeeService;
import com.example.restoranmeni.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;


@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserService userService;

    @PostMapping("/by-username/{username}")
    public ResponseEntity<?> createEmployee(@PathVariable String username, @RequestBody Employee employee) {
        try {
            // Validacija pozicije
            Position.valueOf(employee.getPosition().name());

            User user = userService.findByUsername(username);
            if (user == null) {
                return ResponseEntity.badRequest().body("User with given username does not exist");
            }

            if (user.getEmployee() != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Employee already exists for this user");
            }

            employee.setUser(user);
            user.setEmployee(employee); // Postavljanje reference na User

            Employee createdEmployee = employeeService.createEmployee(employee);
            return ResponseEntity.ok(createdEmployee);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid position value");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Employee already exists");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    // Dohvaćanje zaposlenika po usernameu
    @GetMapping("/by-username/{username}")
    public ResponseEntity<Employee> getEmployeeByUsername(@PathVariable String username) {
        Employee employee = employeeService.findByUsername(username);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employee);
    }

    // Ažuriranje zaposlenika po usernameu
    @PutMapping("/by-username/{username}")
    public ResponseEntity<Employee> updateEmployeeByUsername(@PathVariable String username, @RequestBody Employee employeeDetails) {
        try {
            Employee existingEmployee = employeeService.findByUsername(username);
            if (existingEmployee == null) {
                return ResponseEntity.notFound().build();
            }

            existingEmployee.setFirstname(employeeDetails.getFirstname());
            existingEmployee.setLastname(employeeDetails.getLastname());

            if (employeeDetails.getPosition() != null) {
                existingEmployee.setPosition(employeeDetails.getPosition());
            }

            Employee updatedEmployee = employeeService.updateEmployee(existingEmployee);
            return ResponseEntity.ok(updatedEmployee);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Ostali GET, DELETE endpointi po ID-u mogu ostati ako su potrebni
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        Employee employee = employeeService.findById(id);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        Employee employee = employeeService.findById(id);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
