package org.example.security.controllers;

import org.example.security.model.Employee;
import org.example.security.model.enums.PositionAtWork;
import org.example.security.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Test
    void getAllEmployees() {
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addEmployeePage() throws Exception {
        mockMvc.perform(get("/employee/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee-page"))
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attributeExists("positionAtWorkValues"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addEmployeeTest() throws Exception {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("Test Employee");
        employee.setSalary(5);
        employee.setPositionAtWork(PositionAtWork.CLEANER);

        mockMvc.perform(post("/employee/add")
                        .flashAttr("employee", employee))
                .andExpect(status().is3xxRedirection()) // Ожидаем перенаправление
                .andExpect(redirectedUrl("/employee/list")); // Ожидаем URL перенаправления

        verify(employeeRepository).createEmployee(employee);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateEmployeePageTest() throws Exception {
        Employee employee = new Employee();
        // Настраиваем employeeRepository для возврата ожидаемого значения
        when(employeeRepository.getEmployeeById(1)).thenReturn(employee);

        mockMvc.perform(get("/employee/update/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("employee-update"))
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attributeExists("positionAtWorkValues"));
    }

    @Test
    void updateEmployee() {
    }

    @Test
    void deleteEmployee() {
    }
}