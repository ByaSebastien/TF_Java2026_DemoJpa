# 🛠️ Commandes et Troubleshooting

Guide rapide des commandes pour executer et debugger la demo.

---

## 📦 Compiler et Executer

### Avec Maven

```bash
# Compiler
mvn clean compile

# Executer
mvn exec:java -Dexec.mainClass=be.bstorm.Main

# Tout d'un coup
mvn clean compile exec:java -Dexec.mainClass=be.bstorm.Main

# Voir le SQL genere (deja active dans persistence.xml)
# Output: Hibernate: INSERT INTO ...
```

### Avec IntelliJ

1. Right-click sur `Main.java`
2. Select "Run 'Main'"
3. Voir la console pour le SQL

### Avec Eclipse

1. Right-click sur `Main.java`
2. Select "Run As" → "Java Application"
3. Voir la console

---

## 🗄️ Gerer la base de donnees PostgreSQL

### Verifier que PostgreSQL est en cours d'execution

```bash
# Linux/Mac
sudo systemctl status postgresql
sudo systemctl start postgresql    # Demarrer
sudo systemctl stop postgresql     # Arreter

# Windows (PowerShell)
Get-Service postgresql             # Voir le status
Start-Service postgresql           # Demarrer
Stop-Service postgresql            # Arreter

# OU avec pgAdmin GUI
# Ouvrir pgAdmin et verifier la connexion
```

### Creer la base de donnees demojpa

```bash
# Via createdb
createdb demojpa

# Via psql
psql -U postgres -c "CREATE DATABASE demojpa;"

# Verifier
psql -U postgres -c "\l"  # List databases
```

### Connecter a la base en ligne de commande

```bash
# Connexion par defaut
psql -U postgres -d demojpa

# Voir les tables
\dt

# Voir une table
\d+ category

# Voir les donnees
SELECT * FROM category;

# Sortir
\q
```

### Supprimer et recreer la base

```bash
# Supprimer
dropdb demojpa

# Recreer
createdb demojpa
```

---

## 🐛 Troubleshooting courantes

### Probleme 1: `PersistenceException: No Persistence provider...`

**Cause:** Le nom de persistence-unit ne correspond pas.

**Solution:**
```java
// ERREUR:
Persistence.createEntityManagerFactory("WrongName");

// BON:
Persistence.createEntityManagerFactory("DemoJPA");  // Match <persistence-unit name="DemoJPA">
```

**Verifier:** Le nom est sensible a la casse (DemoJPA ≠ demojpa)

---

### Probleme 2: `SQLException: Connection refused`

**Cause:** PostgreSQL n'est pas en cours d'execution ou n'ecoute pas sur le port.

**Solution:**
```bash
# Verifier que PostgreSQL est actif
Get-Service postgresql  # Windows
sudo systemctl status postgresql  # Linux

# Demarrer si necessaire
Start-Service postgresql  # Windows
sudo systemctl start postgresql  # Linux

# Verifier le port (par defaut 5432)
# Dans persistence.xml: jdbc:postgresql://localhost:5432/demojpa
```

---

### Probleme 3: `FATAL: database "demojpa" does not exist`

**Cause:** La base de donnees n'existe pas.

**Solution:**
```bash
createdb demojpa

# OU via psql
psql -U postgres -c "CREATE DATABASE demojpa;"

# Verifier
psql -U postgres -d demojpa
```

---

### Probleme 4: `SQLException: No suitable driver`

**Cause:** Le driver JDBC PostgreSQL manque dans le classpath.

**Solution:**
```xml
<!-- Verifier que c'est dans pom.xml -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.10</version>
</dependency>

<!-- Recharger les dependances Maven -->
<!-- Dans IntelliJ: Tools > Maven > Reload Projects -->
<!-- OU terminal: mvn clean install -->
```

---

### Probleme 5: `java.io.IOException: Unable to find a single main class...`

**Cause:** Maven ne trouve pas la main class.

**Solution:**
```bash
# Verifier le chemin exact
mvn exec:java -Dexec.mainClass=be.bstorm.Main

# S'assurer que Main.java existe
ls src/main/java/be/bstorm/Main.java

# S'assurer que Main.java a une methode main public static
```

---

### Probleme 6: `ERROR: role "postgres" does not exist`

**Cause:** L'utilisateur PostgreSQL par defaut n'existe pas.

**Solution:**
```bash
# Verifier les utilisateurs existants
psql -U postgres -c "\du"

# Creer un nouvel utilisateur
psql -U postgres -c "CREATE USER postgres WITH PASSWORD 'postgres';"
psql -U postgres -c "ALTER ROLE postgres WITH SUPERUSER;"

# OU utiliser un utilisateur existant dans persistence.xml
```

---

### Probleme 7: Les tables ne sont pas creees

**Cause:** `hbm2ddl.auto=validate` ou `none`.

**Solution:**
```xml
<!-- Dans persistence.xml, changer -->
<property name="hibernate.hbm2ddl.auto" value="create"/>  <!-- Au lieu de validate ou none -->
```

---

### Probleme 8: `org.hibernate.MappingException: Could not find a getter for ...`

**Cause:** Une classe entite manque un getter/setter.

**Solution:**
```java
@Entity
public class Product {
    private String name;
    
    // ERREUR: Getter manquant
    public void setName(String name) { this.name = name; }
    
    // CORRECTION: Ajouter le getter
    public String getName() { return name; }
}
```

---

### Probleme 9: `java.lang.IllegalArgumentException: Unknown entity: ...`

**Cause:** Une classe @Entity n'est pas enregistree dans persistence.xml.

**Solution:**
```xml
<!-- Ajouter dans persistence.xml -->
<class>be.bstorm.entities.NewEntity</class>
```

---

### Probleme 10: `ERROR: duplicate key value violates unique constraint`

**Cause:** Vous tentez d'inserer un doublon (colonne UNIQUE).

**Solution:**
```java
// ERREUR:
Category c1 = new Category("Gaming");
Category c2 = new Category("Gaming");  // Doublon!
em.persist(c1);
em.persist(c2);
em.getTransaction().commit();  // Erreur SQL!

// CORRECTION:
Category c1 = new Category("Gaming");
Category c2 = new Category("Sports");  // Nom different
em.persist(c1);
em.persist(c2);
em.getTransaction().commit();
```

---

## 📊 Verifier le schema SQL cree

### Via pgAdmin GUI
1. Ouvrir pgAdmin
2. Server → postgres → Databases → demojpa
3. Schemas → public → Tables
4. Voir les tables: category, produit, stock, etc.

### Via ligne de commande

```bash
# Connecter a la base
psql -U postgres -d demojpa

# Voir toutes les tables
\dt

# Voir la definition d'une table
\d+ category

# Voir le contenu
SELECT * FROM category;
SELECT * FROM produit;
```

### Exemple de schema cree

```sql
-- Categories
CREATE TABLE category (
    id bigserial NOT NULL,
    name character varying(50) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

-- Products
CREATE TABLE produit (
    id bigserial NOT NULL,
    nom character varying(50) NOT NULL UNIQUE,
    prix integer NOT NULL,
    description character varying(500),
    category_id bigint,
    PRIMARY KEY (id),
    FOREIGN KEY (category_id) REFERENCES category(id)
);

-- Stock
CREATE TABLE stock (
    id serial NOT NULL,
    product_id bigint UNIQUE,
    quantity integer NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (product_id) REFERENCES produit(id)
);

-- etc...
```

---

## 🔍 Debug avec les logs Hibernate

### Activer tous les logs

```xml
<!-- persistence.xml -->
<property name="hibernate.show_sql" value="true"/>
<property name="hibernate.format_sql" value="true"/>
<property name="hibernate.use_sql_comments" value="true"/>
```

### Output exemple

```
Hibernate: 
    INSERT INTO category (name) VALUES (?)
[1] - (Super ORM)

Hibernate: 
    CREATE TABLE category (
        id bigserial not null,
        name varchar(50) not null unique,
        primary key (id)
    )
```

### Logs logback (optionnel)

Creer `src/main/resources/logback.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <logger name="org.hibernate.SQL" level="DEBUG"/>
    <logger name="org.hibernate.type.descriptor.sql.BasicBinder" level="TRACE"/>

    <root level="INFO">
        <appender-ref ref="STDOUT"/>
    </root>
</configuration>
```

---

## 📝 Modification rapide pour tester

### Modifier Main.java

```java
public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DemoJPA");
        try (EntityManager em = emf.createEntityManager()) {
            
            // MODIFIER ICI POUR TESTER DIFFERENTS CAS
            
            // Cas 1: Creer une categorie
            Category cat = new Category("Gaming");
            em.getTransaction().begin();
            em.persist(cat);
            em.getTransaction().commit();
            
            // Cas 2: Creer un produit avec categorie
            // Product prod = new Product("Laptop", 1500, "Gaming laptop");
            // prod.setCategory(cat);
            // em.persist(prod);
            // em.getTransaction().commit();
            
        } finally {
            emf.close();
        }
    }
}
```

### Tester rapidement
```bash
# Compiler et executer
mvn clean compile exec:java -Dexec.mainClass=be.bstorm.Main
```

---

## 💻 IDE Configuration

### IntelliJ IDEA

1. **Compiler automatiquement:** File → Settings → Build, Execution... → Compiler → Build project automatically
2. **Maven:** File → Settings → Build, Execution... → Maven → Enable Maven
3. **Run:** Main.java → Right-click → Run
4. **Debug:** Main.java → Right-click → Debug

### Eclipse

1. **Compiler:** Project → Build Automatically
2. **Maven:** Right-click project → Configure → Convert to Maven Project
3. **Run:** Main.java → Right-click → Run As → Java Application
4. **Debug:** Main.java → Right-click → Debug As → Java Application

### VS Code + Extension Pack for Java

1. **Extension Pack:** Install "Extension Pack for Java"
2. **Run:** Ctrl+Shift+D → Run and Debug
3. **Voir la console:** Output → select "Java"

---

## 🔄 Workflow recommande

```
1. Modifier le code (entite, Main.java, etc.)
   ↓
2. Compiler: mvn clean compile
   ↓
3. Verifier les erreurs
   ↓
4. Executer: mvn exec:java
   ↓
5. Observer le SQL dans la console
   ↓
6. Verifier en base: psql -d demojpa
   ↓
7. Repeter!
```

---

## 📚 Ressources rapides

| Probleme | Resource |
|----------|----------|
| Erreur PostgreSQL | PERSISTENCE_XML.md → Troubleshooting |
| Erreur annotation | ANNOTATIONS_JPA.md |
| Erreur compilation | Verifier les imports |
| Erreur transaction | GUIDE_PRATIQUE.md |
| Erreur relation | GUIDE_PRATIQUE.md |

---

## 🆘 Besoin d'aide?

1. **Verifier les logs** (`show_sql=true`)
2. **Consulter PERSISTENCE_XML.md**
3. **Relire le code commente** (c'est la documentation!)
4. **Googler le message d'erreur exact**

Bon debugging! 🎯

