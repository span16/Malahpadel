package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import models.Produit;
import services.ProduitService;

import java.sql.SQLException;

public class AjouterProduitController {
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

    private ProduitService produitService = new ProduitService();

    @FXML
    public void addProduit() {
        try {
            String nomProduit = nomProduitTf.getText();
            String categorie = categorieTf.getText();
            float prix = Float.parseFloat(prixTf.getText());
            int stock = Integer.parseInt(stockTf.getText());
            String description = descriptionTa.getText();
            String imageProduit = imageProduitTf.getText();

            Produit produit = new Produit(stock, nomProduit, categorie, imageProduit, description, prix);
            produitService.ajouter(produit);

            System.out.println("Produit ajouté avec succès !");
        } catch (NumberFormatException e) {
            System.err.println("Erreur de format : " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
        }
    }
}