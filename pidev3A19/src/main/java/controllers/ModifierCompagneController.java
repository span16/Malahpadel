package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Compagne;
import services.CompagneService;

import java.sql.Date;
import java.sql.SQLException;

public class ModifierCompagneController {

    @FXML
    private TextField nomSponsorField, dateDebutField, dateFinField, logoField, typeMarketingField, statusField, tarifsField;

    @FXML
    private Button modifierButton, annulerButton;

    private CompagneService compagneService = new CompagneService();
    private Compagne compagne;  // Campagne sélectionnée
    private Runnable onUpdateSuccess; // Callback

    public void setCompagne(Compagne compagne) {
        this.compagne = compagne;
        nomSponsorField.setText(compagne.getNom_sponsor());
        dateDebutField.setText(compagne.getDate_debut().toString());
        dateFinField.setText(compagne.getDate_fin().toString());
        logoField.setText(compagne.getLogo_compagne());
        typeMarketingField.setText(compagne.getTypeMarketing());
        statusField.setText(compagne.getStatus());
        tarifsField.setText(String.valueOf(compagne.getTarifs()));
    }

    public void setOnUpdateSuccess(Runnable runnable) {
        this.onUpdateSuccess = runnable;
    }

    @FXML
    private void initialize() {
        modifierButton.setOnAction(event -> modifierCompagne());
        annulerButton.setOnAction(event -> fermerFenetre());
    }

    private void modifierCompagne() {
        try {
            compagne.setNom_sponsor(nomSponsorField.getText());
            compagne.setDate_debut(Date.valueOf(dateDebutField.getText()));
            compagne.setDate_fin(Date.valueOf(dateFinField.getText()));
            compagne.setLogo_compagne(logoField.getText());
            compagne.setTypeMarketing(typeMarketingField.getText());
            compagne.setStatus(statusField.getText());
            compagne.setTarifs(Float.parseFloat(tarifsField.getText()));

            compagneService.modifiercompagne(compagne, compagne.getId_compagne());

            showAlert(Alert.AlertType.INFORMATION, "Succès", "La campagne a été modifiée !");
            fermerFenetre();

            // Déclencher le callback
            if (onUpdateSuccess != null) {
                onUpdateSuccess.run();
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la modification !");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Format de date incorrect !");
        }
    }

    private void fermerFenetre() {
        Stage stage = (Stage) modifierButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}