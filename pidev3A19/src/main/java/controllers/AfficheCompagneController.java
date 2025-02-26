package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
import java.util.stream.Collectors;

public class AfficheCompagneController {

    @FXML
    private TilePane compagneTilePane;

    @FXML
    private TextField searchField;

    @FXML
    private Button sortStatusButton;

    private CompagneService compagneService = new CompagneService();
    private String currentSortStatus = "Active"; // Statut de tri actuel

    @FXML
    public void initialize() {
        loadCampagnes();

        // Ajouter un écouteur sur le champ de recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterCampagnes(newValue);
        });

        // Ajouter un écouteur sur le bouton de tri par statut
        sortStatusButton.setOnAction(event -> sortCampagnesByStatus());
    }

    private void loadCampagnes() {
        compagneTilePane.getChildren().clear();
        try {
            List<Compagne> campagnes = compagneService.recuperercompagne();
            displayCampagnes(campagnes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayCampagnes(List<Compagne> campagnes) {
        compagneTilePane.getChildren().clear();
        for (Compagne compagne : campagnes) {
            VBox vbox = createCompagneCard(compagne);
            compagneTilePane.getChildren().add(vbox);
        }
    }

    private void filterCampagnes(String searchText) {
        try {
            List<Compagne> campagnes = compagneService.recuperercompagne();

            // Filtrer les campagnes en fonction du texte saisi
            List<Compagne> filteredCampagnes = campagnes.stream()
                    .filter(compagne -> compagne.getNom_sponsor().toLowerCase().contains(searchText.toLowerCase()) ||
                            compagne.getTypeMarketing().toLowerCase().contains(searchText.toLowerCase()) ||
                            compagne.getStatus().toLowerCase().contains(searchText.toLowerCase()) ||
                            compagne.getProduit().getNom_produit().toLowerCase().contains(searchText.toLowerCase()))
                    .collect(Collectors.toList());

            // Afficher les campagnes filtrées
            displayCampagnes(filteredCampagnes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sortCampagnesByStatus() {
        try {
            List<Compagne> campagnes = compagneService.recuperercompagne();

            // Changer le statut de tri à chaque clic
            switch (currentSortStatus) {
                case "Active":
                    currentSortStatus = "Inactive";
                    break;
                case "Inactive":
                    currentSortStatus = "Pending";
                    break;
                case "Pending":
                    currentSortStatus = "Active";
                    break;
            }

            // Mettre à jour le texte du bouton
            sortStatusButton.setText("Trier par statut (" + currentSortStatus + ")");

            // Filtrer les campagnes en fonction du statut actuel
            List<Compagne> sortedCampagnes = campagnes.stream()
                    .filter(compagne -> compagne.getStatus().equalsIgnoreCase(currentSortStatus))
                    .collect(Collectors.toList());

            // Afficher les campagnes triées
            displayCampagnes(sortedCampagnes);
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
                suppButton,
                modifButton
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