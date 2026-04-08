package be.bstorm.entities;

import jakarta.persistence.Embeddable;

import java.util.Objects;

/**
 * Objet valeur embarquable dans une entite.
 *
 * <p><b>Role pedagogique:</b> Demontre le concept d'Embedded/Embeddable.
 * Au lieu d'avoir une table separee, les champs d'Address sont integres
 * directement dans la table de l'entite proprietaire (ex: {@link User}).</p>
 *
 * <p><b>@Embeddable:</b> Cette annotation dit:
 * "Je suis une classe metier (logiquement coherente), mais je ne dois pas
 * avoir ma propre table. Place-moi plutot comme colonnes dans la table de
 * celui qui m'utilise."</p>
 *
 * <p><b>Avantages:</b></p>
 * <ul>
 *   <li>Les donnees liees restent logiquement groupees</li>
 *   <li>Pas de table supplementaire (performance)</li>
 *   <li>Plus lisible qu'avoir city, street, zipcode directement dans User</li>
 * </ul>
 *
 * <p><b>Utilisation dans {@link User}:</b></p>
 * <pre>
 * @Entity
 * public class User {
 *     @Embedded
 *     private Address address;  // Les colonnes d'Address sont integrees
 * }
 * </pre>
 *
 * <p><b>Schema SQL resultat:</b></p>
 * <pre>
 * CREATE TABLE user_ (
 *     id SERIAL PRIMARY KEY,
 *     email VARCHAR(150) NOT NULL UNIQUE,
 *     -- Les colonnes d'Address sont INLINE :
 *     city VARCHAR,
 *     street VARCHAR,
 *     zipcode VARCHAR
 * );
 * </pre>
 * 
 * <p>Remarquez: PAS de table separee "address", juste les colonnes inline dans user_.</p>
 *
 * @see User#address
 */
@Embeddable
public class Address {

    /**
     * Ville de l'adresse.
     */
    private String city;

    /**
     * Rue et numero de l'adresse.
     */
    private String street;

    /**
     * Code postal.
     */
    private String zipcode;

    /**
     * Constructeur par defaut (requis par JPA).
     */
    public Address() {}

    /**
     * Constructeur avec toutes les donnees.
     * @param city la ville
     * @param street la rue
     * @param zipcode le code postal
     */
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    // ========== ACCESSEURS ==========

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    // ========== METHODES UTILITAIRES ==========

    /**
     * Egalite basee sur tous les champs d'adresse.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(city, address.city) && Objects.equals(street, address.street) && Objects.equals(zipcode, address.zipcode);
    }

    /**
     * Hash basee sur tous les champs d'adresse.
     */
    @Override
    public int hashCode() {
        return Objects.hash(city, street, zipcode);
    }

    /**
     * Representation textuelle.
     */
    @Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", zipcode='" + zipcode + '\'' +
                '}';
    }
}
