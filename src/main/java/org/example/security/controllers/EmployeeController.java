package org.example.security.controllers;

import org.example.security.model.Employee;
import org.example.security.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/list")
    public String getAllEmployees(Model model) {
        model.addAttribute("employees", employeeRepository.getAllEmployees());
        return "employee-list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add")
    public String addEmployeePage(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("positionAtWorkValues", Arrays.asList("CLEANER", "SUBMANAGER", "PROGRAMMER"));
        return "employee-page";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public String addEmployee(@ModelAttribute("employee") Employee employee) {
        employeeRepository.createEmployee(employee);
        return "redirect:/employee/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/update/{id}")
    public String updateEmployeePage(@PathVariable("id") int id, Model model) {
        model.addAttribute("employee", employeeRepository.getEmployeeById(id));
        model.addAttribute("positionAtWorkValues", Arrays.asList("CLEANER", "SUBMANAGER", "PROGRAMMER"));
        return "employee-update";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/{id}")
    public String updateEmployee(@ModelAttribute("employee") Employee employee, @PathVariable("id") int id) {
        employeeRepository.updateEmployee(id, employee);
        return "redirect:/employee/list";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") int id) {
        employeeRepository.deleteEmployee(id);
        return "redirect:/employee/list";
    }

}