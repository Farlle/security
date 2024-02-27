package org.example.security.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ManagerControllerTest {

    @Test
    void getAllManager() {
    }

    @Test
    void addManagerPage() {
    }

    @Test
    void addManager() {
    }

    @Test
    void updateManagerPage() {
    }

    @Test
    void updateManager() {
    }

    @Test
    void deleteManager() {
    }

    @Test
    void getEmployee() {
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of("ADMIN", post("/manager/add"), 302, "/employee/list"),
                Arguments.of("ADMIN", get("/manager/add"), 200, null),
                Arguments.of("USER", get("/manager/add"), 302, "/auth/access-denied"),
                Arguments.of("USER", post("/manager/add"), 302, "/auth/access-denied"),
                Arguments.of("ADMIN", post("/manager/update/1"), 302, "/employee/list"),
                Arguments.of("USER", get("/manager/update/1"), 302, "/auth/access-denied"),
                Arguments.of("ADMIN", delete("/manager/delete/1"), 302, "/employee/list"),
                Arguments.of("USER", delete("/manager/delete/1"), 302, "/auth/access-denied"),
                Arguments.of("ADMIN", get("/manager/list"), 200, null),
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