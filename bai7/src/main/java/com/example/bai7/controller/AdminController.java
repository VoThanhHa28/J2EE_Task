package com.example.bai7.controller;

import com.example.bai7.entity.Course;
import com.example.bai7.entity.Category;
import com.example.bai7.service.CourseService;
import com.example.bai7.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/courses")
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses(
                org.springframework.data.domain.PageRequest.of(0, Integer.MAX_VALUE)
        ).getContent());
        return "admin/courses";
    }

    @GetMapping("/course/new")
    public String newCourseForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/course-form";
    }

    @GetMapping("/course/{id}/edit")
    public String editCourseForm(@PathVariable Long id, Model model) {
        Optional<Course> course = courseService.getCourseById(id);
        if (course.isPresent()) {
            model.addAttribute("course", course.get());
            model.addAttribute("categories", categoryRepository.findAll());
            return "admin/course-form";
        }
        return "redirect:/admin/courses";
    }

    @PostMapping("/course/save")
    public String saveCourse(@ModelAttribute Course course, @RequestParam Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            course.setCategory(category.get());
            courseService.saveCourse(course);
        }
        return "redirect:/admin/courses";
    }

    @PostMapping("/course/{id}/delete")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "redirect:/admin/courses";
    }
}
