package com.example.bai3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    @GetMapping("/home")
    public String home(Model model){
        model.addAttribute("message", "Xin chào thymeleaf");
        return "index";
    }


}
