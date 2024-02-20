package org.example.security.model;


import org.example.security.model.enums.PositionAtWork;
import org.springframework.stereotype.Component;

@Component
public class Employee {
    private int id;
    private String name;
    private PositionAtWork positionAtWork;
    private int salary;

    public Employee(int id, String name, PositionAtWork positionAtWork, int salary) {
        this.id = id;
        this.name = name;
        this.positionAtWork = positionAtWork;
        this.salary = salary;
    }

    public Employee() {

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

    public PositionAtWork getPositionAtWork() {
        return positionAtWork;
    }

    public void setPositionAtWork(PositionAtWork positionAtWork) {
        this.positionAtWork = positionAtWork;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }
}