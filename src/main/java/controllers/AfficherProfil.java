package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Profil;
import services.ProfilService;

import java.io.IOException;
import java.sql.SQLException;

public class AfficherProfil {


    @FXML
    private VBox profilVBox;

    private final ProfilService profilService = new ProfilService();
    private int userId; // Stocke l'ID de l'utilisateur sélectionné

    /**
     * Initialise l'affichage du profil d'un utilisateur donné.
     */
    @FXML
    public void initialize() {
        if (userId != 0) {
            displayProfil(userId);
        }
    }

    /**
     * Définit l'ID de l'utilisateur et affiche son profil.
     */
    public void setUserId(int userId) {
        this.userId = userId;
        displayProfil(userId);
    }

    /**
     * Charge et affiche les informations du profil d'un utilisateur.
     */
    private void displayProfil(int userId) {
        profilVBox.getChildren().clear(); // Nettoyer avant d'afficher

        try {
            Profil profil = profilService.getProfilByUserId(userId);

            if (profil != null) {
                Label avatarLabel = new Label("Avatar : " + profil.getAvatar());
                Label bioLabel = new Label("Bio : " + profil.getBio());
                Label preferencesLabel = new Label("Préférences : " + profil.getPreferences());

                Button modifierProfilButton = new Button("Modifier Profil");
                modifierProfilButton.getStyleClass().add("modify-button");

                Button supprimerProfilButton = new Button("Supprimer Profil");
                supprimerProfilButton.getStyleClass().add("delete-button");

                modifierProfilButton.setOnAction(event -> modifierProfil(profil));
                supprimerProfilButton.setOnAction(event -> supprimerProfil(profil));

                profilVBox.getChildren().addAll(
                        avatarLabel, bioLabel, preferencesLabel,
                        modifierProfilButton, supprimerProfilButton
                );
            } else {
                profilVBox.getChildren().add(new Label("Aucun profil trouvé pour cet utilisateur."));
            }
        } catch (SQLException e) {
            showAlert("Erreur SQL", "Erreur lors de la récupération du profil : " + e.getMessage());
        }
    }

    /**
     * Méthode pour modifier un profil existant.
     */
    private void modifierProfil(Profil profil) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ModifierProfil.fxml"));
            Parent root = loader.load();

            ModifierProfil controller = loader.getController();
            controller.setProfil(profil);

            // Mise à jour après modification
            controller.setOnUpdateSuccess(() -> displayProfil(userId));

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Profil");
            stage.show();

        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification.");
            e.printStackTrace();
        }
    }

    /**
     * Méthode pour supprimer un profil existant.
     */
    private void supprimerProfil(Profil profil) {
        try {
            profilService.deleteProfil(profil.getIdUser());
            showAlert("Succès", "Le profil a été supprimé avec succès !");
            profilVBox.getChildren().clear();
            profilVBox.getChildren().add(new Label("Profil supprimé."));
        } catch (SQLException e) {
            showAlert("Erreur SQL", "Erreur lors de la suppression du profil : " + e.getMessage());
        }
    }
    /**
     * Affiche une alerte avec un message spécifique.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
