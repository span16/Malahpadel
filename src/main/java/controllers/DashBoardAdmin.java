package controllers;

import Session.SessionManger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.User;

import java.io.IOException;

public class DashBoardAdmin {

    @FXML
    private Button Home;
    @FXML
    private Button user;
    @FXML
    private Button event;
    @FXML
    private Button reserver;
    @FXML
    private Button sponsor;
    @FXML
    private Button matchup;
    @FXML
    private Label admin;

    @FXML
    public void initialize() {
        // Vérifiez si un utilisateur est connecté
        if (SessionManger.isLoggedIn()) {
            User loggedInUser = SessionManger.getCurrentUser();
            // Mettre à jour le texte du Label avec le nom de l'utilisateur
            admin.setText("Bienvenue, " + loggedInUser.getNom());
        } else {
            // Si l'utilisateur n'est pas connecté, rediriger vers la page de login
            System.out.println("Utilisateur non connecté, redirection vers la page de login.");
            redirectToLogin();
        }
    }

    private void redirectToLogin() {
        try {
            // Charger la scène de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
            Parent root = loader.load();

            // Créer la nouvelle scène
            Scene loginScene = new Scene(root);

            // Créer un nouveau stage (fenêtre) et y charger la scène
            Stage loginStage = new Stage();
            loginStage.setTitle("Page de Login");
            loginStage.setScene(loginScene);

            // Afficher la scène de login
            loginStage.show();

            // Fermer la fenêtre actuelle (dashboard)
            Stage currentStage = (Stage) admin.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page de login.");
        }
    }

    // Méthode pour afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
