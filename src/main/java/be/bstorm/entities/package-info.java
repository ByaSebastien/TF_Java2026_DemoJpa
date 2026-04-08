/**
 * Entites JPA du modele metier.
 *
 * <h2>Vue d'ensemble</h2>
 *
 * <p>Ce package regroupe toutes les classes marquees {@code @Entity} ou {@code @Embeddable}.
 * Chaque classe represente soit une table SQL (Entity), soit un objet valeur embed
 * dans d'autres tables (Embeddable).</p>
 *
 * <h2>Entites (Tables)</h2>
 *
 * <ul>
 *   <li><b>{@link be.bstorm.entities.Category}:</b> Categories de produits.
 *       Schema simple: id + name.</li>
 *   <li><b>{@link be.bstorm.entities.Product}:</b> Produits vendables.
 *       Relation N-1 vers Category.</li>
 *   <li><b>{@link be.bstorm.entities.Stock}:</b> Niveau de stock par produit.
 *       Relation 1-1 vers Product.</li>
 *   <li><b>{@link be.bstorm.entities.User}:</b> Utilisateurs de l'app.
 *       Embed {@link Address}, ManyToMany avec Product (favoris).</li>
 *   <li><b>{@link be.bstorm.entities.Order}:</b> Commandes.
 *       Identifiee par UUID au lieu d'auto-increment.</li>
 *   <li><b>{@link be.bstorm.entities.OrderLine}:</b> Lignes de commande.
 *       Cle composite (orderId, productId) avec @EmbeddedId.</li>
 * </ul>
 *
 * <h2>Embeddables (Objets Valeur)</h2>
 *
 * <ul>
 *   <li><b>{@link be.bstorm.entities.Address}:</b> Adresse integree dans User.
 *       Pas de table separee, colonnes inline.</li>
 *   <li><b>{@link be.bstorm.entities.OrderLine.OrderLineId}:</b> Cle composite
 *       de OrderLine. Embedded dans OrderLine via @EmbeddedId.</li>
 * </ul>
 *
 * <h2>Diagramme des relations</h2>
 *
 * <pre>
 * Category (1)
 *     ▲
 *     │ @ManyToOne
 *     │
 *  Product (N) ──────────────────┐
 *     │ @OneToOne                 │ @ManyToMany
 *     ▼                           ▼
 *  Stock (1)                  User
 *                              ├─ @Embedded
 *                              │   └─ Address
 *                              └─ List<Product> favorites
 *
 * Order (1)
 *     │ @OneToMany
 *     ▼
 * OrderLine (N) ◄─────────────── Product (N)
 *   (cle composite:
 *    orderId, productId)
 * </pre>
 *
 * <h2>Concepts pedagogiques</h2>
 *
 * <h3>1. Cles primaires</h3>
 * <ul>
 *   <li><b>Auto-increment:</b> Category, Product, Stock, User (GenerationType.IDENTITY)</li>
 *   <li><b>UUID:</b> Order (genere manuellement, pas @GeneratedValue)</li>
 *   <li><b>Composite:</b> OrderLine (@EmbeddedId avec OrderLineId)</li>
 * </ul>
 *
 * <h3>2. Relations</h3>
 * <ul>
 *   <li><b>ManyToOne:</b> Product -> Category</li>
 *   <li><b>OneToOne:</b> Stock -> Product</li>
 *   <li><b>ManyToMany:</b> User ↔ Product (favoris)</li>
 *   <li><b>OneToMany:</b> Order -> OrderLine (relation inverse)</li>
 * </ul>
 *
 * <h3>3. Types particuliers</h3>
 * <ul>
 *   <li><b>Embeddable:</b> Address (inline dans User)</li>
 *   <li><b>EmbeddedId:</b> OrderLineId (cle composite)</li>
 *   <li><b>Renommage:</b> Product (table "produit"), User (table "user_"), Order (table "order_")</li>
 * </ul>
 *
 * <h2>Annotations principales utilisees</h2>
 *
 * <ul>
 *   <li><b>@Entity:</b> Marque une classe comme table persistante</li>
 *   <li><b>@Embeddable:</b> Classe integrable dans d'autres entites</li>
 *   <li><b>@Table(name=...):</b> Renomme la table SQL</li>
 *   <li><b>@Id:</b> Indique la cle primaire</li>
 *   <li><b>@GeneratedValue(strategy=...):</b> Auto-generation (IDENTITY, UUID, etc.)</li>
 *   <li><b>@Column(...):</b> Personnalise la colonne (name, length, nullable, unique)</li>
 *   <li><b>@Embedded / @Embeddable:</b> Objet valeur inline</li>
 *   <li><b>@ManyToOne:</b> Relation N-1</li>
 *   <li><b>@OneToOne:</b> Relation 1-1</li>
 *   <li><b>@ManyToMany:</b> Relation N-N avec table de jointure auto</li>
 *   <li><b>@EmbeddedId:</b> Cle composite embedded</li>
 *   <li><b>@MapsId(...):</b> Synchronise une FK avec un champ de cle composite</li>
 * </ul>
 *
 * <h2>Flux d'apprentissage recommande</h2>
 *
 * <ol>
 *   <li>Commencer par {@link be.bstorm.entities.Category}: entite simple, cle auto-increment</li>
 *   <li>Etudier {@link be.bstorm.entities.Product}: relation ManyToOne, colonnes renommees</li>
 *   <li>Voir {@link be.bstorm.entities.Stock}: relation OneToOne, différence avec ManyToOne</li>
 *   <li>Explorer {@link be.bstorm.entities.Address}: Embeddable, donnees inline</li>
 *   <li>Comprendre {@link be.bstorm.entities.User}: Embedded + ManyToMany + table de jointure</li>
 *   <li>Decouvrir {@link be.bstorm.entities.Order}: UUID comme PK</li>
 *   <li>Maitriser {@link be.bstorm.entities.OrderLine}: cle composite, @EmbeddedId, @MapsId</li>
 * </ol>
 *
 * <h2>Ressources SQL generees</h2>
 *
 * Lors de l'execution avec {@code hbm2ddl.auto=create}, Hibernate genere:
 *
 * <pre>
 * CREATE TABLE category (id BIGSERIAL PRIMARY KEY, name VARCHAR(50) NOT NULL UNIQUE);
 * CREATE TABLE produit (id BIGSERIAL PRIMARY KEY, nom VARCHAR(50) NOT NULL UNIQUE, 
 *                       prix INTEGER NOT NULL, description VARCHAR(500),
 *                       category_id BIGINT REFERENCES category(id));
 * CREATE TABLE stock (id SERIAL PRIMARY KEY, quantity INTEGER NOT NULL,
 *                     product_id BIGINT UNIQUE REFERENCES produit(id));
 * CREATE TABLE user_ (id SERIAL PRIMARY KEY, email VARCHAR(150) NOT NULL UNIQUE,
 *                     city VARCHAR, street VARCHAR, zipcode VARCHAR);
 * CREATE TABLE user__favorites (user_id INTEGER REFERENCES user_(id),
 *                               favorites_id BIGINT REFERENCES produit(id),
 *                               PRIMARY KEY (user_id, favorites_id));
 * CREATE TABLE order_ (id UUID PRIMARY KEY);
 * CREATE TABLE order_line (order_id UUID NOT NULL, product_id BIGINT NOT NULL,
 *                          quantity INTEGER NOT NULL,
 *                          PRIMARY KEY (order_id, product_id),
 *                          FOREIGN KEY (order_id) REFERENCES order_(id),
 *                          FOREIGN KEY (product_id) REFERENCES produit(id));
 * </pre>
 *
 * @see be.bstorm.Main
 */
package be.bstorm.entities;

