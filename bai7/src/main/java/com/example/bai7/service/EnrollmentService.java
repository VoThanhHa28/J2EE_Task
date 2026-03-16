package com.example.bai7.service;

import com.example.bai7.entity.Enrollment;
import com.example.bai7.entity.Student;
import com.example.bai7.entity.Course;
import com.example.bai7.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public Enrollment enrollStudent(Student student, Course course) {
        // Check if already enrolled
        Optional<Enrollment> existing = enrollmentRepository
                .findByStudentStudentIdAndCourseId(student.getStudentId(), course.getId());
        
        if (existing.isPresent()) {
            throw new RuntimeException("Student already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollDate(LocalDateTime.now());

        return enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getStudentEnrollments(Long studentId) {
        return enrollmentRepository.findByStudentStudentId(studentId);
    }

    public Optional<Enrollment> findById(Long enrollmentId) {
        return enrollmentRepository.findById(enrollmentId);
    }

    public void deleteEnrollment(Long enrollmentId) {
        enrollmentRepository.deleteById(enrollmentId);
    }

    public boolean isStudentEnrolled(Long studentId, Long courseId) {
        return enrollmentRepository.findByStudentStudentIdAndCourseId(studentId, courseId).isPresent();
    }
}
