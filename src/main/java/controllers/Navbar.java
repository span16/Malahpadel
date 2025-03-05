package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Navbar {

    @FXML
    private void goToAddEvent(ActionEvent event) throws IOException {
        switchScene("/Ajouterevent.fxml", event);
    }

    @FXML
    private void showEvents(ActionEvent event) throws IOException {
        switchScene("/Afficherevent.fxml", event);
    }

    @FXML
    private void goToAddEquipe(ActionEvent event) throws IOException {
        switchScene("/AjouterEquipe.fxml", event);
    }

    @FXML
    private void goToAddTerrain(ActionEvent event) throws IOException {
        switchScene("/Ajouterterrain.fxml", event);
    }

    @FXML
    private void showTerrains(ActionEvent event) throws IOException {
        switchScene("/Afficherterrain.fxml", event);
    }

    @FXML
    private void goToAddEmploiDuTemps(ActionEvent event) throws IOException {
        switchScene("/AjouterEmploiDuTemps.fxml", event);
    }

    @FXML
    private void showEmploiDuTemps(ActionEvent event) throws IOException {
        switchScene("/CalendrierView.fxml", event);
    }

    // Bouton existant pour la modification d'événement
    @FXML
    private void goToModifyEvent() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Modifierevent.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Événement");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Nouvelle méthode pour modifier l'emploi du temps
    @FXML
    private void goToModifyEmploiDuTemps(ActionEvent event) throws IOException {
        switchScene("/ModifierEmploiDuTemps.fxml", event);
    }

    private void switchScene(String fxmlFile, ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
