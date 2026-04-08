package be.bstorm.entities;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

/**
 * Ligne de commande (association entre une commande et un produit).
 *
 * <p><b>Role pedagogique:</b> Illustre un cas classique de <b>cle primaire composite</b>.
 * Au lieu d'avoir une cle primaire simple (id auto-genere), OrderLine utilise
 * une cle composite: (orderId, productId).</p>
 *
 * <p><b>Concepts cles:</b></p>
 * <ul>
 *   <li><b>Cle composite:</b> Une cle primaire constituee de plusieurs champs.
 *       Ici: (orderId, productId). Aucune entree n'est unique seule,
 *       mais le couple (orderId, productId) doit etre unique.
 *   </li>
 *   <li><b>@EmbeddedId:</b> La cle composite est une classe embarquable
 *       ({@link OrderLineId}), embedded dans la table.
 *   </li>
 *   <li><b>@MapsId("fieldName"):</b> Synchronise une FK (Many-To-One) avec un
 *       champ de la cle composite. Avantage: pas de colonnes FK supplementaires,
 *       les FK deviennent les champs de la cle.
 *   </li>
 * </ul>
 *
 * <p><b>Modele metier:</b></p>
 * Une commande (Order) peut avoir plusieurs produits (Product),
 * chaque avec une ligne de commande et une quantite:
 * <pre>
 * Order:
 *   ├── OrderLine(orderId=..., productId=1, quantity=2)
 *   ├── OrderLine(orderId=..., productId=3, quantity=5)
 *   └── OrderLine(orderId=..., productId=7, quantity=1)
 * </pre>
 *
 * <p><b>Schema SQL genere:</b></p>
 * <pre>
 * CREATE TABLE order_line (
 *     order_id UUID NOT NULL,
 *     product_id BIGINT NOT NULL,
 *     quantity INTEGER NOT NULL,
 *     PRIMARY KEY (order_id, product_id),
 *     FOREIGN KEY (order_id) REFERENCES order_(id),
 *     FOREIGN KEY (product_id) REFERENCES produit(id)
 * );
 * </pre>
 *
 * <p><b>Avantages de cette approche:</b></p>
 * <ul>
 *   <li>Naturalite: une ligne de commande est logiquement identifiee par
 *       (order_id, product_id). Pourquoi ajouter un id supplementaire?
 *   </li>
 *   <li>Economie: moins de colonnes, plus compact.</li>
 *   <li>Coherence: guarantee qu'une order ne peut pas avoir deux lignes
 *       pour le meme produit.
 *   </li>
 * </ul>
 *
 * @see Order
 * @see Product
 * @see OrderLineId
 */
@Entity
public class OrderLine {

    /**
     * Cle primaire composite embarquee.
     * Constituee de (orderId, productId).
     * Voir {@link OrderLineId} pour les details.
     */
    @EmbeddedId
    private OrderLineId id;

    /**
     * Quantite commandee.
     * Obligatoire.
     */
    @Column(nullable = false)
    private int quantity;

    /**
     * La commande associee a cette ligne.
     * Relation ManyToOne vers Order.
     * @MapsId("orderId") signifie: "le champ orderId de ma cle composite
     * est alimente par la FK vers order".
     * Ainsi, pas de colonne FK supplementaire, la FK devient un champ de la cle.
     */
    @ManyToOne
    @MapsId("orderId")
    private Order order;

    /**
     * Le produit associe a cette ligne.
     * Relation ManyToOne vers Product.
     * @MapsId("productId") synchronise le champ productId de la cle
     * avec cette FK.
     */
    @ManyToOne
    @MapsId("productId")
    private Product product;

    /**
     * Constructeur par defaut (requis par JPA).
     */
    public OrderLine() {}

    /**
     * Constructeur avec donnees metier.
     * @param quantity la quantite commandee
     * @param order la commande
     * @param product le produit
     */
    public OrderLine(int quantity, Order order, Product product) {
        this.quantity = quantity;
        this.order = order;
        this.product = product;
        // Construire la cle composite a partir des relations
        this.id = new OrderLineId(order.getId(), product.getId());
    }

    // ========== ACCESSEURS ==========

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
        // Synchroniser la cle composite quand la relation change
        this.id.setOrderId(order.getId());
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        // Synchroniser la cle composite quand la relation change
        this.id.setProductId(product.getId());
    }

    // ========== METHODES UTILITAIRES ==========

    /**
     * Egalite basee sur la cle composite et la quantite.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderLine orderLine = (OrderLine) o;
        return quantity == orderLine.quantity && Objects.equals(id, orderLine.id);
    }

    /**
     * Hash basee sur la cle composite et la quantite.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, quantity);
    }

    /**
     * Representation textuelle.
     */
    @Override
    public String toString() {
        return "OrderLine{" +
                "id=" + id +
                ", quantity=" + quantity +
                '}';
    }

    /**
     * Cle primaire composite de {@link OrderLine}.
     *
     * <p><b>Role:</b> Embarquable constituant la cle composite (orderId, productId).
     * Cette classe est embedded dans OrderLine via @EmbeddedId.</p>
     *
     * <p><b>@Embeddable:</b> Permet a cette classe d'etre integree comme cle
     * dans une autre entite.</p>
     *
     * <p><b>Champs:</b></p>
     * <ul>
     *   <li>orderId: UUID du {{@link Order} contenant cette ligne.</li>
     *   <li>productId: Long du {@link Product} de cette ligne.</li>
     * </ul>
     *
     * <p><b>Contrainte unicitie:</b> Le couple (orderId, productId) est UNIQUE.
     * Pas deux lignes pour le meme (order, product).</p>
     */
    @Embeddable
    public static class OrderLineId {

        /**
         * UUID de la commande.
         * Mappe directement a la colonne order_id de la table order_line.
         */
        private UUID orderId;

        /**
         * Long de la Product.
         * Mappe directement a la colonne product_id de la table order_line.
         */
        private Long productId;

        /**
         * Constructeur par defaut (requis par JPA).
         */
        public OrderLineId() {}

        /**
         * Constructeur avec orderId et productId.
         * @param orderId l'UUID de la commande
         * @param productId le id du produit
         */
        public OrderLineId(UUID orderId, Long productId) {
            this.orderId = orderId;
            this.productId = productId;
        }

        // ========== ACCESSEURS ==========

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

        // ========== METHODES UTILITAIRES ==========

        /**
         * Egalite basee sur orderId et productId.
         */
        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            OrderLineId that = (OrderLineId) o;
            return Objects.equals(orderId, that.orderId) && Objects.equals(productId, that.productId);
        }

        /**
         * Hash basee sur orderId et productId.
         */
        @Override
        public int hashCode() {
            return Objects.hash(orderId, productId);
        }

        /**
         * Representation textuelle.
         */
        @Override
        public String toString() {
            return "OrderLineId{" +
                    "orderId=" + orderId +
                    ", productId=" + productId +
                    '}';
        }
    }
}
