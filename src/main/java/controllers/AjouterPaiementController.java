package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.PaiementService;
import models.paiement;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterPaiementController {

    @FXML
    private TextField txtMethode_Paiement;

    @FXML
    private TextField txtCommission;

    @FXML
    private TextField txtDescription_Paiement;

    @FXML
    private TextField txtDevise;

    private final PaiementService paiementService = new PaiementService();

    @FXML
    private void addPaiement() {
        if (txtMethode_Paiement.getText().isEmpty() || txtCommission.getText().isEmpty() ||
                txtDescription_Paiement.getText().isEmpty() || txtDevise.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            String methode_Paiement = txtMethode_Paiement.getText().trim();
            float commission = Float.parseFloat(txtCommission.getText().trim());
            String description_Paiement = txtDescription_Paiement.getText().trim();
            String devise = txtDevise.getText().trim();

            if (commission < 0) {
                showAlert(Alert.AlertType.ERROR, "Commission invalide", "La commission ne peut pas être négative.");
                return;
            }

            // Création de l'objet paiement sans id_R, car c'est auto-incrémenté
            paiement p = new paiement(methode_Paiement, commission, description_Paiement, devise);
            paiementService.ajouter(p); // Ajout dans la base de données

            showAlert(Alert.AlertType.INFORMATION, "Succès", "✅ Paiement ajouté avec succès !");

            clearFields();

            goToAfficherPaiement();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de format", "Veuillez entrer des valeurs numériques valides pour la commission.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "❌ Une erreur s'est produite lors de l'ajout du paiement : " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        txtMethode_Paiement.clear();
        txtCommission.clear();
        txtDescription_Paiement.clear();
        txtDevise.clear();
    }

    private void goToAfficherPaiement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPaiement.fxml"));
            Stage stage = (Stage) txtMethode_Paiement.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Afficher Paiement");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la scène Afficher Paiement.");
        }
    }
}