package com.example.bai7.controller;

import com.example.bai7.entity.Student;
import com.example.bai7.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password,
                          @RequestParam String email, Model model) {
        if (studentService.usernameExists(username)) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        if (studentService.emailExists(email)) {
            model.addAttribute("error", "Email already exists");
            return "register";
        }

        try {
            studentService.registerStudent(username, password, email);
            model.addAttribute("success", "Registration successful! Please login.");
            return "register";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }
}
