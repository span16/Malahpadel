package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import models.User;
import services.UserService;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AjouterUser {

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPrenom;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtAge;

    @FXML
    private TextField txtCin;
    @FXML
    private TextField txtfonction;

    @FXML
    private Button addBtn;
    @FXML
    private Button profileBtn;

    private final UserService userService = new UserService();

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    // Méthode d'ajout de l'utilisateur
    @FXML
    public void addUser(ActionEvent event) {
        // Vérifier que tous les champs sont remplis
        if (txtNom.getText().isEmpty() || txtPrenom.getText().isEmpty() || txtEmail.getText().isEmpty() ||
                txtAge.getText().isEmpty() || txtCin.getText().isEmpty()) {
            showAlert("Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        // Vérifier que l'email est valide
        String email = txtEmail.getText();
        if (!isValidEmail(email)) {
            showAlert("Erreur", "L'email n'est pas valide !");
            return;
        }

        // Vérifier que l'email est unique
        try {
            if (userService.emailExists(email)) {
                showAlert("Erreur", "L'email est déjà utilisé !");
                return;
            }
        } catch (SQLException e) {
            showAlert("Erreur SQL", "Erreur lors de la vérification de l'email : " + e.getMessage());
            return;
        }

        // Vérifier que l'âge est un nombre valide
        int age;
        try {
            age = Integer.parseInt(txtAge.getText());
            if (age <= 0) {
                showAlert("Erreur", "L'âge doit être un nombre positif !");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'âge doit être un nombre valide !");
            return;
        }

        // Vérifier que le CIN est un nombre valide
        int cin;
        try {
            cin = Integer.parseInt(txtCin.getText());
            if (cin <= 0) {
                showAlert("Erreur", "Le CIN doit être un nombre valide !");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le CIN doit être un nombre valide !");
            return;
        }

        // Si tout est valide, ajouter l'utilisateur
        try {
            String nom = txtNom.getText();
            String prenom = txtPrenom.getText();

            // Créer un nouvel objet User
            User user = new User();
            user.setNom(nom);
            user.setPrenom(prenom);
            user.setEmail(email);
            user.setAge(age);
            user.setCin(cin);
           // user.setMdp("default123"); // Mot de passe par défaut
          //  user.setEtat("Actif"); // Par défaut
          //  user.setFonction("Utilisateur"); // Par défaut

            // Ajouter l'utilisateur à la base de données
            userService.ajouter1(user);

            // Afficher un message de succès
            showAlert("Succès", "Utilisateur ajouté avec succès !");

            // Réinitialiser les champs après l'ajout
            clearFields();

            // Passer directement à la fenêtre de l'utilisateur ajouté
            loadUserScreen(user); // Passer l'utilisateur ajouté à l'affichage

        } catch (SQLException e) {
            showAlert("Erreur SQL", "Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }



    @FXML
    private void openAddProfile(ActionEvent event) {
        try {
            // Chargement de la fenêtre AjouterProfil.fxml avec le bon chemin
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AjouterProfil.fxml"));

            Parent root = loader.load();

            // Création d'une nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Ajouter un Profil");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre AjouterProfil !");
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

    // Méthode pour réinitialiser les champs
    private void clearFields() {
        txtNom.clear();
        txtPrenom.clear();
        txtEmail.clear();
        txtAge.clear();
        txtCin.clear();
    }




    // Méthode pour charger et afficher un seul utilisateur ajouté
    private void loadUserScreen(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AfficherUser.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur de la fenêtre AfficherUser
            AfficherUser afficherUser = loader.getController();
            afficherUser.setLastAddedUser(user); // Passer l'utilisateur ajouté

            // Ouvrir la fenêtre
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Utilisateur Ajouté");
            stage.show();

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) addBtn.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de AfficherUser.fxml : " + e.getMessage());
        }
    }


}
