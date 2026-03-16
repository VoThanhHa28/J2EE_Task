package com.example.bai7.config;

import com.example.bai7.entity.Role;
import com.example.bai7.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    @Autowired(required = false)
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        try {
            if (roleRepository == null) {
                logger.warn("RoleRepository is not available, skipping role initialization");
                return;
            }
            
            // Initialize default roles if they don't exist
            if (!roleRepository.findByName("ADMIN").isPresent()) {
                Role adminRole = new Role();
                adminRole.setName("ADMIN");
                roleRepository.save(adminRole);
                logger.info("ADMIN role created");
            }

            if (!roleRepository.findByName("STUDENT").isPresent()) {
                Role studentRole = new Role();
                studentRole.setName("STUDENT");
                roleRepository.save(studentRole);
                logger.info("STUDENT role created");
            }
            
            logger.info("Default roles initialized successfully");
        } catch (Exception e) {
            logger.error("Error initializing default roles: {}", e.getMessage(), e);
        }
    }
}
