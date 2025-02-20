package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.Événement;
import services.ÉvénementService;
import tests.MainFX;

import java.io.IOException;
import java.sql.SQLException;

public class Afficherevnet {

    @FXML
    private TableView<Événement> tableEvenements;
    @FXML
    private TableColumn<Événement, String> colNom;
    @FXML
    private TableColumn<Événement, String> colType;
    @FXML
    private TableColumn<Événement, String> colDate;
    @FXML
    private TableColumn<Événement, String> colTerrain;
    @FXML
    private ImageView imageView; // Ajout de l'image pour le fond
    @FXML
    private Button btnModifier; // Bouton pour modifier l'événement

    private static final ObservableList<Événement> listeEvenements = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        // ✅ Affichage correct du nom du terrain
        colTerrain.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getTerrain().getNom()));

        tableEvenements.setItems(listeEvenements);

        // ✅ Chargement sécurisé de l'image
        try {
            String imagePath = getClass().getResource("/IMAGE/pngtree-cartoon-sports-equipment-green-tennis-ball-png-image_344065.jpg").toExternalForm();
            if (imagePath != null) {
                imageView.setImage(new Image(imagePath));
            } else {
                System.out.println("⚠ L'image n'a pas été trouvée !");
            }
        } catch (Exception e) {
            System.out.println("⚠ Erreur de chargement de l'image : " + e.getMessage());
        }
    }

    public static void ajouterEvenement(Événement event) {
        listeEvenements.add(event);
    }

    // ✅ Méthode pour modifier un événement sélectionné
    @FXML
    void modifierEvent() {
        Événement selectedEvent = tableEvenements.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showAlert(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner un événement à modifier.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modifierevent.fxml"));
            Parent root = loader.load();

            Modifierevent controller = loader.getController();
            controller.setÉvénement(selectedEvent);
            controller.setOnUpdateSuccess(() -> {
                tableEvenements.refresh(); // Rafraîchir la table après modification
            });

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Événement");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ Bouton retour vers la page d'ajout
    @FXML
    void retourAjouterEvent() {
        MainFX.switchToAjouterevent();
    }

    // ✅ Méthode pour afficher des alertes
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void supprimerEvent() {
        Événement selectedEvent = tableEvenements.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showAlert(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner un événement à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer l'événement " + selectedEvent.getNom() + " ?");
        ButtonType btnOui = new ButtonType("Oui");
        ButtonType btnNon = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmation.getButtonTypes().setAll(btnOui, btnNon);

        confirmation.showAndWait().ifPresent(response -> {
            if (response == btnOui) {
                try {
                    ÉvénementService événementService = new ÉvénementService();
                    événementService.supprimer(selectedEvent); // ✅ Suppression de la base de données
                    listeEvenements.remove(selectedEvent); // ✅ Suppression de la liste affichée
                    tableEvenements.refresh(); // ✅ Rafraîchir l'affichage
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement supprimé avec succès !");
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression de l'événement : " + e.getMessage());
                }
            }
        });
    }
}
