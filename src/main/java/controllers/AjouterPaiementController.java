package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.PaiementService;
import models.paiement;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterPaiementController {

    @FXML
    private TextField txtId_P;

    @FXML
    private TextField txtId_R;

    @FXML
    private TextField txtMontant;

    @FXML
    private TextField txtStatus_P;

    private final PaiementService paiementService = new PaiementService();

    @FXML
    private void addPaiement() {
        if (txtId_P.getText().isEmpty() || txtId_R.getText().isEmpty() || txtMontant.getText().isEmpty() || txtStatus_P.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            int id_P = Integer.parseInt(txtId_P.getText().trim());
            int id_R = Integer.parseInt(txtId_R.getText().trim());
            float montant = Float.parseFloat(txtMontant.getText().trim());
            String status_P = txtStatus_P.getText().trim();

            if (id_P <= 0 || id_R <= 0) {
                showAlert(Alert.AlertType.ERROR, "ID invalide", "Les identifiants doivent être des entiers positifs.");
                return;
            }

            if (montant <= 0) {
                showAlert(Alert.AlertType.ERROR, "Montant invalide", "Le montant doit être supérieur à zéro.");
                return;
            }

            if (status_P.length() < 3) {
                showAlert(Alert.AlertType.ERROR, "Statut invalide", "Le statut doit contenir au moins 3 caractères.");
                return;
            }

            paiement p = new paiement(id_P, id_R, montant, status_P);
            paiementService.ajouter(p);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "✅ Paiement ajouté avec succès !");

            clearFields();

            goToAfficherPaiement();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de format", "Veuillez entrer des valeurs numériques valides pour les IDs et le montant.");
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
        txtId_P.clear();
        txtId_R.clear();
        txtMontant.clear();
        txtStatus_P.clear();
    }

    private void goToAfficherPaiement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPaiement.fxml"));
            Stage stage = (Stage) txtId_P.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Afficher Paiement");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la scène Afficher Paiement.");
        }
    }
}
