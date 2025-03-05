package tests;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.EmailSender;

import java.io.IOException;

public class MainFx extends Application {

    public static void main(String[] args) {
        launch(args);
    }
   // boolean success = EmailSender.sendEmail("destinataire@gmail.com", "Test", "Ceci est un test d'envoi d'email.");


    @Override
    public void start(Stage primaryStage) {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/views/Home.fxml"));

        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Ajouter user");
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
