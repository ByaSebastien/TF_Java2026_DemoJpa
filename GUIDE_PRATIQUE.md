# Guide Pratique - Demo JPA/Hibernate

Ce guide donne des exemples concrets pour travailler avec les entites de la demo.

---

## 1. Persister une simple entite (Category)

**Scenario:** Creer une nouvelle categorie et la sauvegarder en base.

```java
// 1. Creer l'objet en memoire
Category gaming = new Category("Gaming");

// 2. Ouvrir une transaction
EntityManager em = emf.createEntityManager();
em.getTransaction().begin();

// 3. Persister l'objet
em.persist(gaming);

// 4. Valider (commit)
em.getTransaction().commit();

// L'objet est maintenant en base, avec un id genere
System.out.println(gaming.getId());  // Ex: 1

// 5. Cleanup
em.close();
```

**SQL genere:**
```sql
INSERT INTO category (name) VALUES ('Gaming');
```

---

## 2. Creer une entite avec relation ManyToOne (Product)

**Scenario:** Ajouter un produit a une categorie existante.

```java
// 1. Creer la categorie et le produit
Category electronics = new Category("Electronics");
Product laptop = new Product("Laptop Gamer", 1500, "15\" RTX 4060");

// 2. Assigner la categorie au produit
laptop.setCategory(electronics);

// 3. Persister dans une transaction
EntityManager em = emf.createEntityManager();
em.getTransaction().begin();

// JPA est assez intelligent pour persister les deux en bon ordre
em.persist(electronics);  // Persiste la categorie d'abord
em.persist(laptop);       // Puis le produit avec la FK

em.getTransaction().commit();
em.close();
```

**SQL genere:**
```sql
INSERT INTO category (name) VALUES ('Electronics');
-- Hibernate retient l'id genere, ex: id=2
INSERT INTO produit (nom, prix, description, category_id) 
  VALUES ('Laptop Gamer', 1500, '15" RTX 4060', 2);
```

---

## 3. OneToOne: Associer un Stock a un Product

**Scenario:** Chaque produit a un seul niveau de stock.

```java
Category electronics = new Category("Electronics");
Product phone = new Product("iPhone 15", 999, "Flagship Apple");
phone.setCategory(electronics);

// Creer un stock pour ce produit
Stock stock = new Stock(50, phone);  // 50 unites en stock

EntityManager em = emf.createEntityManager();
em.getTransaction().begin();

em.persist(electronics);
em.persist(phone);
em.persist(stock);  // Cree la relation OneToOne

em.getTransaction().commit();
em.close();
```

**SQL genere:**
```sql
-- Tables creees et donnees inserees
INSERT INTO category (name) VALUES ('Electronics');
INSERT INTO produit (nom, prix, description, category_id) 
  VALUES ('iPhone 15', 999, 'Flagship Apple', 3);
INSERT INTO stock (quantity, product_id) 
  VALUES (50, 4);  -- product_id est UNIQUE (OneToOne)
```

**Difference OneToOne vs ManyToOne:**
- **ManyToOne:** Plusieurs Product peuvent partager une meme Category
  - `product1.category_id = 1` et `product2.category_id = 1` OK
- **OneToOne:** Un Product <-> Un Stock seul
  - `stock1.product_id = 4` et `stock2.product_id = 4` FORBIDDEN (UNIQUE constraint)

---

## 4. Embedded: Adresse integree dans User

**Scenario:** Creer un utilisateur avec une adresse (inline, pas de table separee).

```java
// 1. Creer une adresse
Address address = new Address("Paris", "123 Rue de la Paix", "75001");

// 2. Creer un user avec cette adresse
User user = new User("alice@example.com", address);

// 3. Persister
EntityManager em = emf.createEntityManager();
em.getTransaction().begin();

em.persist(user);

em.getTransaction().commit();
em.close();
```

**SQL genere:**
```sql
-- PAS de table separee pour Address
INSERT INTO user_ (email, city, street, zipcode) 
  VALUES ('alice@example.com', 'Paris', '123 Rue de la Paix', '75001');
-- Les colonnes d'Address sont INLINE dans user_
```

---

## 5. ManyToMany: Favoris d'un utilisateur

**Scenario:** Un utilisateur marque des produits comme favoris.

```java
// Entites deja persistees
User user = em.find(User.class, 1);
Product laptop = em.find(Product.class, 4);
Product phone = em.find(Product.class, 5);

// Ajouter aux favoris
user.addFavorite(laptop);
user.addFavorite(phone);

EntityManager em = emf.createEntityManager();
em.getTransaction().begin();

// Pas besoin de persist(), favoris est une liste modifiee
em.getTransaction().commit();

em.close();
```

**SQL genere:**
```sql
-- Table de jointure creee par JPA
INSERT INTO user__favorites (user_id, favorites_id) 
  VALUES (1, 4);  -- User 1 favorise Product 4
INSERT INTO user__favorites (user_id, favorites_id) 
  VALUES (1, 5);  -- User 1 favorise Product 5
```

**Recuperer les favoris:**
```java
User user = em.find(User.class, 1);
List<Product> favorites = user.getFavorites();
favorites.forEach(p -> System.out.println(p.getName()));
// Output:
// Laptop Gamer
// iPhone 15
```

---

## 6. UUID comme cle primaire (Order)

**Scenario:** Creer une commande identifiee par UUID.

```java
// 1. Creer une commande
Order order = new Order();  // UUID auto-genere dans le constructeur

System.out.println(order.getId());
// Output: "550e8400-e29b-41d4-a716-446655440000"

// 2. Persister
EntityManager em = emf.createEntityManager();
em.getTransaction().begin();

em.persist(order);

em.getTransaction().commit();
em.close();
```

**SQL genere:**
```sql
INSERT INTO order_ (id) 
  VALUES ('550e8400-e29b-41d4-a716-446655440000');
```

**Avantages du UUID:**
- Unique au niveau mondial (probabilite collision negligeable)
- Generable hors-ligne (pas besoin de consulter la base)
- Plus securise (expose moins la structure interne)

---

## 7. Cle composite (OrderLine)

**Scenario:** Ajouter des lignes a une commande (plusieurs produits).

```java
// Entites
Order order = new Order();
Product laptop = em.find(Product.class, 4);
Product phone = em.find(Product.class, 5);

// 1. Creer les lignes de commande
OrderLine line1 = new OrderLine(2, order, laptop);    // 2x laptop
OrderLine line2 = new OrderLine(1, order, phone);     // 1x phone

// 2. Persister
EntityManager em = emf.createEntityManager();
em.getTransaction().begin();

em.persist(order);
em.persist(line1);
em.persist(line2);

em.getTransaction().commit();
em.close();
```

**SQL genere:**
```sql
-- Ordre est insere
INSERT INTO order_ (id) VALUES ('...');

-- Lignes inseres avec cle composite
INSERT INTO order_line (order_id, product_id, quantity) 
  VALUES ('...', 4, 2);
INSERT INTO order_line (order_id, product_id, quantity) 
  VALUES ('...', 5, 1);
```

**Acces a la cle composite:**
```java
OrderLine line = em.find(OrderLine.class, 
    new OrderLine.OrderLineId(order.getId(), product.getId()));

System.out.println(line.getQuantity());  // 2
```

---

## 8. Modifier une entite persistee

**Scenario:** Mettre a jour le prix d'un produit.

```java
// 1. Recuperer le produit
Product product = em.find(Product.class, 4);

// 2. Modifier (l'objet est automatiquement MANAGED par JPA)
product.setPrice(1200);

// 3. La transaction enregistre le changement
em.getTransaction().begin();
// ... JPA detecte le changement automatiquement
em.getTransaction().commit();

em.close();
```

**SQL genere:**
```sql
UPDATE produit SET prix = 1200 WHERE id = 4;
```

---

## 9. Rechercher une entite par id

**Scenario:** Recuperer une categorie par son id.

```java
EntityManager em = emf.createEntityManager();

// Recherche par PK
Category category = em.find(Category.class, 1);

if (category != null) {
    System.out.println(category.getName());  // "Gaming"
} else {
    System.out.println("Categorie non trouvee");
}

em.close();
```

**SQL genere:**
```sql
SELECT ... FROM category WHERE id = 1;
```

---

## 10. Supprimer une entite

**Scenario:** Supprimer une categorie.

```java
EntityManager em = emf.createEntityManager();
em.getTransaction().begin();

// 1. Recuperer l'entite
Category category = em.find(Category.class, 1);

// 2. Supprimer
if (category != null) {
    em.remove(category);
}

// 3. Commit
em.getTransaction().commit();

em.close();
```

**SQL genere:**
```sql
DELETE FROM category WHERE id = 1;
```

---

## Bonnes pratiques

### 1. Toujours fermer les ressources
```java
EntityManager em = emf.createEntityManager();
try {
    // ...
} finally {
    em.close();
}

// OU avec try-with-resources (Java 7+)
try (EntityManager em = emf.createEntityManager()) {
    // ...
}
```

### 2. Persister les relations d'abord
Si vous avez une relation, persistez la cible avant la source:
```java
em.persist(category);   // Cible
em.persist(product);    // Source (depend de category)
```

### 3. Verifier les logs Hibernate
Activez `hibernate.show_sql=true` dans persistence.xml pour voir le SQL genere.

### 4. Comprendre les etats des entites
- **DETACHED:** Objet Java normal, pas en base, pas geree par JPA
- **MANAGED:** En base et geree par JPA (les changements sont tracking)
- **REMOVED:** Marque pour suppression
- **TRANSIENT:** Jamais persistee

---

## Prochaines etapes

1. **Testez les modifications:** Changez les valeurs et relancez
2. **Explorez les relations:** Naviguez d'une entite a l'autre
3. **Ecrivez des requetes JPQL:** `SELECT p FROM Product p WHERE p.price > 500`
4. **Utilisez des Repositories:** Creez des classes pour encapsuler les operations CRUD

Bon apprentissage! 🚀

