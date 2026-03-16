# 📚 Course Registration Application - Complete Documentation Index

## 🎯 Quick Navigation

### Getting Started (Start Here!)
1. **[QUICK_START.md](QUICK_START.md)** ← **START HERE** (5 minutes)
   - 5-minute quick start
   - Default login credentials
   - Common tasks overview
   - Quick troubleshooting

### Setup & Installation
2. **[SETUP_GUIDE.md](SETUP_GUIDE.md)** (15-30 minutes)
   - Detailed prerequisites
   - Step-by-step database setup
   - Application configuration
   - Troubleshooting each step
   - Optional: Google OAuth2 setup

### Learning the Application
3. **[ARCHITECTURE.md](ARCHITECTURE.md)** (20-30 minutes)
   - System architecture (layered model)
   - Data flow diagrams
   - Entity relationships (UML diagram)
   - Design patterns used
   - Request routing
   - Service layer design

### Using the Application
4. **[API_DOCUMENTATION.md](API_DOCUMENTATION.md)** (Reference)
   - All endpoints documented
   - Request/response examples
   - Parameter descriptions
   - Authentication flows
   - Error codes and solutions

### After Setup
5. **[AFTER_SETUP.md](AFTER_SETUP.md)** (After running app)
   - Testing checklist
   - Feature verification
   - Performance testing
   - Deployment preparation
   - Next learning steps

### Reference & Troubleshooting
6. **[TROUBLESHOOTING.md](TROUBLESHOOTING.md)** (When issues arise)
   - Common errors and solutions
   - Database issues
   - Authentication problems
   - Performance optimization
   - IDE-specific issues

### Project Information
7. **[PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)** (Project overview)
   - Feature checklist
   - File structure
   - Dependencies
   - Testing checklist
   - Deployment info

---

## ⚡ By Use Case

### "I just want to run it" (5 minutes)
1. Open **[QUICK_START.md](QUICK_START.md)**
2. Follow the 5-minute quick start
3. Login with default credentials
4. Explore the application

### "I want to understand the code" (30 minutes)
1. Read **[PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)** for overview
2. Read **[ARCHITECTURE.md](ARCHITECTURE.md)** for design
3. Read **[API_DOCUMENTATION.md](API_DOCUMENTATION.md)** for endpoints
4. Browse source code with understanding
5. Check comments in Java files

### "I need to set it up from scratch" (45 minutes)
1. **[SETUP_GUIDE.md](SETUP_GUIDE.md)** - Detailed instructions
2. Database setup (Run `database_schema.sql`)
3. Configure `application.properties`
4. Run `mvn spring-boot:run`
5. Verify with **[AFTER_SETUP.md](AFTER_SETUP.md)** checklist

### "Something is broken" (10-30 minutes)
1. Check **[TROUBLESHOOTING.md](TROUBLESHOOTING.md)**
2. Find your error in common issues section
3. Follow provided solution
4. If unresolved, check in SETUP_GUIDE.md

### "I want to extend the application" (2+ hours)
1. Understand architecture: **[ARCHITECTURE.md](ARCHITECTURE.md)**
2. Understand endpoints: **[API_DOCUMENTATION.md](API_DOCUMENTATION.md)**
3. Understand code structure: **[PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)**
4. Make your changes
5. Test with **[AFTER_SETUP.md](AFTER_SETUP.md)** checklist

---

## 📖 Recommended Reading Order

### For First-Time Users
```
1. QUICK_START.md              (What can this do? 5 min)
   ↓
2. SETUP_GUIDE.md              (How do I run it? 15 min)
   ↓
3. AFTER_SETUP.md              (How do I test it? 10 min)
   ↓
4. API_DOCUMENTATION.md        (What are the endpoints? 20 min)
   ↓
5. ARCHITECTURE.md             (How is it built? 30 min)
```

### For Developers
```
1. PROJECT_SUMMARY.md          (What's included? 10 min)
   ↓
2. ARCHITECTURE.md             (How is it designed? 30 min)
   ↓
3. API_DOCUMENTATION.md        (What can I call? 20 min)
   ↓
4. database_schema.sql         (How is data stored? 10 min)
   ↓
5. Source code                 (How is it implemented? depends)
```

### For Troubleshooting
```
1. TROUBLESHOOTING.md          (This exact issue?)
   ↓
2. SETUP_GUIDE.md              (Did I follow setup correctly?)
   ↓
3. database_schema.sql         (Is database correct?)
   ↓
4. QUICK_START.md              (Basic verification steps)
```

---

## 🚀 Quick Commands

```bash
# Clone/navigate to project
cd bai7

# Build the application
mvn clean package

# Run the application
mvn spring-boot:run

# Access the application
http://localhost:8080/home

# Stop the application
Ctrl+C (in terminal)
```

---

## 📂 Files Reference

### Documentation Files (What to Read)
| File | Purpose | Read Time |
|------|---------|-----------|
| QUICK_START.md | Get running in 5 minutes | 5 min |
| SETUP_GUIDE.md | Complete setup instructions | 20 min |
| ARCHITECTURE.md | System design & patterns | 30 min |
| API_DOCUMENTATION.md | API endpoints reference | 20 min |
| TROUBLESHOOTING.md | Fix common issues | 10 min |
| PROJECT_SUMMARY.md | Project overview | 10 min |
| AFTER_SETUP.md | Testing & next steps | 30 min |

### Code Files (What to Explore)
| Type | Count | Location |
|------|-------|----------|
| Entity Classes | 5 | src/main/java/.../entity/ |
| Repositories | 5 | src/main/java/.../repository/ |
| Services | 4 | src/main/java/.../service/ |
| Controllers | 4 | src/main/java/.../controller/ |
| Configuration | 2 | src/main/java/.../config/ |
| Templates | 6 | src/main/resources/templates/ |

### Configuration Files (What to Configure)
| File | Purpose |
|------|---------|
| pom.xml | Maven dependencies |
| application.properties | Application & database config |
| database_schema.sql | Database structure & data |

---

## 🔑 Key Concepts

### Authentication (Who are you?)
- **Form Login**: Username + Password
- **OAuth2**: Google Account
- **Session**: JSESSIONID cookie
- **Encryption**: BCrypt for passwords

### Authorization (What can you do?)
- **Roles**: ADMIN, STUDENT
- **Rules**: /admin/* → ADMIN only, /enroll/* → STUDENT only
- **Public Pages**: /home, /login, /register

### Data Model
- **Students**: User accounts with roles and enrollments
- **Courses**: With categories and instructors
- **Enrollments**: Many-to-many relationship between students and courses
- **Roles**: ADMIN (full access), STUDENT (limited access)

### Technology Stack
- **Backend**: Spring Boot 4.0.3 (Spring Security, Spring Data JPA)
- **Frontend**: Thymeleaf + Bootstrap 5
- **Database**: MySQL 8.0+
- **Build**: Maven
- **Java**: 21+

---

## ✅ Success Criteria

You've successfully set up when:
- [ ] Application runs without errors
- [ ] Can access http://localhost:8080/home
- [ ] Can see 7 sample courses
- [ ] Can register new student account
- [ ] Can login with credentials
- [ ] Can enroll in course
- [ ] Can view enrolled courses
- [ ] Admin can create/edit/delete courses
- [ ] Non-admin cannot access admin pages
- [ ] Responsive design works on mobile

---

## 🎯 Common Paths

### "I want to deploy this"
→ See **[AFTER_SETUP.md](AFTER_SETUP.md)** Section 8: "Create Deployment Plan"

### "I want to add features"
→ See **[ARCHITECTURE.md](ARCHITECTURE.md)** for understanding structure, then modify code

### "I want to understand OAuth2"
→ See **[API_DOCUMENTATION.md](API_DOCUMENTATION.md)** Section: "OAuth2 Endpoints"

### "I want to customize the UI"
→ Edit templates in `src/main/resources/templates/`

### "I want to add new roles"
→ See **[TROUBLESHOOTING.md](TROUBLESHOOTING.md)** or modify `database_schema.sql` and `SecurityConfig.java`

### "I want to change database"
→ Update `application.properties` and `SecurityConfig.java` with new dialect

---

## 💡 Tips & Tricks

### For Developers
```java
// Enable SQL logging to see queries
// In application.properties:
// spring.jpa.show-sql=true

// Add breakpoints in controllers for debugging
// Use browser DevTools (F12) for frontend debugging

// Check logs for detailed error messages
// View logs in IDE console or: tail -f logs/
```

### For Speed
```
// Development: Use hot reload
// mvn clean spring-boot:run -f

// Testing: Skip tests during build
// mvn install -DskipTests

// Database: Use TRUNCATE for fast cleanup
// TRUNCATE TABLE enrollment;
```

### For Learning
```
1. Read code with architecture in mind
2. Trace request through layers: Controller → Service → Repository
3. Understand entity relationships visually
4. Test each feature individually
5. Modify code and see results
```

---

## 📞 Document Map

```
START HERE ↓
    QUICK_START.md (5 min)
         ↓ Yes, worked
    AFTER_SETUP.md (test it)
         ↓
    API_DOCUMENTATION.md (learn endpoints)
         ↓
    ARCHITECTURE.md (understand design)
         ↓
    Source Code (dive deeper)

    ↕ Trouble?
    TROUBLESHOOTING.md (fix it)
         ↓
    SETUP_GUIDE.md (redo setup)
         ↓
    Try again
```

---

## 🎓 Learning Progression

### Level 1: User (Getting Started)
- Read **[QUICK_START.md](QUICK_START.md)**
- Use the application
- Test all features
- ✓ **Time**: 30 minutes

### Level 2: Developer (Understanding)
- Read **[ARCHITECTURE.md](ARCHITECTURE.md)**
- Read **[API_DOCUMENTATION.md](API_DOCUMENTATION.md)**
- Browse source code
- Understand request flow
- ✓ **Time**: 2 hours

### Level 3: Contributor (Modifying)
- Understand **[PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)**
- Modify templates
- Add new features
- Test changes
- ✓ **Time**: 4+ hours

### Level 4: Expert (Advanced)
- Implement optimization
- Add caching
- Deploy to cloud
- Set up CI/CD
- ✓ **Time**: 8+ hours

---

## 🔍 Finding What You Need

| I want to... | Read This |
|---|---|
| Get it running fast | QUICK_START.md |
| Set it up properly | SETUP_GUIDE.md |
| Understand how it works | ARCHITECTURE.md |
| Know all endpoints | API_DOCUMENTATION.md |
| Fix an error | TROUBLESHOOTING.md |
| See what's included | PROJECT_SUMMARY.md |
| Test it completely | AFTER_SETUP.md |
| Change the database | SETUP_GUIDE.md + database_schema.sql |
| Enable Google login | SETUP_GUIDE.md Section 4 |
| Deploy it | AFTER_SETUP.md Section 8 |

---

## 📋 Checklists

### Setup Checklist
- [ ] Java 21 installed
- [ ] MySQL running
- [ ] database_schema.sql executed
- [ ] application.properties configured
- [ ] Application starts without errors

### Features Checklist
- [ ] Home page displays courses
- [ ] Search functionality works
- [ ] Registration creates students
- [ ] Login authenticates users
- [ ] Enrollment stores in database
- [ ] My Courses page works
- [ ] Admin can CRUD courses
- [ ] Access control working

### Documentation Checklist
- [ ] Read QUICK_START.md
- [ ] Understand ARCHITECTURE.md
- [ ] Know all API endpoints
- [ ] Can troubleshoot basic issues
- [ ] Can extend the application

---

## 🚀 You're Ready!

Choose your adventure:

```
┌─────────────────────────────────┐
│  Course Registration App Ready! │
└─────────────────────────────────┘
         │
    ┌────┴────────────────────┬────────────────────┬────────────────┐
    │                         │                    │                │
    ↓                         ↓                    ↓                ↓
 RUN IT              UNDERSTAND IT         EXTEND IT         DEPLOY IT
QUICK_START.md    ARCHITECTURE.md    Source Code Files   AFTER_SETUP.md
 (5 minutes)      (30 minutes)       (modify & test)      (Section 8)
```

---

**Start with [QUICK_START.md](QUICK_START.md) and enjoy! 🎉**

---

*Last Updated: March 16, 2024*
*For the best experience, read documents in the suggested order.*
*All documentation is included in the project folder.*
