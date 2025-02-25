package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.Produit;
import services.ProduitService;

import java.sql.SQLException;

public class ModifierProduitFrontController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField categorieField;

    @FXML
    private ImageView imagePreview;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField prixField;

    @FXML
    private TextField stockField;

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
            // Vérification des champs obligatoires
            if (nomField.getText().isEmpty() || categorieField.getText().isEmpty() ||
                    prixField.getText().isEmpty() || stockField.getText().isEmpty()) {
                throw new IllegalArgumentException("Tous les champs obligatoires doivent être remplis.");
            }

            // Validation du format des nombres pour le prix et le stock
            float prix;
            int stock;
            try {
                prix = Float.parseFloat(prixField.getText());
                stock = Integer.parseInt(stockField.getText());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Le prix et le stock doivent être des nombres valides.");
            }

            // Vérification que la catégorie ne contient pas de nombres
            if (categorieField.getText().matches(".*\\d.*")) {
                throw new IllegalArgumentException("La catégorie ne doit pas contenir de chiffres.");
            }

            // Validation de la longueur des chaînes de caractères
            if (nomField.getText().length() > 100) {
                throw new IllegalArgumentException("Le nom du produit ne doit pas dépasser 100 caractères.");
            }
            if (categorieField.getText().length() > 50) {
                throw new IllegalArgumentException("La catégorie ne doit pas dépasser 50 caractères.");
            }
            if (descriptionField.getText().length() > 500) {
                throw new IllegalArgumentException("La description ne doit pas dépasser 500 caractères.");
            }

            // Mise à jour du produit (sans toucher à l'image)
            produit.setNom_produit(nomField.getText());
            produit.setCategorie(categorieField.getText());
            produit.setPrix(prix);
            produit.setStock(stock);
            produit.setDescription(descriptionField.getText());

            // Mettre à jour le produit dans la base de données
            ProduitService produitService = new ProduitService();
            produitService.modifier(produit, produit.getId_produit());

            // Fermer la fenêtre de modification
            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.close();

            // Exécution de la callback en cas de succès
            if (onUpdateSuccess != null) {
                onUpdateSuccess.run();
            }

        } catch (IllegalArgumentException e) {
            // Affichage d'un message d'erreur à l'utilisateur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de base de données");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors de la mise à jour du produit.");
            alert.showAndWait();
        }
    }}