package be.bstorm.entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

/**
 * Categorie metier d'un produit.
 *
 * <p>Cette entite montre le mapping le plus simple: une cle primaire generee et
 * une colonne metier unique.</p>
 */
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String name;

//    @OneToMany(mappedBy = "category")
//    private List<Product> products;
// Relation inverse possible si vous voulez naviguer Category -> Product.

    public Category() {}

    public Category(String name) {
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) && Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
