package tn.esprit.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.Recherche;
import tn.esprit.services.RechercheService;

public class ModifierRechercheController {

    @FXML private TextField nomField;
    @FXML private TextField niveauField;
    @FXML private TextField annoncesField;
    @FXML private Button sauvegarderBtn;
    @FXML private Button annulerBtn;

    private RechercheService rechercheService = new RechercheService();
    private Recherche recherche;  // Recherche à modifier

    // Méthode pour initialiser les données de la recherche à modifier
    public void initData(Recherche recherche) {
        this.recherche = recherche;
        nomField.setText(recherche.getNom());
        niveauField.setText(recherche.getNiveau());
        annoncesField.setText(recherche.getAnnonces());
    }

    // Sauvegarder la modification de la recherche
    @FXML
    public void sauvegarderRecherche() {
        String nom = nomField.getText();
        String niveau = niveauField.getText();
        String annonces = annoncesField.getText();

        Recherche updatedRecherche = new Recherche(recherche.getUserId(), nom, niveau, annonces);
        try {
            rechercheService.modifier(updatedRecherche);  // Mettre à jour la base de données
            closeWindow();  // Fermer la fenêtre de modification
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Annuler la modification (fermer la fenêtre sans modifier)
    @FXML
    public void annulerModification() {
        closeWindow();
    }

    // Fermer la fenêtre
    private void closeWindow() {
        Stage stage = (Stage) sauvegarderBtn.getScene().getWindow();
        stage.close();
    }
}
