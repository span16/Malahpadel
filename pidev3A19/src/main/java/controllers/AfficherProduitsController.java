package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Produit;
import services.ProduitService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherProduitsController {

    @FXML
    private TilePane produitTilePane;

    private ProduitService produitService = new ProduitService();

    @FXML
    public void initialize() {
        loadProduits();
    }

    @FXML
    public void loadProduits() {
        produitTilePane.getChildren().clear();
        produitTilePane.setHgap(50);
        produitTilePane.setVgap(120);

        try {
            List<Produit> produits = produitService.recuperer();

            for (Produit produit : produits) {
                VBox produitCard = new VBox(5);
                produitCard.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-background-color: #f4f4f4;");
                produitCard.setPrefSize(200, 250);

                // Éléments de la carte
                Label nomLabel = new Label("Nom: " + produit.getNom_produit());
                Label categorieLabel = new Label("Catégorie: " + produit.getCategorie());
                Label prixLabel = new Label("Prix: " + produit.getPrix());
                Label stockLabel = new Label("Stock: " + produit.getStock());
                Label descriptionLabel = new Label("Description: " + produit.getDescription());
                Label imageLabel = new Label("Image: " + produit.getImage_produit());

                // Boutons
                Button deleteButton = new Button("Supprimer");
                deleteButton.setStyle("-fx-background-color: #528ec6; -fx-text-fill: white;");
                deleteButton.setOnAction(event -> deleteProduit(produit));

                Button modifierButton = new Button("Modifier");
                modifierButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                modifierButton.setOnAction(event -> modifierProduit(produit));

                // Conteneur pour les boutons
                HBox buttonContainer = new HBox(10);
                buttonContainer.getChildren().addAll(deleteButton, modifierButton);

                // Ajout des éléments à la carte
                produitCard.getChildren().addAll(
                         nomLabel, categorieLabel,
                        prixLabel, stockLabel, descriptionLabel,
                        imageLabel, buttonContainer
                );

                produitTilePane.getChildren().add(produitCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteProduit(Produit produit) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce produit ?");
        alert.setContentText("Cette action est irréversible.");

        // Action lorsque l'utilisateur clique sur le bouton "OK"
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    produitService.supprimer(produit, produit.getNom_produit());
                    loadProduits();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void modifierProduit(Produit produit) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de modification");
        alert.setHeaderText("Êtes-vous sûr de vouloir modifier ce produit ?");
        alert.setContentText("Assurez-vous que toutes les informations sont correctes.");

        // Action lorsque l'utilisateur clique sur le bouton "OK"
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierProduit.fxml"));
                    Parent root = loader.load();

                    ModifierProduitController controller = loader.getController();
                    controller.initData(produit);

                    // Définir le callback de rafraîchissement
                    controller.setOnUpdateSuccess(() -> {
                        loadProduits(); // Rafraîchir après modification
                    });

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Modifier Produit");
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}