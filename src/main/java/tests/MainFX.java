package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {

    private static Stage primaryStage; // Stage global

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        switchToAjouterevent();
    }

    public static void switchToAjouterevent() {
        switchScene("/Ajouterevent.fxml", "Ajouter un événement");
    }

    public static void switchToAfficherevent() {
        switchScene("/Afficherevent.fxml", "Liste des événements");
    }

    private static void switchScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource(fxmlPath));
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle(title);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de " + fxmlPath + " : " + e.getMessage());
        }
    }
}
