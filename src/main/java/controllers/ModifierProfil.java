package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Profil;
import services.ProfilService;

import java.sql.SQLException;

public class ModifierProfil {
    @FXML
    private AnchorPane anchorPane;



    @FXML
    private TextField avatarField, bioField, preferencesField;

    @FXML
    private Button modifierButton, annulerButton;

    private final ProfilService profilService = new ProfilService();
    private Profil profil;
    private Runnable onUpdateSuccess; // Callback pour rafraîchir après modification

    /**
     * Méthode pour initialiser les champs avec les données du profil.
     */
    public void setProfil(Profil profil) {
        this.profil = profil;
        avatarField.setText(profil.getAvatar());
        bioField.setText(profil.getBio());
        preferencesField.setText(profil.getPreferences());
    }

    /**
     * Définit une action à exécuter après une mise à jour réussie.
     */
    public void setOnUpdateSuccess(Runnable runnable) {
        this.onUpdateSuccess = runnable;
    }

    /**
     * Initialisation du contrôleur.
     */
    @FXML
    private void initialize() {
        modifierButton.setOnAction(event -> modifierProfil());
        annulerButton.setOnAction(event -> fermerFenetre());
        anchorPane.getStylesheets().add(getClass().getResource("../styles/modifierUser.css").toExternalForm());
    }

    /**
     * Méthode pour modifier un profil existant.
     */
    private void modifierProfil() {
        try {
            profil.setAvatar(avatarField.getText());
            profil.setBio(bioField.getText());
            profil.setPreferences(preferencesField.getText());

            profilService.updateProfil(profil);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le profil a été modifié !");
            fermerFenetre();

            if (onUpdateSuccess != null) {
                onUpdateSuccess.run();
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la modification !");
            e.printStackTrace();
        }
    }

    /**
     * Ferme la fenêtre actuelle.
     */
    private void fermerFenetre() {
        Stage stage = (Stage) modifierButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Affiche une boîte de dialogue avec un message.
     */
    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
