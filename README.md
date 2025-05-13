
# 🖥️ Application Java – Mallah Padel (Client Administrateur)

Cette application **JavaFX** permet aux clubs de padel de gérer localement leurs activités : **réservations, utilisateurs, plannings, produits**, avec synchronisation automatique avec la plateforme web.

## ⚙️ Fonctionnalités principales

### 🔒 Gestion des utilisateurs
- Ajout, modification, suppression d’utilisateurs
- Interface JavaFX intuitive
- Intégration avec la base de données centralisée

### 🗓️ Réservations & événements
- Réservation d’événements créés via la plateforme web
- Visualisation des créneaux disponibles
- Synchronisation avec les événements créés sur le site web
- Système de réclamation et génération de PDF

### 🛍️ Gestion des produits
- Affichage, ajout, modification et suppression de produits
- Système d’avis utilisateur local
- Génération de fichiers Excel

### 📅 Emploi du temps & équipes
- Création automatique de match-ups
- Planification des matchs
- Visualisation d'un calendrier local des équipes

### 🔗 Synchronisation & APIs
- Synchronisation temps réel avec la plateforme web Symfony
- Intégration avec des APIs externes (QR Code, météo, etc.)
- Lecture des codes-barres pour les réservations

## 🛠️ Technologies utilisées

- Java 11+
- JavaFX
- MySQL / JDBC
- Apache POI (Excel)
- iText (PDF)
- Gson / JSON
- HTTP Client pour la communication avec l'API web

## 📁 Structure du projet

```
java-admin/
├── src/
│   ├── controllers/        # Contrôleurs JavaFX
│   ├── models/             # Modèles de données (User, Reservation, Produit, etc.)
│   ├── service/            # Services (Base de données, API, PDF, Excel, etc.)
│   ├── views/              # FXML pour les interfaces utilisateur
│   └── Main.java           # Point d’entrée principal
├── lib/                    # Dépendances externes
└── README.md
```

## 🔧 Lancer le projet

1. Ouvrir le projet dans **IntelliJ IDEA** ou **NetBeans**
2. S’assurer que JavaFX est bien configuré
3. Vérifier la connexion avec la base de données distante
4. Lancer `MainFX.java`

## 🌐 Communication avec la plateforme web

L'application échange des données via des requêtes HTTP avec l'API Symfony/Flask, notamment pour :
- Les prédictions de succès d’un événement
- Les suggestions automatiques
- La mise à jour des réservations

## 👨‍💻 Auteur

Wael Mokaddem  
yassmine megbli 
chaima khiari 
rana zakraoui
oussema chakroun

## 📄 Licence

Ce projet est sous licence MIT – voir le fichier [LICENSE](LICENSE).
