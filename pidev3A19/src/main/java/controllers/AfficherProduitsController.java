package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import models.Produit;
import services.ProduitService;

import java.sql.SQLException;
import java.util.List;

public class AfficherProduitsController {

    @FXML
    private TilePane produitTilePane;

    private ProduitService produitService = new ProduitService();

    @FXML
    public void initialize() {
        loadProduits(); // Charger les produits au démarrage
    }

    @FXML
    public void loadProduits() {
        produitTilePane.getChildren().clear(); // Vider le TilePane avant de charger les nouveaux produits
        produitTilePane.setHgap(50); // Espacement horizontal entre les cartes
        produitTilePane.setVgap(120); // Espacement vertical entre les cartes

        try {
            List<Produit> produits = produitService.recuperer(); // Récupérer la liste des produits

            for (Produit produit : produits) {
                VBox produitCard = new VBox(5); // Créer une carte pour chaque produit
                produitCard.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-background-color: #f4f4f4; -fx-border-radius: 10px;");
                produitCard.setPrefSize(200, 250);

                // Ajouter les informations du produit à la carte
                Label idLabel = new Label("ID Produit: " + produit.getId_produit());
                Label nomLabel = new Label("Nom: " + produit.getNom_produit());
                Label categorieLabel = new Label("Catégorie: " + produit.getCategorie());
                Label prixLabel = new Label("Prix: " + produit.getPrix());
                Label stockLabel = new Label("Stock: " + produit.getStock());
                Label descriptionLabel = new Label("Description: " + produit.getDescription());
                Label imageLabel = new Label("Image: " + produit.getImage_produit());

                // Bouton Supprimer
                Button deleteButton = new Button("Supprimer");
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-padding: 5px 10px; -fx-font-size: 12px;");
                deleteButton.setOnAction(event -> deleteProduit(produit)); // Action pour supprimer le produit


                // Ajouter les éléments à la carte
                produitCard.getChildren().addAll(idLabel, nomLabel, categorieLabel, prixLabel, stockLabel, descriptionLabel, imageLabel, deleteButton);

                // Ajouter la carte au TilePane
                produitTilePane.getChildren().add(produitCard);
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors du chargement des produits : " + e.getMessage());
        }
    }



    private void deleteProduit(Produit produit) {
        try {
            produitService.supprimer(produit, produit.getNom_produit()); // Supprimer le produit
            loadProduits(); // Recharger les produits après suppression
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du produit : " + e.getMessage());
        }
    }


}