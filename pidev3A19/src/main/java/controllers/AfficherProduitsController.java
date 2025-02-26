package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Produit;
import services.ProduitService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class AfficherProduitsController {

    @FXML
    private TilePane produitTilePane;

    @FXML
    private TextField searchField;

    private ProduitService produitService = new ProduitService();
    private boolean isSortedAscending = true; // Pour alterner entre ascendant et descendant

    @FXML
    public void initialize() {
        loadProduits();

        // Ajouter un écouteur sur le champ de recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterProduits(newValue); // Appeler la méthode de filtrage
        });
    }

    @FXML
    public void loadProduits() {
        produitTilePane.getChildren().clear();
        produitTilePane.setHgap(50);
        produitTilePane.setVgap(120);

        try {
            List<Produit> produits = produitService.recuperer();
            displayProduits(produits);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayProduits(List<Produit> produits) {
        produitTilePane.getChildren().clear(); // Effacer les anciens produits

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

        // Log pour vérifier l'ordre des produits
        System.out.println("Produits affichés :");
        for (Produit produit : produits) {
            System.out.println("Nom: " + produit.getNom_produit() + ", Prix: " + produit.getPrix());
        }
    }

    private void filterProduits(String searchText) {
        try {
            List<Produit> produits = produitService.recuperer();

            // Filtrer les produits dont le nom contient le texte saisi (insensible à la casse)
            List<Produit> filteredProduits = produits.stream()
                    .filter(produit -> produit.getNom_produit().toLowerCase().contains(searchText.toLowerCase()))
                    .collect(Collectors.toList());

            // Afficher les produits filtrés
            displayProduits(filteredProduits);
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

    @FXML
    public void trierparpix(ActionEvent actionEvent) {
        try {
            List<Produit> produits = produitService.recuperer();

            if (isSortedAscending) {
                // Trier par prix ascendant
                produits.sort((p1, p2) -> Double.compare(p1.getPrix(), p2.getPrix()));
                System.out.println("Tri par prix ascendant");
            } else {
                // Trier par prix descendant
                produits.sort((p1, p2) -> Double.compare(p2.getPrix(), p1.getPrix()));
                System.out.println("Tri par prix descendant");
            }

            // Inverser l'ordre de tri pour le prochain clic
            isSortedAscending = !isSortedAscending;

            // Mettre à jour l'affichage
            displayProduits(produits);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}