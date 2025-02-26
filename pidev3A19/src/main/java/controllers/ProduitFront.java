package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Produit;
import services.ProduitService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ProduitFront {
    @FXML private FlowPane produitsContainer;
    @FXML private Button frontprodajout;

    @FXML
    private void ajoutprodfront(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutProduitForm.fxml"));
            VBox form = loader.load();
            AjoutProduitFormController controller = loader.getController();
            controller.setProduitFrontController(this);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Ajouter un produit");
            stage.setScene(new Scene(form));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement du formulaire d'ajout.");
        }
    }

    public void afficherProduit(Produit produit) {
        VBox produitBox = new VBox(10);
        produitBox.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-padding: 10;");
        produitBox.setUserData(produit);

        ImageView imageView = new ImageView(new Image(produit.getImage_produit()));
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        Label nomLabel = new Label(produit.getNom_produit());
        nomLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Label prixLabel = new Label(String.format("Prix : %.2f €", produit.getPrix()));
        prixLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #85a570;");

        Label descriptionLabel = new Label("Description : " + produit.getDescription());
        descriptionLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #333;");

        Button supprimerBtn = new Button("Supprimer");
        supprimerBtn.setStyle("-fx-background-color: #4ca680; -fx-text-fill: white;");
        supprimerBtn.setOnAction(event -> supprimerProduit(produit));

        HBox boutonsBox = new HBox(10, supprimerBtn);
        boutonsBox.setAlignment(Pos.CENTER);

        produitBox.getChildren().addAll(imageView, nomLabel, prixLabel, descriptionLabel, boutonsBox);
        produitBox.setOnMouseClicked(this::ouvrirModifierProduit);
        produitsContainer.getChildren().add(produitBox);
    }

    private void supprimerProduit(Produit produit) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce produit ?");
        alert.setContentText("Cette action est irréversible.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    ProduitService produitService = new ProduitService();
                    produitService.supprimer(produit, produit.getNom_produit());
                    produitsContainer.getChildren().removeIf(node -> {
                        VBox produitBox = (VBox) node;
                        Produit p = (Produit) produitBox.getUserData();
                        return p.getId_produit() == produit.getId_produit();
                    });
                    System.out.println("Produit supprimé avec succès : " + produit.getNom_produit());
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Erreur lors de la suppression du produit.");
                }
            }
        });
    }

    @FXML
    private void ouvrirModifierProduit(MouseEvent event) {
        VBox produitBox = (VBox) event.getSource();
        Produit produit = (Produit) produitBox.getUserData();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierProduitFront.fxml"));
            VBox form = loader.load();
            ModifierProduitFrontController controller = loader.getController();
            controller.setProduit(produit);
            controller.setOnUpdateSuccess(() -> {
                produitsContainer.getChildren().clear();
                ProduitService produitService = new ProduitService();
                List<Produit> produits = null;
                try {
                    produits = produitService.recuperer();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                for (Produit p : produits) {
                    afficherProduit(p);
                }
            });
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modifier un produit");
            stage.setScene(new Scene(form));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de l'interface de modification.");
        }
    }
}