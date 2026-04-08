package be.bstorm.entities;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

/**
 * Ligne de commande (association entre une commande et un produit).
 *
 * <p>Cette classe illustre un cas classique de cle primaire composite:
 * {@code (orderId, productId)}.</p>
 */
@Entity
public class OrderLine {

    @EmbeddedId
    private OrderLineId id;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    // @MapsId lie la FK order au champ orderId de la cle composite.
    @MapsId("orderId")
    private Order order;

    @ManyToOne
    // @MapsId lie la FK product au champ productId de la cle composite.
    @MapsId("productId")
    private Product product;

    public OrderLine() {}

    public OrderLine(int quantity, Order order, Product product) {
        this.quantity = quantity;
        this.order = order;
        this.product = product;
        this.id = new OrderLineId(order.getId(), product.getId());
    }

    public OrderLineId getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
        this.id.setOrderId(order.getId());
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        this.id.setProductId(product.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderLine orderLine = (OrderLine) o;
        return quantity == orderLine.quantity && Objects.equals(id, orderLine.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity);
    }

    @Override
    public String toString() {
        return "OrderLine{" +
                "id=" + id +
                ", quantity=" + quantity +
                '}';
    }

    @Embeddable
    /**
     * Cle primaire composite de {@link OrderLine}.
     */
    public static class OrderLineId {
        private UUID orderId;
        private Long productId;

        public OrderLineId() {}

        public OrderLineId(UUID orderId, Long productId) {
            this.orderId = orderId;
            this.productId = productId;
        }

        public UUID getOrderId() {
            return orderId;
        }

        public void setOrderId(UUID orderId) {
            this.orderId = orderId;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            OrderLineId that = (OrderLineId) o;
            return Objects.equals(orderId, that.orderId) && Objects.equals(productId, that.productId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(orderId, productId);
        }

        @Override
        public String toString() {
            return "OrderLineId{" +
                    "orderId=" + orderId +
                    ", productId=" + productId +
                    '}';
        }
    }
}
