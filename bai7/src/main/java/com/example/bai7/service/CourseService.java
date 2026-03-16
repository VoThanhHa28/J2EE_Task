package com.example.bai7.service;

import com.example.bai7.entity.Course;
import com.example.bai7.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public Page<Course> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public List<Course> searchCourses(String keyword) {
        return courseRepository.findByNameContainingIgnoreCase(keyword);
    }

    public Page<Course> searchCourses(String keyword, Pageable pageable) {
        return courseRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }
}
