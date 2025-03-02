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
    private TextField txtIdP;

    private final PaiementService paiementService = new PaiementService();

    @FXML
    void deletePaiement(ActionEvent event) {
        try {
            String devise = txtIdP.getText().trim(); // ✅ Récupérer la devise depuis le champ de texte

            if (devise.isEmpty()) {
                System.out.println("❌ Veuillez entrer une devise valide !");
                return;
            }

            PaiementService ps = new PaiementService();
            int rowsDeleted = ps.supprimer(devise);  // ✅ Utilisation correcte de la devise

            if (rowsDeleted > 0) {
                System.out.println("✅ Paiement(s) supprimé(s) avec succès !");
            } else {
                System.out.println("⚠️ Aucun paiement trouvé avec la devise " + devise);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL lors de la suppression : " + e.getMessage());
        }
    }



    @FXML
    void goToAjouterPaiement(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPaiement.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) txtIdP.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

            System.out.println("➡️ Navigation vers AjouterPaiement réussie !");
        } catch (IOException e) {
            System.out.println("❌ Erreur de navigation vers AjouterPaiement : " + e.getMessage());
        }
    }



}
