package app.persistence;

import app.entities.Course;
import app.entities.Student;
import app.entities.Teacher;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class CourseDAO implements IDao<Course, Integer> {
    private final EntityManagerFactory emf;

    public CourseDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Course create(Course course) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(course);
            em.getTransaction().commit();
        }
        return course;
    }

    @Override
    public Optional<Course> getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Course> query = em.createQuery(
                    "SELECT c FROM Course c WHERE c.id = :id",
                    Course.class
            );
            query.setParameter("id", id);
            return query.getResultStream().findFirst();
        }
    }

    @Override
    public List<Course> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Course> query = em.createQuery("SELECT c FROM Course c", Course.class);
            return query.getResultList();
        }
    }

    @Override
    public Course update(Course course) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            course.getStudents().forEach(em::merge);
            course = em.merge(course);
            em.getTransaction().commit();
            return course;
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Optional<Course> course = getById(id);
            course.ifPresent(em::remove);
            em.getTransaction().commit();
            return course.isPresent();
        }
    }

    public Optional<Course> getCourseByStudent(Student student) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Course> query = em.createQuery(
                    "SELECT c FROM Course c WHERE :student MEMBER OF c.students",
                    Course.class
            );
            query.setParameter("student", student);
            return query.getResultStream().findFirst();
        }
    }

    public List<Course> getCoursesByTeacher(Teacher teacher) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Course> query = em.createQuery(
                    "SELECT c FROM Course c WHERE c.teacher = :teacher",
                    Course.class
            );
            query.setParameter("teacher", teacher);
            return query.getResultStream().toList();
        }
    }
}
