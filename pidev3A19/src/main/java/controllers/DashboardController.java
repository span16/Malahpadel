package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class DashboardController {

    @FXML
    private StackPane contentPane; // Conteneur pour le contenu dynamique

    // Méthode pour charger une vue dans le StackPane
    private void loadView(String fxmlPath) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/AjouterProduit.fxml"));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showSponsors(ActionEvent event) {
        loadView("/fxml/AjouterProduit.fxml"); // Charger la gestion des sponsors
    }

    @FXML
    void showEvents(ActionEvent event) {
        loadView("/fxml/events.fxml"); // Charger la gestion des événements
    }

    @FXML
    void showMatchups(ActionEvent event) {
        loadView("/fxml/matchups.fxml"); // Charger la gestion des matchs
    }

    @FXML
    void showReservations(ActionEvent event) {
        loadView("/fxml/reservations.fxml"); // Charger la gestion des réservations
    }

    @FXML
    void showUsers(ActionEvent event) {
        loadView("/fxml/users.fxml"); // Charger la gestion des utilisateurs
    }
}