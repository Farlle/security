package org.example.security.controllers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.function.RequestPredicates.param;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeController employeeController;

    private MockMvc mockMvc;

    @Test
    public void testGetAllEmployees() throws Exception {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.getAllEmployees()).thenReturn(employees);

        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/employee/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("employees", employees));

        verify(employeeRepository, times(1)).getAllEmployees();
    }


}

/*

public class EmployeeControllerTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeController employeeController;

    private MockMvc mockMvc;

    @Test
    public void testGetAllEmployees() throws Exception {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.getAllEmployees()).thenReturn(employees);

        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/employee/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("employees", employees));

        verify(employeeRepository, times(1)).getAllEmployees();
    }

    @Test
    public void testAddEmployee() throws Exception {
        Employee employee = new Employee();

        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();

        mockMvc.perform(MockMvcRequestBuilders.post("/employee/add")
                .param("employee", employee.toString()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/employee/list"));

        verify(employeeRepository, times(1)).createEmployee(employee);
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        int id = 1;
        Employee employee = new Employee();

        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();

        mockMvc.perform(MockMvcRequestBuilders.post("/employee/update/{id}", id)
                .param("employee", employee.toString()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/employee/list"));

        verify(employeeRepository, times(1)).updateEmployee(id, employee);
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        int id = 1;

        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/employee/delete/{id}", id))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/employee/list"));


        verify(employeeRepository, times(1)).deleteEmployee(id);
 }

*/

