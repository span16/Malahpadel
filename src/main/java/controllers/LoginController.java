package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.User;
import services.UserService;
import org.mindrot.jbcrypt.BCrypt;
import utils.SessionManager;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button loginBtn;

    private final UserService userService = new UserService();

    // Méthode pour se connecter
    @FXML
    public void loginUser() {
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        try {
            User user = userService.getUserByEmail(email);

            if (user == null) {
                showAlert("Erreur", "Aucun utilisateur trouvé avec cet email.");
                return;
            }

            // Vérification du mot de passe via BCrypt.checkpw()
            if (BCrypt.checkpw(password, user.getMdp())) {
                showAlert("Succès", "Connexion réussie !");
                SessionManager.getInstance().setCurrentUser(user);
                System.out.println("Rôle de l'utilisateur : " + user.getFonction());
                redirectToPage(user);
            } else {
                showAlert("Erreur", "Mot de passe incorrect.");
            }

        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la connexion : " + e.getMessage());
        }
    }

    private void redirectToPage(User user) {
        if (user == null) {
            showAlert("Erreur", "Utilisateur non valide.");
            return;
        }
        switch (user.getFonction()) {
            case ADMIN:
                System.out.println("Redirection vers Admin");
                loadAdminPage();
                break;
            case USER:
                System.out.println("Redirection vers Utilisateur");
                loadUserPage();
                break;
            default:
                showAlert("Erreur", "Rôle de l'utilisateur non défini.");
                break;
        }
    }

    private void loadAdminPage() {
        try {
            System.out.println(getClass().getResource("/views/DashBoardAdmin.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Admin.fxml"));
            Parent root = loader.load();
            Scene adminScene = new Scene(root);
            Stage adminStage = new Stage();
            adminStage.setTitle("Page Admin");
            adminStage.setScene(adminScene);
            adminStage.show();
            Stage currentStage = (Stage) loginBtn.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page Admin. Vérifiez le chemin du fichier FXML.");
        }
    }

    private void loadUserPage() {
        try {
            System.out.println("Chargement de la page Utilisateur...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/page1.fxml"));
            Parent root = loader.load();
            Scene userScene = new Scene(root);
            Stage userStage = new Stage();
            userStage.setTitle("Page Utilisateur");
            userStage.setScene(userScene);
            userStage.show();
            Stage currentStage = (Stage) loginBtn.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page Utilisateur.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void forgotPassword(ActionEvent event) {
        String email = txtEmail.getText();
        if (email.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer votre email.");
            return;
        }
        boolean emailSent = userService.sendResetEmail(email);
        if (emailSent) {
            showAlert("Succès", "Un lien de réinitialisation a été envoyé à votre email.");
            openResetPasswordPage();
        } else {
            showAlert("Erreur", "Aucun utilisateur trouvé avec cet email.");
        }
    }

    private void openResetPasswordPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ResetPassword.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Réinitialisation du mot de passe");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la page de réinitialisation.");
        }
    }

    @FXML
    private void openSignUpPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Register.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Inscription");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la page d'inscription.");
        }
    }
}
