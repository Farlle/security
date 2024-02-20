package org.example.security.repository;

import org.example.security.model.Employee;
import org.example.security.model.Manager;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ManagerRepository {
    Manager getManagerById(int id);

    List<Manager> getAllManager();

    List<Employee> getManagersEmployees(Manager manager);

    void createManager(Manager manager);

    void updateManager(int id, Manager manager);

    void deleteManager(int id);
}
