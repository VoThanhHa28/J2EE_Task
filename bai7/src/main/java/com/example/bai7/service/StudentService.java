package com.example.bai7.service;

import com.example.bai7.entity.Student;
import com.example.bai7.entity.Role;
import com.example.bai7.repository.StudentRepository;
import com.example.bai7.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Student registerStudent(String username, String password, String email) {
        Student student = new Student();
        student.setUsername(username);
        student.setPassword(passwordEncoder.encode(password));
        student.setEmail(email);

        // Assign default STUDENT role
        Role studentRole = roleRepository.findByName("STUDENT").orElseThrow(
                () -> new RuntimeException("STUDENT role not found")
        );
        student.setRoles(Collections.singleton(studentRole));

        return studentRepository.save(student);
    }

    public Optional<Student> findByUsername(String username) {
        return studentRepository.findByUsername(username);
    }

    public Optional<Student> findById(Long studentId) {
        return studentRepository.findById(studentId);
    }

    public boolean usernameExists(String username) {
        return studentRepository.findByUsername(username).isPresent();
    }

    public boolean emailExists(String email) {
        return studentRepository.findByEmail(email).isPresent();
    }
}
