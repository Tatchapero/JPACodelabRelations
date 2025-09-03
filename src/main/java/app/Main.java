package app;

import app.config.HibernateConfig;
import app.entities.Course;
import app.entities.Student;
import app.entities.Teacher;
import app.enums.CourseName;
import app.persistence.CourseDAO;
import app.persistence.StudentDAO;
import app.persistence.TeacherDAO;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        TeacherDAO teacherDAO = new TeacherDAO(emf);
        CourseDAO courseDAO = new CourseDAO(emf);
        StudentDAO studentDAO = new StudentDAO(emf);

        // CREATE
        Course course = courseDAO.create(Course.builder()
                .courseName(CourseName.SCIENCE)
                .description("Computer Science")
                .startDate(LocalDate.parse("2025-09-01"))
                .endDate(LocalDate.parse("2025-12-31"))
                .build());

        Teacher teacher = teacherDAO.create(Teacher.builder()
                .name("Jon")
                .email("jones@ek.dk")
                .zoom("https://cphbusiness.zoom.us/my/jones?pwd=C1Cy8MdsVRjXFFcHlMq0xPv0Z2MYc0.1")
                .build());

        Student student = studentDAO.create(Student.builder()
                .name("Thomas")
                .email("cph-ta241@stud.ek.dk")
                .build());

        // UPDATE
        teacher.addCourse(course);
        teacherDAO.update(teacher);

        course.addStudent(student);
        courseDAO.update(course);

        // A) Get all students that are attending a specific course
        studentDAO.getStudentsByCourse(course).forEach(System.out::println);

        // B) Get all courses that a specific student is attending
        courseDAO.getCourseByStudent(student).ifPresent(System.out::println);

        // C) Get all courses that a specific teacher is teaching
        courseDAO.getCoursesByTeacher(teacher).forEach(System.out::println);

        // D) Get all students that are attending a course that a specific teacher is teaching
        studentDAO.getStudentsByTeacher(teacher).forEach(System.out::println);

        emf.close();
    }
}