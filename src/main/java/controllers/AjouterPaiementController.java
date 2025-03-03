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
    private TextField txtId_R; // id_R est une clé étrangère, et donc à saisir ici

    @FXML
    private TextField txtMontant;

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
        if (txtId_R.getText().isEmpty() || txtMontant.getText().isEmpty() || txtMethode_Paiement.getText().isEmpty() ||
                txtCommission.getText().isEmpty() || txtDescription_Paiement.getText().isEmpty() || txtDevise.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            int id_R = Integer.parseInt(txtId_R.getText().trim());
            float montant = Float.parseFloat(txtMontant.getText().trim());
            String methode_Paiement = txtMethode_Paiement.getText().trim();
            float commission = Float.parseFloat(txtCommission.getText().trim());
            String description_Paiement = txtDescription_Paiement.getText().trim();
            String devise = txtDevise.getText().trim();

            if (id_R <= 0) {
                showAlert(Alert.AlertType.ERROR, "ID invalide", "L'ID de réservation doit être un entier positif.");
                return;
            }

            if (montant <= 0) {
                showAlert(Alert.AlertType.ERROR, "Montant invalide", "Le montant doit être supérieur à zéro.");
                return;
            }

            if (commission < 0) {
                showAlert(Alert.AlertType.ERROR, "Commission invalide", "La commission ne peut pas être négative.");
                return;
            }

            // Création de l'objet paiement sans id_P, car c'est auto-incrémenté
            paiement p = new paiement(id_R, methode_Paiement, commission, description_Paiement, devise);
            paiementService.ajouter(p); // Ajout dans la base de données

            showAlert(Alert.AlertType.INFORMATION, "Succès", "✅ Paiement ajouté avec succès !");

            clearFields();

            goToAfficherPaiement();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de format", "Veuillez entrer des valeurs numériques valides pour l'ID, le montant et la commission.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "❌ Une erreur s'est produite lors de l'ajout du paiement : " + e.getMessage());
        }
    }

    @FXML
    private void supprimerPaiement() throws SQLException {
        if (txtDevise.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir le champ 'Devise'.");
            return;
        }

        String devise = txtDevise.getText().trim();

        paiementService.supprimer(devise); // Suppression par devise

        showAlert(Alert.AlertType.INFORMATION, "Succès", "✅ Paiement(s) supprimé(s) avec succès.");

        clearFields();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        txtId_R.clear();
        txtMontant.clear();
        txtMethode_Paiement.clear();
        txtCommission.clear();
        txtDescription_Paiement.clear();
        txtDevise.clear();
    }

    private void goToAfficherPaiement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPaiement.fxml"));
            Stage stage = (Stage) txtId_R.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Afficher Paiement");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la scène Afficher Paiement.");
        }
    }
}