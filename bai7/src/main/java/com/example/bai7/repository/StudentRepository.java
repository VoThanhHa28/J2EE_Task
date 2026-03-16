package com.example.bai7.repository;

import com.example.bai7.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUsername(String username);
    Optional<Student> findByEmail(String email);
    
    @Query(value = "SELECT r.name FROM role r " +
           "INNER JOIN student_role sr ON r.role_id = sr.role_id " +
           "INNER JOIN student s ON sr.student_id = s.student_id " +
           "WHERE s.username = :username", nativeQuery = true)
    java.util.List<String> findRolesByUsername(@Param("username") String username);
}
