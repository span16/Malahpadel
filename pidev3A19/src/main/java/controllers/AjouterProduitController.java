package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Produit;
import services.ProduitService;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class AjouterProduitController {

    // Champs FXML
    @FXML
    private TextField nomProduitTf;
    @FXML
    private TextField categorieTf;
    @FXML
    private TextField prixTf;
    @FXML
    private TextField stockTf;
    @FXML
    private TextArea descriptionTa;
    @FXML
    private Button afficherBtn;
    @FXML
    private Button compagnegestion;
    @FXML
    private ImageView imgpa;
    @FXML
    private Button selectImageBtn;

    private String imagePath;

    private ProduitService produitService = new ProduitService();

    @FXML
    public void addProduit() {
        if (nomProduitTf.getText().isEmpty() || categorieTf.getText().isEmpty() ||
                prixTf.getText().isEmpty() || stockTf.getText().isEmpty() ||
                descriptionTa.getText().isEmpty() || imagePath == null) {
            showAlert("Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        float prix;
        try {
            prix = Float.parseFloat(prixTf.getText());
            if (prix <= 0) {
                showAlert("Erreur", "Le prix doit être un nombre positif !");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix doit être un nombre valide !");
            return;
        }

        int stock;
        try {
            stock = Integer.parseInt(stockTf.getText());
            if (stock < 0) {
                showAlert("Erreur", "Le stock ne peut pas être négatif !");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le stock doit être un nombre valide !");
            return;
        }

        if (nomProduitTf.getText().length() > 50) {
            showAlert("Erreur", "Le nom du produit ne doit pas dépasser 50 caractères !");
            return;
        }
        if (categorieTf.getText().length() > 50) {
            showAlert("Erreur", "La catégorie ne doit pas dépasser 50 caractères !");
            return;
        }
        if (descriptionTa.getText().length() > 255) {
            showAlert("Erreur", "La description ne doit pas dépasser 255 caractères !");
            return;
        }

        try {
            String nomProduit = nomProduitTf.getText();
            String categorie = categorieTf.getText();
            String description = descriptionTa.getText();

            Produit produit = new Produit(stock, nomProduit, categorie, imagePath, description, prix);

            produitService.ajouter(produit);

            showAlert("Succès", "Produit ajouté avec succès !");

            clearFields();
        } catch (SQLException e) {
            showAlert("Erreur SQL", "Erreur lors de l'ajout du produit : " + e.getMessage());
        }
    }

    @FXML
    public void selectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            imagePath = selectedFile.getAbsolutePath();

            Image image = new Image(selectedFile.toURI().toString());
            imgpa.setImage(image);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        nomProduitTf.clear();
        categorieTf.clear();
        prixTf.clear();
        stockTf.clear();
        descriptionTa.clear();
        imgpa.setImage(null);
        imagePath = null;
    }

    @FXML
    public void affichebtn(ActionEvent event) {
        try {
            // Charger l'interface AfficherProduit.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherProduit.fxml"));
            Parent root = loader.load();

            // Afficher la nouvelle interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Produits");
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de l'interface AfficherProduit.fxml : " + e.getMessage());
        }
    }
    public void gestioncomp() {
        try {
            // Charger l'interface du formulaire CompagneForm.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CompagneForm.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène et la mettre en place
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Formulaire Compagne");
            stage.show();

            Stage currentStage = (Stage) compagnegestion.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}