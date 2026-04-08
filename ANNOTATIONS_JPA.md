# Annotations JPA - Reference Complete

Ce document explique chaque annotation utilisee dans la demo.

---

## Annotations de classe

### `@Entity`

Marque une classe Java comme **entite persistante** (table SQL).

**Utilisation:**
```java
@Entity
public class Product {
    // ...
}
```

**Resultat SQL:** Cree une table `product` (ou renommee via @Table).

**Points clés:**
- Hibernate scanne le classpath pour les @Entity
- Chaque @Entity doit avoir une cle primaire (@Id)
- Sans @Entity, la classe est un POJO normal (pas de table)

---

### `@Table(name="...")`

Renomme la table SQL generee (par defaut, Hibernate utilise le nom de la classe en minuscules).

**Utilisation:**
```java
@Entity
@Table(name = "produit")  // Utilise "produit" au lieu de "product"
public class Product {
    // ...
}
```

**Resultat SQL:**
```sql
CREATE TABLE produit (...)  -- et non "product"
```

**Cas d'usage:**
- Aligner avec des schemas legacys
- Eviter les mots-cles reserves (`order`, `user`, `transaction`)

---

### `@Embeddable`

Marque une classe comme **objet valeur embarquable**.
Les champs de cette classe sont integres dans la table de l'entite proprietaire (pas de table separee).

**Utilisation:**
```java
@Embeddable
public class Address {
    private String city;
    private String street;
    private String zipcode;
}
```

**Dans une entite:**
```java
@Entity
public class User {
    @Embedded
    private Address address;
}
```

**Resultat SQL:**
```sql
CREATE TABLE user_ (
    id SERIAL PRIMARY KEY,
    email VARCHAR(150),
    -- Les colonnes d'Address sont INLINE:
    city VARCHAR,
    street VARCHAR,
    zipcode VARCHAR
    -- PAS de table separee "address"
);
```

**Avantages:**
- Donnees groupees logiquement sans table supplementaire
- Meilleure performance (join economise)
- Plus lisible que des colonnes isolees

---

## Annotations de champ

### `@Id`

Indique que le champ est la **cle primaire** (PRIMARY KEY en SQL).

**Utilisation:**
```java
@Entity
public class Category {
    @Id
    private Long id;  // Cle primaire
    
    private String name;
}
```

**Points clés:**
- Obligatoire sur chaque @Entity
- Un seul @Id par classe
- Peut etre simple (Long, UUID) ou composite (@EmbeddedId)

---

### `@GeneratedValue(strategy=...)`

Specifie comment generer la cle primaire.

**Strategies disponibles:**

#### `GenerationType.IDENTITY` (Auto-increment)
```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```
**Resultat SQL:** 
- PostgreSQL: `BIGSERIAL`
- MySQL: `AUTO_INCREMENT`
- La base genere l'id automatiquement lors de l'INSERT

**Utilisation dans la demo:** Category, Product, Stock, User

#### `GenerationType.SEQUENCE`
```java
@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE)
private Long id;
```
**Resultat SQL:**
```sql
CREATE SEQUENCE seq_category;
INSERT ... VALUES (nextval('seq_category'));
```

#### `GenerationType.TABLE`
```java
@Id
@GeneratedValue(strategy = GenerationType.TABLE)
private Long id;
```
Stocke les compteurs dans une table (moins efficace).

#### Pas de @GeneratedValue (manuel)
```java
@Id
private UUID id;  // Genere manuellement dans le constructeur

public Order() {
    this.id = UUID.randomUUID();
}
```
**Utilisation dans la demo:** Order (UUID)

---

### `@Column(...)`

Personnalise la colonne SQL.

**Options principales:**

```java
@Column(
    name = "prix",           // Renomme la colonne
    length = 50,             // VARCHAR(50)
    nullable = false,        // NOT NULL
    unique = true            // UNIQUE constraint
)
private int price;
```

**Resultat SQL:**
```sql
CREATE TABLE produit (
    prix INTEGER NOT NULL UNIQUE
);
```

**Options les plus utilisees:**
- `name`: Renomme la colonne
- `length`: Max characters (VARCHAR)
- `nullable`: NOT NULL constraint
- `unique`: UNIQUE constraint
- `columnDefinition`: Definition SQL brute (ex: "INT DEFAULT 0")

---

### `@Embedded`

Utilise une classe `@Embeddable` dans l'entite.

**Utilisation:**
```java
@Entity
public class User {
    @Embedded
    private Address address;  // Les champs d'Address sont integres
}
```

**Sans @Embedded vs avec:**
```java
// SANS @Embedded: Address n'est pas embedded, juste oubliee
@Entity
public class User {
    private Address address;  // Colonne address (type complexe) - erreur!
}

// AVEC @Embedded: Address est embedded
@Entity
public class User {
    @Embedded
    private Address address;  // Colonnes city, street, zipcode
}
```

---

### `@EmbeddedId`

Utilise une classe `@Embeddable` comme **cle primaire composite**.

**Utilisation:**
```java
@Entity
public class OrderLine {
    @EmbeddedId
    private OrderLineId id;  // Cle composite
    
    private int quantity;
}

@Embeddable
public class OrderLineId {
    private UUID orderId;
    private Long productId;
}
```

**Resultat SQL:**
```sql
CREATE TABLE order_line (
    order_id UUID NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    PRIMARY KEY (order_id, product_id)
);
```

---

## Annotations de relation

### `@ManyToOne`

Relation **plusieurs vers un** (N-1).
Plusieurs entites source pointent vers une seule entite cible.

**Utilisation:**
```java
@Entity
public class Product {
    @ManyToOne
    private Category category;  // Plusieurs Product -> une Category
}
```

**Relation:**
```
Product (N) ---ManyToOne---> Category (1)
```

**Resultat SQL:**
```sql
CREATE TABLE produit (
    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT REFERENCES category(id)  -- FK automatique
);
```

**Points clés:**
- JPA ajoute une colonne FK (foreign key)
- Pas de contrainte UNIQUE sur la FK (plusieurs produits peuvent partager une categorie)

---

### `@OneToOne`

Relation **un vers un** (1-1).
Une entite source pointe vers exactement une entite cible, et vice-versa.

**Utilisation:**
```java
@Entity
public class Stock {
    @OneToOne
    private Product product;  // Un Stock <-> Un Product
}
```

**Relation:**
```
Stock (1) ---OneToOne---> Product (1)
```

**Resultat SQL:**
```sql
CREATE TABLE stock (
    id SERIAL PRIMARY KEY,
    product_id BIGINT UNIQUE REFERENCES produit(id)  -- UNIQUE = 1-1
);
```

**Points clés:**
- Difference avec @ManyToOne: la FK a une contrainte UNIQUE
- Garantit qu'un Product n'a qu'un Stock

**@OneToOne vs @ManyToOne:**
```
ManyToOne:  Product.category (N) -> Category (1)
            Plusieurs products partent de 1 category OK
            
OneToOne:   Stock.product (1) -> Product (1)
            Un seul stock pour ce produit
```

---

### `@ManyToMany`

Relation **plusieurs vers plusieurs** (N-N).
Les deux cotes peuvent avoir plusieurs entites.

**Utilisation:**
```java
@Entity
public class User {
    @ManyToMany
    private List<Product> favorites;  // Plusieurs users, plusieurs products
}
```

**Relation:**
```
User (N) <---ManyToMany---> Product (N)
       via table user__favorites
```

**Resultat SQL:**
```sql
-- Table de jointure creee automatiquement
CREATE TABLE user__favorites (
    user_id INTEGER REFERENCES user_(id),
    favorites_id BIGINT REFERENCES produit(id),
    PRIMARY KEY (user_id, favorites_id)
);
```

**Points clés:**
- Hibernate cree automatiquement une table de jointure
- Nom par defaut: `{source}_{field_name}` (ex: `user__favorites`)
- Pas besoin de classe intermediaire (comme OrderLine)

**Utilisation:**
```java
user.addFavorite(product);  // JPA insere dans user__favorites
user.removeFavorite(product);  // JPA supprime de user__favorites
```

---

### `@MapsId(...)`

Synchronise une colonne FK avec un champ d'une cle composite.

**Probleme qu'elle resout:**
Sans @MapsId, vous auriez deux colonnes FK separees:
```java
@Entity
public class OrderLine {
    @EmbeddedId
    private OrderLineId id;  // Cle composite
    
    @ManyToOne
    private Order order;  // FK SEPAREE
    
    @ManyToOne
    private Product product;  // FK SEPAREE
}
// Resultat SQL: 4 colonnes! (orderId, productId, order_id, product_id)
```

**Solution avec @MapsId:**
```java
@Entity
public class OrderLine {
    @EmbeddedId
    private OrderLineId id;  // Cle composite
    
    @ManyToOne
    @MapsId("orderId")  // Lie la FK au champ orderId de la cle
    private Order order;
    
    @ManyToOne
    @MapsId("productId")  // Lie la FK au champ productId de la cle
    private Product product;
}
// Resultat SQL: 2 colonnes (orderId, productId qui font la cle)
```

**Resultat SQL:**
```sql
CREATE TABLE order_line (
    order_id UUID NOT NULL,      -- Partie de la cle ET FK
    product_id BIGINT NOT NULL,  -- Partie de la cle ET FK
    quantity INTEGER NOT NULL,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES order_(id),
    FOREIGN KEY (product_id) REFERENCES produit(id)
);
```

---

## Autres annotations utiles

### `@OneToMany` (Inverse)

Inverse de @ManyToOne. Naviguer de la cible vers les sources.

**Exemple non utilise dans la demo (comment):**
```java
@Entity
public class Category {
    @Id
    private Long id;
    
    @OneToMany(mappedBy = "category")  // Inverse de Product.category
    private List<Product> products;
}
```

**Avantage:** Navigation dans les deux sens
```java
Category cat = em.find(Category.class, 1);
List<Product> prods = cat.getProducts();  // Tous les produits de cette categorie
```

**Points clés:**
- `mappedBy="category"` dit "Cette relation est geree par le champ 'category' dans Product"
- Pas de colonne FK supplementaire
- Optionnel (la relation fonctionne sans elle)

---

### `@Transient`

Exclut un champ de la persistance.

**Utilisation:**
```java
@Entity
public class Product {
    @Transient  // Ce champ n'est PAS persiste
    private String cachedSummary;
}
```

**Resultat SQL:** Pas de colonne pour cachedSummary

---

## Resume des annotations par type

| Annotation | Utilisation |
|-----------|------------|
| `@Entity` | Classe = table persistante |
| `@Table(name=...)` | Renomme la table SQL |
| `@Embeddable` | Classe = objet valeur integrable |
| `@Embedded` | Utilise un objet valeur |
| `@Id` | Cle primaire |
| `@GeneratedValue(strategy=...)` | Auto-generation de la PK |
| `@Column(...)` | Personnalise la colonne SQL |
| `@ManyToOne` | Relation N-1 |
| `@OneToOne` | Relation 1-1 |
| `@ManyToMany` | Relation N-N |
| `@OneToMany` | Inverse de @ManyToOne |
| `@EmbeddedId` | Cle composite |
| `@MapsId(...)` | Synchronise FK avec cle composite |
| `@Transient` | N'est pas persistee |

---

## Ressources

- [Jakarta Persistence Documentation](https://jakarta.ee/specifications/persistence/)
- [Hibernate User Guide](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html)

Bon apprentissage! 🚀

