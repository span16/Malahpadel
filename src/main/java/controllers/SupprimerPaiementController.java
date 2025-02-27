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
                int id_P = Integer.parseInt(txtIdP.getText());
                PaiementService ps = new PaiementService();

                int rowsDeleted = ps.supprimer(id_P);
                if (rowsDeleted > 0) {
                    System.out.println("✅ Paiement supprimé avec succès !");
                } else {
                    System.out.println("⚠️ Aucun paiement trouvé avec l'ID " + id_P);
                }

            } catch (NumberFormatException e) {
                System.out.println("❌ Veuillez entrer un ID valide !");
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
