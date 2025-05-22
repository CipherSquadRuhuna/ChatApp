package client.gui.utility;

import models.User;
import jakarta.persistence.*;

public class UserService {

    public void saveUser(User user) {
        EntityManager em = JPAUtil.getEntityManager();

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
        EntityManager em = JPAUtil.getEntityManager();

        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class);
            query.setParameter("username", username);
            query.setParameter("password", password);

            return query.getSingleResult(); // Login successful
        } catch (NoResultException e) {
            return null; // Login failed
        } finally {
            em.close();
        }
    }
}
