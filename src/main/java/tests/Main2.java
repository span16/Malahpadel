package tests;

import models.Terrain;
import services.TerrainService;

import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.Scanner;

public class Main2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TerrainService terrainService = new TerrainService();

        try {
            while (true) {
                System.out.println("\n📌 MENU GESTION DES TERRAINS 📌");
                System.out.println("1️⃣ Ajouter un terrain");
                System.out.println("2️⃣ Afficher les terrains");
                System.out.println("3️⃣ Modifier un terrain");
                System.out.println("4️⃣ Supprimer un terrain");
                System.out.println("5️⃣ Quitter");
                System.out.print("👉 Faites votre choix : ");

                int choix = scanner.nextInt();
                scanner.nextLine(); // Consommer le retour à la ligne

                switch (choix) {
                    case 1:
                        ajouterTerrain(scanner, terrainService);
                        break;
                    case 2:
                        afficherTerrains(terrainService);
                        break;
                    case 3:
                        modifierTerrain(scanner, terrainService);
                        break;
                    case 4:
                        supprimerTerrain(scanner, terrainService);
                        break;
                    case 5:
                        System.out.println("✅ Fin du programme.");
                        scanner.close();
                        return;
                    default:
                        System.out.println("❌ Choix invalide !");
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL : " + e.getMessage());
        }
    }

    // ✅ Ajouter un terrain
    private static void ajouterTerrain(Scanner scanner, TerrainService terrainService) throws SQLException {
        System.out.println("\n➕ Ajout d'un nouveau terrain");

        System.out.print("📌 Nom du terrain : ");
        String nom = scanner.nextLine();

        System.out.print("📍 Adresse du terrain : ");
        String adresse = scanner.nextLine();

        System.out.print("💰 Prix par personne : ");
        double prix = scanner.nextDouble();
        scanner.nextLine(); // Consommer le retour à la ligne

        System.out.print("🕒 Heure d'ouverture (HH:MM:SS) : ");
        Time heureOuverture = Time.valueOf(scanner.nextLine());

        System.out.print("🕒 Heure de fermeture (HH:MM:SS) : ");
        Time heureFermeture = Time.valueOf(scanner.nextLine());

        Terrain terrain = new Terrain( nom, adresse, prix, heureOuverture, heureFermeture);
        terrainService.ajouter(terrain);
    }

    // ✅ Afficher les terrains
    private static void afficherTerrains(TerrainService terrainService) throws SQLException {
        System.out.println("\n📋 Liste des terrains disponibles :");
        List<Terrain> terrains = terrainService.recuperer();

        if (terrains.isEmpty()) {
            System.out.println("❌ Aucun terrain trouvé !");
        } else {
            for (Terrain t : terrains) {
                System.out.println("🔹 ID: " + t.getId() + " | Nom: " + t.getNom() +
                        " | Adresse: " + t.getAdresse() + " | Prix: " + t.getPrix_par_personne() +
                        " | Ouverture: " + t.getHeure_ouverture() + " | Fermeture: " + t.getHeure_fermeture());
            }
        }
    }

    // ✅ Modifier un terrain
    private static void modifierTerrain(Scanner scanner, TerrainService terrainService) throws SQLException {
        System.out.println("\n✏️ Modification d'un terrain");

        afficherTerrains(terrainService);

        System.out.print("👉 Entrez l'ID du terrain à modifier : ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consommer le retour à la ligne

        System.out.print("📌 Nouveau nom : ");
        String nom = scanner.nextLine();

        System.out.print("📍 Nouvelle adresse : ");
        String adresse = scanner.nextLine();

        System.out.print("💰 Nouveau prix par personne : ");
        double prix = scanner.nextDouble();
        scanner.nextLine(); // Consommer le retour à la ligne

        System.out.print("🕒 Nouvelle heure d'ouverture (HH:MM:SS) : ");
        Time heureOuverture = Time.valueOf(scanner.nextLine());

        System.out.print("🕒 Nouvelle heure de fermeture (HH:MM:SS) : ");
        Time heureFermeture = Time.valueOf(scanner.nextLine());

        Terrain terrain = new Terrain(nom, adresse, prix, heureOuverture, heureFermeture);
        terrainService.modifier(terrain, nom);
    }

    // ✅ Supprimer un terrain
    private static void supprimerTerrain(Scanner scanner, TerrainService terrainService) throws SQLException {
        System.out.println("\n🗑️ Suppression d'un terrain");

        afficherTerrains(terrainService);

        System.out.print("👉 Entrez l'ID du terrain à supprimer : ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consommer le retour à la ligne

        Terrain terrain = new Terrain();
        terrain.setId(id);

        terrainService.supprimer(terrain);
    }
}
