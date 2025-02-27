package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.reservation;
import models.evenement; // Assurez-vous d'importer la classe Evenement
import service.ReservationService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.regex.Pattern;

public class AjouterReservationController {

    @FXML private TextField txtNombrePlaces;
    @FXML private TextField txtTypeReservation;
    @FXML private TextField txtCodeConfirmation;
    @FXML private TextField txtRemarque;
    @FXML private TextField txtEvenementId;

    // 🔔 Affiche une alerte avec un message donné
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void addReservation(ActionEvent event) {
        // Vérification des champs
        if (txtNombrePlaces.getText().isEmpty() || txtTypeReservation.getText().isEmpty() ||
                txtCodeConfirmation.getText().isEmpty() || txtRemarque.getText().isEmpty() || txtEvenementId.getText().isEmpty()) {
            showAlert("Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        int nombrePlaces, codeConfirmation, evenementId;
        try {
            nombrePlaces = Integer.parseInt(txtNombrePlaces.getText());
            codeConfirmation = Integer.parseInt(txtCodeConfirmation.getText());
            evenementId = Integer.parseInt(txtEvenementId.getText());
            if (nombrePlaces <= 0 || codeConfirmation <= 0 || evenementId <= 0) {
                showAlert("Erreur", "Les valeurs doivent être des nombres positifs.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur de Saisie", "Les identifiants et autres champs numériques doivent être valides.");
            return;
        }

        String typeReservation = txtTypeReservation.getText().trim();
        if (typeReservation.isEmpty()) {
            showAlert("Erreur de Type", "Le type de réservation ne peut pas être vide.");
            return;
        }

        String remarque = txtRemarque.getText().trim();

        // Créer l'événement avec l'ID fourni
        evenement ev = new evenement(evenementId);

        // Créer la réservation
        reservation r = new reservation(nombrePlaces, typeReservation, codeConfirmation, remarque, ev);

        ReservationService sr = new ReservationService();

        try {
            sr.ajouter(r);
            showAlert("Succès", "Réservation ajoutée avec succès !");
        } catch (SQLException e) {
            showAlert("Erreur Base de Données", "Échec de l'ajout : " + e.getMessage());
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherReservation.fxml"));
            Parent root = loader.load();

            AfficherReservationController ar = loader.getController();
            ar.loadReservations();

            txtTypeReservation.getScene().setRoot(root);
        } catch (IOException e) {
            showAlert("Erreur de Navigation", "Impossible de charger l'affichage des réservations.");
        }
    }

    public void goToModifier(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ModifierReservation.fxml")));
            ((Stage) txtTypeReservation.getScene().getWindow()).setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Erreur de Navigation", "Impossible d'accéder à la modification des réservations.");
        }
    }

    public void goToSupprimer(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/SupprimerReservation.fxml"));
            ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Erreur de Navigation", "Impossible d'accéder à la suppression des réservations.");
        }
    }

    @FXML
    public void goToPaiement(ActionEvent actionEvent) {
        System.out.println("Aller à Paiement");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPaiement.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la scène Paiement : " + e.getMessage());
        }
    }
}
