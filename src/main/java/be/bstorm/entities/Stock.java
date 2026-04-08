package be.bstorm.entities;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * Niveau de stock associe a un produit.
 *
 * <p><b>Role pedagogique:</b> Illustre la relation OneToOne (1-1).
 * Dans ce modele, chaque produit a exactement un enregistrement de stock.</p>
 *
 * <p><b>@OneToOne vs @ManyToOne:</b></p>
 * <ul>
 *   <li><b>@ManyToOne:</b> Plusieurs sources -> une cible.
 *       Exemple: plusieurs Product -> une Category.
 *       La FK (category_id) n'a pas de contrainte UNIQUE.
 *   </li>
 *   <li><b>@OneToOne:</b> Une source <-> une cible.
 *       Exemple: un Stock <-> un Product.
 *       La FK (product_id) a une contrainte UNIQUE.
 *   </li>
 * </ul>
 *
 * <p><b>Schema SQL genere:</b></p>
 * <pre>
 * CREATE TABLE stock (
 *     id SERIAL PRIMARY KEY,
 *     quantity INTEGER NOT NULL,
 *     product_id BIGINT UNIQUE REFERENCES produit(id)
 *     -- Remarque: product_id est UNIQUE, enforcer la relation 1-1
 * );
 * </pre>
 *
 * <p><b>Impact metier:</b></p>
 * Avec @OneToOne + UNIQUE, garantie:
 * - Chaque Product a au plus 1 Stock
 * - Chaque Stock pointe vers exactement 1 Product
 * - Pas de "doublon" de stock pour un meme produit
 *
 * <p><b>Cas d'usage:</b></p>
 * - 1 Product <-> 1 Stock (tracking du niveau)
 * - 1 User <-> 1 Profil detaille
 * - 1 Employe <-> 1 Bureau
 *
 * @see Product
 */
@Entity
public class Stock {

    /**
     * Cle primaire auto-generee.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Quantite en stock du produit.
     * Obligatoire.
     */
    @Column(nullable = false)
    private int quantity;

    /**
     * Le produit associe a ce stock.
     * Relation OneToOne: un stock pour exactement un produit.
     * Hibernate ajoute automatiquement une contrainte UNIQUE sur la FK,
     * garantissant qu'aucun produit n'a deux stocks.
     */
    @OneToOne
    private Product product;

    /**
     * Constructeur par defaut (requis par JPA).
     */
    public Stock() {}

    /**
     * Constructeur avec quantite et produit.
     * @param quantity la quantite initiale
     * @param product le produit associe
     */
    public Stock(int quantity, Product product) {
        this.quantity = quantity;
        this.product = product;
    }

    // ========== ACCESSEURS ==========

    public int getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    // ========== METHODES UTILITAIRES ==========

    /**
     * Egalite basee sur l'id.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return id == stock.id;
    }

    /**
     * Hash basee sur l'id.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /**
     * Representation textuelle.
     */
    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", quantity=" + quantity +
                '}';
    }
}
