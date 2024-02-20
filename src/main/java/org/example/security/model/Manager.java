package org.example.security.model;


import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Manager {

    private int id;
    private String name;
    private String department;
    private List<Employee> employees;

    public Manager(int id, String name, String department, List<Employee> employees) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.employees = employees;
    }

    public Manager() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}