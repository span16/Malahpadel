package tests;

import models.Événement;
import models.TypeV;
import services.ÉvénementService;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ÉvénementService événementService = new ÉvénementService();

        try {
            // Création d'un nouvel événement
            Événement événement = new Événement();
            événement.setNom("maaman");
            événement.setType(TypeV.MATCH);
            événement.setDate(new Date()); // Date actuelle

            // Ajout de l'événement
            événementService.ajouter(événement);
            System.out.println("✅ Événement ajouté avec succès.");

            // Récupération et affichage des événements
            List<Événement> événements = événementService.recuperer();
            System.out.println("\n📋 Liste des événements :");
            for (Événement e : événements) {
                System.out.println(e);
            }
            String ancienNom = événement.getNom();


            // Demande de modification
            System.out.print("\n Entrez le nouveau nom de l'événement : ");
            String nouveauNom = scanner.nextLine();

            // Choix du type d'événement
            TypeV type = null;
            while (type == null) {
                System.out.print(" Choisissez le type de l'événement (MATCH ou TOURNOIS) : ");
                String typeInput = scanner.nextLine().toUpperCase();
                try {
                    type = TypeV.valueOf(typeInput);
                } catch (IllegalArgumentException e) {
                    System.out.println(" Type invalide ! Veuillez entrer 'MATCH' ou 'TOURNOIS'.");
                }
            }

            // Appliquer les nouvelles valeurs à l'événement
            événement.setNom(nouveauNom);
            événement.setType(type);

            // Modification de l'événement
            try {
                événementService.modifier(événement, ancienNom); // Passer l'ancien nom ici
                System.out.println("\n✅ Événement modifié avec succès.");
            } catch (SQLException ex) {
                System.err.println(" Erreur lors de la modification de l'événement : " + ex.getMessage());
            }

            // Suppression de l'événement
            événementService.supprimer(événement);
            System.out.println("\n Événement supprimé avec succès.");

            // Affichage des événements après suppression
            événements = événementService.recuperer();
            System.out.println("\n Liste des événements après suppression :");
            for (Événement e : événements) {
                System.out.println(e);
            }

            scanner.close();
        } catch (SQLException e) {
            System.err.println(" Erreur SQL : " + e.getMessage());
        }
    }
}
