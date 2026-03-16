package com.example.bai7.controller;

import com.example.bai7.entity.Course;
import com.example.bai7.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    @Autowired
    private CourseService courseService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(@RequestParam(defaultValue = "0") int page, Model model,
                       @RequestParam(required = false) String search) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Course> coursePage;

        if (search != null && !search.isEmpty()) {
            coursePage = courseService.searchCourses(search, pageable);
        } else {
            coursePage = courseService.getAllCourses(pageable);
        }

        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("search", search);

        return "home";
    }
}
