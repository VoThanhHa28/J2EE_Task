package com.example.bai7.controller;

import com.example.bai7.entity.Course;
import com.example.bai7.entity.Student;
import com.example.bai7.service.CourseService;
import com.example.bai7.service.EnrollmentService;
import com.example.bai7.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/enroll")
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @PostMapping("/{courseId}")
    public String enrollCourse(@PathVariable Long courseId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Student> student = studentService.findByUsername(username);
        Optional<Course> course = courseService.getCourseById(courseId);

        if (student.isPresent() && course.isPresent()) {
            try {
                enrollmentService.enrollStudent(student.get(), course.get());
                model.addAttribute("success", "Successfully enrolled in course!");
            } catch (RuntimeException e) {
                model.addAttribute("error", e.getMessage());
            }
        }

        return "redirect:/home";
    }

    @GetMapping("/my-courses")
    public String myEnrollments(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Student> student = studentService.findByUsername(username);
        if (student.isPresent()) {
            List<Course> enrolledCourses = enrollmentService
                    .getStudentEnrollments(student.get().getStudentId())
                    .stream()
                    .map(enrollment -> enrollment.getCourse())
                    .collect(Collectors.toList());

            model.addAttribute("courses", enrolledCourses);
            return "my-courses";
        }

        return "redirect:/home";
    }
}
