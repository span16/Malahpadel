package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.reservation;
import models.Evenement; // Assurez-vous d'importer la classe Evenement
import service.ReservationService;

import java.io.IOException;
import java.sql.SQLException;

public class ModifierReservationController {

    @FXML
    private TextField txtIdR;

    @FXML
    private TextField txtNombrePlaces;

    @FXML
    private TextField txtTypeReservation;

    @FXML
    private TextField txtCodeConfirmation;

    @FXML
    private TextField txtRemarque;

    @FXML
    private TextField txtEvenementId;

    private ReservationService reservationService = new ReservationService();

    /*@FXML
    void modifierReservation(ActionEvent event) {
        // Récupération des valeurs saisies
        int id_R = Integer.parseInt(txtIdR.getText());  // L'ID de la réservation (auto-incrémenté)
        int nombrePlaces = Integer.parseInt(txtNombrePlaces.getText());
        String typeReservation = txtTypeReservation.getText();
        int codeConfirmation = Integer.parseInt(txtCodeConfirmation.getText());
        String remarque = txtRemarque.getText();

        // Récupérer l'ID de l'événement
        int evenementId = Integer.parseInt(txtEvenementId.getText());

        // Créer l'objet evenement à partir de l'ID
        evenement ev = new evenement(evenementId);

        // Créer la réservation avec les nouvelles valeurs
        reservation updatedReservation = new reservation(id_R, nombrePlaces, typeReservation, codeConfirmation, remarque, ev);

        // Appeler la méthode modifier de ReservationService
        int rowsUpdated = reservationService.modifier(updatedReservation);
        if (rowsUpdated > 0) {
            System.out.println("✅ Réservation mise à jour avec succès !");
        } else {
            System.out.println("⚠️ Aucune réservation trouvée avec l'ID : " + id_R);
        }
    }

    */public void goToSupprimer(ActionEvent event) {
        System.out.println("Le bouton a été cliqué !");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SupprimerReservation.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible de charger la scène de suppression.");
        }
    }

    private void showError(String title, String message) {
        System.out.println(title + ": " + message);
    }
}
