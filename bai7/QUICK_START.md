# Course Registration Application - Quick Start Guide

## ⚡ 5-Minute Quick Start

### Step 1: Database Setup (2 minutes)

1. Open MySQL command line or MySQL Workbench:
```bash
mysql -u root -p
```

2. Run the database schema (copy all from `database_schema.sql` and paste):
```sql
-- Copy entire database_schema.sql content and execute
```

3. Verify database is created:
```sql
USE course_registration;
SHOW TABLES;
SELECT COUNT(*) FROM course;  -- Should show 7 sample courses
SELECT * FROM student;         -- Should show 3 sample students
```

### Step 2: Configure Application (1 minute)

Edit `src/main/resources/application.properties`:

```properties
# Change only this line with your MySQL password:
spring.datasource.password=your_mysql_password  # <-- CHANGE THIS
```

**Optional - Enable Google Login:**
```properties
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
```

### Step 3: Run Application (2 minutes)

```bash
# Navigate to project directory
cd path/to/bai7

# Run with Maven
mvn spring-boot:run

# Wait for message: "Started Bai7Application in X seconds"
```

### Step 4: Access Application

Open browser and visit: **http://localhost:8080/home**

---

## 🔑 Login Credentials

**Admin Account (Full Access):**
```
Username: admin
Password: password
Role: ADMIN
```

**Student Accounts (Limited Access):**
```
Username: student1
Password: password
Role: STUDENT

Username: student2  
Password: password
Role: STUDENT
```

---

## 🧭 Quick Navigation

### For Students:
1. **Home** (`http://localhost:8080/home`) - Browse courses
2. **Search** - Use search box to find courses
3. **Enroll** - Click "Enroll Now" on any course
4. **My Courses** - View all enrolled courses
5. **Logout** - Click logout button

### For Admins:
1. **Home** - Same as students
2. **Admin Panel** - Click "Admin Panel" in navbar
3. **Create Course** - Click "+ Create New Course"
4. **Edit Course** - Click "Edit" on course row
5. **Delete Course** - Click "Delete" on course row

---

## 🛠️ Common Tasks

### Register a New Student
```
1. Click "Register" button in navbar
2. Fill in: username, email, password
3. Click "Register Now"
4. Login with new credentials
5. Student role is auto-assigned
```

### Create a New Course (Admin Only)
```
1. Login as admin
2. Click "Admin Panel"
3. Click "+ Create New Course"
4. Fill in:
   - Course Name (required)
   - Category (required) - dropdown
   - Lecturer (required)
   - Credits (required) - number
   - Image URL (optional) - full URL to image
5. Click "Create Course"
6. New course appears on home page
```

### Search for Courses
```
1. On home page, use search box at top
2. Type course name or keyword
3. Press Enter or click Search
4. Results shown with pagination
5. Click "Clear" to reset search
```

### Enroll in Course
```
1. Login as student
2. Find course on home page
3. Click "Enroll Now" button
4. Course added to "My Courses"
5. Cannot enroll twice in same course (duplicate prevention)
```

### View My Courses
```
1. Login as student
2. Click "My Courses" in navbar
3. See all enrolled courses
4. Click course name or return to home
```

---

## 📊 Sample Data

### Roles
- ADMIN
- STUDENT

### Sample Students
- admin / password (ADMIN role)
- student1 / password (STUDENT role)
- student2 / password (STUDENT role)

### Sample Categories
- Web Development
- Mobile Development
- Data Science
- Cloud Computing

### Sample Courses (7 courses in 4 categories)
1. Java Spring Boot Mastery (Web Dev, 4 credits) - John Doe
2. React & Next.js (Web Dev, 4 credits) - Jane Smith
3. Flutter Mobile Development (Mobile, 3 credits) - Mike Johnson
4. AWS Cloud Solutions (Cloud, 4 credits) - Sarah Williams
5. Machine Learning Basics (Data Science, 4 credits) - Dr. Robert Brown
6. Python for Data Analysis (Data Science, 3 credits) - Emily White
7. Vue.js Advanced (Web Dev, 3 credits) - Chris Lee

---

## 🔒 Security Features

✅ Password encrypted with BCrypt
✅ Role-based access control (RBAC)
✅ OAuth2 Google Login support
✅ CSRF protection enabled
✅ Secure session management
✅ Login required pages protected
✅ Admin-only endpoints protected
✅ Student-only enrollment

---

## 📱 Responsive Design

- ✅ Works on Desktop (1920px+)
- ✅ Works on Tablets (768px+)
- ✅ Works on Mobile (320px+)
- ✅ Bootstrap 5 responsive grid
- ✅ Mobile-friendly navbar toggle
- ✅ Touch-friendly buttons

---

## 🔍 Features Overview

### Home Page
- [x] Display all courses (paginated, 5 per page)
- [x] Course card with image, name, credits, lecturer
- [x] Search functionality
- [x] Responsive grid layout
- [x] Enroll/Login buttons based on auth
- [x] Category display

### Login
- [x] Username/password form
- [x] Google OAuth2 login button
- [x] Remember username feature
- [x] Error message display
- [x] Password reset link (optional)

### Registration
- [x] Username, email, password form
- [x] Duplicate username/email prevention
- [x] Password BCrypt encoding
- [x] Auto-assign STUDENT role
- [x] Validation messages

### Student Features
- [x] Enroll in courses (prevent duplicates)
- [x] View my enrolled courses
- [x] Search courses
- [x] Pagination support
- [x] Logout functionality

### Admin Features
- [x] Create courses (with form validation)
- [x] Edit course details
- [x] Delete courses
- [x] View all courses in table
- [x] Category selection dropdown
- [x] Image URL support

---

## 🚀 Performance Tips

### Database Optimization
- Indexes on: username, email, student_id, course_id
- Foreign key constraints for data integrity
- Unique constraints to prevent duplicates

### Pagination
- Courses paginated at 5 per page
- Reduces memory usage
- Faster page load times
- Better UX on mobile

### Caching (Optional)
To enable caching, add to `application.properties`:
```properties
spring.cache.type=simple
```

---

## 🐛 Troubleshooting Quick Solutions

### Problem: "Connection refused to localhost:3306"
**Solution**: 
```bash
# Make sure MySQL is running
# Windows: Start MySQL Service
# Mac: brew services start mysql
# Linux: sudo service mysql start
```

### Problem: "Access denied for user 'root'@localhost"
**Solution**: 
```
Check password in application.properties matches your MySQL password
```

### Problem: "Table doesn't exist"
**Solution**:
```bash
# Re-run database_schema.sql
# Verify: mysql> USE course_registration; SHOW TABLES;
```

### Problem: "Port 8080 already in use"
**Solution**:
```properties
# Edit application.properties:
server.port=8081  # Use different port
```

### Problem: "Tables empty"
**Solution**:
```bash
# Re-run INSERT statements from database_schema.sql
# Verify: SELECT COUNT(*) FROM course;
```

---

## 📞 Frequently Asked Questions

**Q: Can I change the pagination size?**
A: Yes, edit HomeController.java line with `PageRequest.of(page, 5)`, change 5 to desired size.

**Q: How do I reset the database?**
A: Drop the database and re-run database_schema.sql:
```sql
DROP DATABASE course_registration;
-- Then re-run database_schema.sql
```

**Q: Can I add more roles?**
A: Yes, insert into role table and update SecurityConfig.java if needed.

**Q: How do I disable Google Login?**
A: Remove Google client ID/secret from application.properties or comment out oauth2Login in SecurityConfig.

**Q: Can students see admin panel?**
A: No, SecurityConfig restricts /admin/** to ADMIN role only.

**Q: What happens if student enrolls twice?**
A: Duplicate enrollment is prevented by UNIQUE constraint and EnrollmentService check.

**Q: How long is session timeout?**
A: Default 30 minutes. Edit SecurityConfig.java to change.

**Q: Can I use PostgreSQL instead of MySQL?**
A: Yes, change spring.datasource.url and hibernate.dialect in application.properties.

---

## 🎓 Learning Resources

- Spring Boot Documentation: https://spring.io/projects/spring-boot
- Spring Security: https://spring.io/projects/spring-security
- Spring Data JPA: https://spring.io/projects/spring-data-jpa
- Thymeleaf: https://www.thymeleaf.org/
- Bootstrap 5: https://getbootstrap.com/docs/5.1/
- BCrypt Info: https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html

---

## ✨ Project Features Summary

| Feature | Status | Details |
|---------|--------|---------|
| Course Listing | ✅ | With pagination, search, responsive |
| Student Registration | ✅ | BCrypt password, auto STUDENT role |
| Login/Logout | ✅ | Secure session, custom login page |
| Google OAuth2 | ✅ | Optional, needs client ID/secret |
| Admin CRUD | ✅ | Create, read, update, delete courses |
| Enrollment | ✅ | Prevent duplicates, track dates |
| My Courses | ✅ | View enrolled courses |
| Search | ✅ | By course name, case-insensitive |
| Authorization | ✅ | ADMIN and STUDENT roles |
| Responsive UI | ✅ | Mobile, tablet, desktop |
| Bootstrap 5 | ✅ | Modern, clean design |

---

## 🎉 You're All Set!

Your Course Registration Application is ready to use!

**Start browsing courses**: http://localhost:8080/home

**Questions?** Check SETUP_GUIDE.md for detailed information.

Happy learning! 📚
