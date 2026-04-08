package be.bstorm;

import be.bstorm.entities.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
    static void main() {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DemoJPA");
        EntityManager em = emf.createEntityManager();
        System.out.println(
                em.getMetamodel().getEntities()
        );

        Category category = new Category("Super ORM");

        em.getTransaction().begin();

        em.persist(category);

        em.getTransaction().commit();

    }
}
