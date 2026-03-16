# API Documentation - Course Registration Application

## 📋 Table of Contents
1. [Public Endpoints](#public-endpoints)
2. [Student Endpoints](#student-endpoints)
3. [Admin Endpoints](#admin-endpoints)
4. [OAuth2 Endpoints](#oauth2-endpoints)
5. [Response Formats](#response-formats)
6. [Error Handling](#error-handling)

---

## 🌐 Public Endpoints

### GET /home
Display all courses with pagination and optional search.

**Access**: Public (No authentication required)

**Query Parameters**:
- `page` (optional): Page number (0-indexed). Default: 0
- `search` (optional): Search keyword for course name

**Response**:
```html
<!-- Renders home.html template with:
- courses: List<Course> (5 per page)
- currentPage: int
- totalPages: int
- search: String (search query if provided)
-->
```

**Example Requests**:
```
GET /home                          # List first page
GET /home?page=1                   # List page 2
GET /home?search=java              # Search "java" courses
GET /home?page=0&search=spring     # First page of "spring" results
```

---

### GET /login
Display login form page.

**Access**: Public, but redirects authenticated users to /home

**Response**: 
```html
<!-- Renders login.html with:
- Login form
- Google OAuth2 button
- Registration link
- Error messages if login failed
-->
```

**URL**: `GET http://localhost:8080/login`

---

### POST /login
Process login request (Form-based authentication).

**Access**: Public

**Request Body** (Form Data):
```
username=admin
password=password
```

**Response**:
- Success: Redirect to `/home` with authenticated session
- Failure: Redirect to `/login?error` with error message

**Security**: 
- CSRF token included in form
- Session created on success
- Password validated with BCrypt

---

### GET /register
Display student registration form.

**Access**: Public

**Response**:
```html
<!-- Renders register.html with:
- Registration form (username, email, password)
- Validation messages
- Login link
-->
```

**URL**: `GET http://localhost:8080/register`

---

### POST /register
Create new student account.

**Access**: Public

**Request Body** (Form Data):
```
username=newstudent
email=newstudent@example.com
password=securepassword123
```

**Validation**:
- Username: Must be unique, required
- Email: Must be valid, unique, required
- Password: Required (minimum recommended 8 chars)

**Response**:
- Success: Displays success message on register.html
- Failure: Shows error message (username/email exists, validation failed)

**Auto-Assignment**: 
- STUDENT role automatically assigned
- Role fetched from database

**Example**:
```bash
POST /register
Content-Type: application/x-www-form-urlencoded

username=john_doe&email=john@example.com&password=mySecurePass123
```

---

### GET /logout
Logout current user and invalidate session.

**Access**: Authenticated users only

**Response**: Redirect to `/home` with session cleared

**Example**:
```bash
GET /logout
```

---

## 👨‍🎓 Student Endpoints

### GET /enroll/my-courses
Display all courses the student has enrolled in.

**Access**: STUDENT role required

**Authentication**: Required (Spring Security)

**Response**:
```html
<!-- Renders my-courses.html with:
- courses: List<Course> (all enrolled courses)
- Student username in navbar
-->
```

**Security**: 
- Must be logged in
- Must have STUDENT role
- Shows only current student's enrollments

**Example**:
```bash
GET /enroll/my-courses
Authorization: Session Cookie
```

---

### POST /enroll/{courseId}
Enroll student in a course.

**Access**: STUDENT role required

**Path Parameters**:
- `courseId` (required): ID of course to enroll in

**Request**:
```
POST /enroll/1
```

**Response**: 
- Success: Redirect to `/home` with success message
- Failure: Redirect to `/home` with error message

**Validation**:
- Course must exist
- Student must not already be enrolled
- duplicate enrollment check in EnrollmentService

**Enrollment Record Created**:
```json
{
  "id": 1,
  "student_id": 2,
  "course_id": 1,
  "enroll_date": "2024-03-16T14:30:00"
}
```

**Example**:
```bash
# Student1 enrolls in course with ID 5
POST /enroll/5
Authorization: Session Cookie
```

---

## 🔐 Admin Endpoints

All admin endpoints require **ADMIN role**.

### GET /admin/courses
List all courses in admin panel table view.

**Access**: ADMIN role required

**Response**:
```html
<!-- Renders admin/courses.html with:
- courses: List<Course> (all courses, no pagination)
- Edit button per course
- Delete button per course
- Create new course button
-->
```

**Table Columns**: ID, Name, Category, Lecturer, Credits, Actions

**Example**:
```bash
GET /admin/courses
Authorization: Session Cookie (ADMIN user)
```

---

### GET /admin/course/new
Display form to create new course.

**Access**: ADMIN role required

**Response**:
```html
<!-- Renders admin/course-form.html with:
- Empty course form
- Category dropdown (populated from DB)
- Create Course button
-->
```

**Example**:
```bash
GET /admin/course/new
```

---

### GET /admin/course/{id}/edit
Display form to edit existing course.

**Access**: ADMIN role required

**Path Parameters**:
- `id` (required): Course ID to edit

**Response**:
```html
<!-- Renders admin/course-form.html with:
- Populated course form
- Pre-selected category
- Update Course button
-->
```

**Example**:
```bash
GET /admin/course/3/edit
```

---

### POST /admin/course/save
Create new course or update existing course.

**Access**: ADMIN role required

**Request Body** (Form Data):
```
id=                           # Empty for new, populated for edit
name=Web Development Basics
categoryId=1
lecturer=John Smith
credits=3
image=https://example.com/image.jpg
```

**Form Fields**:
| Field | Type | Required | Notes |
|-------|------|----------|-------|
| id | Long | No | Empty for create, set for update |
| name | String | Yes | Course name, max 200 chars |
| categoryId | Long | Yes | Must exist in category table |
| lecturer | String | Yes | Lecturer name, max 100 chars |
| credits | Integer | Yes | Must be > 0 |
| image | String | No | Full URL to image, stored as LONGTEXT |

**Response**:
- Success: Redirect to `/admin/courses`
- Failure: Redirect to form with validation messages

**Validation**:
- Name: Required, not blank
- Credits: Required, must be positive integer
- Category: Required, must exist

**Example**:
```bash
POST /admin/course/save
Content-Type: application/x-www-form-urlencoded

id=0&name=Spring+Boot+Advanced&categoryId=1&lecturer=Jane+Doe&credits=4&image=https://example.com/spring.jpg
```

---

### POST /admin/course/{id}/delete
Delete course by ID.

**Access**: ADMIN role required

**Path Parameters**:
- `id` (required): Course ID to delete

**Request**:
```
POST /admin/course/5/delete
```

**Cascade**: 
- Deletes course
- All enrollments for this course deleted (cascade)
- Course removed from all students' "My Courses"

**Response**: Redirect to `/admin/courses`

**Confirmation**: Browser shows JavaScript confirm dialog

**Example**:
```bash
POST /admin/course/7/delete
Authorization: Session Cookie (ADMIN user)
```

---

## 🔑 OAuth2 Endpoints

### GET /oauth2/authorization/google
Initiates Google OAuth2 login flow.

**Access**: Public

**Requires**: Google Client ID/Secret configured in application.properties

**Flow**:
1. User clicks "Login with Google" button
2. Redirected to Google login
3. After auth, redirected to callback URL

**URL**: 
```html
<a href="/oauth2/authorization/google">Login with Google</a>
```

---

### GET /login/oauth2/code/google
Google OAuth2 callback endpoint.

**Access**: Internal Spring Security endpoint

**Function**: 
- Receives authorization code from Google
- Exchanges for access token
- Creates user if not exists
- Establish session
- Redirect to default success URL

**Configuration** (application.properties):
```properties
spring.security.oauth2.client.registration.google.client-id=YOUR_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_SECRET
```

**Redirect URI** (must be registered in Google Console):
```
http://localhost:8080/login/oauth2/code/google
```

---

## 📤 Response Formats

### HTML Templates (Most Endpoints)
All endpoints render Thymeleaf HTML templates:

- `/home` → `home.html`
- `/login` → `login.html`
- `/register` → `register.html`
- `/enroll/my-courses` → `my-courses.html`
- `/admin/courses` → `admin/courses.html`
- `/admin/course/new` → `admin/course-form.html`
- `/admin/course/{id}/edit` → `admin/course-form.html`

### Messages in Templates

**Success Messages**:
```html
<div class="alert alert-success">
    Successfully enrolled in course!
</div>
```

**Error Messages**:
```html
<div class="alert alert-danger">
    Username already exists
</div>
```

---

## 🚨 Error Handling

### HTTP Status Codes

| Code | Scenario |
|------|----------|
| 200 | Success - page rendered |
| 302 | Redirect - after form submission |
| 403 | Forbidden - insufficient role |
| 404 | Not found - endpoint doesn't exist |
| 500 | Server error |

### Common Errors

#### 1. "Access Denied" (403)
**Cause**: Insufficient permissions

**Solution**:
- Check user role in session
- Login with appropriate user
- Verify SecurityConfig authorization rules

#### 2. "Username Already Exists"
**Cause**: Trying to register with duplicate username

**Solution**: Use unique username

#### 3. "Email Already Exists"
**Cause**: Email already used in registration

**Solution**: Use different email

#### 4. "Student Already Enrolled"
**Cause**: Trying to enroll in course twice

**Solution**: Student already enrolled, visit "My Courses" to see

#### 5. "Connection Refused"
**Cause**: MySQL not running

**Solution**: 
```bash
# Start MySQL
mysql.server start  # Mac
service mysql start # Linux
# Windows: Start MySQL from Services
```

---

## 🔄 Request/Response Examples

### Example 1: Student Registration

**Request**:
```http
POST /register HTTP/1.1
Host: localhost:8080
Content-Type: application/x-www-form-urlencoded

username=jane_doe&email=jane@example.com&password=SecurePass123
```

**Response** (Success):
```http
HTTP/1.1 200 OK
Content-Type: text/html

<!-- register.html with success message -->
```

---

### Example 2: Student Login

**Request**:
```http
POST /login HTTP/1.1
Host: localhost:8080
Content-Type: application/x-www-form-urlencoded

username=student1&password=password
```

**Response** (Success):
```http
HTTP/1.1 302 Found
Location: /home
Set-Cookie: JSESSIONID=ABC123...
```

---

### Example 3: View Home Page with Search

**Request**:
```http
GET /home?page=0&search=spring HTTP/1.1
Host: localhost:8080
Cookie: JSESSIONID=ABC123...
```

**Response**:
```http
HTTP/1.1 200 OK
Content-Type: text/html

<!-- home.html with filtered courses -->
<!-- Courses matching "spring" in name -->
```

---

### Example 4: Enroll in Course

**Request**:
```http
POST /enroll/2 HTTP/1.1
Host: localhost:8080
Cookie: JSESSIONID=ABC123...
```

**Response** (Success):
```http
HTTP/1.1 302 Found
Location: /home?success=enrolled
```

---

### Example 5: Create Course (Admin)

**Request**:
```http
POST /admin/course/save HTTP/1.1
Host: localhost:8080
Content-Type: application/x-www-form-urlencoded
Cookie: JSESSIONID=ABC123...

id=&name=Docker+Mastery&categoryId=4&lecturer=Mike+Brown&credits=4&image=https://example.com/docker.jpg
```

**Response** (Success):
```http
HTTP/1.1 302 Found
Location: /admin/courses
```

---

## 🔐 Security Notes

### CSRF Protection
- All POST requests require CSRF token
- Token included automatically in Thymeleaf forms
- Disable with `http.csrf(csrf -> csrf.disable())` in SecurityConfig (done in this project)

### Password Security
- Passwords stored as BCrypt hashed values
- Sample password hash:
  ```
  Plain: password
  Hashed: $2a$10$slYQmyNdGzin7olVN3p5aOSvJeIW2/Y6.u5XfLqkCfqfaQfFTTT7C
  ```

### Session Management
- Default timeout: 30 minutes
- Session cookie: JSESSIONID
- Secure flag on production

### Role-Based Access
```
GET  /                     → Public
GET  /home                 → Public
POST /login                → Public
POST /register             → Public
GET  /login/oauth2/...     → Public

GET  /enroll/my-courses    → STUDENT role
POST /enroll/{id}          → STUDENT role

GET  /admin/**             → ADMIN role
```

---

## 📚 Additional Resources

- [Spring Security Documentation](https://docs.spring.io/spring-security/)
- [REST API Best Practices](https://restfulapi.net/)
- [HTTP Status Codes](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status)
- [OAuth2 Guide](https://oauth.net/2/)

---

**API Documentation Last Updated**: March 16, 2024
