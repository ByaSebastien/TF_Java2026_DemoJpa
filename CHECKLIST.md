# ✅ Checklist Apprentissage JPA/Hibernate

Utilisez ce document pour tracker votre progression dans la demo.

---

## Phase 1: Fondamentaux (Concepts de base)

### Concepts theoriques
- [ ] Je comprends la difference entre SQL brut et JPA
- [ ] Je sais qu'une @Entity = une table SQL
- [ ] Je sais qu'@GeneratedValue auto-increment = BIGSERIAL/AUTO_INCREMENT
- [ ] Je comprends les etats des entites (DETACHED, MANAGED, TRANSIENT, REMOVED)
- [ ] Je sais qu'Hibernate est l'implementation de JPA

### Fichiers a lire
- [ ] README.md (ce projet)
- [ ] INDEX.md (vue d'ensemble)
- [ ] src/main/java/be/bstorm/Main.java (entree de la demo)
- [ ] src/main/resources/META-INF/persistence.xml (configuration)

### Code a lire
- [ ] Category.java (entite simple)
- [ ] Address.java (objet valeur embedded)

### Pratique
- [ ] J'ai lance la demo avec `mvn exec:java`
- [ ] J'ai observe le SQL genere dans la console
- [ ] J'ai compris comment persister une entite simple

---

## Phase 2: Relations simples (ManyToOne, OneToOne)

### Concepts theoriques
- [ ] Je sais la difference entre ManyToOne et OneToOne
- [ ] Je comprends que @ManyToOne ajoute une FK (pas UNIQUE)
- [ ] Je comprends que @OneToOne ajoute une FK UNIQUE
- [ ] Je sais que @OneToMany est l'inverse de @ManyToOne
- [ ] Je comprends la cardinalite (1-1, 1-N, N-N)

### Fichiers a lire
- [ ] Product.java (ManyToOne vers Category)
- [ ] Stock.java (OneToOne vers Product)
- [ ] GUIDE_PRATIQUE.md exemples 2-3

### SQL a comprendre
- [ ] Je peux lire un schema avec FK et UNIQUE
- [ ] Je comprends les contraintes FOREIGN KEY

### Pratique
- [ ] J'ai lu le code commente de Product et Stock
- [ ] J'ai compris pourquoi Stock utilise UNIQUE et Product pas
- [ ] Je pourrais creer une nouvelle entite avec @ManyToOne

---

## Phase 3: Objets embarques et relations complexes

### Concepts theoriques
- [ ] Je sais qu'@Embeddable signifie "pas de table separee"
- [ ] Je sais qu'@Embedded integre une classe embeddable dans la table
- [ ] Je comprends que @ManyToMany cree une table de jointure
- [ ] Je sais que @ManyToMany est bidirectionnel par nature

### Fichiers a lire
- [ ] User.java (@Embedded + @ManyToMany)
- [ ] GUIDE_PRATIQUE.md exemples 4-5
- [ ] ANNOTATIONS_JPA.md (@Embedded et @ManyToMany)

### SQL a comprendre
- [ ] Je peux lire une table avec colonnes embedded
- [ ] Je peux lire une table de jointure (PK composite)

### Pratique
- [ ] J'ai compris pourquoi Address est @Embeddable
- [ ] J'ai compris comment les favoris sont stockes dans user__favorites
- [ ] Je pourrais ajouter une nouvelle propriete embedded a User

---

## Phase 4: Identificateurs avances (UUID, cle composite)

### Concepts theoriques
- [ ] Je sais que UUID est genere manuellement (pas @GeneratedValue)
- [ ] Je comprends les avantages du UUID vs auto-increment
- [ ] Je sais qu'une cle composite est faite de plusieurs champs
- [ ] Je comprends @EmbeddedId et @MapsId

### Fichiers a lire
- [ ] Order.java (UUID comme PK)
- [ ] OrderLine.java (cle composite avec @EmbeddedId)
- [ ] OrderLine.OrderLineId (classe embarquable pour la cle)
- [ ] GUIDE_PRATIQUE.md exemples 6-7
- [ ] ANNOTATIONS_JPA.md (@EmbeddedId et @MapsId)

### SQL a comprendre
- [ ] Je sais que UUID PRIMARY KEY utilise le type UUID en SQL
- [ ] Je comprends les cles composites (PK multi-colonnes)
- [ ] Je comprends pourquoi @MapsId economise des colonnes FK

### Pratique
- [ ] J'ai compris pourquoi Order utilise UUID
- [ ] J'ai compris la cle composite (orderId, productId)
- [ ] Je comprends @MapsId("orderId") et @MapsId("productId")

---

## Phase 5: Configuration JPA (persistence.xml)

### Concepts theoriques
- [ ] Je sais que persistence.xml doit etre dans src/main/resources/META-INF/
- [ ] Je comprends le role de <persistence-unit>
- [ ] Je sais qu'on peut avoir plusieurs persistence-unit
- [ ] Je comprends le role du <provider> (Hibernate)

### Proprietes cles a comprendre
- [ ] jdbc.url, jdbc.user, jdbc.password
- [ ] jdbc.driver (different selon la BD)
- [ ] hibernate.show_sql (pour debug)
- [ ] hibernate.hbm2ddl.auto (create, update, validate, none)

### Fichiers a lire
- [ ] src/main/resources/META-INF/persistence.xml (ce projet)
- [ ] PERSISTENCE_XML.md (reference complete)

### Pratique
- [ ] J'ai compris pourquoi le nom de persistence-unit doit matcher le code
- [ ] J'ai change show_sql=false et observe la difference
- [ ] J'ai change hbm2ddl.auto=update et observe la difference
- [ ] Je pourrais configurer une autre base de donnees (MySQL, H2)

---

## Phase 6: Cycle de vie des entites (CRUD)

### Concepts theoriques
- [ ] Je sais qu'INSERT = persist() + commit()
- [ ] Je sais que SELECT = em.find() ou requete JPQL
- [ ] Je sais que UPDATE = setProperty() + commit()
- [ ] Je sais que DELETE = em.remove() + commit()

### Operations a maitriser
- [ ] Create: Creer et persister une entite
- [ ] Read: Recuperer une entite par id
- [ ] Update: Modifier une entite et sauvegarder
- [ ] Delete: Supprimer une entite
- [ ] Lister: Recuperer plusieurs entites

### Fichiers a lire
- [ ] GUIDE_PRATIQUE.md exemples 1, 8-10

### Pratique
- [ ] J'ai modifie Main.java pour creer une autre entite
- [ ] J'ai observe le SQL genere pour INSERT
- [ ] J'ai modifie une entite et observe l'UPDATE
- [ ] J'ai supprime une entite et observe le DELETE

---

## Phase 7: Annotations JPA (Reference complete)

### Annotations de classe
- [ ] @Entity
- [ ] @Table(name=...)
- [ ] @Embeddable

### Annotations de champ
- [ ] @Id
- [ ] @GeneratedValue(strategy=...)
- [ ] @Column(name=..., length=..., nullable=..., unique=...)
- [ ] @Embedded
- [ ] @EmbeddedId
- [ ] @Transient

### Annotations de relation
- [ ] @ManyToOne
- [ ] @OneToOne
- [ ] @ManyToMany
- [ ] @OneToMany (inverse)
- [ ] @MapsId(...)

### Fichiers a lire
- [ ] ANNOTATIONS_JPA.md (reference complete)

### Pratique
- [ ] J'ai identifie toutes les annotations dans le code
- [ ] Je pourrais expliquer le role de chaque annotation
- [ ] Je sais quelle annotation utiliser pour un cas donne

---

## Phase 8: Bonnes pratiques et cas d'usage

### Patterns a connaitre
- [ ] Try-with-resources pour fermer l'EntityManager
- [ ] Toujours persister la cible avant la source
- [ ] Deserialiser les relations avant la transaction
- [ ] Utiliser update a la place de delete+insert

### Performance
- [ ] Je sais qu'il faut desactiver show_sql en production
- [ ] Je sais qu'il faut utiliser validate a la place de update/create en production
- [ ] Je comprends lazy loading vs eager loading
- [ ] Je sais qu'il faut indexer les colonnes frequently queries

### Migrer vers la production
- [ ] J'utilise hbm2ddl.auto=validate en production
- [ ] J'utilise des migrations (Liquibase, Flyway)
- [ ] Les credentials ne sont pas en dur (variables env)
- [ ] J'utilise un pool de connexions

### Fichiers a lire
- [ ] GUIDE_PRATIQUE.md (bonnes pratiques)
- [ ] PERSISTENCE_XML.md (configurations par contexte)

---

## Phase 9: Experimentation libre

### Modifiez et testez
- [ ] J'ai ajoute une nouvelle colonne a une entite
- [ ] J'ai relance la demo et observe le changement de schema
- [ ] J'ai modifie le nom d'une colonne (via @Column(name=...))
- [ ] J'ai change le type d'une colonne

### Creer de nouvelles entites
- [ ] J'ai cree une entite Avis (Review)
- [ ] J'ai cree une relation entre Avis et Product
- [ ] J'ai persiste Avis en base
- [ ] J'ai veriez le schema cree

### Explorez les limites
- [ ] Je sais ce qui se passe quand je ne mets pas @Entity
- [ ] Je sais ce qui se passe quand je n'ai pas @Id
- [ ] Je sais ce qui se passe quand un @Column manque
- [ ] Je sais comment recuperer les erreurs Hibernate

---

## Phase 10: Prochaines etapes

### Ressources additionnelles
- [ ] J'ai lu la section "Ressources" de INDEX.md
- [ ] J'ai cherche "Spring Data JPA" (niveau suivant)
- [ ] J'ai cherche "JPQL" (langage de requete)
- [ ] J'ai explore Hibernate Query Language (HQL)

### Projets a faire
- [ ] Creer une small app avec CRUD complet
- [ ] Integrer JPA dans Spring Boot
- [ ] Ecrire des tests unitaires avec H2
- [ ] Utiliser Liquibase pour les migrations

### Documentation a lire
- [ ] [Jakarta Persistence Spec](https://jakarta.ee/specifications/persistence/)
- [ ] [Hibernate User Guide](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/)
- [ ] [Baeldung JPA tutorials](https://www.baeldung.com/jpa-hibernate)

---

## 🎓 Votre progression globale

Comptez le nombre de cases cochees:

| Phase | Cases | Status |
|-------|-------|--------|
| 1. Fondamentaux | __/13 | ⚫⚫⚫ |
| 2. Relations simples | __/12 | ⚫⚫⚫ |
| 3. Embarques & complexes | __/11 | ⚫⚫⚫ |
| 4. Identificateurs avances | __/11 | ⚫⚫⚫ |
| 5. Configuration | __/12 | ⚫⚫⚫ |
| 6. Cycle de vie CRUD | __/11 | ⚫⚫⚫ |
| 7. Annotations | __/15 | ⚫⚫⚫ |
| 8. Bonnes pratiques | __/12 | ⚫⚫⚫ |
| 9. Experimentation | __/8 | ⚫⚫⚫ |
| 10. Prochaines etapes | __/9 | ⚫⚫⚫ |
| **TOTAL** | __/104 | **Progress** |

---

## 💡 Tips pour une bonne progression

1. **Ne sautez pas les phases** - Elles progressent logiquement
2. **Lisez le code comment** - C'est la meilleure source de verite
3. **Experimentez librement** - Cassez le code, reparatez-le
4. **Observez le SQL** - Le SQL genere par Hibernate est souvent plus lisible que le code
5. **Utilisez les logs** - `show_sql=true` est votre ami

---

## 🎉 Bravo!

Une fois cette checklist completee, vous maitriserez les concepts fondamentaux de JPA/Hibernate!

Continuez vers Spring Data JPA ou un autre framework. 🚀

