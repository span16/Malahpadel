package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Role;
import models.User;
import services.UserService;
import javafx.event.ActionEvent;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class Register {

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
    private TextField txtmdp;  // Mot de passe
    @FXML
    private Button addBtn;
    @FXML
    private Button closeBtn;  // Bouton fermer

    private final UserService userService = new UserService();

    // Méthode pour valider l'email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    // Méthode pour ajouter un utilisateur
    @FXML
    public void addUser(ActionEvent event) {
        // Vérifier que tous les champs sont remplis
        if (txtNom.getText().isEmpty() || txtPrenom.getText().isEmpty() || txtEmail.getText().isEmpty() ||
                txtAge.getText().isEmpty() || txtCin.getText().isEmpty() || txtmdp.getText().isEmpty()) {
            showAlert("Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        // Vérification des autres champs comme l'email et l'âge (à compléter si besoin)

        try {
            String nom = txtNom.getText();
            String prenom = txtPrenom.getText();
            String email = txtEmail.getText();
            int age = Integer.parseInt(txtAge.getText());
            int cin = Integer.parseInt(txtCin.getText());
            String password = txtmdp.getText();

            // Hachage du mot de passe avec BCrypt
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

            // Créer un nouvel objet User
            User user = new User();
            user.setNom(nom);
            user.setPrenom(prenom);
            user.setEmail(email);
            user.setAge(age);
            user.setCin(cin);
            user.setMdp(hashedPassword); // On stocke le mot de passe haché
            user.setEtat("Actif");

            // Initialiser 'fonction' avec un rôle par défaut
            Role userRole = Role.USER;  // Assurez-vous que le rôle "USER" existe dans votre enum Role
            user.setFonction(userRole);  // Assigner le rôle à l'utilisateur

            // Ajouter l'utilisateur à la base de données
            userService.ajouter1(user);

            // Afficher un message de succès et réinitialiser les champs
            showAlert("Succès", "Utilisateur ajouté avec succès !");
            clearFields();

        } catch (SQLException e) {
            showAlert("Erreur SQL", "Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'âge et le CIN doivent être des nombres !");
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
        txtmdp.clear();  // Réinitialiser le mot de passe
    }

    // Méthode pour fermer la fenêtre de l'inscription
    @FXML
    private void closeForm(ActionEvent event) {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }
}
