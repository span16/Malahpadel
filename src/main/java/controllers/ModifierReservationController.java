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
import service.ReservationService;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ModifierReservationController {

    @FXML
    private TextField txtIdR;

    @FXML
    private TextField txtIdP;

    @FXML
    private TextField txtNomC;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtDateR;

    @FXML
    private TextField txtStatus;

    private ReservationService reservationService = new ReservationService();

    @FXML
    void modifierReservation(ActionEvent event) {
        int id_R = Integer.parseInt(txtIdR.getText());
        int id_P = Integer.parseInt(txtIdP.getText());
        String nomC = txtNomC.getText();
        String email = txtEmail.getText();

        // Récupérer la chaîne de la date et la convertir en java.sql.Date
        String dateString = txtDateR.getText();
        java.util.Date utilDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Formatez selon le format attendu
            utilDate = sdf.parse(dateString); // Convertir en java.util.Date
        } catch (ParseException e) {
            System.out.println("Erreur de format de date : " + e.getMessage());
        }

        // Convertir en java.sql.Date
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        String status = txtStatus.getText();

        // Créer un objet reservation avec la date convertie
        reservation updatedReservation = new reservation(id_R, id_P, nomC, email, sqlDate, status);

        try {
            // Appeler la méthode modifier avec l'objet reservation et nomC
            int rowsUpdated = reservationService.modifier(updatedReservation, nomC);
            if (rowsUpdated > 0) {
                System.out.println("✅ Réservation mise à jour avec succès !");
            } else {
                System.out.println("⚠️ Aucune réservation trouvée avec le nom : " + nomC);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de la réservation : " + e.getMessage());
        }
    }

    public void goToSupprimer(ActionEvent event) {
        System.out.println("Le bouton a été cliqué !");

        try {
            // Charger le FXML pour la scène de suppression
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SupprimerReservation.fxml"));
            Parent root = loader.load();

            // Récupérer la fenêtre actuelle et la modifier
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible de charger la scène de suppression.");
        }
    }


    // Optionnel : méthode pour afficher des messages d'erreur (si nécessaire)
    private void showError(String title, String message) {
        // Implémentation pour afficher un message d'erreur à l'utilisateur
        System.out.println(title + ": " + message);
    }
}

