package be.bstorm.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Utilisateur de l'application.
 *
 * <p><b>Role pedagogique:</b> Demontre deux concepts importants:
 * <ul>
 *   <li>{@code @Embedded}: utilisation d'un objet embarquable ({@link Address})</li>
 *   <li>{@code @ManyToMany}: relation N-N avec table de jointure automatique</li>
 * </ul>
 * </p>
 *
 * <p><b>Concepts cles:</b></p>
 * <ul>
 *   <li><b>@Table(name = "user_"):</b> "user" est un mot-cle reserve en SQL.
 *       On renomme donc la table en "user_" pour eviter les conflits.
 *   </li>
 *   <li><b>@Embedded private Address address:</b> Les champs d'Address
 *       sont integres directement dans la table user_ (pas de table address separee).
 *   </li>
 *   <li><b>@ManyToMany private List&lt;Product&gt; favorites:</b>
 *       Relation N-N: plusieurs users peuvent avoir les memes produits favoris,
 *       et un produit peut etre favori de plusieurs users.
 *       Hibernate cree automatiquement une table de jointure.
 *   </li>
 * </ul>
 *
 * <p><b>Schema SQL genere:</b></p>
 * <pre>
 * CREATE TABLE user_ (
 *     id SERIAL PRIMARY KEY,
 *     email VARCHAR(150) NOT NULL UNIQUE,
 *     -- Colonnes d'Address integrees:
 *     city VARCHAR,
 *     street VARCHAR,
 *     zipcode VARCHAR
 * );
 *
 * -- Table de jointure creee automatiquement par Hibernate:
 * CREATE TABLE user__favorites (
 *     user_id INTEGER REFERENCES user_(id),
 *     favorites_id BIGINT REFERENCES produit(id),
 *     PRIMARY KEY (user_id, favorites_id)
 * );
 * </pre>
 *
 * <p><b>Utilisation typique:</b></p>
 * <pre>
 * User user = new User("alice@example.com", new Address("Paris", "123 Rue de la Paix", "75001"));
 * Product laptop = productRepository.findById(1);
 * user.addFavorite(laptop);  // JPA insere automatiquement dans user__favorites
 * </pre>
 *
 * @see Address
 * @see Product
 */
@Entity
@Table(name = "user_")  // Renomme "user" en "user_" (user est reserve)
public class User {

    /**
     * Cle primaire auto-generee.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Email unique de l'utilisateur.
     * Obligatoire et unique.
     */
    @Column(length = 150, nullable = false, unique = true)
    private String email;

    /**
     * Adresse de l'utilisateur.
     * Grace a @Embedded, les champs city, street, zipcode
     * seront integres directement dans la table user_ (pas de table separee).
     *
     * @see Address
     */
    @Embedded
    private Address address;

    /**
     * Liste des produits favoris de l'utilisateur.
     * Relation ManyToMany: plusieurs users peuvent partager les memes produits favoris.
     * Hibernate cree une table de jointure "user__favorites" avec:
     * - user_id (FK vers user_)
     * - favorites_id (FK vers produit)
     * - PRIMARY KEY composee sur (user_id, favorites_id)
     */
    @ManyToMany
    // Par defaut, Hibernate nomme la table de jointure: "user__favorites"
    // (nom de la table source + "__" + nom de l'attribut)
    private List<Product> favorites = new ArrayList<>();

    /**
     * Constructeur par defaut (requis par JPA).
     */
    public User() {}

    /**
     * Constructeur avec email et adresse.
     * @param email l'email de l'utilisateur
     * @param address l'adresse
     */
    public User(String email, Address address) {
        this.email = email;
        this.address = address;
    }

    // ========== ACCESSEURS ==========

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Retourne la liste des produits favoris.
     */
    public List<Product> getFavorites() {
        return favorites;
    }

    /**
     * Ajoute un produit a la liste de favoris.
     * JPA synchronise automatiquement la table user__favorites.
     * @param product le produit a ajouter aux favoris
     */
    public void addFavorite(Product product) {
        favorites.add(product);
    }

    /**
     * Retire un produit de la liste de favoris.
     * JPA synchronise automatiquement la table user__favorites.
     * @param product le produit a retirer des favoris
     */
    public void removeFavorite(Product product) {
        favorites.remove(product);
    }

    // ========== METHODES UTILITAIRES ==========

    /**
     * Egalite basee sur id et email.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(email, user.email);
    }

    /**
     * Hash basee sur id et email.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    /**
     * Representation textuelle.
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }
}
