package Utility;

import models.User;
import jakarta.persistence.*;
import org.hibernate.SessionFactory;

public class UserService {

    private EntityManagerFactory emf;

    public UserService() {
        emf = Persistence.createEntityManagerFactory("default");
    }

    public void saveUser(User user) {
        EntityManager em = emf.createEntityManager();

//        SessionFactory factory= BuidSessionFactory();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public User userlogin(String username, String password) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class);
            query.setParameter("username", username);
            query.setParameter("password", password);

//            return query.getSingleResult(); // Login success
            User user=query.getSingleResult();
            return user;
        } catch (NoResultException e) {
            return null; // Login failed

        } finally {
            em.close();
            emf.close();
        }
    }




    public void close() {
        emf.close();
    }
}

