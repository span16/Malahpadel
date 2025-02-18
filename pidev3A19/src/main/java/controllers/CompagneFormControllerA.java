package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import models.Compagne;
import models.Produit;
import services.CompagneService;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class CompagneFormControllerA {

    // Références aux éléments du formulaire FXML
    @FXML
    private TextField nomSponsorTf;

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML
    private Button selectLogoBtn;

    @FXML
    private ImageView logoImageView;

    @FXML
    private ComboBox<String> typeMarketingComboBox;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private TextField tarifsTf;

    @FXML
    private ComboBox<String> idProduitComboBox;

    // Service pour gérer les campagnes
    private CompagneService compagneService = new CompagneService();

    // Méthode d'initialisation
    @FXML
    public void initialize() {
        // Contrôle de saisie pour le nom du sponsor (lettres et espaces uniquement)
        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("[a-zA-Z\\s]*")) {
                return change; // Accepter la modification
            }
            return null; // Rejeter la modification
        });
        nomSponsorTf.setTextFormatter(textFormatter);

        // Contrôle de saisie pour les tarifs (nombres et point décimal uniquement)
        TextFormatter<String> floatFormatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*(\\.\\d*)?")) {
                return change; // Accepter la modification
            }
            return null; // Rejeter la modification
        });
        tarifsTf.setTextFormatter(floatFormatter);

        // Remplir les ComboBox avec les données nécessaires
        populateTypeMarketingComboBox();
        populateStatusComboBox();
        populateIdProduitComboBox();
    }

    // Remplir le ComboBox pour le type de marketing
    private void populateTypeMarketingComboBox() {
        typeMarketingComboBox.getItems().addAll("Email", "Réseaux sociaux", "Publicité en ligne", "Télévision", "Autre");
    }

    // Remplir le ComboBox pour le statut
    private void populateStatusComboBox() {
        statusComboBox.getItems().addAll("active", "inactive", "pending");
    }

    // Remplir le ComboBox pour l'idProduit
    private void populateIdProduitComboBox() {
        try {
            // Connexion à la base de données
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidev3A19", "root", "");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id_Produit FROM Produit");

            // Ajouter les idProduit au ComboBox
            while (rs.next()) {
                idProduitComboBox.getItems().add(rs.getString("id_Produit"));
            }

            // Fermer les ressources
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur de base de données", "Impossible de charger les idProduit.");
        }
    }

    // Méthode pour sélectionner un logo
    @FXML
    private void selectLogo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un logo");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            logoImageView.setImage(image);
        }
    }

    // Méthode pour valider le formulaire
    @FXML
    private void valider() {
        // Récupérer les valeurs du formulaire
        String nomSponsor = nomSponsorTf.getText();
        LocalDate dateDebut = dateDebutPicker.getValue();
        LocalDate dateFin = dateFinPicker.getValue();
        String typeMarketing = typeMarketingComboBox.getValue();
        String statut = statusComboBox.getValue();
        String tarifs = tarifsTf.getText();
        String idProduit = idProduitComboBox.getValue();

        // Vérifier que tous les champs sont remplis
        if (nomSponsor.isEmpty() || dateDebut == null || dateFin == null || typeMarketing == null || statut == null || tarifs.isEmpty() || idProduit == null) {
            showAlert("Erreur de validation", "Veuillez remplir tous les champs.");
            return;
        }

        // Vérifier que le statut est valide
        if (!statut.equals("active") && !statut.equals("inactive") && !statut.equals("pending")) {
            showAlert("Erreur de validation", "Le statut sélectionné est invalide. Les valeurs autorisées sont : active, inactive, pending.");
            return;
        }

        // Convertir l'ID du produit en entier
        int produitId = Integer.parseInt(idProduit);

        // Créer un objet Produit avec l'ID sélectionné
        Produit produit = new Produit();
        produit.setId_produit(produitId);

        // Créer un objet Compagne avec les données du formulaire
        Compagne compagne = new Compagne();
        compagne.setNom_sponsor(nomSponsor);
        compagne.setDate_debut(Date.valueOf(dateDebut));
        compagne.setDate_fin(Date.valueOf(dateFin));
        compagne.setLogo_compagne(logoImageView.getImage().getUrl()); // Récupérer l'URL de l'image
        compagne.setTypeMarketing(typeMarketing);
        compagne.setStatus(statut);
        compagne.setTarifs(Float.parseFloat(tarifs));
        compagne.setProduit(produit);

        // Appeler la méthode ajoutercompagne
        try {
            compagneService.ajoutercompagne(compagne);
            showAlert("Succès", "La campagne a été ajoutée avec succès.");
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur de base de données", "Impossible d'ajouter la campagne : " + e.getMessage());
        }
    }

    // Méthode pour annuler et réinitialiser le formulaire
    @FXML
    private void annuler() {
        clearForm();
    }

    // Réinitialiser le formulaire
    private void clearForm() {
        nomSponsorTf.clear();
        dateDebutPicker.setValue(null);
        dateFinPicker.setValue(null);
        typeMarketingComboBox.setValue(null);
        statusComboBox.setValue(null);
        tarifsTf.clear();
        idProduitComboBox.setValue(null);
        logoImageView.setImage(null);
    }

    // Afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void affichercompagne(ActionEvent actionEvent) {
        try {
            // Charger la nouvelle interface FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeCompagnes.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Créer une nouvelle fenêtre (Stage)
            Stage stage = new Stage();
            stage.setTitle("Liste des Campagnes");
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la liste des campagnes.");
        }
    }

    public void interfaceproduit(ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML de l'interface AjouterProduit
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterProduit.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle (stage) à partir de l'événement
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Définir la nouvelle scène sur le stage
            stage.setScene(scene);
            stage.setTitle("Ajouter un Produit"); // Titre de la nouvelle fenêtre
            stage.show(); // Afficher la nouvelle scène
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir l'interface AjouterProduit.");
        }
    }
}