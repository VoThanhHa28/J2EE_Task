-- Course Registration Database Schema

-- Create Database
CREATE DATABASE IF NOT EXISTS course_registration;
USE course_registration;

-- Create Role Table
CREATE TABLE role (
    role_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Create Student Table
CREATE TABLE student (
    student_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

-- Create Student-Role Junction Table (Many-to-Many)
CREATE TABLE student_role (
    student_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (student_id, role_id),
    FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role(role_id) ON DELETE CASCADE
);

-- Create Category Table
CREATE TABLE category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
);

-- Create Course Table
CREATE TABLE course (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    image LONGTEXT,
    credits INT NOT NULL,
    lecturer VARCHAR(100) NOT NULL,
    category_id BIGINT NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE
);

-- Create Enrollment Table
CREATE TABLE enrollment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    enroll_date DATETIME NOT NULL,
    UNIQUE KEY unique_enrollment (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE
);

-- Insert Sample Data

-- Insert Roles
INSERT INTO role (name) VALUES ('ADMIN');
INSERT INTO role (name) VALUES ('STUDENT');

-- Insert Sample Categories
INSERT INTO category (name) VALUES ('Web Development');
INSERT INTO category (name) VALUES ('Mobile Development');
INSERT INTO category (name) VALUES ('Data Science');
INSERT INTO category (name) VALUES ('Cloud Computing');

-- Insert Sample Students
INSERT INTO student (username, password, email) VALUES 
('admin', '$2a$10$slYQmyNdGzin7olVN3p5aOSvJeIW2/Y6.u5XfLqkCfqfaQfFTTT7C', 'admin@example.com'),
('student1', '$2a$10$slYQmyNdGzin7olVN3p5aOSvJeIW2/Y6.u5XfLqkCfqfaQfFTTT7C', 'student1@example.com'),
('student2', '$2a$10$slYQmyNdGzin7olVN3p5aOSvJeIW2/Y6.u5XfLqkCfqfaQfFTTT7C', 'student2@example.com');

-- Assign Roles (admin -> ADMIN, student1 & student2 -> STUDENT)
INSERT INTO student_role (student_id, role_id) VALUES (1, 1);  -- admin has ADMIN role
INSERT INTO student_role (student_id, role_id) VALUES (2, 2);  -- student1 has STUDENT role
INSERT INTO student_role (student_id, role_id) VALUES (3, 2);  -- student2 has STUDENT role

-- Insert Sample Courses
INSERT INTO course (name, credits, lecturer, category_id, image) VALUES 
('Java Spring Boot Mastery', 4, 'John Doe', 1, 'https://via.placeholder.com/300x200?text=Spring+Boot'),
('React & Next.js', 4, 'Jane Smith', 1, 'https://via.placeholder.com/300x200?text=React'),
('Flutter Mobile Development', 3, 'Mike Johnson', 2, 'https://via.placeholder.com/300x200?text=Flutter'),
('AWS Cloud Solutions', 4, 'Sarah Williams', 4, 'https://via.placeholder.com/300x200?text=AWS'),
('Machine Learning Basics', 4, 'Dr. Robert Brown', 3, 'https://via.placeholder.com/300x200?text=ML'),
('Python for Data Analysis', 3, 'Emily White', 3, 'https://via.placeholder.com/300x200?text=Python'),
('Vue.js Advanced', 3, 'Chris Lee', 1, 'https://via.placeholder.com/300x200?text=Vue');

-- Sample Enrollment (student1 enrolled in some courses)
INSERT INTO enrollment (student_id, course_id, enroll_date) VALUES 
(2, 1, NOW()),
(2, 2, NOW()),
(2, 5, NOW());

-- Create Indexes for Better Performance
CREATE INDEX idx_student_username ON student(username);
CREATE INDEX idx_student_email ON student(email);
CREATE INDEX idx_course_category ON course(category_id);
CREATE INDEX idx_enrollment_student ON enrollment(student_id);
CREATE INDEX idx_enrollment_course ON enrollment(course_id);

-- Display Tables
SHOW TABLES;
DESC role;
DESC student;
DESC student_role;
DESC category;
DESC course;
DESC enrollment;
