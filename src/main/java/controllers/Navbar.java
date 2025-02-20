package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Navbar {

    @FXML
    private void goToAddEvent(ActionEvent event) throws Exception {
        switchScene("/Ajouterevent.fxml", event);
    }

    @FXML
    private void showEvents(ActionEvent event) throws Exception {
        switchScene("/Afficherevent.fxml", event);
    }

    @FXML
    private void goToAddTerrain(ActionEvent event) throws Exception {
        switchScene("/Ajouterterrain.fxml", event);
    }

    @FXML
    private void showTerrains(ActionEvent event) throws Exception {
        switchScene("/Afficherterrain.fxml", event);
    }

    private void switchScene(String fxmlFile, ActionEvent event) throws Exception {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
