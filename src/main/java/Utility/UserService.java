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

    public void close() {
        emf.close();
    }
}

