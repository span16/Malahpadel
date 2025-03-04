package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import models.Equipe;
import services.EquipeService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherEquipeController {

    @FXML private TableView<Equipe> tableEquipes;
    @FXML private TableColumn<Equipe, String> colNomEquipe;
    @FXML private TableColumn<Equipe, String> colJoueur1;
    @FXML private TableColumn<Equipe, String> colJoueur2;
    @FXML private Button btnModifier;
    @FXML private Button btnSupprimer;

    private final EquipeService equipeService = new EquipeService();

    @FXML
    public void initialize() {
        // ✅ Correction avec SimpleStringProperty pour éviter l'erreur "Qualifier must be an expression"
        colNomEquipe.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomEquipe()));
        colJoueur1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmailJoueur1()));
        colJoueur2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmailJoueur2()));

        chargerEquipes();
    }

    private void chargerEquipes() {
        try {
            List<Equipe> equipes = equipeService.recupererEquipes();
            tableEquipes.setItems(FXCollections.observableArrayList(equipes));
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les équipes.");
        }
    }

    @FXML
    private void modifierEquipe() {
        Equipe equipeSelectionnee = tableEquipes.getSelectionModel().getSelectedItem();
        if (equipeSelectionnee == null) {
            showAlert("Erreur", "Veuillez sélectionner une équipe.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ModifierEquipe.fxml"));
            Parent root = loader.load();

            ModifierEquipeController controller = loader.getController();
            controller.setEquipe(equipeSelectionnee);
            controller.setOnUpdateSuccess(this::chargerEquipes);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Équipe");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Problème lors de l'ouverture.");
        }
    }

    @FXML
    private void supprimerEquipe() {
        Equipe equipeSelectionnee = tableEquipes.getSelectionModel().getSelectedItem();
        if (equipeSelectionnee == null) {
            showAlert("Erreur", "Veuillez sélectionner une équipe.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer l'équipe " + equipeSelectionnee.getNomEquipe() + " ?");
        ButtonType btnOui = new ButtonType("Oui");
        ButtonType btnNon = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmation.getButtonTypes().setAll(btnOui, btnNon);

        confirmation.showAndWait().ifPresent(response -> {
            if (response == btnOui) {
                try {
                    equipeService.supprimerEquipe(equipeSelectionnee.getEquipeId());
                    chargerEquipes();
                    showAlert("Succès", "Équipe supprimée avec succès !");
                } catch (SQLException e) {
                    showAlert("Erreur", "Impossible de supprimer.");
                }
            }
        });
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
