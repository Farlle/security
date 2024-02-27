package org.example.security.controllers;

import org.example.security.controllers.EmployeeController;
import org.example.security.model.Employee;
import org.example.security.model.enums.PositionAtWork;
import org.example.security.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    public void testGetAllEmployees() throws Exception {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/employee/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee-list"));

        verify(employeeRepository).getAllEmployees();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testAddEmployeePage() throws Exception {
        mockMvc.perform(get("/employee/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee-page"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testAddEmployee() throws Exception {
        Employee employee = new Employee();

        mockMvc.perform(post("/employee/add")
                        .flashAttr("employee", employee))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/employee/list"));

        verify(employeeRepository).createEmployee(employee);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testUpdateEmployeePage() throws Exception {
        int id = 1;
        Employee employee = new Employee();
        when(employeeRepository.getEmployeeById(id)).thenReturn(employee);

        mockMvc.perform(get("/employee/update/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("employee-update"));

        verify(employeeRepository).getEmployeeById(id);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testUpdateEmployee() throws Exception {
        int id = 1;
        Employee employee = new Employee();

        mockMvc.perform(post("/employee/update/{id}", id)
                        .flashAttr("employee", employee))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/employee/list"));

        verify(employeeRepository).updateEmployee(id, employee);
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        int id = 1;

        mockMvc.perform(get("/employee/delete/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/employee/list"));

        verify(employeeRepository).deleteEmployee(id);
    }


    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of("ADMIN", post("/employee/add"), 302, "/employee/list"),
                Arguments.of("ADMIN", get("/employee/add"), 200, null),
                Arguments.of("USER", get("/employee/add"), 302, "/auth/access-denied"),
                Arguments.of("USER", post("/employee/add"), 302, "/auth/access-denied"),
                Arguments.of("ADMIN", post("/employee/update/1"), 302, "/employee/list"),
                Arguments.of("USER", get("/employee/update/1"), 302, "/auth/access-denied"),
                Arguments.of("ADMIN", delete("/employee/delete/1"), 302, "/employee/list"),
                Arguments.of("USER", delete("/employee/delete/1"), 302, "/auth/access-denied"),
                Arguments.of("ADMIN", get("/employee/list"), 200, null),
                Arguments.of("USER", get("/employee/list"), 200, null)

        );
    }


    @ParameterizedTest
    @MethodSource("provideTestCases")
    public void testEndpointsWithDifferentRoles(String role, MockHttpServletRequestBuilder requestBuilder,
                                                int expectedStatus, String expectedRedirectUrl) throws Exception {
        var user = userDetailsService.loadUserByUsername(role);
        mockMvc.perform(requestBuilder.with(user(user)))
                .andExpect(status().is(expectedStatus))
                .andExpect(redirectedUrl(expectedRedirectUrl));

    }


}