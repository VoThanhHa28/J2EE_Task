package com.example.bai7.repository;

import com.example.bai7.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentStudentId(Long studentId);
    Optional<Enrollment> findByStudentStudentIdAndCourseId(Long studentId, Long courseId);
}
