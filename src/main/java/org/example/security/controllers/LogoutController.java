package org.example.security.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/auth")
public class LogoutController {

    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "logout";
    }
}