package org.example.security.controllers;

import org.example.security.controllers.EmployeeController;
import org.example.security.model.Employee;
import org.example.security.model.enums.PositionAtWork;
import org.example.security.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository employeeRepository;

    List<Employee> employees = Arrays.asList(new Employee(1, "Oleg", PositionAtWork.CLEANER, 1000),
            new Employee(2, "Sashok", PositionAtWork.SUBMANAGER, 1000));

   /* @BeforeEach
    public void setUp() {
        Employee emp1 = new Employee(1, "Oleg", PositionAtWork.CLEANER, 1000);
        Employee emp2 = new Employee(2, "Sashok", PositionAtWork.SUBMANAGER, 1000);

        List<Employee> employees = new ArrayList<>();
        employees.add(emp1);
        employees.add(emp2);
    }*/

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    public void testGetAllEmployees() throws Exception {

        when(employeeRepository.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/employee/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee-list"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attribute("employees", employees));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAddEmployeePage() throws Exception {
        Employee newEmployee = new Employee(3, "New Employee", PositionAtWork.SUBMANAGER, 508900);

        // Отправляем POST-запрос с данными нового пользователя
        mockMvc.perform(post("/employee/add")
                        .param("name", newEmployee.getName())
                        .param("PositionAtWork", String.valueOf(newEmployee.getPositionAtWork()))
                        .param("salary", String.valueOf(newEmployee.getSalary())))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/employee/list"));
    }
}