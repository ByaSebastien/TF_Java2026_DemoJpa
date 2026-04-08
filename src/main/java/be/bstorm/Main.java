package be.bstorm;

import be.bstorm.entities.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Point d'entree de la demo JPA.
 *
 * <p>Au lancement, l'application cree une {@link EntityManagerFactory}, ouvre
 * un {@link EntityManager}, puis persiste une categorie pour illustrer le cycle
 * transactionnel minimal en JPA.</p>
 */
public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DemoJPA");

        try (EntityManager em = emf.createEntityManager()) {
            System.out.println(em.getMetamodel().getEntities());

            Category category = new Category("Super ORM");

            em.getTransaction().begin();
            em.persist(category);
            em.getTransaction().commit();
        } finally {
            emf.close();
        }
    }
}
