# 🚀 Quick Start - En 5 minutes

Pour les impatients qui veulent juste ca marche!

---

## 1️⃣ Prerequisites (2 min)

```bash
# Verifier Java 21+
java -version

# Verifier Maven 3.8+
mvn -v

# Verifier PostgreSQL en cours d'execution
psql -V

# Creer la base (une seule fois)
createdb demojpa
```

---

## 2️⃣ Compiler (1 min)

```bash
cd C:\Users\byase\Documents\Programming\BirthList\TF_Java2026_DemoJpa

mvn clean compile
```

---

## 3️⃣ Executer (1 min)

```bash
mvn exec:java -Dexec.mainClass=be.bstorm.Main
```

---

## 4️⃣ Resultat attendu

```
=== ENTITES DETECTEES ===
[Entity-1 Category, Entity-2 Product, Entity-3 Stock, Entity-4 User, 
 Entity-5 Order, Entity-6 OrderLine, Entity-7 Address]

=== LANCEMENT DE LA TRANSACTION ===
Objet Java cree: Category{id=null, name='Super ORM'}

Hibernate: CREATE TABLE category (...)
Hibernate: INSERT INTO category (name) VALUES ('Super ORM')
...
```

✅ **Ca marche!**

---

## 📖 Ensuite?

### Pour comprendre le code:
1. Lire **INDEX.md** (vue generale)
2. Lire **GUIDE_PRATIQUE.md** (exemples)
3. Lire le code commente (see `src/main/java/be/bstorm/entities/`)

### Pour debug:
- Voir **COMMANDES.md**
- Voir **PERSISTENCE_XML.md** (Troubleshooting)

### Pour approfondir:
- Voir **ANNOTATIONS_JPA.md** (reference)
- Voir **CHECKLIST.md** (apprentissage progressif)

---

## 🆘 Ca ne marche pas?

### PostgreSQL ne marche pas?
```bash
# Demarrer
Start-Service postgresql  # Windows
sudo systemctl start postgresql  # Linux

# Verifier
Get-Service postgresql
```

### Base de donnees n'existe pas?
```bash
createdb demojpa
```

### Maven not found?
Installer Maven ou utiliser le `/mvn` bundled dans le projet:
```bash
.\.mvn\wrapper\mvn.cmd clean compile
```

### Plus de detailles?
→ Voir **COMMANDES.md** section "Troubleshooting"

---

## 💡 Tips

- Modifiez le code et relancez pour voir les changements SQL
- Utilisez `show_sql=true` dans `persistence.xml` (deja active)
- Consultez les commentaires dans le code (la meilleure doc!)

---

**Maintenant, plongez dans le code et apprenez! 🎓**

