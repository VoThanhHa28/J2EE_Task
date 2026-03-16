# Course Registration Application - Complete Setup Guide

## 📚 Project Overview

This is a full-featured Spring Boot application for course registration with the following components:

- **Spring Boot**: Web framework
- **Spring Security**: Authentication & Authorization with OAuth2 Google Login
- **Spring Data JPA**: Database access layer
- **Thymeleaf**: Server-side template engine
- **MySQL**: Database
- **Bootstrap 5**: Responsive UI
- **Lombok**: Reducing boilerplate code

## 🏗️ Project Structure

```
bai7/
├── src/
│   ├── main/
│   │   ├── java/com/example/bai7/
│   │   │   ├── Bai7Application.java          (Main entry point)
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java       (Security configuration)
│   │   │   ├── controller/
│   │   │   │   ├── HomeController.java       (Home & course listing)
│   │   │   │   ├── AuthController.java       (Login & registration)
│   │   │   │   ├── AdminController.java      (Admin CRUD operations)
│   │   │   │   └── EnrollmentController.java (Course enrollment)
│   │   │   ├── entity/
│   │   │   │   ├── Student.java
│   │   │   │   ├── Role.java
│   │   │   │   ├── Course.java
│   │   │   │   ├── Category.java
│   │   │   │   └── Enrollment.java
│   │   │   ├── repository/
│   │   │   │   ├── StudentRepository.java
│   │   │   │   ├── RoleRepository.java
│   │   │   │   ├── CourseRepository.java
│   │   │   │   ├── CategoryRepository.java
│   │   │   │   └── EnrollmentRepository.java
│   │   │   └── service/
│   │   │       ├── StudentService.java
│   │   │       ├── CourseService.java
│   │   │       ├── EnrollmentService.java
│   │   │       └── CustomUserDetailsService.java
│   │   ├── resources/
│   │   │   ├── application.properties        (Configuration)
│   │   │   └── templates/
│   │   │       ├── home.html                 (Course listing page)
│   │   │       ├── login.html                (Login & Google OAuth)
│   │   │       ├── register.html             (Student registration)
│   │   │       ├── my-courses.html           (Enrolled courses)
│   │   │       └── admin/
│   │   │           ├── courses.html          (Admin course list)
│   │   │           └── course-form.html      (Create/Edit course)
│   └── test/...                              (Unit tests)
├── pom.xml                                   (Maven dependencies)
├── database_schema.sql                       (MySQL schema)
└── README.md
```

## 🛠️ Setup Instructions

### 1. Prerequisites

- **JDK 21** or higher
- **MySQL Server 8.0** or higher
- **Maven 3.8+**
- **Git**

### 2. Database Setup

#### Using MySQL Command Line:

```bash
# Open MySQL command line
mysql -u root -p

# Copy all SQL from database_schema.sql and execute
```

**OR create the database manually:**

```sql
CREATE DATABASE course_registration;
USE course_registration;

-- Execute SQL commands from database_schema.sql
```

**After running the script, verify:**
```sql
USE course_registration;
SHOW TABLES;
SELECT * FROM role;
SELECT * FROM student;
```

### 3. Update application.properties

Edit `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/course_registration?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=your_mysql_password  # Change this to your MySQL password
```

### 4. Google OAuth2 Setup (Optional but Recommended)

To enable "Login with Google":

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project
3. Enable OAuth 2.0 API
4. Create OAuth 2.0 Credentials (Web Application)
5. Set authorized redirect URIs: `http://localhost:8080/login/oauth2/code/google`
6. Update `application.properties`:

```properties
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
```

### 5. Run the Application

```bash
# Navigate to project directory
cd bai7

# Build the project
mvn clean package

# Run the application
mvn spring-boot:run

# OR run directly
java -jar target/bai7-0.0.1-SNAPSHOT.jar
```

The application will start at: **http://localhost:8080**

## 📖 Using the Application

### Default Login Credentials

```
Username: admin
Password: password (encoded with BCrypt)
```

**Admin credentials (role: ADMIN):**
- Username: `admin`
- Password: `password`
- Email: `admin@example.com`

**Student credentials (role: STUDENT):**
- Username: `student1`
- Password: `password`
- Email: `student1@example.com`

### Features by Role

#### Public Pages (No Login Required)
- **Home Page** (`/home`): Browse all courses with pagination (5 courses per page)
- **Search**: Search courses by name
- **Login Page** (`/login`): 
  - Username/Password login
  - Google OAuth2 login
- **Register Page** (`/register`): Create new student account

#### Student Pages (Login Required)
- **My Courses** (`/enroll/my-courses`): View enrolled courses
- **Enroll** (`/enroll/{courseId}`): Enroll in a course
- Access to all public pages

#### Admin Pages (Admin Role Required)
- **Admin Panel** (`/admin/courses`): List all courses
- **Create Course** (`/admin/course/new`): Add new course
- **Edit Course** (`/admin/course/{id}/edit`): Update course details
- **Delete Course** (`/admin/course/{id}/delete`): Remove course

## 🔐 Security Configuration

### Authorization Rules

```
/                      → Public
/home                  → Public
/login                 → Public
/register              → Public
/css/**, /js/**, ...   → Public (static resources)

/admin/**              → ADMIN only
/enroll/**             → STUDENT only (except redirect)
/courses               → Public
```

### Password Encoding

Passwords are encoded using **BCryptPasswordEncoder** with strength 10.

Example password encoding:
- Plain: `password`
- Encoded: `$2a$10$slYQmyNdGzin7olVN3p5aOSvJeIW2/Y6.u5XfLqkCfqfaQfFTTT7C`

## 📊 Database Schema

### Tables

#### 1. **role**
- `role_id` (PK)
- `name` (ADMIN, STUDENT)

#### 2. **student**
- `student_id` (PK)
- `username` (UNIQUE)
- `password` (encrypted)
- `email` (UNIQUE)

#### 3. **student_role** (Many-to-Many)
- `student_id` (FK)
- `role_id` (FK)

#### 4. **category**
- `id` (PK)
- `name`

#### 5. **course**
- `id` (PK)
- `name`
- `image` (URL, LONGTEXT)
- `credits`
- `lecturer`
- `category_id` (FK)

#### 6. **enrollment**
- `id` (PK)
- `student_id` (FK)
- `course_id` (FK)
- `enroll_date`
- UNIQUE constraint on (student_id, course_id)

## 🎨 UI Features

### Navigation Bar
- Responsive Bootstrap 5 navbar
- Dynamic links based on user role
- Logout button for authenticated users
- User display (username)

### Home Page
- Hero section with title
- Course grid (3 columns on desktop, responsive)
- Course cards with:
  - Course image
  - Name, credits, lecturer
  - Category
  - Enroll/Login button
- Search functionality
- Pagination (5 courses per page)
- Professional styling with hover effects

### Login Page
- Clean, gradient background
- Username/password form
- Google OAuth2 button
- Registration link
- Error message display

### Admin Panel
- Course management table
- Edit/Delete buttons
- Create new course form
- Form validation

## 🚀 API Endpoints

### Public Endpoints
```
GET  /home                    - List courses with pagination
GET  /login                   - Login form
POST /login                   - Process login
GET  /register                - Registration form
POST /register                - Create new student
```

### Student Endpoints
```
GET  /enroll/my-courses       - View enrolled courses
POST /enroll/{courseId}       - Enroll in course
```

### Admin Endpoints
```
GET  /admin/courses           - List all courses
GET  /admin/course/new        - New course form
GET  /admin/course/{id}/edit  - Edit course form
POST /admin/course/save       - Save course
POST /admin/course/{id}/delete - Delete course
```

### OAuth2 Endpoints
```
GET  /oauth2/authorization/google  - Initiate Google login
GET  /login/oauth2/code/google     - Google callback
```

## 🐛 Troubleshooting

### Database Connection Error
```
Error: Can't connect to MySQL server at 'localhost:3306'

Solution:
1. Ensure MySQL is running
2. Check spring.datasource.url in application.properties
3. Verify username/password
4. Check MySQL port (default 3306)
```

### Password BCrypt Encoding
```
To encode password for database insert:
Use online BCrypt encoder: https://www.bcryptencoder.org

Or use Java:
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
String encoded = encoder.encode("password");
```

### 404 Pages Not Found
- Clear browser cache
- Check templates are in `src/main/resources/templates/`
- Verify controller mappings
- Check for typos in template names

### Roles Not Working
- Verify student has role in `student_role` table
- Check Role entity fetch type (set to EAGER)
- Verify role names: `ADMIN` or `STUDENT`

## 📝 Example Workflow

### As a Student:
1. Visit `http://localhost:8080/home`
2. Click "Register" 
3. Fill registration form (username, email, password)
4. Click "Login" and enter credentials
5. Browse courses on home page
6. Click "Enroll Now" on desired course
7. Visit "My Courses" to see enrolled courses

### As an Admin:
1. Login with admin credentials
2. Click "Admin Panel"
3. Create/Edit/Delete courses
4. Set course details (name, category, lecturer, credits, image)
5. Return to home page to verify changes

## 🔧 Customization

### Change Pagination Size
Edit `HomeController.java`:
```java
Pageable pageable = PageRequest.of(page, 5); // Change 5 to desired size
```

### Add New Roles
Insert into database:
```sql
INSERT INTO role (name) VALUES ('INSTRUCTOR');
INSERT INTO role (name) VALUES ('MODERATOR');
```

### Change Currency/Credits Label
Edit templates HTML to replace "Credits" with your preferred term.

### Customize Color Scheme
Edit CSS in templates `<style>` sections or create external CSS file.

## 📦 Dependencies

```xml
<!-- Core -->
spring-boot-starter-web
spring-boot-starter-data-jpa

<!-- Security -->
spring-boot-starter-security
spring-boot-starter-oauth2-client

<!-- UI -->
spring-boot-starter-thymeleaf
thymeleaf-extras-springsecurity6

<!-- Database -->
mysql-connector-j

<!-- Utility -->
lombok

<!-- Bootstrap 5 (via CDN in templates) -->
```

## 📄 License

This project is created for educational purposes.

## 👨‍💻 Support

For issues or questions:
1. Check the troubleshooting section
2. Review the database schema
3. Check Spring Boot logs
4. Verify all configurations in application.properties

---

**Happy coding! 🎉**
