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

    @FXML
    public void initialize() {
        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("[a-zA-Z\\s]*")) {
                return change;
            }
            return null;
        });
        nomSponsorTf.setTextFormatter(textFormatter);

        TextFormatter<String> floatFormatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*(\\.\\d*)?")) {
                return change;
            }
            return null;
        });
        tarifsTf.setTextFormatter(floatFormatter);

        populateTypeMarketingComboBox();
        populateStatusComboBox();
        populateIdProduitComboBox();
    }

    private void populateTypeMarketingComboBox() {
        typeMarketingComboBox.getItems().addAll("Email", "Réseaux sociaux", "Publicité en ligne", "Télévision", "Autre");
    }


    private void populateStatusComboBox() {
        statusComboBox.getItems().addAll("active", "inactive", "pending");
    }

    private void populateIdProduitComboBox() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidev3A19", "root", "");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT nom_produit FROM Produit"); // Récupérer le nom_produit

            while (rs.next()) {
                idProduitComboBox.getItems().add(rs.getString("nom_produit")); // Ajouter le nom_produit à la ComboBox
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur de base de données", "Impossible de charger les noms des produits.");
        }
    }
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
    @FXML
    private void valider() {
        String nomSponsor = nomSponsorTf.getText();
        LocalDate dateDebut = dateDebutPicker.getValue();
        LocalDate dateFin = dateFinPicker.getValue();
        String typeMarketing = typeMarketingComboBox.getValue();
        String statut = statusComboBox.getValue();
        String tarifs = tarifsTf.getText();
        String nomProduit = idProduitComboBox.getValue(); // Récupérer le nom_produit sélectionné

        if (nomSponsor.isEmpty() || dateDebut == null || dateFin == null || typeMarketing == null || statut == null || tarifs.isEmpty() || nomProduit == null) {
            showAlert("Erreur de validation", "Veuillez remplir tous les champs.");
            return;
        }

        if (!statut.equals("active") && !statut.equals("inactive") && !statut.equals("pending")) {
            showAlert("Erreur de validation", "Le statut sélectionné est invalide. Les valeurs autorisées sont : active, inactive, pending.");
            return;
        }

        // Récupérer l'id_produit correspondant au nom_produit sélectionné
        int produitId = getProduitIdByNom(nomProduit);
        if (produitId == -1) {
            showAlert("Erreur", "Produit non trouvé dans la base de données.");
            return;
        }

        Produit produit = new Produit();
        produit.setId_produit(produitId);

        Compagne compagne = new Compagne();
        compagne.setNom_sponsor(nomSponsor);
        compagne.setDate_debut(Date.valueOf(dateDebut));
        compagne.setDate_fin(Date.valueOf(dateFin));
        compagne.setLogo_compagne(logoImageView.getImage().getUrl()); // Récupérer l'URL de l'image
        compagne.setTypeMarketing(typeMarketing);
        compagne.setStatus(statut);
        compagne.setTarifs(Float.parseFloat(tarifs));
        compagne.setProduit(produit);

        try {
            compagneService.ajoutercompagne(compagne);
            showAlert("Succès", "La campagne a été ajoutée avec succès.");
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur de base de données", "Impossible d'ajouter la campagne : " + e.getMessage());
        }
    }

    // Méthode pour récupérer l'id_produit à partir du nom_produit
    private int getProduitIdByNom(String nomProduit) {
        int produitId = -1;
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidev3A19", "root", "");
            PreparedStatement stmt = conn.prepareStatement("SELECT id_produit FROM Produit WHERE nom_produit = ?");
            stmt.setString(1, nomProduit);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                produitId = rs.getInt("id_produit");
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur de base de données", "Impossible de récupérer l'id_produit.");
        }
        return produitId;
    }
    @FXML
    private void annuler() {
        clearForm();
    }

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeCompagnes.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Liste des Campagnes");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la liste des campagnes.");
        }
    }

    public void interfaceproduit(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterProduit.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Ajouter un Produit");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir l'interface AjouterProduit.");
        }
    }
}