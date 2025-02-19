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
    private TextField txtIdR; // Champ pour entrer l'ID de la réservation à supprimer

    @FXML
    void deleteReservation(ActionEvent event) {
        // Récupérer l'ID de la réservation à supprimer
        int id_R = Integer.parseInt(txtIdR.getText());

        // Créer une instance du service pour gérer les réservations
        ReservationService sr = new ReservationService();

        try {
            // Vérifier si la réservation existe avant de la supprimer
            if (sr.reservationExists(id_R)) {
                // Supprimer la réservation
                int rowsDeleted = sr.supprimer(id_R);

                // Afficher un message de succès ou d'échec
                if (rowsDeleted > 0) {
                    System.out.println("✅ Réservation supprimée avec succès !");
                } else {
                    System.out.println("⚠️ Échec de la suppression de la réservation !");
                }
            } else {
                System.out.println("⚠️ Aucune réservation trouvée avec l'ID " + id_R);
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }

        // Rediriger vers la page d'affichage des réservations après la suppression
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherReservation.fxml"));
            Parent root = loader.load();
            AfficherReservationController ar = loader.getController();
            ar.setRlist(sr.recuperer().toString()); // Mettre à jour la liste des réservations
            txtIdR.getScene().setRoot(root); // Charger la vue mise à jour
        } catch (IOException | SQLException e) {
            System.out.println("Erreur lors de la redirection vers la page d'affichage des réservations : " + e.getMessage());
        }
    }

    public void goToAjouter(ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML pour l'interface AjouterReservation
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReservation.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec le fichier chargé
            Stage stage = (Stage) txtIdR.getScene().getWindow();
            Scene scene = new Scene(root);

            // Changer la scène
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur de navigation vers AjouterReservation : " + e.getMessage());
        }
    }
}
