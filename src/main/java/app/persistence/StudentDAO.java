package app.persistence;

import app.entities.Course;
import app.entities.Student;
import app.entities.Teacher;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class StudentDAO implements IDao<Student, Integer> {
    private final EntityManagerFactory emf;

    public StudentDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Student create(Student student) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(student);
            em.getTransaction().commit();
        }
        return student;
    }

    @Override
    public Optional<Student> getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE s.id = :id",
                    Student.class
            );
            query.setParameter("id", id);
            return query.getResultStream().findFirst();
        }
    }

    @Override
    public List<Student> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Student> query = em.createQuery("SELECT s FROM Student s", Student.class);
            return query.getResultList();
        }
    }

    @Override
    public Student update(Student student) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            student = em.merge(student);
            em.getTransaction().commit();
            return student;
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Optional<Student> student = getById(id);
            student.ifPresent(em::remove);
            em.getTransaction().commit();
            return student.isPresent();
        }
    }

    public List<Student> getStudentsByCourse(Course course) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE s.course = :course",
                    Student.class
            );
            query.setParameter("course", course);
            return query.getResultStream().toList();
        }
    }

    public List<Student> getStudentsByTeacher(Teacher teacher) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s JOIN FETCH s.course c WHERE c.teacher = :teacher",
                    Student.class
            );
            query.setParameter("teacher", teacher);
            return query.getResultStream().toList();
        }
    }
}
