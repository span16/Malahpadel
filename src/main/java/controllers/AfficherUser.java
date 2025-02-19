package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.User;
import services.UserService;

import java.io.IOException;
import java.sql.SQLException;

public class AfficherUser {

    @FXML
    private TilePane userTilePane;

    private UserService userService = new UserService();

    private User lastAddedUser; // Stocke le dernier utilisateur ajouté

    @FXML
    public void initialize() {
        if (lastAddedUser != null) {
            displayUser(lastAddedUser);
        }
    }

    /**
     * Permet d'afficher uniquement l'utilisateur récemment ajouté.
     */
    public void displayUser(User user) {
        userTilePane.getChildren().clear(); // On vide l'affichage précédent

        VBox userCard = new VBox(5);
        userCard.getStyleClass().add("user-card"); // Appliquer le style CSS

        // Création des labels
        Label nomLabel = new Label("Nom: " + user.getNom());
        Label prenomLabel = new Label("Prénom: " + user.getPrenom());
        Label emailLabel = new Label("Email: " + user.getEmail());
        Label cinLabel = new Label("CIN: " + user.getCin());
        Label ageLabel = new Label("Âge: " + user.getAge());
        Label etatLabel = new Label("État: " + user.getEtat());
        Label fonctionLabel = new Label("Fonction: " + user.getFonction());

        // Boutons
        Button deleteButton = new Button("Supprimer");
        deleteButton.getStyleClass().add("delete-button");

        Button modifierButton = new Button("Modifier");
        modifierButton.getStyleClass().add("modify-button");

        deleteButton.setOnAction(event -> deleteUser(user));
        modifierButton.setOnAction(event -> modifierUser(user));

        // Conteneur pour les boutons
        HBox buttonContainer = new HBox(10);
        buttonContainer.getChildren().addAll(deleteButton, modifierButton);

        // Ajout des éléments à la carte utilisateur
        userCard.getChildren().addAll(
                nomLabel, prenomLabel, emailLabel,
                cinLabel, ageLabel, etatLabel,
                fonctionLabel, buttonContainer
        );

        // Ajouter l'utilisateur à l'affichage
        userTilePane.getChildren().add(userCard);
    }

    /**
     * Définit l'utilisateur récemment ajouté et l'affiche.
     */
    public void setLastAddedUser(User user) {
        this.lastAddedUser = user;
        if (userTilePane != null) {
            displayUser(user);
        }
    }

    private void deleteUser(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet utilisateur ?");
        alert.setContentText("Cette action est irréversible.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    userService.Delete(user.getId());
                    userTilePane.getChildren().clear(); // Supprime l'affichage après suppression
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void modifierUser(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de modification");
        alert.setHeaderText("Êtes-vous sûr de vouloir modifier cet utilisateur ?");
        alert.setContentText("Assurez-vous que toutes les informations sont correctes.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ModifierUser.fxml"));
                    Parent root = loader.load();

                    ModifierUser controller = loader.getController();
                    controller.setUser(user);

                    controller.setOnUpdateSuccess(() -> {
                        displayUser(user); // Rafraîchir après modification
                    });

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Modifier Utilisateur");
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
