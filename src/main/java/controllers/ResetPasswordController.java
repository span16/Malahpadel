package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import services.UserService;

public class ResetPasswordController {
    @FXML
    private GridPane resetPane;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtToken;

    @FXML
    private PasswordField txtNewPassword;

    private final UserService userService = new UserService();

    @FXML
    private void resetPassword(ActionEvent event) {
        String email = txtEmail.getText().trim();
        String token = txtToken.getText().trim();
        String newPassword = txtNewPassword.getText().trim();

        if (email.isEmpty() || token.isEmpty() || newPassword.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        boolean success = userService.resetPassword(email, newPassword, token);
        if (success) {
            showAlert("Succès", "Votre mot de passe a été réinitialisé avec succès.");
        } else {
            showAlert("Erreur", "Lien de réinitialisation invalide ou expiré.");
        }
    }

    @FXML
    private void forgotPassword(ActionEvent event) {
        String email = txtEmail.getText().trim();

        if (email.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer votre email.");
            return;
        }

        boolean emailSent = userService.sendResetEmail(email);

        if (emailSent) {
            showAlert("Succès", "Un lien de réinitialisation a été envoyé à votre email.");
        } else {
            showAlert("Erreur", "Aucun utilisateur trouvé avec cet email.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
