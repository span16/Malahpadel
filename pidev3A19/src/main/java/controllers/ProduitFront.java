package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Produit;

import java.io.IOException;

public class ProduitFront {

    @FXML
    private FlowPane produitsContainer;

    @FXML
    private Button frontprodajout;

    @FXML
    private void ajoutprodfront(ActionEvent event) {
        try {
            // Charger le fichier FXML du formulaire d'ajout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutProduitForm.fxml"));
            VBox form = loader.load(); // Charger l'interface

            // Configurer le contrôleur du formulaire
            AjoutProduitFormController controller = loader.getController();
            controller.setProduitFrontController(this); // Passer une référence du contrôleur actuel

            // Créer une nouvelle fenêtre (Stage) pour le formulaire
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Fenêtre modale (bloque l'interface principale)
            stage.setTitle("Ajouter un produit"); // Titre de la fenêtre
            stage.setScene(new Scene(form)); // Définir la scène
            stage.showAndWait(); // Afficher la fenêtre et attendre sa fermeture
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement du formulaire d'ajout.");
        }
    }

    public void afficherProduit(Produit produit) {
        // Créer une carte pour le produit
        VBox produitBox = new VBox(10);
        produitBox.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-padding: 10;");

        // Image du produit
        ImageView imageView = new ImageView(new Image(produit.getImage_produit()));
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        // Nom du produit
        Label nomLabel = new Label(produit.getNom_produit());
        nomLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        // Prix du produit
        Label prixLabel = new Label(String.format("Prix : %.2f €", produit.getPrix()));
        prixLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #e74c3c;");

        // Description du produit
        Label descriptionLabel = new Label("Description : " + produit.getDescription());
        descriptionLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #333;");

        // Boutons d'action
        Button modifierBtn = new Button("Modifier");
        modifierBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");

        Button supprimerBtn = new Button("Supprimer");
        supprimerBtn.setStyle("-fx-background-color: #4ca680; -fx-text-fill: white;");

        HBox boutonsBox = new HBox(10, modifierBtn, supprimerBtn);
        boutonsBox.setAlignment(Pos.CENTER);

        // Ajouter les éléments à la carte
        produitBox.getChildren().addAll(imageView, nomLabel, prixLabel, descriptionLabel, boutonsBox);

        // Ajouter la carte à la FlowPane
        produitsContainer.getChildren().add(produitBox);
    }
}