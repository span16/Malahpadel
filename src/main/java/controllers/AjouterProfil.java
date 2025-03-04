package controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import services.ProfilService;
import services.UserService;
import models.Profil;
import models.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AjouterProfil {

    @FXML
    private TextField txtAvatar;

    @FXML
    private TextField txtBio;

    @FXML
    private TextField txtPreferences;

    @FXML
    private ComboBox<User> comboUser; // ComboBox pour sélectionner un utilisateur

    @FXML
    private Button addBtn;

    private final ProfilService profilService = new ProfilService();
    private final UserService userService = new UserService(); // Service pour récupérer les utilisateurs

    // Initialisation du formulaire
    @FXML
    public void initialize() {
        initUserComboBox();
    }

    // Récupération de la liste des utilisateurs pour le ComboBox
    private void initUserComboBox() {
        try {
            List<User> users = userService.recuperer();
            comboUser.setItems(FXCollections.observableArrayList(users));

            comboUser.setConverter(new StringConverter<User>() {
                @Override
                public String toString(User user) {
                    return user != null ? user.getNom() + " " + user.getPrenom() : "";
                }

                @Override
                public User fromString(String string) {
                    return comboUser.getItems().stream()
                            .filter(u -> (u.getNom() + " " + u.getPrenom()).equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
    }

    // Méthode d'ajout du profil
    @FXML
    public void addProfil(ActionEvent event) {
        if (txtAvatar.getText().isEmpty() || txtBio.getText().isEmpty() || txtPreferences.getText().isEmpty() || comboUser.getValue() == null) {
            showAlert("Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        // Vérifier si l'utilisateur est bien sélectionné
        User selectedUser = comboUser.getValue();
        if (selectedUser == null) {
            showAlert("Erreur", "Veuillez sélectionner un utilisateur !");
            return;
        }

        int userId = selectedUser.getId(); // ID de l'utilisateur sélectionné

        // Vérification de l'existence de l'utilisateur dans la base
        try {
            System.out.println("Vérification de l'existence de l'utilisateur avec ID : " + userId);

            if (!userService.userExists(userId)) {
                showAlert("Erreur", "L'utilisateur sélectionné n'existe pas !");
                return;
            }

            // Vérifier si l'utilisateur a déjà un profil
            if (profilService.getProfilByUserId(userId) != null) {
                showAlert("Erreur", "Cet utilisateur possède déjà un profil !");
                return;
            }

            // Création du profil
            Profil profil = new Profil();
            profil.setIdUser(userId);
            profil.setAvatar(txtAvatar.getText());
            profil.setBio(txtBio.getText());
            profil.setPreferences(txtPreferences.getText());

            // Ajouter le profil
            profilService.addProfil(profil);
            showAlert("Succès", "Profil ajouté avec succès !");
            clearFields();

            // Ouvrir automatiquement la fenêtre du profil après l'ajout
            openProfilWindow(userId);

        } catch (SQLException e) {
            showAlert("Erreur SQL", "Erreur lors de l'ajout du profil : " + e.getMessage());
        }
    }

    private void openProfilWindow(int userId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AfficherProfil.fxml"));
            Parent root = loader.load();

            AfficherProfil controller = loader.getController();
            controller.setUserId(userId);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Profil de l'Utilisateur");
            stage.show();

        } catch (IOException e) {
            System.err.println("Erreur lors de l'ouverture de la fenêtre de profil : " + e.getMessage());
        }
    }

    // Méthode pour afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Réinitialisation des champs
    private void clearFields() {
        txtAvatar.clear();
        txtBio.clear();
        txtPreferences.clear();
        comboUser.getSelectionModel().clearSelection();
    }
}
