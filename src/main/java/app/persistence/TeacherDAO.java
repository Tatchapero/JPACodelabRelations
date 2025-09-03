package app.persistence;

import app.entities.Teacher;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class TeacherDAO implements IDao<Teacher, Integer> {
    private final EntityManagerFactory emf;

    public TeacherDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Teacher create(Teacher teacher) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(teacher);
            em.getTransaction().commit();
        }
        return teacher;
    }

    @Override
    public Optional<Teacher> getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Teacher> query = em.createQuery(
                    "SELECT t FROM Teacher t WHERE t.id = :id",
                    Teacher.class
            );
            query.setParameter("id", id);
            return query.getResultStream().findFirst();
        }
    }

    @Override
    public List<Teacher> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Teacher> query = em.createQuery("SELECT t FROM Teacher t", Teacher.class);
            return query.getResultList();
        }
    }

    @Override
    public Teacher update(Teacher teacher) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            teacher.getCourses().forEach(em::merge);
            teacher = em.merge(teacher);
            em.getTransaction().commit();
            return teacher;
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Optional<Teacher> teacher = getById(id);
            teacher.ifPresent(em::remove);
            em.getTransaction().commit();
            return teacher.isPresent();
        }
    }
}
