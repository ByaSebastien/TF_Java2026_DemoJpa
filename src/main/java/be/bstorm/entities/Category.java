package be.bstorm.entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

/**
 * Categorie metier d'un produit.
 *
 * <p><b>Role pedagogique:</b> Cet exemple montre l'entite JPA la plus simple possible:
 * une cle primaire generee automatiquement et une colonne metier unique.</p>
 *
 * <p><b>Annotations expliquees:</b></p>
 * <ul>
 *   <li><b>@Entity:</b> Marque la classe comme table persistante.
 *       Hibernate scanne le classpath pour les @Entity et les configure.
 *   </li>
 *   <li><b>@Id:</b> Indique que "id" est la cle primaire (PRIMARY KEY en SQL).</li>
 *   <li><b>@GeneratedValue(strategy = GenerationType.IDENTITY):</b> Auto-increment.
 *       Hibernate genere: BIGSERIAL (PostgreSQL) ou AUTO_INCREMENT (MySQL).
 *   </li>
 *   <li><b>@Column:</b> Personnalise la colonne SQL:
 *       <ul>
 *         <li>length = 50: VARCHAR(50) en SQL</li>
 *         <li>nullable = false: NOT NULL constraint</li>
 *         <li>unique = true: UNIQUE constraint (pas de doublon)</li>
 *       </ul>
 *   </li>
 * </ul>
 *
 * <p><b>Schema SQL genere:</b></p>
 * <pre>
 * CREATE TABLE category (
 *     id BIGSERIAL PRIMARY KEY,
 *     name VARCHAR(50) NOT NULL UNIQUE
 * );
 * </pre>
 *
 * <p><b>Relation avec Product:</b></p>
 * {@link Product} utilise @ManyToOne pour pointer vers cette entite.
 * Une Category peut avoir N Product, mais 1 Product ne peut pointer vers 1 Category.
 * Voir la relation: {@code Product.category} -> {@code Category.id}
 *
 * @see Product#category
 */
@Entity
public class Category {

    /**
     * Cle primaire auto-generee.
     * Chaque categorie a un id unique genere par la base de donnees.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nom unique de la categorie.
     * Contraintes:
     * - max 50 caracteres
     * - obligatoire (NOT NULL)
     * - unique (pas deux categories de meme nom)
     */
    @Column(length = 50, nullable = false, unique = true)
    private String name;

//    // OPTIONNEL: Relation inverse (OneToMany)
//    @OneToMany(mappedBy = "category")
//    private List<Product> products;
//    
//    // Si vous decommentez ceci, vous pourrez naviguer:
//    //   Category cat = ...;
//    //   List<Product> allProducts = cat.getProducts();
//    // Mais ce n'est pas necessaire pour que la relation fonctionne.

    /**
     * Constructeur par defaut (requis par JPA).
     */
    public Category() {}

    /**
     * Constructeur avec nom.
     * @param name le nom de la categorie
     */
    public Category(String name) {
        this.name = name;
    }

    // ========== ACCESSEURS ==========

    /**
     * Retourne l'id (cle primaire).
     * Null si l'entite n'a pas encore ete persistee.
     */
    public Long getId() {
        return id;
    }

    /**
     * Retourne le nom de la categorie.
     */
    public String getName() {
        return name;
    }

    /**
     * Modifie le nom de la categorie.
     * @param name le nouveau nom
     */
    public void setName(String name) {
        this.name = name;
    }

    // ========== METHODES UTILITAIRES ==========

    /**
     * Egalite basee sur l'id et le nom.
     * Deux categories sont egales si elles ont le meme id et le meme nom.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) && Objects.equals(name, category.name);
    }

    /**
     * Hash basee sur id et name.
     * Necessaire pour utiliser la classe dans des Sets ou comme cle de Map.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    /**
     * Representation textuelle pour debug.
     */
    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
