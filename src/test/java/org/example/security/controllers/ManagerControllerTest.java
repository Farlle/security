package org.example.security.controllers;

import org.example.security.model.Employee;
import org.example.security.model.Manager;
import org.example.security.repository.EmployeeRepository;
import org.example.security.repository.ManagerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ManagerController.class)
class ManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManagerRepository managerRepository;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testGetAllManager() throws Exception {
        List<Manager> managers = Arrays.asList(new Manager(), new Manager());
        when(managerRepository.getAllManager()).thenReturn(managers);

        mockMvc.perform(get("/manager/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("manager-list"));

        verify(managerRepository).getAllManager();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testAddManagerPage() throws Exception {
        mockMvc.perform(get("/manager/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("manager-page"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testAddManager() throws Exception {
        List<Integer> employeeIds = Arrays.asList(0, 1);
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        Manager manager = new Manager();
        manager.setEmployees(employees);
        managerRepository.createManager(manager);

        when(employeeRepository.getAllEmployeeIds(employeeIds)).thenReturn(employees);

        mockMvc.perform(MockMvcRequestBuilders.post("/manager/add")
                        .param("employeeIds", String.valueOf(employees.get(0).getId()),
                                String.valueOf(employees.get(1).getId()))
                        .flashAttr("manager", manager))

                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manager/list"));

    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testUpdateManagerPage() throws Exception {
        int id = 1;
        Manager manager = new Manager();
        when(managerRepository.getManagerById(id)).thenReturn(manager);

        mockMvc.perform(get("/manager/update/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("manager-update"));

        verify(managerRepository).getManagerById(id);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testUpdateManager() throws Exception {
        int id = 1;
        Manager manager = new Manager();
        List<Integer> employeeIds = Arrays.asList(1, 2);
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.getAllEmployeeIds(employeeIds)).thenReturn(employees);

        mockMvc.perform(post("/manager/update/{id}", id)
                        .param("employeeIds", String.valueOf(employees.get(0).getId()),
                                String.valueOf(employees.get(1).getId()))
                        .flashAttr("manager", manager))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manager/list"));

        verify(managerRepository).updateManager(id, manager);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testDeleteManager() throws Exception {
        int id = 1;

        mockMvc.perform(delete("/manager/delete/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manager/list"));

        verify(managerRepository).deleteManager(id);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testGetEmployee() throws Exception {
        Manager manager = new Manager();
        manager.setId(1);

        List<Employee> employees = new ArrayList<>();
        Employee employee1 = new Employee();
        employees.add(employee1);

        when(managerRepository.getManagerById(1)).thenReturn(manager);
        when(managerRepository.getManagersEmployees(manager)).thenReturn(employees);
        mockMvc.perform(get("/manager/employees/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("manager"))
                .andExpect(model().attributeExists("employees"));
        verify(managerRepository).getManagerById(1);
        verify(managerRepository).getManagersEmployees(manager);
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of("USER", get("/manager/add"), 302, "/auth/access-denied"),
                Arguments.of("USER", post("/manager/add"), 302, "/auth/access-denied"),
                Arguments.of("USER", get("/manager/update/1"), 302, "/auth/access-denied"),
                Arguments.of("USER", delete("/manager/delete/1"), 302, "/auth/access-denied"),
                Arguments.of("USER", get("/manager/list"), 200, null)

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