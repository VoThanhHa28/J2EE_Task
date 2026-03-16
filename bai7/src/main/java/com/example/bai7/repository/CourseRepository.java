package com.example.bai7.repository;

import com.example.bai7.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findAll(Pageable pageable);
    List<Course> findByNameContainingIgnoreCase(String keyword);
    Page<Course> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
