package controllers;

import javafx.fxml.FXML;
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
            // Mettre à jour l'objet Produit
            produit.setNom_produit(nomProduitTf.getText());
            produit.setCategorie(categorieTf.getText());
            produit.setPrix(Float.parseFloat(prixTf.getText()));
            produit.setStock(Integer.parseInt(stockTf.getText()));
            produit.setDescription(descriptionTa.getText());
            produit.setImage_produit(imageProduitTf.getText());

            // Appeler le service
            produitService.modifier(produit, produit.getId_produit());

            // Fermer la fenêtre
            Stage stage = (Stage) validerButton.getScene().getWindow();
            stage.close();

            // Déclencher le callback
            if (onUpdateSuccess != null) {
                onUpdateSuccess.run();
            }

        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
        }
    }
}