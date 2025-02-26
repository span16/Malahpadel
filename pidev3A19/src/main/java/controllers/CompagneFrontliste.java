package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Compagne;
import services.CompagneService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CompagneFrontliste {

    @FXML
    private FlowPane compagnesContainer; // Conteneur des cartes

    private CompagneService compagneService;

    public CompagneFrontliste() {
        compagneService = new CompagneService();
    }

    @FXML
    public void initialize() {
        refreshCompagnes(); // Afficher les campagnes au démarrage
    }

    @FXML
    public void ajouterCampagne() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterCompagneFront.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajouter Campagne");
            stage.setScene(new Scene(root));

            // Attendre que la fenêtre soit fermée
            stage.showAndWait();

            // Rafraîchir la liste des campagnes après l'ajout
            refreshCompagnes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshCompagnes() {
        compagnesContainer.getChildren().clear(); // Effacer les cartes existantes

        CompagneService service = new CompagneService();
        try {
            List<Compagne> campagnes = service.recuperercompagne(); // Récupérer les campagnes depuis la base de données

            for (Compagne c : campagnes) {
                // Créer une carte pour chaque campagne
                VBox carteCompagne = new VBox(10);
                carteCompagne.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 10; -fx-padding: 20; "
                        + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0.1, 0, 4); "
                        + "-fx-min-width: 250; -fx-max-width: 250;"); // Taille fixe pour toutes les cartes

                // Ajouter le logo de la campagne (si disponible)
                if (c.getLogo_compagne() != null && !c.getLogo_compagne().isEmpty()) {
                    ImageView logoImageView = new ImageView(new Image(c.getLogo_compagne()));
                    logoImageView.setFitHeight(80);
                    logoImageView.setFitWidth(80);
                    logoImageView.setPreserveRatio(true);
                    carteCompagne.getChildren().add(logoImageView);
                }

                // Ajouter le nom du sponsor
                Label nomSponsorLabel = new Label("Sponsor: " + c.getNom_sponsor());
                nomSponsorLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #333;");
                carteCompagne.getChildren().add(nomSponsorLabel);

                // Ajouter les dates de début et fin
                Label datesLabel = new Label("Du " + c.getDate_debut() + " au " + c.getDate_fin());
                datesLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #666;");
                carteCompagne.getChildren().add(datesLabel);

                // Ajouter le type de marketing et le statut
                Label typeMarketingLabel = new Label("Type: " + c.getTypeMarketing() + " | Statut: " + c.getStatus());
                typeMarketingLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #666;");
                carteCompagne.getChildren().add(typeMarketingLabel);

                // Ajouter les informations sur le produit
                if (c.getProduit() != null) {
                    Label produitLabel = new Label("Produit: " + c.getProduit().getNom_produit());
                    produitLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #8dcd65; -fx-font-weight: bold;");
                    carteCompagne.getChildren().add(produitLabel);
                }

                // Ajouter la carte au conteneur
                compagnesContainer.getChildren().add(carteCompagne);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Afficher une alerte en cas d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Impossible de charger les campagnes.");
            alert.showAndWait();
        }
    }
}