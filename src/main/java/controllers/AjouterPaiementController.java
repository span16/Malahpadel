package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import models.paiement;
import service.PaiementService;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterPaiementController {

    @FXML
    private TextField txtIdP;

    @FXML
    private TextField txtIdR;

    @FXML
    private TextField txtMontant;

    @FXML
    private TextField txtStatusP;

    @FXML
    void addPaiement(ActionEvent event) {
        // Récupérer les valeurs des champs
        int id_P = Integer.parseInt(txtIdP.getText());
        int id_R = Integer.parseInt(txtIdR.getText());
        float montant = Float.parseFloat(txtMontant.getText());
        String status_P = txtStatusP.getText();

        // Créer une instance de Paiement
        paiement paiement = new paiement(id_P, id_R, montant, status_P);

        // Créer une instance de PaiementService pour ajouter le paiement
        PaiementService paiementService = new PaiementService();
        try {
            paiementService.ajouter(paiement); // Ajouter le paiement dans la base de données
        } catch (SQLException e) {
            System.out.println(e.getMessage()); // Afficher une erreur SQL si nécessaire
        }

        // Charger la vue "AfficherPaiement" après l'ajout
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPaiement.fxml"));
            Parent root = loader.load();
            AfficherPaiementController ac = loader.getController();

            // Passer les informations à la vue AfficherPaiement
            ac.setRIdP(id_P);
            ac.setRIdR(id_R);
            ac.setRMontant(montant);
            ac.setRStatusP(status_P);

            // Récupérer la liste des paiements et la formater
            StringBuilder sb = new StringBuilder();
            for (paiement p : paiementService.recuperer()) {
                sb.append("ID Paiement: ").append(p.getId_P()).append(", ");
                sb.append("ID Réservation: ").append(p.getId_R()).append(", ");
                sb.append("Montant: ").append(p.getMontant()).append(", ");
                sb.append("Statut: ").append(p.getStatus_P()).append("\n");
            }
            ac.setRlist(sb.toString()); // Passer la liste formatée à AfficherPaiementController

            // Afficher la nouvelle vue
            txtIdP.getScene().setRoot(root);

        } catch (IOException | SQLException e) {
            System.out.println(e.getMessage()); // Afficher l'erreur de chargement ou SQL
        }
    }

    public void goToModifierPaiement(ActionEvent actionEvent) {
    }

    public void goToSupprimerPaiement(ActionEvent actionEvent) {
    }
}
