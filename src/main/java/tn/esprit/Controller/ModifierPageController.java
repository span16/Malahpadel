package tn.esprit.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.models.AnnonceMatch;
import tn.esprit.services.AnnonceService;

import java.sql.SQLException;

public class ModifierPageController {

    @FXML private TextField titreField;
    @FXML private TextField lieuField;
    @FXML private TextField descriptionField;
    @FXML private Button enregistrerBtn;

    private AnnonceMatch annonce;
    private AnnonceMatchController parentController;
    private final AnnonceService annonceService = new AnnonceService();

    // Initialisation des données passées à ce contrôleur
    public void initData(AnnonceMatch annonce, AnnonceMatchController parentController) {
        this.annonce = annonce;
        this.parentController = parentController;
        titreField.setText(annonce.getTitre());
        lieuField.setText(annonce.getLieu());
        descriptionField.setText(annonce.getDescription());
    }

    // Méthode pour enregistrer les modifications
    @FXML
    private void enregistrerModification() {
        // Mise à jour des champs de l'annonce
        annonce.setTitre(titreField.getText());
        annonce.setLieu(lieuField.getText());
        annonce.setDescription(descriptionField.getText());

        // Appel à la méthode de modification dans le service
        try {
            // Vous devez passer l'ID de l'annonce, pas juste le titre
            annonceService.modifier(annonce);
            parentController.chargerAnnonces(); // Recharger les annonces dans le parent
            ((Stage) enregistrerBtn.getScene().getWindow()).close(); // Fermer la fenêtre actuelle
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
