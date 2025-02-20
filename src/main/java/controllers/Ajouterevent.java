package controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    @FXML
    public void initialize() {
        initTypeVComboBox();
        initTerrainComboBox();
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
                    return comboTerrain.getItems().stream().filter(t -> t.getNom().equals(string)).findFirst().orElse(null);
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

        if (nom.isEmpty() || type == null || localDate == null || terrain == null) {
            System.out.println("Veuillez remplir tous les champs.");
            return;
        }

        Date date = java.sql.Date.valueOf(localDate);
        Événement evenement = new Événement(nom, type, date, terrain);

        ÉvénementService serviceÉvénement = new ÉvénementService();
        try {
            serviceÉvénement.ajouter(evenement);
            System.out.println("Événement ajouté avec succès.");

            // ✅ Ajout immédiat dans la liste d'affichage
            Afficherevnet.ajouterEvenement(evenement);

            // ✅ Afficher une alerte pour confirmation
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Ajout réussi");
            alert.setContentText("Événement ajouté avec succès !");
            alert.showAndWait();

            // ✅ Redirection automatique vers Afficherevent.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Afficherevent.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) txtNom.getScene().getWindow(); // Obtenir la fenêtre actuelle
            stage.setScene(new Scene(root)); // Changer de scène
            stage.setTitle("Liste des événements");
            stage.show();

        } catch (IOException | SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'événement : " + e.getMessage());
        }
    }
}
