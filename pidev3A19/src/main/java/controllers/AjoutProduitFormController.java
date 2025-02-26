package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Produit;
import services.ProduitService;

import java.io.File;
import java.sql.SQLException;

public class AjoutProduitFormController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField categorieField;

    @FXML
    private Button selectImageBtn;

    @FXML
    private ImageView imagePreview;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField prixField;

    @FXML
    private TextField stockField;

    private String imagePath; // Chemin de l'image sélectionnée

    private ProduitFront produitFrontController; // Référence au contrôleur principal

    public void setProduitFrontController(ProduitFront produitFrontController) {
        this.produitFrontController = produitFrontController;
    }

    @FXML
    private void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            imagePath = selectedFile.toURI().toString();
            imagePreview.setImage(new Image(imagePath));
        }
    }

    @FXML
    private void validerAjout() {
        try {
            String nom = nomField.getText();
            String categorie = categorieField.getText();
            String description = descriptionField.getText();
            float prix = Float.parseFloat(prixField.getText());
            int stock = Integer.parseInt(stockField.getText());

            // Créer un nouveau produit
            Produit produit = new Produit(0, stock, nom, categorie, imagePath, description, prix);

            // Ajouter le produit via le service
            ProduitService produitService = new ProduitService();
            produitService.ajouter(produit);

            // Afficher le produit dans l'interface principale
            if (produitFrontController != null) {
                produitFrontController.afficherProduit(produit);
            }

            // Afficher un message de succès
            showAlert("Succès", "Produit ajouté avec succès !");

            // Fermer la fenêtre du formulaire
            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            showAlert("Erreur SQL", "Erreur lors de l'ajout du produit : " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix et le stock doivent être des nombres valides.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}