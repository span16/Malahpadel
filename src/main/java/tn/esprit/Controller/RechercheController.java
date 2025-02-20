package tn.esprit.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.models.Recherche;
import tn.esprit.services.RechercheService;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.SQLException;
import java.util.List;

public class RechercheController {

    @FXML private TextField nomField;
    @FXML private TextField niveauField;
    @FXML private TextField annoncesField;
    @FXML private Button ajouterBtn;
    @FXML private Button modifierBtn;
    @FXML private Button supprimerBtn;
    @FXML private VBox recherchesVBox;

    private RechercheService rechercheService = new RechercheService();

    // Initialiser la vue avec les recherches existantes
    public void initialize() {
        // Clear the VBox before adding the updated list
        recherchesVBox.getChildren().clear();

        try {
            List<Recherche> recherches = rechercheService.recuperer();
            for (Recherche recherche : recherches) {
                HBox hbox = createRechercheHBox(recherche);
                recherchesVBox.getChildren().add(hbox);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ajouter une recherche
    @FXML
    public void ajouterRecherche() {
        String nom = nomField.getText();
        String niveau = niveauField.getText();
        String annonces = annoncesField.getText();

        Recherche recherche = new Recherche(0, nom, niveau, annonces); // userId est généré par la base de données
        try {
            rechercheService.ajouter(recherche);
            initialize();  // Réactualiser l'affichage
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Modifier une recherche
    @FXML
    public void modifierRecherche() {
        String nom = nomField.getText();
        String niveau = niveauField.getText();
        String annonces = annoncesField.getText();

        Recherche recherche = new Recherche(0, nom, niveau, annonces); // Mettre à jour la recherche
        try {
            rechercheService.modifier(recherche);
            initialize();  // Réactualiser l'affichage
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Supprimer une recherche
    @FXML
    public void supprimerRecherche() {
        String nom = nomField.getText();
        String niveau = niveauField.getText();
        String annonces = annoncesField.getText();

        Recherche recherche = new Recherche(0, nom, niveau, annonces);
        try {
            rechercheService.supprimer(recherche);
            initialize();  // Réactualiser l'affichage
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Créer un HBox pour afficher une recherche
    private HBox createRechercheHBox(Recherche recherche) {
        HBox hbox = new HBox(10);

        // Créer des Labels pour afficher les informations
        Label nomLabel = new Label(recherche.getNom());
        Label niveauLabel = new Label(recherche.getNiveau());
        Label annoncesLabel = new Label(recherche.getAnnonces());

        Button modifierBtn = new Button("Modifier");
        Button supprimerBtn = new Button("Supprimer");

        // Gestion de l'action "Modifier"
        modifierBtn.setOnAction(event -> {
            openModifierWindow(recherche);  // Ouvrir la fenêtre de modification
        });

        // Gestion de l'action "Supprimer"
        supprimerBtn.setOnAction(event -> {
            try {
                rechercheService.supprimer(recherche);
                initialize(); // Réactualiser l'affichage
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        hbox.getChildren().addAll(nomLabel, niveauLabel, annoncesLabel, modifierBtn, supprimerBtn);
        return hbox;
    }

    // Méthode pour ouvrir la fenêtre de modification
    private void openModifierWindow(Recherche recherche) {
        try {
            // Charger le fichier FXML de la fenêtre de modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierRecherche.fxml"));
            VBox root = loader.load();

            // Obtenir le contrôleur de la nouvelle fenêtre
            ModifierRechercheController modifierController = loader.getController();
            modifierController.initData(recherche);  // Passer l'objet Recherche

            // Créer une nouvelle fenêtre (Stage)
            Stage stage = new Stage();
            stage.setTitle("Modifier Recherche");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
