package controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Terrain;
import services.TerrainService;

import java.sql.SQLException;
import java.sql.Time;
import java.util.stream.IntStream;

public class Modifierterrain {

    @FXML private TextField txtNom;
    @FXML private TextField txtAdresse;
    @FXML private TextField txtPrix;
    @FXML private ComboBox<String> comboHeureOuvertureHeure;
    @FXML private ComboBox<String> comboHeureOuvertureMinute;
    @FXML private ComboBox<String> comboHeureFermetureHeure;
    @FXML private ComboBox<String> comboHeureFermetureMinute;
    @FXML private Button btnModifier;
    @FXML private Button btnAnnuler;

    private Terrain terrainActuel;
    private TerrainService terrainService = new TerrainService();
    private Runnable onUpdateSuccess;

    public void setTerrain(Terrain terrain) {
        this.terrainActuel = terrain;
        txtNom.setText(terrain.getNom());
        txtAdresse.setText(terrain.getAdresse());
        txtPrix.setText(String.valueOf(terrain.getPrixParPersonne()));

        // Définir les heures d'ouverture et de fermeture
        if (terrain.getHeureOuverture() != null && terrain.getHeureFermeture() != null) {
            String[] heureOuverture = terrain.getHeureOuverture().toString().split(":");
            String[] heureFermeture = terrain.getHeureFermeture().toString().split(":");

            comboHeureOuvertureHeure.setValue(heureOuverture[0]);
            comboHeureOuvertureMinute.setValue(heureOuverture[1]);

            comboHeureFermetureHeure.setValue(heureFermeture[0]);
            comboHeureFermetureMinute.setValue(heureFermeture[1]);
        }
    }

    public void setOnUpdateSuccess(Runnable runnable) {
        this.onUpdateSuccess = runnable;
    }

    @FXML
    public void initialize() {
        // Remplir les ComboBoxes avec les heures et minutes
        IntStream.rangeClosed(0, 23).forEach(hour -> {
            String formattedHour = String.format("%02d", hour);
            comboHeureOuvertureHeure.getItems().add(formattedHour);
            comboHeureFermetureHeure.getItems().add(formattedHour);
        });

        IntStream.rangeClosed(0, 59).forEach(minute -> {
            String formattedMinute = String.format("%02d", minute);
            comboHeureOuvertureMinute.getItems().add(formattedMinute);
            comboHeureFermetureMinute.getItems().add(formattedMinute);
        });

        // Assigner les actions des boutons
        btnModifier.setOnAction(event -> modifierTerrain());
        btnAnnuler.setOnAction(event -> fermerFenetre());
    }

    @FXML
    private void modifierTerrain() {
        if (!validateInputs()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            // Mettre à jour les valeurs du terrain
            terrainActuel.setNom(txtNom.getText());
            terrainActuel.setAdresse(txtAdresse.getText());
            terrainActuel.setPrixParPersonne(Double.parseDouble(txtPrix.getText()));

            // Mettre à jour les horaires d'ouverture et fermeture
            String ouverture = comboHeureOuvertureHeure.getValue() + ":" + comboHeureOuvertureMinute.getValue() + ":00";
            String fermeture = comboHeureFermetureHeure.getValue() + ":" + comboHeureFermetureMinute.getValue() + ":00";

            Time heureOuverture = Time.valueOf(ouverture);
            Time heureFermeture = Time.valueOf(fermeture);

            terrainActuel.setHeureOuverture(heureOuverture);
            terrainActuel.setHeureFermeture(heureFermeture);

            // Modifier le terrain dans la base de données
            terrainService.modifier(terrainActuel, terrainActuel.getId()); // ✅ Correction ici

            // Exécuter la mise à jour si un callback est défini
            if (onUpdateSuccess != null) {
                onUpdateSuccess.run();
            }

            showAlert("Succès", "Terrain modifié avec succès !");
            fermerFenetre();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la modification du terrain : " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix doit être un nombre valide.");
        }
    }

    private boolean validateInputs() {
        return !txtNom.getText().isEmpty() &&
                !txtAdresse.getText().isEmpty() &&
                !txtPrix.getText().isEmpty() &&
                comboHeureOuvertureHeure.getValue() != null &&
                comboHeureOuvertureMinute.getValue() != null &&
                comboHeureFermetureHeure.getValue() != null &&
                comboHeureFermetureMinute.getValue() != null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) btnModifier.getScene().getWindow();
        stage.close();
    }
}
