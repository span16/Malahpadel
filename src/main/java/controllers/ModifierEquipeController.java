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

public class ModifierEquipeController {

    @FXML private TextField txtNomEquipe;
    @FXML private ComboBox<String> comboJoueur1;
    @FXML private ComboBox<String> comboJoueur2;
    @FXML private Button btnModifier;
    @FXML private Button btnAnnuler;

    private Equipe equipe;
    private final EquipeService equipeService = new EquipeService();
    private final UserService userService = new UserService();
    private Runnable onUpdateSuccess;

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

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
        txtNomEquipe.setText(equipe.getNomEquipe());
        comboJoueur1.setValue(equipe.getEmailJoueur1());
        comboJoueur2.setValue(equipe.getEmailJoueur2());
    }

    @FXML
    private void modifierEquipe() {
        try {
            String nomEquipe = txtNomEquipe.getText();
            String emailJoueur1 = comboJoueur1.getValue();
            String emailJoueur2 = comboJoueur2.getValue();

            if (nomEquipe.isEmpty() || emailJoueur1 == null || emailJoueur2 == null || emailJoueur1.equals(emailJoueur2)) {
                showAlert("Erreur", "Veuillez remplir tous les champs avec des joueurs différents.");
                return;
            }

            equipe.setNomEquipe(nomEquipe);
            equipe.setEmailJoueur1(emailJoueur1);
            equipe.setEmailJoueur2(emailJoueur2);

            equipeService.modifierEquipe(equipe);
            showAlert("Succès", "Équipe modifiée !");
            if (onUpdateSuccess != null) onUpdateSuccess.run();
            fermerFenetre();
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de modifier.");
        }
    }

    @FXML
    private void fermerFenetre() {
        Stage stage = (Stage) btnModifier.getScene().getWindow();
        stage.close();
    }

    public void setOnUpdateSuccess(Runnable onUpdateSuccess) {
        this.onUpdateSuccess = onUpdateSuccess;
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
