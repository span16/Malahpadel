package tests;

import models.Compagne;
import models.Produit;
import services.CompagneService;
import services.ProduitService;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        ProduitService ps1 = new ProduitService();
        CompagneService ps2 = new CompagneService();

        // Create and add a product
        Produit p = new Produit(100, "svr", "skincare", "svr.jpg", "haute gamme", 50);
        ps1.ajouter(p);

        // Debug: Print the product ID after adding it to the database
        System.out.println("Produit ID after insertion: " + p.getId_produit());

        // Create a campaign and associate the product
        Compagne c = new Compagne();
        c.setNom_sponsor("riovaciar");
        c.setDate_debut(Date.valueOf("2003-08-11"));
        c.setDate_fin(Date.valueOf("2025-05-11"));
        c.setLogo_compagne("rio.png");
        c.setTypeMarketing("ads");  // Ensure this is set
        c.setStatus("active");
        c.setTarifs(500);
        c.setProduit(p);  // Associate the product with the campaign

        // Debug: Print the Produit object in the Compagne object
        System.out.println("Produit in Compagne: " + c.getProduit());

        try {
            ps2.ajoutercompagne(c);  // Add the campaign to the database
            System.out.println("Compagne ajoutée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la compagne : " + e.getMessage());
        }

        // Example of modifying a campaign
        try {
            Compagne modifiedCampagne = new Compagne();
            modifiedCampagne.setNom_sponsor("riovaciar2"); // Same sponsor name to find the correct campaign
            modifiedCampagne.setDate_debut(Date.valueOf("2003-09-01")); // New start date
            modifiedCampagne.setDate_fin(Date.valueOf("2025-06-11")); // New end date
            modifiedCampagne.setLogo_compagne("new_rio.png"); // New logo
            modifiedCampagne.setTypeMarketing("social media"); // New marketing type
            modifiedCampagne.setStatus("active"); // Status remains active
            modifiedCampagne.setTarifs(600); // New tarifs

            ps2.modifiercompagne(modifiedCampagne, 28); // Assuming you know the ID of the campaign to update
            System.out.println("Compagne modifiée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de la compagne : " + e.getMessage());
        }
        // Example of deleting a campaign
        try {
            ps2.supprimercompagne(c, "riovaciar");  // Delete based on the sponsor name
            System.out.println("Compagne supprimée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la compagne : " + e.getMessage());
        }

    }
}
