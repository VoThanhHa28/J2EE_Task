# Troubleshooting Guide - Course Registration Application

## 🔍 Common Issues & Solutions

### 1. Database Connection Issues

#### Error: "com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure"

**Cause**: MySQL server is not running or connection string is incorrect

**Solutions**:

**Windows**:
```bash
# Check if MySQL is running
tasklist | findstr mysql

# Start MySQL Service
net start MySQL80
# OR use MySQL Command Line Client from Start Menu
```

**macOS**:
```bash
# Start MySQL
brew services start mysql

# or start manually
mysql.server start
```

**Linux**:
```bash
# Start MySQL service
sudo systemctl start mysql
# OR
sudo service mysql start
```

**Verify Connection**:
```bash
mysql -u root -p
mysql> SELECT 1;  # Should return 1
```

---

#### Error: "Access denied for user 'root'@'localhost'"

**Cause**: Password in application.properties doesn't match MySQL password

**Solution**:
1. Verify MySQL password:
   ```bash
   mysql -u root -p
   ```
   Enter the password you use to login to MySQL

2. Update `application.properties`:
   ```properties
   spring.datasource.password=YOUR_EXACT_MYSQL_PASSWORD
   ```

3. If password has special characters, escape them:
   ```properties
   # Password: p@ss#word123
   spring.datasource.password=p@ss#word123
   ```

---

#### Error: "Unknown database 'course_registration'"

**Cause**: Database hasn't been created yet

**Solution**:
```bash
# Run the SQL schema file
mysql -u root -p < database_schema.sql

# Or manually in MySQL:
mysql -u root -p
mysql> source /path/to/database_schema.sql;
mysql> USE course_registration;
mysql> SHOW TABLES;
```

**Verify**:
```sql
USE course_registration;
SELECT COUNT(*) FROM course;  -- Should show 7
SELECT * FROM student LIMIT 3;  -- Should show sample students
```

---

### 2. Application Startup Issues

#### Error: "Address already in use: bind"

**Cause**: Port 8080 is already in use

**Solutions**:

**Option 1**: Use different port
```properties
# In application.properties
server.port=8081
```

**Option 2**: Kill process using port 8080
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# macOS/Linux
lsof -i :8080
kill -9 <PID>
```

---

#### Error: "The driver could not be loaded: org.h2.Driver"

**Cause**: Dependency version mismatch or missing MySQL driver

**Solution**:
```xml
<!-- Verify pom.xml has correct dependency -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Clean and rebuild -->
mvn clean
mvn install
mvn spring-boot:run
```

---

#### Error: "No bean of type 'com.example.bai7.service.StudentService' found"

**Cause**: Service class not recognized as Spring bean

**Solution**:
1. Verify `@Service` annotation on service class:
   ```java
   @Service
   public class StudentService {
       // ...
   }
   ```

2. Verify package structure (services must be in com.example.bai7.service)

3. Clear Spring cache:
   ```bash
   mvn clean
   mvn spring-boot:run
   ```

---

### 3. Authentication & Security Issues

#### Error: "Invalid CSRF token found in the request"

**Cause**: CSRF protection enabled but token missing

**Solution**: 
The project disables CSRF for demo purposes. If you want to enable it:
```java
// In SecurityConfig.java, remove or modify:
.csrf(csrf -> csrf.disable());

// Add CSRF token to forms:
<form method="post">
    <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>
    <!-- form fields -->
</form>
```

---

#### Error: "Access is denied (403)" on admin pages

**Cause**: User doesn't have ADMIN role

**Solutions**:

1. Login as admin user:
   ```
   Username: admin
   Password: password
   ```

2. Verify user has ADMIN role in database:
   ```sql
   SELECT s.username, r.name
   FROM student s
   JOIN student_role sr ON s.student_id = sr.student_id
   JOIN role r ON sr.role_id = r.role_id
   WHERE s.username = 'yourusername';
   ```

3. If missing, assign role:
   ```sql
   INSERT INTO student_role (student_id, role_id)
   SELECT student_id, role_id
   FROM student, role
   WHERE student.username = 'yourusername' AND role.name = 'ADMIN';
   ```

---

#### Error: "Cannot convert value of type 'java.lang.Long' to required type 'java.lang.Long'" on enrollment

**Cause**: Course not found or ID conversion issue

**Solution**:
1. Verify course exists:
   ```sql
   SELECT id FROM course WHERE id = 1;
   ```

2. Check controller receives valid ID:
   ```java
   @PostMapping("/{courseId}")
   public String enrollCourse(@PathVariable Long courseId) {
       // Verify courseId is not null and > 0
   }
   ```

---

### 4. Page & Template Issues

#### Error: "Whitelabel Error Page" (404 Not Found)

**Cause**: Template file not found or endpoint doesn't exist

**Solutions**:

**Check template file exists**:
```
src/main/resources/templates/
├── home.html           ✓
├── login.html          ✓
├── register.html       ✓
├── my-courses.html     ✓
└── admin/
    ├── courses.html    ✓
    └── course-form.html ✓
```

**Check controller endpoint**:
```java
@GetMapping("/home")  // Check mapping matches requested URL
public String home() {
    return "home";    // Check return matches filename
}
```

**Verify template name matches return value**:
```
Controller: return "home";
Template:  home.html  ✓

Controller: return "admin/courses";
Template:  admin/courses.html  ✓
```

---

#### Error: "Template not found" with Thymeleaf

**Cause**: Template file missing or incorrect path

**Solutions**:

1. Verify file location:
   ```
   MUST be in: src/main/resources/templates/
   NOT in: src/main/java/
   NOT in: src/test/resources/
   ```

2. Verify file extension is .html

3. Check return statement matches filename:
   ```java
   return "home";         // Returns home.html
   return "admin/courses"; // Returns admin/courses.html
   ```

4. Clear cache:
   ```bash
   mvn clean
```

---

### 5. Database Issues

#### Error: "Duplicate entry 'student1' for key 'student.username'"

**Cause**: Trying to insert username that already exists

**Solution**:
1. Use unique username for registration, OR
2. Delete duplicate from database:
   ```sql
   DELETE FROM student WHERE username = 'student1';
   ```

---

#### Error: "Cannot delete or update a parent row: a foreign key constraint fails"

**Cause**: Trying to delete category with courses in it

**Solution**:
1. Delete courses first:
   ```sql
   DELETE FROM course WHERE category_id = 1;
   ```

2. Then delete category:
   ```sql
   DELETE FROM category WHERE id = 1;
   ```

OR use ON DELETE CASCADE in schema (already configured)

---

#### Error: "Table 'course_registration.xyz_pkey' doesn't exist"

**Cause**: Schema was created for different database system (PostgreSQL)

**Solution**:
Use the correct MySQL schema:
```bash
# Drop and recreate database
DROP DATABASE course_registration;
mysql -u root -p < database_schema.sql
```

---

### 6. Dependency & Build Issues

#### Error: "Cannot resolve symbol" in IDE

**Cause**: IDE cache is stale or dependencies not downloaded

**Solutions**:

**IntelliJ IDEA**:
1. File → Invalidate Caches → Invalidate and Restart
2. Maven → Reload Projects
3. Right-click pom.xml → Maven → Reload

**Eclipse**:
1. Project → Clean
2. Right-click → Maven → Update Project
3. Restart IDE

**VS Code**:
1. Delete `.vscode` folder
2. `Ctrl+Shift+P` → "Java: Clean Language Server Workspace"
3. Reload window

---

#### Error: "Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin"

**Cause**: Java version mismatch

**Solution**:

1. Verify Java 21 is installed:
   ```bash
   java -version
   ```

2. Update pom.xml if needed:
   ```xml
   <properties>
       <java.version>21</java.version>
   </properties>
   ```

3. Maven rebuild:
   ```bash
   mvn clean
   mvn compile
   mvn spring-boot:run
   ```

---

#### Error: "Unresolved import" for Lombok

**Cause**: Lombok processor not configured in IDE

**Solutions**:

**IntelliJ IDEA**:
1. File → Settings → Plugins
2. Search for "Lombok"
3. Install "Lombok"
4. Restart IDE

**Eclipse**:
1. Download Lombok JAR from https://projectlombok.org/download
2. Run: `java -jar lombok.jar`
3. Select Eclipse installation
4. Click Install/Update
5. Restart Eclipse

**VS Code**:
```bash
# No action needed, works automatically with Maven
mvn clean install
```

---

### 7. OAuth2 Google Login Issues

#### Error: "invalid_grant" with Google login

**Cause**: Client ID/Secret invalid or expired

**Solution**:
1. Get new credentials from [Google Cloud Console](https://console.cloud.google.com/)
2. Create new OAuth 2.0 credentials
3. Set authorized redirect URI: `http://localhost:8080/login/oauth2/code/google`
4. Update application.properties:
   ```properties
   spring.security.oauth2.client.registration.google.client-id=YOUR_NEW_ID
   spring.security.oauth2.client.registration.google.client-secret=YOUR_NEW_SECRET
   ```

---

#### Error: "Redirect URI mismatch"

**Cause**: Redirect URI in code doesn't match Google Console

**Solution**:
1. In Google Console, ensure redirect URI is:
   ```
   http://localhost:8080/login/oauth2/code/google
   ```

2. For production, update to:
   ```
   https://yourdomain.com/login/oauth2/code/google
   ```

3. Update in application.properties if needed

---

### 8. Performance Issues

#### Application runs slowly

**Solutions**:

1. **Enable logging to check queries**:
   ```properties
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.format_sql=true
   logging.level.org.hibernate.SQL=DEBUG
   ```

2. **Optimize database queries**:
   - Add indexes: Create indexes on foreign keys, username, email
   - Use pagination: Already implemented (5 courses per page)
   - Eager load relationships: Done for Student roles

3. **Reduce session timeout for testing**:
   ```java
   // In SecurityConfig.java
   http.sessionManagement(session -> 
       session.sessionFixationProtection(SessionFixationProtectionStrategy.MIGRATE_SESSION)
   );
   ```

---

#### "Too many open connections" error

**Cause**: Connection pool exhausted

**Solution**:
```properties
# In application.properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
```

---

### 9. Testing Issues

#### JUnit test fails: "Could not instantiate TestExecutionListener"

**Cause**: Test configuration issue

**Solution**:
```bash
mvn clean test
# OR
mvn test -DskipTests  # Skip tests to run app
```

---

### 10. IDE-Specific Issues

#### IntelliJ: "Spring is not configured"

**Solution**:
1. File → Project Structure → Facets
2. Click "+" → Spring
3. Select the project
4. OK

---

#### Eclipse: "Type Mismatch" with Lombok

**Solution**:
1. Project → Properties → Project Facets
2. Convert to faceted form (if not already)
3. Right-click Project → Maven → Update Project
4. Clean and build

---

#### VS Code: Intellisense not working

**Solution**:
1. Install extensions:
   - Extension Pack for Java
   - Spring Boot Extension Pack
   - Lombok Annotations Support

2. Reload window: F1 → "Developer: Reload Window"

---

## ✅ Verification Checklist

After fixing issues, verify:

- [ ] MySQL service is running
- [ ] `application.properties` has correct password
- [ ] `database_schema.sql` has been executed
- [ ] Application starts without errors
- [ ] Can access http://localhost:8080/home
- [ ] Can see course listings
- [ ] Can register new student
- [ ] Can login with credentials
- [ ] Can enroll in course
- [ ] Can view my courses
- [ ] Admin can CRUD courses
- [ ] Access control works (admin only pages)

---

## 📞 If Issues Persist

1. **Check Logs**: Look for detailed error messages in console
2. **Database Check**: `SELECT * FROM course;` should show 7 courses
3. **Service Status**: `mysql -u root -p` should work
4. **Port Check**: Only one application should use port 8080
5. **Clean Build**: `mvn clean install -DskipTests`

---

## 🆘 Getting Help

### Required Information to Provide:

1. **Error message** (full stack trace)
2. **What you were doing** when error occurred
3. **Current step** (setup, running, testing)
4. **Operating system** (Windows/Mac/Linux)
5. **Java version** (`java -version`)
6. **MySQL version** (`mysql --version`)
7. **Maven version** (`mvn --version`)

### Useful Diagnostic Commands:

```bash
# Check Java
java -version

# Check MySQL
mysql -u root -p -e "SELECT VERSION();"

# Check Maven
mvn --version

# Test MySQL connection
mysql -h localhost -u root -p -e "USE course_registration; SHOW TABLES;"

# Check if port 8080 is in use
netstat -ano | findstr :8080  # Windows
lsof -i :8080                 # Mac/Linux

# View application logs
mvn spring-boot:run -X  # Detailed output
```

---

**Remember**: Most issues are either database connection or file path related. Verify these two things first before troubleshooting further! 🎯
