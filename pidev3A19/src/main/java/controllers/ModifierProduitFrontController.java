package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.Produit;
import services.ProduitService;

import java.sql.SQLException;

public class ModifierProduitFrontController {
    @FXML private TextField nomField;
    @FXML private TextField categorieField;
    @FXML private ImageView imagePreview;
    @FXML private TextArea descriptionField;
    @FXML private TextField prixField;
    @FXML private TextField stockField;

    private Produit produit;
    private Runnable onUpdateSuccess;

    public void setProduit(Produit produit) {
        this.produit = produit;
        nomField.setText(produit.getNom_produit());
        categorieField.setText(produit.getCategorie());
        descriptionField.setText(produit.getDescription());
        prixField.setText(String.valueOf(produit.getPrix()));
        stockField.setText(String.valueOf(produit.getStock()));
        imagePreview.setImage(new Image(produit.getImage_produit()));
    }

    public void setOnUpdateSuccess(Runnable runnable) {
        this.onUpdateSuccess = runnable;
    }

    @FXML
    private void validerModification() {
        try {
            if (nomField.getText().isEmpty() || categorieField.getText().isEmpty() || prixField.getText().isEmpty() || stockField.getText().isEmpty()) {
                throw new IllegalArgumentException("Tous les champs obligatoires doivent être remplis.");
            }

            float prix = Float.parseFloat(prixField.getText());
            int stock = Integer.parseInt(stockField.getText());

            if (categorieField.getText().matches(".*\\d.*")) {
                throw new IllegalArgumentException("La catégorie ne doit pas contenir de chiffres.");
            }

            if (nomField.getText().length() > 100) {
                throw new IllegalArgumentException("Le nom du produit ne doit pas dépasser 100 caractères.");
            }

            if (categorieField.getText().length() > 50) {
                throw new IllegalArgumentException("La catégorie ne doit pas dépasser 50 caractères.");
            }

            if (descriptionField.getText().length() > 500) {
                throw new IllegalArgumentException("La description ne doit pas dépasser 500 caractères.");
            }

            produit.setNom_produit(nomField.getText());
            produit.setCategorie(categorieField.getText());
            produit.setPrix(prix);
            produit.setStock(stock);
            produit.setDescription(descriptionField.getText());

            ProduitService produitService = new ProduitService();
            produitService.modifier(produit, produit.getId_produit());

            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.close();

            if (onUpdateSuccess != null) {
                onUpdateSuccess.run();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de base de données");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors de la mise à jour du produit.");
            alert.showAndWait();
        }
    }
}