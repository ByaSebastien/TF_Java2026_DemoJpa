package be.bstorm.entities;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * Produit vendable du catalogue.
 *
 * <p><b>Role pedagogique:</b> Cet exemple demontre:
 * <ul>
 *   <li>Colonnes simples avec contraintes (@Column)</li>
 *   <li>Renommage de table et colonnes en SQL</li>
 *   <li>Relation ManyToOne vers {@link Category}</li>
 * </ul>
 * </p>
 *
 * <p><b>Concepts cles:</b></p>
 * <ul>
 *   <li><b>@Table(name = "produit"):</b> Personnalise le nom de la table.
 *       Par defaut, Hibernate utiliserait "product" (minuscule).
 *       Ici on force "produit" pour correspondre aux conventions metier.
 *   </li>
 *   <li><b>@Column(name = "..."):</b> Renomme la colonne SQL.
 *       Exemple: le champ Java "name" devient colonne "nom" en BD.
 *       Utile pour aligner avec des schemas legacys.
 *   </li>
 *   <li><b>@ManyToOne:</b> Relation N-1.
 *       Plusieurs Product peuvent avoir la meme Category,
 *       mais 1 Product ne peut pointer vers qu'une seule Category.
 *   </li>
 * </ul>
 *
 * <p><b>Schema SQL genere:</b></p>
 * <pre>
 * CREATE TABLE produit (
 *     id BIGSERIAL PRIMARY KEY,
 *     nom VARCHAR(50) NOT NULL UNIQUE,
 *     prix INTEGER NOT NULL,
 *     description VARCHAR(500),
 *     category_id BIGINT REFERENCES category(id)
 * );
 * </pre>
 *
 * <p><b>Relations:</b></p>
 * <ul>
 *   <li>Vers {@link Category}: @ManyToOne (plusieurs produits par categorie)</li>
 *   <li>Depuis {@link Stock}: @OneToOne inverse (1 stock par produit)</li>
 *   <li>Depuis {@link User}: @ManyToMany (favoris)</li>
 *   <li>Depuis {@link OrderLine}: @OneToMany inverse (plusieurs lignes de commande)</li>
 * </ul>
 *
 * @see Category
 * @see Stock
 * @see OrderLine
 */
@Entity
@Table(name = "produit")  // Renomme la table "product" en "produit"
public class Product {

    /**
     * Cle primaire auto-generee.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nom unique du produit.
     * Renomme en colonne SQL "nom" (au lieu de "name").
     * Contraintes:
     * - max 50 caracteres
     * - obligatoire
     * - unique (pas deux produits du meme nom)
     */
    @Column(length = 50, name = "nom", nullable = false, unique = true)
    private String name;

    /**
     * Prix du produit en centimes (ou unites de devise).
     * Renomme en colonne SQL "prix".
     * Obligatoire.
     */
    @Column(name = "prix", nullable = false)
    private int price;

    /**
     * Description detaillee du produit.
     * Optionnelle (nullable = true par defaut).
     */
    @Column(length = 500, nullable = true)
    private String description;

    /**
     * Categorie a laquelle appartient ce produit.
     * Relation ManyToOne: plusieurs produits peuvent avoir la meme categorie.
     * Hibernate cree automatiquement une colonne FK "category_id"
     * qui reference category(id).
     */
    @ManyToOne
    private Category category;

    /**
     * Constructeur par defaut (requis par JPA).
     */
    public Product() {
    }

    /**
     * Constructeur avec donnees de base.
     * @param name le nom du produit
     * @param price le prix
     * @param description la description
     */
    public Product(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    // ========== ACCESSEURS ==========

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retourne la categorie.
     * Peut etre null si le produit n'a pas de categorie assignee.
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Assigne une categorie au produit.
     * @param category la categorie (peut etre null)
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    // ========== METHODES UTILITAIRES ==========

    /**
     * Egalite basee sur id, name et price.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return price == product.price && Objects.equals(id, product.id) && Objects.equals(name, product.name);
    }

    /**
     * Hash basee sur id, name et price.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }

    /**
     * Representation textuelle.
     */
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
