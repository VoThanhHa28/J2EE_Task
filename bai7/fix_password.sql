USE course_registration;

-- Update all student passwords to BCrypt hash of "password" (strength 10)
-- BCrypt hash: $2a$10$slYQmyNdGzin7olVN3C1Be7DlH.PKZbv5H8KnzzVgXXbVxzy990e2
UPDATE student SET password='$2a$10$slYQmyNdGzin7olVN3C1Be7DlH.PKZbv5H8KnzzVgXXbVxzy990e2' WHERE username IN ('admin', 'student1', 'student2');

SELECT 'Updated passwords' as Status;
SELECT username, password FROM student;
