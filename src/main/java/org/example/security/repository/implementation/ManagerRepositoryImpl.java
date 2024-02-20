package org.example.security.repository.implementation;

import org.example.security.model.Employee;
import org.example.security.model.Manager;
import org.example.security.repository.ManagerRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ManagerRepositoryImpl implements ManagerRepository {

    private List<Manager> managers = new ArrayList<>();
    private int id = 0;
    private EmployeeRepositoryImpl employeeRepositoryImp = new EmployeeRepositoryImpl();

    {
        List<Employee> employees = employeeRepositoryImp.getAllEmployees();
        managers.add(new Manager(id++, "Anton", "Credit", employees));
    }

    @Override
    public Manager getManagerById(int id) {
        return managers.stream()
                .filter(manager -> manager.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Manager with id " + id + " not found"));
    }

    @Override
    public List<Manager> getAllManager() {
        return managers;
    }

    @Override
    public List<Employee> getManagersEmployees(Manager manager) {
        return manager.getEmployees();
    }

    @Override
    public void createManager(Manager manager) {
        manager.setId(id++);
        managers.add(manager);
    }

    @Override
    public void updateManager(int id, Manager updManager) {
        Optional<Manager> optionalManager = managers.stream()
                .filter(manager -> manager.getId() == id)
                .findFirst();

        if (optionalManager.isPresent()) {
            Manager existingManager = optionalManager.get();
            existingManager.setName(updManager.getName());
            existingManager.setDepartment(updManager.getDepartment());
            existingManager.setEmployees((List<Employee>) updManager.getEmployees());
        } else {
            throw new IllegalArgumentException("Employee with id " + id + " not found");
        }
    }

    @Override
    public void deleteManager(int id) {
        boolean removed = managers.removeIf(manager -> manager.getId() == id);
        if (!removed) {
            throw new IllegalArgumentException("Manager with id " + id + " not found");
        }
    }

}