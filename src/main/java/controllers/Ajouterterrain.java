package controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import models.Terrain;
import services.TerrainService;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.util.stream.IntStream;

public class Ajouterterrain {

    @FXML private TextField txtNom;
    @FXML private TextField txtAdresse;
    @FXML private TextField txtPrix;
    @FXML private ComboBox<String> comboHeureOuvertureHeure;
    @FXML private ComboBox<String> comboHeureOuvertureMinute;
    @FXML private ComboBox<String> comboHeureFermetureHeure;
    @FXML private ComboBox<String> comboHeureFermetureMinute;

    private final TerrainService terrainService = new TerrainService();

    @FXML
    public void initialize() {
        initComboBoxes();
    }

    private void initComboBoxes() {
        IntStream.rangeClosed(0, 23).forEach(hour -> comboHeureOuvertureHeure.getItems().add(String.format("%02d", hour)));
        IntStream.rangeClosed(0, 59).forEach(minute -> comboHeureOuvertureMinute.getItems().add(String.format("%02d", minute)));
        comboHeureFermetureHeure.setItems(FXCollections.observableArrayList(comboHeureOuvertureHeure.getItems()));
        comboHeureFermetureMinute.setItems(FXCollections.observableArrayList(comboHeureOuvertureMinute.getItems()));
    }

    @FXML
    void addTerrain(ActionEvent event) {
        // ✅ Vérification des entrées utilisateur
        if (!validateInputs()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            String nom = txtNom.getText().trim();
            String adresse = txtAdresse.getText().trim();
            double prix = Double.parseDouble(txtPrix.getText().trim());

            String ouverture = comboHeureOuvertureHeure.getValue() + ":" + comboHeureOuvertureMinute.getValue() + ":00";
            String fermeture = comboHeureFermetureHeure.getValue() + ":" + comboHeureFermetureMinute.getValue() + ":00";

            Time heureOuverture = Time.valueOf(ouverture);
            Time heureFermeture = Time.valueOf(fermeture);

            Terrain terrain = new Terrain(nom, adresse, prix, heureOuverture, heureFermeture);

            terrainService.ajouter(terrain);
            System.out.println("✅ Terrain ajouté avec succès.");

            // ✅ Ajout immédiat dans la liste d'affichage
            Afficherterrain.ajouterTerrain(terrain);

            // ✅ Afficher une alerte pour confirmation
            showAlert("Succès", "Terrain ajouté avec succès !");

            // ✅ Redirection automatique vers Afficherterrain.fxml
            switchScene("/Afficherterrain.fxml", event);

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix doit être un nombre valide.");
        } catch (SQLException | IOException e) {
            System.err.println("❌ Erreur lors de l'ajout du terrain : " + e.getMessage());
            showAlert("Erreur", "Erreur lors de l'ajout du terrain : " + e.getMessage());
        }
    }

    private boolean validateInputs() {
        return txtNom.getText() != null && !txtNom.getText().trim().isEmpty() &&
                txtAdresse.getText() != null && !txtAdresse.getText().trim().isEmpty() &&
                txtPrix.getText() != null && !txtPrix.getText().trim().isEmpty() &&
                comboHeureOuvertureHeure.getValue() != null &&
                comboHeureOuvertureMinute.getValue() != null &&
                comboHeureFermetureHeure.getValue() != null &&
                comboHeureFermetureMinute.getValue() != null;
    }

    private void switchScene(String fxmlFile, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        Stage stage = (Stage) txtNom.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Liste des terrains");
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
