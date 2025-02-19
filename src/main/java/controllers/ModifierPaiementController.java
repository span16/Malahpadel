package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.paiement;
import service.PaiementService;

import java.sql.SQLException;

public class ModifierPaiementController {

    @FXML
    private TextField txtIdP; // Champ pour l'ID du paiement

    @FXML
    private TextField txtStatusP; // Champ pour le nouveau statut du paiement

    private PaiementService paiementService = new PaiementService();  // Service de paiement

    @FXML
    void modifierPaiement(ActionEvent event) {
        try {
            // Récupérer l'ID du paiement et le nouveau statut depuis l'interface
            int id_P = Integer.parseInt(txtIdP.getText());
            String status_P = txtStatusP.getText();

            // Créer un objet paiement avec l'ID seulement (les autres valeurs peuvent être ignorées si inutiles ici)
            paiement updatedPaiement = new paiement(id_P, 0, 0.0f, null);

            // Appeler la méthode modifier avec les deux paramètres requis
            paiementService.modifier(updatedPaiement, status_P);

            System.out.println("✅ Statut du paiement mis à jour avec succès !");
        } catch (NumberFormatException e) {
            System.out.println("❌ Veuillez entrer un ID valide !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la modification du statut du paiement : " + e.getMessage());
        }
    }
}
