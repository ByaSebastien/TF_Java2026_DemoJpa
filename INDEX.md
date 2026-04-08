# 🎓 Demo JPA/Hibernate - Pour Apprenants

Bienvenue dans cette **demo pedagogique** de JPA et Hibernate !

Ce projet illustre les **concepts fondamentaux** de la persistance Java en utilisant le mapping objet-relationnel (ORM).

---

## 🎯 Objectifs pedagogiques

A la fin de cette demo, vous comprendrez:

✅ Qu'est-ce que JPA et pourquoi c'est utile  
✅ Comment marquer une classe Java comme table persistante (@Entity)  
✅ Les differentes types de relations (1-1, 1-N, N-N)  
✅ Comment configurer une base de donnees (persistence.xml)  
✅ Le cycle complet: creer → persister → modifier → supprimer  

---

## 📚 Structure de la documentation

### 1. **README.md** (CE FICHIER)
Vue d'ensemble du projet et concepts cles.

### 2. **GUIDE_PRATIQUE.md** ⭐ **COMMENCEZ ICI**
10 exemples concrets avec code et SQL genere.
- Persister une entite
- Creer des relations (ManyToOne, OneToOne, ManyToMany)
- Rechercher et modifier des donnees
- Bonnes pratiques

### 3. **ANNOTATIONS_JPA.md**
Reference complete de toutes les annotations.
- @Entity, @Table, @Column
- @ManyToOne, @OneToOne, @ManyToMany
- @EmbeddedId, @MapsId
- Cas d'usage et exemples

### 4. **PERSISTENCE_XML.md**
Configuration detaillee du fichier persistence.xml.
- Que fait chaque propriete?
- Comment configurer PostgreSQL, MySQL, H2
- Troubleshooting des erreurs courantes

### 5. **Code source commente**
Chaque classe entite a des commentaires pedagogiques:
- `src/main/java/be/bstorm/entities/Category.java` - Entite simple
- `src/main/java/be/bstorm/entities/Product.java` - Relation ManyToOne
- `src/main/java/be/bstorm/entities/Stock.java` - Relation OneToOne
- `src/main/java/be/bstorm/entities/User.java` - @Embedded + ManyToMany
- `src/main/java/be/bstorm/entities/Address.java` - Objet valeur embedded
- `src/main/java/be/bstorm/entities/Order.java` - UUID comme PK
- `src/main/java/be/bstorm/entities/OrderLine.java` - Cle composite

---

## 🚀 Quick Start (5 minutes)

### Prerequisites
1. PostgreSQL installe et en cours d'execution
2. Maven 3.8+ et Java 21+
3. Git (pour cloner)

### Setup

```bash
# 1. Creer la base de donnees
createdb demojpa
# OU via psql:
psql -c "CREATE DATABASE demojpa;"

# 2. Compiler le projet
cd TF_Java2026_DemoJpa
mvn clean compile

# 3. Lancer la demo
mvn exec:java -Dexec.mainClass=be.bstorm.Main
```

### Resultat attendu

```
[Entity-1 Category, Entity-2 Product, Entity-3 Stock, Entity-4 User, 
 Entity-5 Order, Entity-6 OrderLine, Entity-7 Address]

=== ENTITES DETECTEES ===
...

=== LANCEMENT DE LA TRANSACTION ===
Hibernate: CREATE TABLE category (...)
Hibernate: CREATE TABLE produit (...)
Hibernate: INSERT INTO category (name) VALUES ('Super ORM')
...
```

---

## 🗂️ Architecture du projet

```
TF_Java2026_DemoJpa/
├── src/
│   ├── main/
│   │   ├── java/be/bstorm/
│   │   │   ├── Main.java                    ← Point d'entree
│   │   │   └── entities/
│   │   │       ├── Category.java            ← Entite simple
│   │   │       ├── Product.java             ← ManyToOne
│   │   │       ├── Stock.java               ← OneToOne
│   │   │       ├── User.java                ← @Embedded + ManyToMany
│   │   │       ├── Address.java             ← @Embeddable
│   │   │       ├── Order.java               ← UUID PK
│   │   │       ├── OrderLine.java           ← Cle composite
│   │   │       └── package-info.java        ← Doc du package
│   │   └── resources/META-INF/
│   │       └── persistence.xml              ← Configuration JPA
│   └── test/
├── pom.xml                                  ← Dependances Maven
├── README.md                                ← Vue generale
├── GUIDE_PRATIQUE.md                        ← 10 exemples
├── ANNOTATIONS_JPA.md                       ← Reference annotations
└── PERSISTENCE_XML.md                       ← Config detaillee
```

---

## 🔑 Concepts cles en 2 minutes

### Sans JPA (approche SQL classique)
```java
String sql = "INSERT INTO category (name) VALUES ('Gaming')";
statement.executeUpdate(sql);
// ❌ SQL brut = peu lisible, peu typesafe, repetitif
```

### Avec JPA (approche orientee objet)
```java
Category category = new Category("Gaming");
em.persist(category);
em.getTransaction().commit();
// ✅ Objet Java = plus lisible, typesafe, reutilisable
```

### Hibernate genere le SQL automatiquement
```
JPA scanne les @Entity
-> Genere les CREATE TABLE, INSERT, UPDATE, DELETE
-> Execute via le driver JDBC
-> Vous manipulez des objets, pas du SQL
```

---

## 📊 Les 7 entites de la demo

| Entite | Concepts | Exemple SQL |
|--------|----------|------------|
| **Category** | Entite simple, PK auto-increment | `CREATE TABLE category (id BIGSERIAL PK, name VARCHAR UNIQUE)` |
| **Product** | ManyToOne vers Category, renommage colonnes | `CREATE TABLE produit (id, nom, prix, category_id FK)` |
| **Stock** | OneToOne vers Product (UNIQUE FK) | `CREATE TABLE stock (id, quantity, product_id UNIQUE FK)` |
| **Address** | @Embeddable (inline, pas de table) | Colonnes integrees dans `user_` |
| **User** | @Embedded + ManyToMany | `CREATE TABLE user_ (id, email, city, street, zipcode)` |
| **Order** | UUID comme PK (pas auto-increment) | `CREATE TABLE order_ (id UUID PK)` |
| **OrderLine** | Cle composite, @EmbeddedId, @MapsId | `CREATE TABLE order_line (order_id, product_id, qty, PK(order_id, product_id))` |

---

## 🎓 Parcours pedagogique recommande

### Phase 1: Fundamentals (1h)
1. Lire **README.md** (ce fichier)
2. Executer `mvn exec:java` et observer la sortie
3. Lire le code commente de **Category.java** et **Product.java**
4. Lire **GUIDE_PRATIQUE.md** exemples 1-3

### Phase 2: Entites complexes (1h)
1. Etudier **User.java** (Embedded + ManyToMany)
2. Etudier **Stock.java** (OneToOne)
3. Lire **GUIDE_PRATIQUE.md** exemples 4-7
4. Modifier le code et relancer pour voir les changements

### Phase 3: Configuration et relations avancees (1h)
1. Lire **PERSISTENCE_XML.md** pour comprendre la config
2. Etudier **Order.java** et **OrderLine.java** (UUID, cle composite)
3. Lire **ANNOTATIONS_JPA.md** pour reference complete
4. Experimenter avec les annotations

### Phase 4: Pratique libre
1. Ajouter une nouvelle entite (ex: Avis, Review)
2. Creer des relations avec les entites existantes
3. Modifier Main.java pour persister vos entites
4. Observer le SQL genere

---

## 🛠️ Technologies utilisees

- **Java 25** - Langage de programmation
- **Maven 3.8+** - Build tool
- **JPA (Jakarta Persistence) 3.1** - Specification ORM
- **Hibernate 7.3.0** - Implementation JPA
- **PostgreSQL 14+** - Base de donnees
- **JDBC** - Driver de connexion BD

---

## ⚠️ Points d'attention

### Confusions courantes

❌ **"JPA genere automatiquement le SQL"**
✅ Hibernate (l'implementation) genere le SQL a partir des entites JPA

❌ **"@Entity suffit pour persister"**
✅ Il faut aussi `em.persist()` ou `em.getTransaction().commit()`

❌ **"@ManyToOne et @OneToMany sont la meme chose"**
✅ ManyToOne: colonne FK dans la table source
✅ OneToMany: inverse, navigation du cote cible (optionnel)

❌ **"Je peux utiliser @Embeddable sans avoir une table separee"**
✅ Correct! C'est toute l'idee de @Embeddable

### Performance

- En production, utilisez `hbm2ddl.auto=validate` (pas de generation auto)
- Utilisez des migrations (Liquibase, Flyway) pour evoluer le schema
- Indexez les colonnes frequemment recherchees
- Faites attention aux relations lazy/eager (chargement des donnees)

---

## 📖 Ressources additionnelles

### Documentation officielle
- [Jakarta Persistence Specification](https://jakarta.ee/specifications/persistence/)
- [Hibernate User Guide](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/)
- [Baeldung: Hibernate & JPA](https://www.baeldung.com/hibernate-5)

### Livres recommandes
- "Pro JPA 2" de Mike Keith
- "Hibernate in Action" - Bien que legerement ancien

### Videos
- [Hibernate Beginner Tutorial (Udemy)](https://www.udemy.com/)
- [Spring Data JPA (YouTube)](https://www.youtube.com/)

---

## 🤝 Contribution et feedback

Cette demo est conçue pour apprendre. N'hesitez pas a:
- Ajouter des commentaires dans le code
- Creer de nouvelles entites
- Partager vos questions

---

## 📝 Changelog

### v1.0 (2026-04-08)
- ✅ 7 entites avec relations variees
- ✅ Code commente pour apprentissage
- ✅ 4 documents de reference
- ✅ Exemples practiques
- ✅ Configuration PostgreSQL

---

## 🎯 Prochaines etapes

Une fois a l'aise avec cette demo:

1. **Explorez Spring Data JPA** - Interface de haut niveau au-dessus de JPA
2. **Apprenez JPQL** - Langage de requete oriente objet
3. **Testez avec H2** - BD in-memory pour les tests
4. **Integrez avec Spring Boot** - Framework populaire Java
5. **Decouvrez les migrations** - Liquibase ou Flyway

---

## 📞 Support

En cas de probleme:
1. Consultez **PERSISTENCE_XML.md** (Troubleshooting section)
2. Verifiez que PostgreSQL est en cours d'execution
3. Verifiez que les credentials dans `persistence.xml` sont corrects
4. Consultez les logs Hibernate (show_sql=true)

---

## 🚀 Bon apprentissage!

Cette demo a pour but de rendre JPA/Hibernate **comprehensible et pratique**.

Prenez votre temps, experimentez, et n'hesitez pas a modifier le code!

**Lisez d'abord:** [GUIDE_PRATIQUE.md](GUIDE_PRATIQUE.md) pour 10 exemples concrets.

Bon coding! 💻

