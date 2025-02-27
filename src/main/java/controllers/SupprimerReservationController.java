package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.ReservationService;

import java.io.IOException;
import java.sql.SQLException;

public class SupprimerReservationController {

    @FXML
    private TextField txtIdR;

    @FXML
    void deleteReservation(ActionEvent event) {
        int code_confirmation = Integer.parseInt(txtIdR.getText());  // Utilisation de code_confirmation pour la suppression

        ReservationService sr = new ReservationService();

        try {
            // Vérification si la réservation existe avec le code de confirmation
            if (sr.reservationExists(code_confirmation)) {
                // Suppression de la réservation par code_confirmation
                int rowsDeleted = sr.supprimer(code_confirmation);

                if (rowsDeleted > 0) {
                    System.out.println("✅ Réservation avec code confirmation " + code_confirmation + " supprimée !");
                } else {
                    System.out.println("⚠️ Échec de la suppression de la réservation !");
                }
            } else {
                System.out.println("⚠️ Aucune réservation trouvée avec le code de confirmation : " + code_confirmation);
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }

        // Rediriger vers la page d'affichage des réservations
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherReservation.fxml"));
            Parent root = loader.load();

            AfficherReservationController ar = loader.getController();
            ar.loadReservations();  // Charger toutes les réservations
            txtIdR.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur lors de la redirection vers la page d'affichage des réservations : " + e.getMessage());
        }
    }


    public void goToAjouter(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReservation.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) txtIdR.getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur de navigation vers AjouterReservation : " + e.getMessage());
        }
    }
}
