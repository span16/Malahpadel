package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Equipe;
import models.User;
import services.EquipeService;
import services.UserService;

import java.sql.SQLException;
import java.util.List;

public class AjouterEquipeController {

    @FXML private TextField txtNomEquipe;
    @FXML private ComboBox<String> comboJoueur1;
    @FXML private ComboBox<String> comboJoueur2;
    @FXML private Button btnAjouter;
    @FXML private Button btnAnnuler;

    private final EquipeService equipeService = new EquipeService();
    private final UserService userService = new UserService();

    private Runnable onUpdateSuccess; // Callback après ajout

    @FXML
    public void initialize() {
        chargerJoueurs();
    }

    private void chargerJoueurs() {
        try {
            List<User> users = userService.recuperer();
            List<String> emails = users.stream().map(User::getEmail).toList();
            comboJoueur1.setItems(FXCollections.observableArrayList(emails));
            comboJoueur2.setItems(FXCollections.observableArrayList(emails));
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les utilisateurs.");
        }
    }

    @FXML
    private void ajouterEquipe() {
        String nomEquipe = txtNomEquipe.getText();
        String emailJoueur1 = comboJoueur1.getValue();
        String emailJoueur2 = comboJoueur2.getValue();

        if (nomEquipe.isEmpty() || emailJoueur1 == null || emailJoueur2 == null || emailJoueur1.equals(emailJoueur2)) {
            showAlert("Erreur", "Veuillez remplir tous les champs avec des joueurs différents.");
            return;
        }

        try {
            User user = userService.recupererParEmail(emailJoueur1); // L'utilisateur qui crée l'équipe
            Equipe equipe = new Equipe(nomEquipe, emailJoueur1, emailJoueur2, user);
            equipeService.ajouterEquipe(equipe);

            if (onUpdateSuccess != null) onUpdateSuccess.run();
            showAlert("Succès", "Équipe ajoutée !");
            fermerFenetre();
        } catch (SQLException e) {
            showAlert("Erreur", "Problème lors de l'ajout : " + e.getMessage());
        }
    }

    @FXML
    private void fermerFenetre() {
        Stage stage = (Stage) btnAjouter.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setOnUpdateSuccess(Runnable onUpdateSuccess) {
        this.onUpdateSuccess = onUpdateSuccess;
    }
}
