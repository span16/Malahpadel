package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Compagne;
import services.CompagneService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AfficheCompagneController {

    @FXML
    private TilePane compagneTilePane;

    private CompagneService compagneService = new CompagneService();

    @FXML
    public void initialize() {
        try {
            List<Compagne> campagnes = compagneService.recuperercompagne();
            for (Compagne compagne : campagnes) {
                VBox vbox = createCompagneCard(compagne);
                compagneTilePane.getChildren().add(vbox);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private VBox createCompagneCard(Compagne compagne) {
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 5px; -fx-padding: 10px;");

        // Logo de la campagne
        ImageView logoImageView = new ImageView();
        try {
            Image image = new Image(compagne.getLogo_compagne());
            logoImageView.setImage(image);
            logoImageView.setFitHeight(100);
            logoImageView.setFitWidth(100);
        } catch (Exception e) {
            logoImageView.setImage(null);
        }

        // Informations de la campagne
        Label nomSponsorLabel = new Label("Sponsor: " + compagne.getNom_sponsor());
        Label typeMarketingLabel = new Label("Type: " + compagne.getTypeMarketing());
        Label statutLabel = new Label("Statut: " + compagne.getStatus());
        Label tarifsLabel = new Label("Tarifs: " + compagne.getTarifs());

        // Informations du produit associé
        Label produitNomLabel = new Label("Produit: " + compagne.getProduit().getNom_produit());
        Label produitCategorieLabel = new Label("Catégorie: " + compagne.getProduit().getCategorie());
        Label produitPrixLabel = new Label("Prix: " + compagne.getProduit().getPrix());

        // Image du produit
        ImageView produitImageView = new ImageView();
        try {
            Image produitImage = new Image(compagne.getProduit().getImage_produit());
            produitImageView.setImage(produitImage);
            produitImageView.setFitHeight(50);
            produitImageView.setFitWidth(50);
        } catch (Exception e) {
            produitImageView.setImage(null);
        }

        Button suppButton = new Button("Supprimer");
        suppButton.setStyle("-fx-background-color: #529fbc; -fx-text-fill: white;");
        suppButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText(null);
            alert.setContentText("Voulez-vous vraiment supprimer cette campagne ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    compagneService.supprimercompagne(compagne, compagne.getNom_sponsor());
                    compagneTilePane.getChildren().remove(vbox);
                    System.out.println("Campagne supprimée : " + compagne.getNom_sponsor());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        Button modifButton = new Button("Modifier");
        modifButton.setStyle("-fx-background-color: #6999d0; -fx-text-fill: white;");
        modifButton.setOnAction(event -> {
            if (compagne.getNom_sponsor() == null || compagne.getNom_sponsor().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom du sponsor est vide.");
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de modification");
            alert.setHeaderText(null);
            alert.setContentText("Voulez-vous vraiment modifier cette campagne ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierCompagne.fxml"));
                    Scene scene = new Scene(loader.load());

                    ModifierCompagneController controller = loader.getController();
                    controller.setCompagne(compagne); // Passer la campagne sélectionnée

                    // Définir un callback pour mettre à jour la carte après la modification
                    controller.setOnUpdateSuccess(() -> {
                        // Mettre à jour la carte de la campagne dans l'interface
                        VBox updatedCard = createCompagneCard(compagne);
                        int index = compagneTilePane.getChildren().indexOf(vbox);
                        compagneTilePane.getChildren().set(index, updatedCard);
                    });

                    Stage stage = new Stage();
                    stage.setTitle("Modifier Campagne");
                    stage.setScene(scene);
                    stage.show();

                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire de modification.");
                }
            }
        });

// Ajouter les éléments à la carte
        vbox.getChildren().addAll(
                logoImageView,
                nomSponsorLabel,
                typeMarketingLabel,
                statutLabel,
                tarifsLabel,
                produitNomLabel,
                produitCategorieLabel,
                produitPrixLabel,
                produitImageView,
                suppButton,  // Ajouter le bouton supprimer
                modifButton  // Ajouter le bouton modifier
        );

        return vbox;

    }

    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
