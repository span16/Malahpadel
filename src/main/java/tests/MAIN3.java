package tests;

import models.*;
import services.*;
import tools.MyDataBase;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MAIN3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TerrainService terrainService = new TerrainService();
        ÉvénementService événementService = new ÉvénementService();

        while (true) {
            afficherMenu();
            int choix = scanner.nextInt();
            scanner.nextLine(); // Consommer le retour à la ligne

            try {
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
                        ajouterÉvénement(scanner, événementService, terrainService);
                        break;
                    case 6:
                        afficherÉvénements(événementService);
                        break;
                    case 7:
                        modifierÉvénement(scanner, événementService, terrainService);
                        break;
                    case 8:
                        supprimerÉvénement(scanner, événementService);
                        break;
                    case 9:
                        System.out.println("Au revoir !");
                        return; // Quitter le programme
                    default:
                        System.out.println("Choix invalide, veuillez réessayer.");
                }
            } catch (SQLException ex) {
                System.err.println("❌ Erreur SQL : " + ex.getMessage());
            }
        }
    }

    // Affichage du menu
    private static void afficherMenu() {
        System.out.println("\n----- Menu -----");
        System.out.println("1. Ajouter un terrain");
        System.out.println("2. Afficher tous les terrains");
        System.out.println("3. Modifier un terrain");
        System.out.println("4. Supprimer un terrain");
        System.out.println("5. Ajouter un événement");
        System.out.println("6. Afficher tous les événements");
        System.out.println("7. Modifier un événement");
        System.out.println("8. Supprimer un événement");
        System.out.println("9. Quitter");
        System.out.print("Votre choix : ");
    }

    // Ajouter un terrain
    private static void ajouterTerrain(Scanner scanner, TerrainService terrainService) throws SQLException {
        System.out.println("\n🔨 Ajout d'un terrain");
        System.out.print("Nom du terrain : ");
        String nom = scanner.nextLine();
        System.out.print("Adresse du terrain : ");
        String adresse = scanner.nextLine();
        System.out.print("Prix par personne : ");
        double prix = scanner.nextDouble();
        scanner.nextLine(); // Consommer le retour à la ligne
        System.out.print("Heure d'ouverture (hh:mm:ss) : ");
        String heureOuvertureStr = scanner.nextLine();
        System.out.print("Heure de fermeture (hh:mm:ss) : ");
        String heureFermetureStr = scanner.nextLine();

        // Convertir les heures en objets Time
        java.sql.Time heureOuverture = java.sql.Time.valueOf(heureOuvertureStr);
        java.sql.Time heureFermeture = java.sql.Time.valueOf(heureFermetureStr);

        Terrain terrain = new Terrain(nom, adresse, prix, heureOuverture, heureFermeture);
        terrainService.ajouter(terrain);
    }

    // Afficher tous les terrains
    private static void afficherTerrains(TerrainService terrainService) throws SQLException {
        System.out.println("\n📋 Liste des terrains");
        List<Terrain> terrains = terrainService.recuperer();
        for (Terrain t : terrains) {
            System.out.println(t);
        }
    }

    // Modifier un terrain
    private static void modifierTerrain(Scanner scanner, TerrainService terrainService) throws SQLException {
        System.out.println("\n✏️ Modification d'un terrain");

        afficherTerrains(terrainService);

        System.out.print("👉 Entrez le nom du terrain à modifier : ");
        String ancienNom = scanner.nextLine();

        System.out.print("Nouveau nom du terrain : ");
        String nouveauNom = scanner.nextLine();
        System.out.print("Nouvelle adresse du terrain : ");
        String nouvelleAdresse = scanner.nextLine();
        System.out.print("Nouveau prix par personne : ");
        double nouveauPrix = scanner.nextDouble();
        scanner.nextLine(); // Consommer le retour à la ligne
        System.out.print("Nouvelle heure d'ouverture (hh:mm:ss) : ");
        String nouvelleHeureOuvertureStr = scanner.nextLine();
        System.out.print("Nouvelle heure de fermeture (hh:mm:ss) : ");
        String nouvelleHeureFermetureStr = scanner.nextLine();

        // Convertir les heures en objets Time
        java.sql.Time nouvelleHeureOuverture = java.sql.Time.valueOf(nouvelleHeureOuvertureStr);
        java.sql.Time nouvelleHeureFermeture = java.sql.Time.valueOf(nouvelleHeureFermetureStr);

        Terrain terrain = new Terrain(nouveauNom, nouvelleAdresse, nouveauPrix, nouvelleHeureOuverture, nouvelleHeureFermeture);
        terrainService.modifier(terrain, ancienNom);
    }

    // Supprimer un terrain
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

    // Ajouter un événement
    private static void ajouterÉvénement(Scanner scanner, ÉvénementService événementService, TerrainService terrainService) throws SQLException {
        System.out.println("\n🔨 Ajout d'un événement");

        System.out.print("Nom de l'événement : ");
        String nom = scanner.nextLine();
        System.out.print("Type de l'événement (MATCH ou TOURNOIS) : ");
        String typeInput = scanner.nextLine().toUpperCase();
        TypeV type = TypeV.valueOf(typeInput); // Assurez-vous que l'énumération est correcte
        System.out.print("Date de l'événement (format YYYY-MM-DD) : ");
        Date date = Date.valueOf(scanner.nextLine());

        // Afficher la liste des terrains disponibles
        System.out.println("\n📋 Liste des terrains disponibles :");
        List<Terrain> terrains = terrainService.recuperer();
        if (terrains.isEmpty()) {
            System.out.println("❌ Aucun terrain disponible !");
            return;
        }

        System.out.print("👉 Entrez l'ID du terrain à associer à l'événement : ");
        int terrainId = scanner.nextInt();
        scanner.nextLine(); // Consommer le retour à la ligne

        // Vérifier si le terrain est valide
        Terrain terrainChoisi = terrainService.recupererParId(terrainId);
        if (terrainChoisi == null) {
            System.out.println("❌ Terrain introuvable !");
            return;
        }

        Événement événement = new Événement(nom, type, date, terrainChoisi);
        événementService.ajouter(événement);
    }

    // Afficher tous les événements
    private static void afficherÉvénements(ÉvénementService événementService) throws SQLException {
        System.out.println("\n📋 Liste des événements");
        List<Événement> événements = événementService.recuperer();
        for (Événement e : événements) {
            System.out.println(e);
        }
    }

    // Modifier un événement
    private static void modifierÉvénement(Scanner scanner, ÉvénementService événementService, TerrainService terrainService) throws SQLException {
        System.out.println("\n✏️ Modification d'un événement");

        afficherÉvénements(événementService);

        System.out.print("👉 Entrez l'ID de l'événement à modifier : ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consommer le retour à la ligne

        System.out.print("Nouveau nom de l'événement : ");
        String nouveauNom = scanner.nextLine();

        System.out.print("Nouveau type de l'événement (MATCH ou TOURNOIS) : ");
        String nouveauTypeInput = scanner.nextLine().toUpperCase();
        TypeV nouveauType = TypeV.valueOf(nouveauTypeInput);

        System.out.print("Nouvelle date de l'événement (format YYYY-MM-DD) : ");
        Date nouvelleDate = Date.valueOf(scanner.nextLine());

        // Afficher la liste des terrains disponibles
        System.out.println("\n📋 Liste des terrains disponibles :");
        List<Terrain> terrains = terrainService.recuperer();
        System.out.print("👉 Entrez l'ID du terrain à associer à l'événement : ");
        int terrainId = scanner.nextInt();
        scanner.nextLine(); // Consommer le retour à la ligne

        Terrain terrainChoisi = terrainService.recupererParId(terrainId);
        if (terrainChoisi == null) {
            System.out.println("❌ Terrain introuvable !");
            return;
        }

        Événement événement = new Événement();
        événement.setId(id);
        événement.setNom(nouveauNom);
        événement.setType(nouveauType);
        événement.setDate(nouvelleDate);
        événement.setTerrain(terrainChoisi);

        événementService.modifier(événement, id);
        System.out.println("✅ Événement modifié avec succès.");
    }

    // Supprimer un événement
    private static void supprimerÉvénement(Scanner scanner, ÉvénementService événementService) throws SQLException {
        System.out.println("\n🗑️ Suppression d'un événement");

        afficherÉvénements(événementService);

        System.out.print("👉 Entrez l'ID de l'événement à supprimer : ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consommer le retour à la ligne

        Événement événement = new Événement();
        événement.setId(id);
        événementService.supprimer(événement);
    }
}
