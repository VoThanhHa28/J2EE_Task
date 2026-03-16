# Architecture & Design Documentation

## 🏗️ System Architecture

### Layered Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                        │
│                   (Thymeleaf Templates)                      │
│                                                               │
│  home.html | login.html | register.html | my-courses.html  │
│         admin/courses.html | admin/course-form.html         │
└─────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────┐
│                    CONTROLLER LAYER                          │
│                                                               │
│  HomeController | AuthController | AdminController         │
│              EnrollmentController                           │
└─────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────┐
│                     SERVICE LAYER                            │
│                                                               │
│  StudentService | CourseService | EnrollmentService       │
│         CustomUserDetailsService                           │
└─────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────┐
│                   REPOSITORY LAYER (Data Access)            │
│                                                               │
│  StudentRepository | RoleRepository | CourseRepository    │
│  CategoryRepository | EnrollmentRepository                │
└─────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────┐
│                      DATABASE LAYER                          │
│                        (MySQL)                               │
│                                                               │
│  student | role | student_role | course | category |      │
│                    enrollment                               │
└─────────────────────────────────────────────────────────────┘
```

---

## 📦 Package Structure

```
com.example.bai7
├── Bai7Application                 (Entry point, @SpringBootApplication)
│
├── config/
│   ├── SecurityConfig              (Spring Security configuration)
│   └── DataInitializer             (Initialize default roles on startup)
│
├── controller/
│   ├── HomeController              (Home page & search)
│   ├── AuthController              (Login & registration)
│   ├── AdminController             (CRUD operations)
│   └── EnrollmentController        (Course enrollment)
│
├── entity/
│   ├── Student                     (JPA Entity)
│   ├── Role                        (JPA Entity)
│   ├── Course                      (JPA Entity)
│   ├── Category                    (JPA Entity)
│   └── Enrollment                  (JPA Entity)
│
├── repository/
│   ├── StudentRepository           (JPA Repository)
│   ├── RoleRepository              (JPA Repository)
│   ├── CourseRepository            (JPA Repository)
│   ├── CategoryRepository          (JPA Repository)
│   └── EnrollmentRepository        (JPA Repository)
│
└── service/
    ├── StudentService              (Business logic)
    ├── CourseService               (Business logic)
    ├── EnrollmentService           (Business logic)
    └── CustomUserDetailsService    (Spring Security integration)
```

---

## 🔄 Data Flow Diagrams

### 1. User Registration Flow

```
┌─────────────────┐
│  Register Page  │
│  (register.html)│
└────────┬────────┘
         │
         │ POST /register
         ↓
┌──────────────────────────┐
│   AuthController         │
│  register() method       │
└────────┬─────────────────┘
         │
         │ Check username/email exists
         ↓
┌──────────────────────────┐
│   StudentService         │
│  registerStudent()       │
└────────┬─────────────────┘
         │
         │ Encode password with BCrypt
         │ Assign STUDENT role
         ↓
┌──────────────────────────┐
│   StudentRepository      │
│  save(student)           │
└────────┬─────────────────┘
         │
         ↓
┌──────────────────────────┐
│   MySQL Database         │
│  INSERT INTO student     │
└──────────────────────────┘
         │
         │ Success/Error
         ↓
┌──────────────────────────┐
│  register.html           │
│  Display message         │
└──────────────────────────┘
```

### 2. Course Enrollment Flow

```
┌──────────────────┐
│   Home Page      │
│  Course Cards    │
└────────┬─────────┘
         │
         │ POST /enroll/{courseId}
         ↓
┌──────────────────────────────────┐
│  EnrollmentController            │
│  enrollCourse() method           │
└────────┬─────────────────────────┘
         │
         │ Get current user (SecurityContext)
         │ Get student & course from DB
         ↓
┌──────────────────────────────────┐
│  EnrollmentService               │
│  enrollStudent(student, course)  │
└────────┬─────────────────────────┘
         │
         │ Check if already enrolled
         │ Create Enrollment record
         │ Set enrollment date = NOW()
         ↓
┌──────────────────────────────────┐
│  EnrollmentRepository            │
│  save(enrollment)                │
└────────┬─────────────────────────┘
         │
         ↓
┌──────────────────────────────────┐
│  MySQL Database                  │
│  INSERT INTO enrollment          │
└────────┬─────────────────────────┘
         │
         │ Success/Error
         ↓
┌──────────────────────────────────┐
│  Redirect to /home               │
│  Display success/error message   │
└──────────────────────────────────┘
```

### 3. Authentication Flow

```
┌──────────────────┐
│   Login Page     │
│  (login.html)    │
└────────┬─────────┘
         │
         │ Option 1: Username/Password
         │ Option 2: Google OAuth2
         │
         ├─────────────────────────────┐
         │                             │
         │ Traditional Auth            │ OAuth2 Auth
         ↓                             ↓
  ┌─────────────┐         ┌──────────────────────┐
  │ POST/login  │         │ /oauth2/authorization/google
  └────┬────────┘         └──────────┬───────────┘
       │                             │
       │                             │ Redirect to Google
       │                             ↓
       │                          ┌──────────────┐
       │                          │ Google Login │
       │                          └──────┬───────┘
       │                                 │
       │                     Authorization code
       │                                 ↓
       │                    ┌────────────────────────┐
       │                    │ /login/oauth2/code/... │
       │                    └────────┬───────────────┘
       │                             │
       ├─────────────────────────────┤
       │                             │
       ↓ CustomUserDetailsService   ↓
  ┌─────────────────────────┐
  │ Load user details       │
  │ Validate credentials    │
  └────────┬────────────────┘
           │
           │ Check roles
           ↓
  ┌─────────────────────────┐
  │ Create authentication   │
  │ Set in SecurityContext  │
  └────────┬────────────────┘
           │
           │ Create session
           ↓
  ┌─────────────────────────┐
  │ Redirect to /home       │
  │ Set JSESSIONID cookie   │
  └─────────────────────────┘
```

---

## 🔐 Security Architecture

### Authentication Flow

```
Request with credentials
        ↓
Spring Security Filter Chain
        ↓
UsernamePasswordAuthenticationFilter
        ↓
AuthenticationManager
        ↓
CustomUserDetailsService.loadUserByUsername()
        ↓
StudentRepository.findByUsername()
        ↓
BCryptPasswordEncoder.matches(raw, encoded)
        ↓
✓ Success: Create Authentication object
✗ Failure: AuthenticationException
        ↓
Create SecurityContext with Authentication
        ↓
Create HttpSession (JSESSIONID)
        ↓
Redirect to /home
```

### Authorization Flow

```
Authenticated Request to protected endpoint
        ↓
FilterSecurityInterceptor
        ↓
Check endpoint authorization rules:
- /admin/** → ADMIN role
- /enroll/** → STUDENT role
- /home → Public
        ↓
SecurityContextHolder.getContext().getAuthentication()
        ↓
Check user authorities/roles
        ↓
✓ Authorized: Process request
✗ Denied: 403 Forbidden or redirect to login
        ↓
Execute controller method
```

---

## 📊 Entity Relationships

### UML Class Diagram

```
┌──────────────┐                    ┌──────────────┐
│    Student   │                    │    Role      │
├──────────────┤                    ├──────────────┤
│ studentId (PK)─────┐              │ roleId (PK)  │
│ username     │     │              │ name         │
│ password     │     │              └──────────────┘
│ email        │     │                     ▲
│ roles (*)    │     └──────M─────M────────┤
│ enrollments  │       student_role        │
└──────────────┘        (Junction)         │
        △                                  │
        │                                  │
        │ 1──────────M                     │
        │                           Roles: ADMIN
  ┌─────┴─────────────┐                   STUDENT
  │   Enrollment      │
  ├───────────────────┤
  │ id (PK)           │
  │ student_id (FK)   │
  │ course_id (FK)    │
  │ enroll_date       │
  │ UNIQUE: student_id + course_id
  └────────┬──────────┘
           │
           │ M──────────1
           │
        ┌──┴──────────┐
        │ Course       │
        ├──────────────┤
        │ id (PK)      │
        │ name         │
        │ image        │
        │ credits      │
        │ lecturer     │
        │ category_id  │
        └──────┬─────── │
               │        │
               │M    1──┘
               │
        ┌──────┴──────┐
        │ Category     │
        ├──────────────┤
        │ id (PK)      │
        │ name         │
        └──────────────┘
```

### Relationship Summary

| Relationship | Type | Details |
|--------------|------|---------|
| Student ↔ Role | Many-to-Many | Via student_role junction table |
| Category → Course | One-to-Many | category_id FK in course |
| Student → Enrollment | One-to-Many | student_id FK in enrollment |
| Course → Enrollment | One-to-Many | course_id FK in enrollment |
| Enrollment → Student | Many-to-One | Junction point for M:M with courses |

---

## 🔄 Service Layer Design

### StudentService
```java
Purpose: Manage student-related operations

Methods:
- registerStudent()       → Create new student with STUDENT role
- findByUsername()        → Retrieve student by username
- findById()              → Retrieve student by ID
- usernameExists()        → Check if username is taken
- emailExists()           → Check if email is taken

Dependencies:
- StudentRepository       (Database access)
- RoleRepository          (Get STUDENT role)
- PasswordEncoder         (Encode password)
```

### CourseService
```java
Purpose: Manage course CRUD operations

Methods:
- getAllCourses(Pageable)         → Get paginated courses
- getCourseById()                 → Get single course
- saveCourse()                    → Create or update course
- deleteCourse()                  → Remove course (cascade)
- searchCourses()                 → Search by keyword

Dependencies:
- CourseRepository        (Database access)
```

### EnrollmentService
```java
Purpose: Manage course enrollments

Methods:
- enrollStudent()                      → Enroll student (with duplicate check)
- getStudentEnrollments()             → Get courses student enrolled
- findById()                          → Get specific enrollment
- deleteEnrollment()                  → Remove enrollment
- isStudentEnrolled()                 → Check if already enrolled

Dependencies:
- EnrollmentRepository    (Database access)

Business Logic:
- Prevent duplicate enrollments
- Set enroll_date to NOW()
- Cascade delete when course deleted
```

### CustomUserDetailsService
```java
Purpose: Spring Security integration

Methods:
- loadUserByUsername()    → Load user with authorities

Security:
- Throws UsernameNotFoundException if not found
- Builds GrantedAuthority from roles (ROLE_ADMIN, ROLE_STUDENT)
- Returns Spring UserDetails object

Dependencies:
- StudentRepository       (Fetch user)
```

---

## 🛣️ Request Routing & URL Mapping

### Controller Route Mapping

#### HomeController
```
GET  /         → redirect: /home
GET  /home     → home courses listing page (with pagination & search)
```

#### AuthController
```
GET  /login    → login page
POST /login    → Spring Security handles
GET  /register → registration page
POST /register → create new student
```

#### AdminController
```
@RequestMapping("/admin")

GET    /courses                 → list all courses
GET    /course/new              → create course form
GET    /course /{id}/edit       → edit course form
POST   /course/save             → save (create/update) course
POST   /course/{id}/delete      → delete course
```

#### EnrollmentController
```
@RequestMapping("/enroll")

GET    /my-courses              → enrolled courses list
POST   /{courseId}              → enroll in course
```

---

## 🔌 Key Technologies & Design Patterns

### Design Patterns Used

| Pattern | Where | Purpose |
|---------|-------|---------|
| **MVC** | Spring Boot | Separation of concerns (Model, View, Controller) |
| **Layered Architecture** | Package structure | Separation by layer (Controller, Service, Repository) |
| **Repository Pattern** | JpaRepository | Abstraction of data access layer |
| **Service Locator** | Dependency Injection | Loose coupling with @Autowired |
| **Singleton** | Beans | Controllers, Services, Repositories are singletons |
| **Template Method** | JpaRepository | findById(), findAll(), etc. |
| **Decorator** | Security | @EnableWebSecurity decorates configuration |

### Spring Boot Features Used

```
@SpringBootApplication     → Main entry point, auto-configuration
@Entity                    → JPA entity mapping
@Repository                → Spring Data JPA repository
@Service                   → Business logic service
@Controller                → Request handler
@GetMapping, @PostMapping  → HTTP method handlers
@Autowired                 → Dependency injection
@Configuration             → Configuration class
@EnableWebSecurity         → Enable Spring Security
@Table, @Column            → Database mapping
@JoinColumn, @ManyToOne    → Relationship mapping
```

---

## 🔗 Integration Points

### Spring Security Integration

```
SecurityConfig.java
    ↓
HttpSecurity configuration
    ├─ authorizeHttpRequests() → Define auth rules
    ├─ formLogin()             → Form-based login config
    ├─ oauth2Login()           → OAuth2 config
    └─ logout()                → Logout config
    ↓
CustomUserDetailsService
    ↓
Users retrieved with roles
    ↓
Authentication established
```

### Thymeleaf + Spring Security Integration

```
Template: home.html
    ├─ sec:authorize="hasRole('STUDENT')"  → Show for STUDENTS
    ├─ sec:authorize="hasRole('ADMIN')"    → Show for ADMINS
    ├─ sec:authorize="isAuthenticated()"   → Show if logged in
    ├─ sec:authorize="isAnonymous()"       → Show if not logged in
    └─ sec:authentication="name"           → Display username
```

### JPA Relationship Management

```
Student entity
    ├─ @ManyToMany(fetch = FetchType.EAGER)  → Load roles immediately
    └─ roles (Set<Role>)

Course entity
    ├─ @ManyToOne()            → Category relationship
    └─ @OneToMany()            → Enrollments relationship

Enrollment entity
    ├─ @ManyToOne()            → Student reference
    └─ @ManyToOne()            → Course reference
```

---

## 📈 Scalability Considerations

### Database Optimization
- ✅ Indexed columns: username, email, student_id, course_id
- ✅ Foreign key constraints
- ✅ Unique constraints prevent duplicates
- ✅ Cascade deletes for data consistency

### Performance Features
- ✅ Pagination (5 courses per page) reduces memory
- ✅ EAGER fetch for roles (prevents lazy loading issues)
- ✅ Stateless OAuth2 tokens
- ✅ SQL optimized queries via Spring Data

### Future Enhancements
- [ ] Caching with Spring Cache
- [ ] Pagination for admin course list
- [ ] Async enrollment processing
- [ ] Event-driven architecture
- [ ] Microservices split
- [ ] API rate limiting
- [ ] Audit logging

---

## 🧪 Testing Strategy

### Unit Tests (Example structure):
```
StudentServiceTests
    - testRegisterStudentSuccess()
    - testRegisterDuplicateUsername()
    - testRegisterDuplicateEmail()

CourseServiceTests
    - testGetAllCourses()
    - testSearchCurses()
    - testDeleteCourse()

EnrollmentServiceTests
    - testEnrollSuccess()
    - testPreventDuplicateEnrollment()
```

### Integration Tests:
```
AuthControllerTests
    - testLoginPage()
    - testRegisterNewStudent()

HomeControllerTests
    - testListCourses()
    - testSearchFunctionality()

AdminControllerTests
    - testAdminOnlyAccess()
    - testCreateEditDeleteCourse()
```

---

## 🔄 Data Flow Summary

### Complete User Journey

1. **Anonymous User**
   - Visits `/home` → See all courses
   - Searches courses
   - Clicks "Register" → Registration form
   - Enters details → System validates & creates student
   - Auto-assigns STUDENT role

2. **After Registration**
   - Clicks "Login" → Login form
   - Enters credentials → Spring Security authenticates
   - Session created, redirected to `/home`

3. **Student User**
   - Views courses on home page
   - Clicks "Enroll Now" → POST /enroll/{courseId}
   - EnrollmentService creates record
   - Redirects to home with success message
   - Can visit "My Courses" to see enrolled courses

4. **Admin User**
   - Clicks "Admin Panel" → /admin/courses
   - Views all courses in table
   - Click "New" → /admin/course/new → Form
   - Fill form, click "Create" → POST /admin/course/save
   - Course saved, list refreshed

---

This architecture ensures:
- ✅ **Separation of Concerns**: Each layer has specific responsibility
- ✅ **Scalability**: Easy to add new features
- ✅ **Maintainability**: Clear structure and patterns
- ✅ **Security**: Multiple layers of protection
- ✅ **Testability**: Each component can be tested independently
