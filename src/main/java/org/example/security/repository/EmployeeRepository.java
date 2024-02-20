package org.example.security.repository;

import org.example.security.model.Employee;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface EmployeeRepository {
    Employee getEmployeeById(int id);

    List<Employee> getAllEmployees();

    void createEmployee(Employee employee);

    void updateEmployee(int id, Employee employee);

    void deleteEmployee(int id);

    List<Employee> getAllEmployeeIds(List<Integer> ids);
}
