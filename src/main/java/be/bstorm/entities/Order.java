package be.bstorm.entities;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

/**
 * Commande metier identifiee par UUID.
 *
 * <p><b>Role pedagogique:</b> Demontre l'utilisation d'un UUID comme cle primaire,
 * au lieu d'un auto-increment classique.</p>
 *
 * <p><b>Concepts cles:</b></p>
 * <ul>
 *   <li><b>@Table(name = "order_"):</b> "order" est un mot-cle reserve en SQL.
 *       On renomme donc la table en "order_".
 *   </li>
 *   <li><b>private UUID id:</b> Pas de @GeneratedValue!
 *       L'UUID est genere manuellement dans le constructeur.
 *   </li>
 *   <li><b>Avantages du UUID:</b>
 *       <ul>
 *         <li>Unique au niveau mondial (probabilite de collision negligeable)</li>
 *         <li>Generable sans acces a la base (utile en architecture distribuee)</li>
 *         <li>Plus securise qu'un ID incrementel qui expose la structure metier</li>
 *       </ul>
 *   </li>
 *   <li><b>Inconvenients du UUID:</b>
 *       <ul>
 *         <li>Plus volumineux (16 bytes vs 4-8 pour un int/long)</li>
 *         <li>Impact sur les performances de recherche et indexation</li>
 *         <li>Plus difficile a lire pour les logs/debugging</li>
 *       </ul>
 *   </li>
 * </ul>
 *
 * <p><b>Schema SQL genere:</b></p>
 * <pre>
 * CREATE TABLE order_ (
 *     id UUID PRIMARY KEY
 * );
 * </pre>
 *
 * <p><b>Utilisation typique:</b></p>
 * <pre>
 * Order order = new Order();  // UUID auto-genere
 * System.out.println(order.getId());  // Ex: "550e8400-e29b-41d4-a716-446655440000"
 * </pre>
 *
 * <p><b>Relation avec OrderLine:</b></p>
 * {@link OrderLine} pointe vers Order via sa cle composite.
 * Une commande peut avoir plusieurs lignes (1 Order -> N OrderLine).
 *
 * @see OrderLine
 * @see OrderLine.OrderLineId
 */
@Entity
@Table(name = "order_")  // Renomme "order" en "order_" (order est reserve)
public class Order {

    /**
     * Cle primaire: UUID (Universally Unique Identifier).
     * Aucune @GeneratedValue: l'UUID est genere manuellement dans le constructeur.
     * Type SQL resultat: UUID en PostgreSQL.
     */
    @Id
    private UUID id;

    /**
     * Constructeur par defaut (requis par JPA).
     * Genere automatiquement un UUID unique.
     */
    public Order() {
        this.id = UUID.randomUUID();
    }

    // ========== ACCESSEURS ==========

    /**
     * Retourne l'UUID de la commande.
     * Jamais null (genere dans le constructeur).
     */
    public UUID getId() {
        return id;
    }

    /**
     * Modifie l'UUID (rarement utilise).
     * @param id le nouvel UUID
     */
    public void setId(UUID id) {
        this.id = id;
    }

    // ========== METHODES UTILITAIRES ==========

    /**
     * Egalite basee sur l'UUID.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    /**
     * Hash basee sur l'UUID.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /**
     * Representation textuelle (affiche l'UUID).
     */
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                '}';
    }
}
