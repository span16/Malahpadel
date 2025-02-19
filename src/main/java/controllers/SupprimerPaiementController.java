package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.PaiementService;

import java.io.IOException;
import java.sql.SQLException;

public class SupprimerPaiementController {

    @FXML
    private TextField txtIdP; // Champ pour entrer l'ID du paiement à supprimer

    private final PaiementService paiementService = new PaiementService();

        @FXML
        void deletePaiement(ActionEvent event) {
            try {
                int id_P = Integer.parseInt(txtIdP.getText()); // Récupère l'ID depuis le champ texte
                PaiementService ps = new PaiementService();    // Crée une instance du service

                int rowsDeleted = ps.supprimer(id_P);          // Appelle la méthode supprimer
                if (rowsDeleted > 0) {
                    System.out.println("✅ Paiement supprimé avec succès !");
                } else {
                    System.out.println("⚠️ Aucun paiement trouvé avec l'ID " + id_P);
                }

            } catch (NumberFormatException e) {
                System.out.println("❌ Veuillez entrer un ID valide !");
            }
        }


        // ✅ Méthode pour naviguer vers l'interface d'ajout de paiement
    @FXML
    void goToAjouterPaiement(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPaiement.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) txtIdP.getScene().getWindow();
            stage.setScene(new Scene(root)); // Charger la scène d'ajout
            stage.show();

            System.out.println("➡️ Navigation vers AjouterPaiement réussie !");
        } catch (IOException e) {
            System.out.println("❌ Erreur de navigation vers AjouterPaiement : " + e.getMessage());
        }
    }



}
