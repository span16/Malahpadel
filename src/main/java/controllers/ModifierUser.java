package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.User;
import services.UserService;

import java.sql.SQLException;

public class ModifierUser {

    @FXML
    private TextField nomField, prenomField, emailField, cinField, ageField, mdpField, etatField, fonctionField;

    @FXML
    private Button modifierButton, annulerButton;

    private UserService userService = new UserService();
    private User user;
    private Runnable onUpdateSuccess;

    public void setUser(User user) {
        this.user = user;
        nomField.setText(user.getNom());
        prenomField.setText(user.getPrenom());
        emailField.setText(user.getEmail());
        cinField.setText(String.valueOf(user.getCin()));
        ageField.setText(String.valueOf(user.getAge()));
        mdpField.setText(user.getMdp());
        etatField.setText(user.getEtat());
        fonctionField.setText(user.getFonction());
    }

    public void setOnUpdateSuccess(Runnable runnable) {
        this.onUpdateSuccess = runnable;
    }

    @FXML
    private void initialize() {
        modifierButton.setOnAction(event -> modifierUser());
        annulerButton.setOnAction(event -> fermerFenetre());
        //nomField.getScene().getStylesheets().add(getClass().getResource("/styles/modifierUser.css").toExternalForm());
    }

    private void modifierUser() {
        try {
            user.setNom(nomField.getText());
            user.setPrenom(prenomField.getText());
            user.setEmail(emailField.getText());
            user.setCin(Integer.parseInt(cinField.getText()));
            user.setAge(Integer.parseInt(ageField.getText()));
            user.setMdp(mdpField.getText());
            user.setEtat(etatField.getText());
            user.setFonction(fonctionField.getText());

            userService.modifier(user, user.getEmail());

            showAlert(Alert.AlertType.INFORMATION, "Succès", "L'utilisateur a été modifié !");
            fermerFenetre();

            if (onUpdateSuccess != null) {
                onUpdateSuccess.run();
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la modification !");
            e.printStackTrace();
        }
    }

    private void fermerFenetre() {
        Stage stage = (Stage) modifierButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
