package org.example.security.controllers;


import org.example.security.model.Employee;
import org.example.security.model.Manager;
import org.example.security.repository.EmployeeRepository;
import org.example.security.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin/manager")
public class ManagerController {

    private ManagerRepository managerRepository;
    private EmployeeRepository employeeRepository;

    @Autowired
    public ManagerController(ManagerRepository managerRepository, EmployeeRepository employeeRepository) {
        this.managerRepository = managerRepository;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/list")
    public String getAllManager(Model model) {
        model.addAttribute("managers", managerRepository.getAllManager());
        return "manager-list";
    }

    @GetMapping("/add")
    public String addManagerPage(Model model) {
        model.addAttribute("manager", new Manager());

        List<Employee> availableEmployees = employeeRepository.getAllEmployees();
        model.addAttribute("availableEmployees", availableEmployees);
        return "manager-page";
    }

    @PostMapping("/add")
    public String addManager(@ModelAttribute Manager manager, @RequestParam("employeeIds") List<Integer> employeeIds) {
        List<Employee> employees = employeeRepository.getAllEmployeeIds(employeeIds);
        manager.setEmployees(employees);
        managerRepository.createManager(manager);
        return "redirect:/manager/list";
    }

    @GetMapping("/update/{id}")
    public String updateManagerPage(@PathVariable("id") int id, Model model) {
        Manager manager = managerRepository.getManagerById(id);
        model.addAttribute("manager", manager);

        List<Employee> availableEmployees = employeeRepository.getAllEmployees();
        model.addAttribute("availableEmployees", availableEmployees);
        return "manager-update";
    }

    @PostMapping("/update/{id}")
    public String updateManager(@ModelAttribute("manager") Manager manager, @PathVariable("id") int id,
                                @RequestParam("employeeIds") List<Integer> employeeIds) {
        List<Employee> employees = employeeRepository.getAllEmployeeIds(employeeIds);
        manager.setEmployees(employees);
        managerRepository.updateManager(id, manager);
        return "redirect:/manager/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteManager(@PathVariable("id") int id) {
        managerRepository.deleteManager(id);
        return "redirect:/manager/list";
    }

    @GetMapping("/employees/{id}")
    public String getEmployee(@PathVariable("id") int managerId, Model model) {
        Manager manager = managerRepository.getManagerById(managerId);
        List<Employee> employees = managerRepository.getManagersEmployees(manager);
        model.addAttribute("manager", manager);
        model.addAttribute("employees", employees);
        return "manager-employee-list";
    }

}