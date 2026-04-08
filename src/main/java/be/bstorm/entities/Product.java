package be.bstorm.entities;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * Produit vendable du catalogue.
 *
 * <p>Cette entite combine colonnes simples et relation {@code ManyToOne}
 * vers {@link Category}.</p>
 */
@Entity
@Table(name = "produit") // renommer la table (optionel)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, name = "nom", nullable = false, unique = true) // renommer la colonne (optionel)
    private String name;

    @Column(name = "prix", nullable = false)
    private int price;

    @Column(length = 500, nullable = true)
    private String description;

    @ManyToOne
    private Category category;

    public Product() {
    }

    public Product(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return price == product.price && Objects.equals(id, product.id) && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
