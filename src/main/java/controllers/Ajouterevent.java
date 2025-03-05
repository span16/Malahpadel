package controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.Événement;
import models.Terrain;
import models.TypeV;
import services.ÉvénementService;
import services.TerrainService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;

public class Ajouterevent {

    @FXML
    private TextField txtNom;
    @FXML
    private ComboBox<TypeV> comboType;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<Terrain> comboTerrain;
    // Nouveau champ pour le lien de l'image
    @FXML
    private TextField txtImageUrl;

    @FXML
    public void initialize() {
        initTypeVComboBox();
        initTerrainComboBox();
        // Désactiver les dates passées dans le DatePicker
        datePicker.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });
    }

    private void initTypeVComboBox() {
        comboType.setItems(FXCollections.observableArrayList(TypeV.values()));
    }

    private void initTerrainComboBox() {
        TerrainService terrainService = new TerrainService();
        try {
            comboTerrain.setItems(FXCollections.observableArrayList(terrainService.recuperer()));
            comboTerrain.setConverter(new StringConverter<Terrain>() {
                @Override
                public String toString(Terrain terrain) {
                    return terrain != null ? terrain.getNom() : "";
                }
                @Override
                public Terrain fromString(String string) {
                    return comboTerrain.getItems().stream()
                            .filter(t -> t.getNom().equals(string))
                            .findFirst().orElse(null);
                }
            });
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des terrains : " + e.getMessage());
        }
    }

    @FXML
    void addEvenement(ActionEvent event) {
        String nom = txtNom.getText();
        TypeV type = comboType.getValue();
        LocalDate localDate = datePicker.getValue();
        Terrain terrain = comboTerrain.getValue();
        String imageUrl = txtImageUrl.getText();

        // Vérifier que tous les champs sont remplis
        if (nom.isEmpty() || type == null || localDate == null || terrain == null || imageUrl.isEmpty()) {
            System.out.println("Veuillez remplir tous les champs.");
            showAlert("Champs incomplets", "Veuillez remplir tous les champs avant de valider.");
            return;
        }

        // Vérifier que la date n'est pas antérieure à aujourd'hui
        if (localDate.isBefore(LocalDate.now())) {
            System.out.println("La date choisie est dans le passé.");
            showAlert("Date invalide", "Vous ne pouvez pas choisir une date antérieure à aujourd'hui.");
            return;
        }

        // Conversion de la LocalDate en java.sql.Date
        Date date = java.sql.Date.valueOf(localDate);
        // Créer l'événement avec l'image
        Événement evenement = new Événement(nom, type, date, terrain, imageUrl);

        ÉvénementService serviceÉvénement = new ÉvénementService();
        try {
            serviceÉvénement.ajouter(evenement);
            System.out.println("Événement ajouté avec succès.");

            // Ajout immédiat dans la liste d'affichage (si vous utilisez cette méthode dans votre interface)
            Afficherevnet.ajouterEvenement(evenement);

            // Afficher une alerte pour confirmation
            showAlert("Ajout réussi", "Événement ajouté avec succès !");

            // Redirection automatique vers Afficherevent.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Afficherevent.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) txtNom.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des événements");
            stage.show();

        } catch (IOException | SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'événement : " + e.getMessage());
            showAlert("Erreur", "Impossible d'ajouter l'événement : " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
