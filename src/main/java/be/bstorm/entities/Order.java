package be.bstorm.entities;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

/**
 * Commande metier identifiee par UUID.
 *
 * <p>Le nom de table est force a {@code order_} car {@code order} est souvent
 * reserve en SQL.</p>
 */
@Entity
@Table(name = "order_")
public class Order {

    @Id
    private UUID id;

    public Order() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                '}';
    }
}
