package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Produit;
import services.ProduitService;

import java.sql.SQLException;

public class ModifierProduitController {

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
    private TextField imageProduitTf;
    @FXML
    private Button validerButton;

    private Produit produit;
    private ProduitService produitService = new ProduitService();
    private Runnable onUpdateSuccess; // Callback

    public void initData(Produit produit) {
        this.produit = produit;
        nomProduitTf.setText(produit.getNom_produit());
        categorieTf.setText(produit.getCategorie());
        prixTf.setText(String.valueOf(produit.getPrix()));
        stockTf.setText(String.valueOf(produit.getStock()));
        descriptionTa.setText(produit.getDescription());
        imageProduitTf.setText(produit.getImage_produit());
    }

    public void setOnUpdateSuccess(Runnable runnable) {
        this.onUpdateSuccess = runnable;
    }

    @FXML
    private void validerModification() {
        try {
            // Vérification que les champs obligatoires ne sont pas vides
            if (nomProduitTf.getText().isEmpty() || categorieTf.getText().isEmpty() ||
                    prixTf.getText().isEmpty() || stockTf.getText().isEmpty()) {
                throw new IllegalArgumentException("Tous les champs obligatoires doivent être remplis.");
            }

            // Validation du format des nombres pour le prix et le stock
            float prix;
            int stock;
            try {
                prix = Float.parseFloat(prixTf.getText());
                stock = Integer.parseInt(stockTf.getText());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Le prix et le stock doivent être des nombres valides.");
            }

            // Vérification que la catégorie ne contient pas de nombres
            if (categorieTf.getText().matches(".*\\d.*")) {
                throw new IllegalArgumentException("La catégorie ne doit pas contenir de chiffres.");
            }

            // Validation de la longueur des chaînes de caractères
            if (nomProduitTf.getText().length() > 100) {
                throw new IllegalArgumentException("Le nom du produit ne doit pas dépasser 100 caractères.");
            }
            if (categorieTf.getText().length() > 50) {
                throw new IllegalArgumentException("La catégorie ne doit pas dépasser 50 caractères.");
            }
            if (descriptionTa.getText().length() > 500) {
                throw new IllegalArgumentException("La description ne doit pas dépasser 500 caractères.");
            }

            // Mise à jour du produit
            produit.setNom_produit(nomProduitTf.getText());
            produit.setCategorie(categorieTf.getText());
            produit.setPrix(prix);
            produit.setStock(stock);
            produit.setDescription(descriptionTa.getText());
            produit.setImage_produit(imageProduitTf.getText());

            // Appel du service pour modifier le produit
            produitService.modifier(produit, produit.getId_produit());

            // Fermeture de la fenêtre
            Stage stage = (Stage) validerButton.getScene().getWindow();
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
            // Affichage d'un message d'erreur pour les problèmes de base de données
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de base de données");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors de la mise à jour du produit.");
            alert.showAndWait();
        }}}