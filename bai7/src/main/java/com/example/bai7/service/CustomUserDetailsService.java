package com.example.bai7.service;

import com.example.bai7.entity.Student;
import com.example.bai7.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private StudentRepository studentRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Student not found: " + username));

        // Fetch roles using native SQL to avoid Hibernate collection issues
        List<String> roleNames = studentRepository.findRolesByUsername(username);
        
        Set<GrantedAuthority> authorities = new HashSet<>();
        roleNames.forEach(roleName -> 
            authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName))
        );

        return User.builder()
                .username(student.getUsername())
                .password(student.getPassword())
                .authorities(authorities)
                .build();
    }
}
