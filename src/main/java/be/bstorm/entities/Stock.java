package be.bstorm.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int quantity;

    @OneToOne
    private Product product;

    public Stock() {}

    public Stock(int quantity, Product product) {
        this.quantity =  quantity;
        this.product = product;
    }

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return id == stock.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", quantity=" + quantity +
                '}';
    }
}
