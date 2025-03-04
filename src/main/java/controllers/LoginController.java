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

            // Vérifier si le mot de passe est correct
            if (user.getMdp().equals(password)) {


                showAlert("Succès", "Connexion réussie !");
                System.out.println("Rôle de l'utilisateur : " + user.getFonction());
                redirectToPage(user);
                // Redirection en fonction du rôle
            } else {
                showAlert("Erreur", "Mot de passe incorrect.");
            }

        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la connexion : " + e.getMessage());
        }
    }

    private void redirectToPage(User user) {
        // Vérifier que l'utilisateur n'est pas nul avant de procéder
        if (user == null) {
            showAlert("Erreur", "Utilisateur non valide.");
            return;
        }

        // Selon le rôle de l'utilisateur, on charge la page correspondante
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
            // Vérifiez si le chemin est correct en affichant le message dans la console
            System.out.println(getClass().getResource("/views/DashBoardAdmin.fxml"));

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Admin.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec la page Admin
            Scene adminScene = new Scene(root);
            Stage adminStage = new Stage();
            adminStage.setTitle("Page Admin");
            adminStage.setScene(adminScene);
            adminStage.show();

            // Fermer la fenêtre de connexion actuelle
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
            // Charger le fichier FXML pour la page Utilisateur
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/page1.fxml"));

            Parent root = loader.load();

            // Créer une nouvelle scène avec la page Utilisateur
            Scene userScene = new Scene(root);
            Stage userStage = new Stage();
            userStage.setTitle("Page Utilisateur");
            userStage.setScene(userScene);
            userStage.show();

            // Fermer la fenêtre de connexion actuelle
            Stage currentStage = (Stage) loginBtn.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page Utilisateur.");
        }
    }

    // Méthode pour afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void forgotPassword(ActionEvent event) {
        // Implémentez ici la logique pour réinitialiser le mot de passe
        showAlert("Mot de passe oublié", "Un lien de réinitialisation a été envoyé à votre email.");
    }

    @FXML
    private void openSignUpPage(ActionEvent event) {
        try {
            // Ouvrir la fenêtre d'inscription
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