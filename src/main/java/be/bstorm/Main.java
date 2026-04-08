package be.bstorm;

import be.bstorm.entities.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Point d'entree de la demo JPA/Hibernate.
 *
 * <p><strong>Role:</strong> Cette classe illustre le cycle minimal JPA:</p>
 * <ol>
 *   <li>Creer une {@link EntityManagerFactory} (lit persistence.xml)</li>
 *   <li>Ouvrir une {@link EntityManager} (session de persistance)</li>
 *   <li>Creer une entite metier (objet Java)</li>
 *   <li>Demarrer une transaction</li>
 *   <li>Persister l'entite (INSERT vers la BD)</li>
 *   <li>Valider la transaction (COMMIT)</li>
 * </ol>
 *
 * <p><strong>Concepts cles:</strong></p>
 * <ul>
 *   <li><b>EntityManagerFactory:</b> Fabrique unique, chere a creer, generalement creee une fois</li>
 *   <li><b>EntityManager:</b> Session par requete, ouvert/ferme rapidement</li>
 *   <li><b>Transaction:</b> Unitaire (ACID) - tout reussit ou tout echoue</li>
 *   <li><b>persist():</b> Passe l'objet du state DETACHED au state MANAGED</li>
 *   <li><b>Metamodel:</b> Reflection JPA sur les entites chargees</li>
 * </ul>
 *
 * <p><strong>Ce qui se passe en coulisse:</strong></p>
 * Lors de l'execution:
 * <pre>
 * 1. Hibernate charge persistence.xml
 * 2. Scanne les @Entity dans le classpath
 * 3. Avec hbm2ddl.auto=create, DROP + CREATE toutes les tables
 * 4. Affiche les entites detectees (getMetamodel().getEntities())
 * 5. INSERT la categorie en base
 * 6. Affiche le SQL genere (grace a show_sql=true)
 * </pre>
 *
 * @see jakarta.persistence.EntityManagerFactory
 * @see jakarta.persistence.EntityManager
 * @see jakarta.persistence.Persistence
 */
public class Main {
    public static void main(String[] args) {
        // ETAPE 1: Creer la fabrique
        // Lit persistence.xml et cree un EntityManagerFactory.
        // C'est une operation couteuse, idealement faite une seule fois au demarrage.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DemoJPA");

        try (EntityManager em = emf.createEntityManager()) {
            // ETAPE 2: Afficher les entites detectees par JPA
            // getMetamodel() offre une reflexion sur les @Entity scannees.
            System.out.println("=== ENTITES DETECTEES ===");
            System.out.println(em.getMetamodel().getEntities());
            System.out.println();

            // ETAPE 3: Creer une entite metier
            // Cet objet n'existe QUE en memoire, pas encore en base.
            Category category = new Category("Super ORM");
            System.out.println("Objet Java cree: " + category);
            System.out.println();

            // ETAPE 4: Demarrer une transaction et persister
            System.out.println("=== LANCEMENT DE LA TRANSACTION ===");
            em.getTransaction().begin();

            // persist() ajoute l'objet au EntityManager (state MANAGED).
            // A ce stade, une INSERT est preparee mais non executee.
            em.persist(category);
            System.out.println("Entite persist()ed");

            // ETAPE 5: Valider (COMMIT)
            // C'est le commit qui genere et execute le SQL (INSERT).
            em.getTransaction().commit();
            System.out.println("Transaction commit()ed -> SQL execute en base");
            System.out.println();

        } finally {
            // ETAPE 6: Cleanup
            // Fermer le EntityManagerFactory (libere toutes les ressources).
            emf.close();
            System.out.println("EntityManagerFactory fermee.");
        }
    }
}
