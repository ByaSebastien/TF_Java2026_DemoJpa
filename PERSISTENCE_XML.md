# Configuration JPA - persistence.xml

Ce document explique chaque ligne du fichier `persistence.xml`.

---

## Structure generale

Le fichier `persistence.xml` se trouve obligatoirement dans:
```
src/main/resources/META-INF/persistence.xml
```

**Pourquoi cette localisation?**
- JPA scanne la classpath pour ce chemin specifique
- C'est la norme Jakarta Persistence

---

## Contenu complet commente

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- Declaration XML standard. Toujours la meme. -->

<persistence version="3.1"
             xmlns="https://jakarta.ee/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence
      https://jakarta.ee/xml/ns/persistence/persistence_3_1.xsd">
  <!-- 
    Racine du document.
    version="3.1": Jakarta Persistence 3.1 (Hibernate 7+)
    Les namespaces permettent au parser XML de valider le format.
  -->

  <persistence-unit name="DemoJPA" transaction-type="RESOURCE_LOCAL">
    <!--
      Une "persistence-unit" est une configuration JPA independante.
      name="DemoJPA": Nom unique que vous utilisez pour creer l'EntityManagerFactory
        Exemple: Persistence.createEntityManagerFactory("DemoJPA")
      
      transaction-type="RESOURCE_LOCAL":
        - RESOURCE_LOCAL: Gestion des transactions locale (EntityManager.getTransaction())
        - JTA: Gestion par un serveur d'application (conteneur Java EE)
        
        Pour une app standalone, utilisez RESOURCE_LOCAL.
    -->

    <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
    <!--
      Le "provider" est l'implementation JPA.
      Ici: Hibernate 7.x
      
      JPA est une interface, Hibernate est une implementation.
      Autres implementations: EclipseLink, OpenJPA (rares).
    -->

    <!-- REGISTRE DES ENTITES -->
    <class>be.bstorm.entities.Category</class>
    <class>be.bstorm.entities.Order</class>
    <class>be.bstorm.entities.OrderLine</class>
    <class>be.bstorm.entities.Product</class>
    <class>be.bstorm.entities.Stock</class>
    <class>be.bstorm.entities.User</class>
    <!--
      Liste explicite des classes @Entity et @Embeddable.
      
      ALTERNATIVE (moins explicite): Utiliser auto-detection
        <property name="hibernate.archive.autodetection" value="class"/>
      
      Avantage de la liste explicite (comme ici):
      - Controle total sur les entites scannees
      - Plus clair pour les apprenants
      - Performance: pas de scanning du classpath
      
      La demo ne liste PAS Address car elle est @Embeddable et detectee
      automatiquement via User.
    -->

    <properties>
      <!--
        Les proprietes configurent Hibernate.
        Deux categories: connexion BD + comportement JPA
      -->

      <!-- ========== CONNEXION A LA BASE DE DONNEES ========== -->

      <property name="jakarta.persistence.jdbc.url" 
                value="jdbc:postgresql://localhost:5432/demojpa"/>
      <!--
        URL de connexion a PostgreSQL.
        Format: jdbc:postgresql://HOST:PORT/DATABASE
        
        A adapter:
        - localhost: Le serveur PostgreSQL
        - 5432: Port par defaut PostgreSQL
        - demojpa: Nom de la base de donnees
        
        La base "demojpa" doit exister ou etre creee:
          CREATE DATABASE demojpa;
      -->

      <property name="jakarta.persistence.jdbc.user" value="postgres"/>
      <!--
        Nom d'utilisateur PostgreSQL.
        A adapter a votre config.
      -->

      <property name="jakarta.persistence.jdbc.password" value="postgres"/>
      <!--
        Mot de passe PostgreSQL.
        A adapter a votre config.
        
        ATTENTION: Ne commitez JAMAIS les credentials en Git!
        En production, utilisez des variables d'environnement ou un gestionnaire secrets.
      -->

      <property name="jakarta.persistence.jdbc.driver" 
                value="org.postgresql.Driver"/>
      <!--
        Classe JDBC du driver PostgreSQL.
        Toujours: org.postgresql.Driver pour PostgreSQL.
        
        A adapter selon la base:
        - MySQL: com.mysql.jdbc.Driver
        - H2: org.h2.Driver
        - SQLite: org.sqlite.JDBC
      -->

      <!-- ========== COMPORTEMENT HIBERNATE ========== -->

      <property name="hibernate.show_sql" value="true"/>
      <!--
        Si true: Affiche tout SQL genere et execute en console.
        Tres utile pour debug et apprentissage!
        
        Output en console:
          Hibernate: INSERT INTO category (name) VALUES (?)
          Hibernate: [1] - (Super ORM)
        
        ATTENTION: Desactivez en production (baisse les perfs).
      -->

      <property name="hibernate.hbm2ddl.auto" value="create"/>
      <!--
        Auto-generation du schema SQL. Options:
        
        - "create": 
          ✓ DROP toutes les tables
          ✓ CREATE nouvelles tables
          Resultat: Schema vide, tables recreees (utile en dev/test)
          
        - "create-drop":
          ✓ CREATE au demarrage
          ✓ DROP a l'arret
          Resultat: BD ephemere (test unitaires)
          
        - "update":
          ✓ Cree les tables manquantes
          ✓ Ajoute les colonnes manquantes
          ✗ Ne supprime jamais rien
          Resultat: Evolution progressive du schema
          Utilisation: Dev/Prod quand le schema existe
          
        - "validate":
          ✗ Ne fait RIEN (ni create, ni update)
          ✓ Verifie que le schema existe et est coherent
          ✗ Erreur si tables manquent
          Utilisation: Prod (schema deja cree)
          
        - "none":
          ✓ Desactive completement l'auto-generation
          Utilisation: Prod avec migrations manuelles
        
        POUR LA DEMO: "create" pour repartir de zero a chaque execution.
      -->

      <!-- ========== PROPRIETES OPTIONNELLES UTILES ========== -->

      <!-- Afficher les parametres SQL (? binds)
      <property name="hibernate.use_sql_comments" value="true"/>
      <property name="hibernate.format_sql" value="true"/>
      -->
      
      <!-- Dialect SQL (optionnel, normalement auto-detecte)
      <property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQL10Dialect"/>
      -->
      
      <!-- Batch size pour inserts/updates (perf)
      <property name="hibernate.jdbc.batch_size" value="20"/>
      -->
      
      <!-- Pool de connexions (optionnel)
      <property name="hibernate.hikari.maximum.pool_size" value="10"/>
      -->

    </properties>
  </persistence-unit>

</persistence>
```

---

## Configuration minimale

Voici le minimum requis pour un app JPA:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="3.1"
             xmlns="https://jakarta.ee/xml/ns/persistence">
  <persistence-unit name="MyApp" transaction-type="RESOURCE_LOCAL">
    <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
    
    <class>com.example.User</class>
    <class>com.example.Product</class>
    
    <properties>
      <property name="jakarta.persistence.jdbc.url" 
                value="jdbc:postgresql://localhost:5432/mydb"/>
      <property name="jakarta.persistence.jdbc.user" value="postgres"/>
      <property name="jakarta.persistence.jdbc.password" value="password"/>
      <property name="jakarta.persistence.jdbc.driver" 
                value="org.postgresql.Driver"/>
      <property name="hibernate.hbm2ddl.auto" value="create"/>
    </properties>
  </persistence-unit>
</persistence>
```

---

## Utilisation dans le code

```java
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

// Charger la persistence-unit "DemoJPA"
EntityManagerFactory emf = 
    Persistence.createEntityManagerFactory("DemoJPA");

// Le nom "DemoJPA" correspond a:
// <persistence-unit name="DemoJPA" ...>
```

Si le nom ne correspond pas, exception:
```
PersistenceException: No persistence provider for EntityManager named ...
```

---

## Configurations courantes

### Configuration pour MySQL

```xml
<property name="jakarta.persistence.jdbc.url" 
          value="jdbc:mysql://localhost:3306/demojpa"/>
<property name="jakarta.persistence.jdbc.driver" 
          value="com.mysql.cj.jdbc.Driver"/>
```

### Configuration pour H2 (in-memory, test)

```xml
<property name="jakarta.persistence.jdbc.url" 
          value="jdbc:h2:mem:testdb"/>
<property name="jakarta.persistence.jdbc.driver" 
          value="org.h2.Driver"/>
<property name="hibernate.hbm2ddl.auto" value="create-drop"/>
```

### Configuration pour SQLite

```xml
<property name="jakarta.persistence.jdbc.url" 
          value="jdbc:sqlite:./app.db"/>
<property name="jakarta.persistence.jdbc.driver" 
          value="org.sqlite.JDBC"/>
```

---

## Modes hbm2ddl.auto par scenario

| Scenario | Valeur | Justification |
|----------|--------|---------------|
| **Developpement local** | `create` | Repartir de zero a chaque execution |
| **Tests unitaires** | `create-drop` | BD ephemere, nettoyage auto |
| **Developpement avec persistance** | `update` | Garder les donnees, schema evolue |
| **Production** | `validate` | Verifier le schema existant, rien d'auto |
| **Production avec migrations** | `none` | Migrations Liquibase/Flyway |

---

## Checklist de configuration

- [ ] `persistence.xml` dans `src/main/resources/META-INF/`
- [ ] `<persistence-unit name="...">` correspond au code: `Persistence.createEntityManagerFactory(...)`
- [ ] `<provider>` pointe vers l'implementation (ex: Hibernate)
- [ ] Toutes les `<class>` @Entity sont listees
- [ ] `jdbc.url`, `jdbc.user`, `jdbc.password` sont corrects
- [ ] `jdbc.driver` correspond a la base de donnees
- [ ] La base de donnees existe (ou sera creee)
- [ ] `hbm2ddl.auto` correspond au contexte (dev, test, prod)
- [ ] Les credentials ne sont PAS en dur en Git (utiliser variables env en prod)

---

## Troubleshooting

### `PersistenceException: No Persistence provider...`
**Cause:** Le nom de persistence-unit ne correspond pas au code.
```java
// Erreur:
Persistence.createEntityManagerFactory("WrongName");

// Correction:
Persistence.createEntityManagerFactory("DemoJPA");  // Match <persistence-unit name="DemoJPA">
```

### `SQLException: Connection refused`
**Cause:** PostgreSQL n'ecoute pas sur localhost:5432 ou est eteint.
```bash
# Verifier que PostgreSQL est en cours d'execution
sudo systemctl status postgresql  # Linux/Mac
Get-Service postgresql            # Windows
```

### `SQLException: FATAL: database "demojpa" does not exist`
**Cause:** La base de donnees n'existe pas.
```sql
CREATE DATABASE demojpa;
```

### `java.sql.SQLException: No suitable driver`
**Cause:** Le JAR du driver JDBC manque dans le classpath.
```xml
<!-- Ajouter dans pom.xml -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.10</version>
</dependency>
```

### Les tables ne sont pas creees
**Cause:** Probablement `hbm2ddl.auto=validate` ou `none`.
```xml
<!-- Changer en: -->
<property name="hibernate.hbm2ddl.auto" value="create"/>
```

---

## Ressources

- [Jakarta Persistence XML Spec](https://jakarta.ee/specifications/persistence/)
- [Hibernate Configuration Guide](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html)

Bon apprentissage! 🚀

