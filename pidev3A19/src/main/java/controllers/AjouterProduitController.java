package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import models.Produit;
import services.ProduitService;

import java.io.IOException;
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
    @FXML
    private Button afficherBtn;

    @FXML
    private Button modifierBtn;

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


}