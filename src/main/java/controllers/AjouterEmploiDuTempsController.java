package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Événement;
import models.TypeV;
import services.ServiceEmploiDuTemps;
import services.ÉvénementService;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AjouterEmploiDuTempsController {

    @FXML
    private ComboBox<Événement> comboTournoi; // Sélection du tournoi (événement de type TOURNOIS)
    @FXML
    private DatePicker datePicker;           // Date du match (pour toutes les parties)
    @FXML
    private Button btnAjouter;
    @FXML
    private Button btnAnnuler;

    private final ÉvénementService evenementService = new ÉvénementService();
    private final ServiceEmploiDuTemps edtService = new ServiceEmploiDuTemps();
    private Runnable onUpdateSuccess; // Callback éventuel après ajout

    @FXML
    public void initialize() {
        chargerTournois();
        // Configuration du DatePicker pour désactiver et colorer en rouge les dates antérieures à aujourd'hui
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ff0000;"); // Fond rouge
                }
            }
        });
        datePicker.setValue(LocalDate.now());
    }

    private void chargerTournois() {
        try {
            List<Événement> evenements = evenementService.recuperer();
            // Ne conserver que les événements de type TOURNOIS
            evenements.removeIf(e -> e.getType() != TypeV.TOURNOIS);
            comboTournoi.setItems(FXCollections.observableArrayList(evenements));
            comboTournoi.setConverter(new javafx.util.StringConverter<Événement>() {
                @Override
                public String toString(Événement event) {
                    return event != null ? event.getNom() : "";
                }
                @Override
                public Événement fromString(String string) {
                    return comboTournoi.getItems().stream()
                            .filter(e -> e.getNom().equals(string))
                            .findFirst().orElse(null);
                }
            });
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les tournois : " + e.getMessage());
        }
    }

    @FXML
    private void ajouterEmploiDuTemps() {
        Événement tournoi = comboTournoi.getValue();
        LocalDate localDate = datePicker.getValue();
        if (tournoi == null || localDate == null) {
            showAlert("Erreur", "Veuillez sélectionner un tournoi et une date.");
            return;
        }
        // Vérifier que la date n'est pas antérieure à aujourd'hui
        if (localDate.isBefore(LocalDate.now())) {
            showAlert("Erreur", "La date doit être supérieure ou égale à aujourd'hui.");
            return;
        }
        java.sql.Date matchDate = java.sql.Date.valueOf(localDate);
        try {
            // Générer automatiquement 5 parties pour le tournoi à la date donnée
            edtService.genererEmploiDuTempsAuto(tournoi, matchDate, 5);
            showAlert("Succès", "Emploi du temps généré avec succès !");
            if (onUpdateSuccess != null) {
                onUpdateSuccess.run();
            }
            // La fenêtre reste ouverte (ligne fermerFenetre() non appelée)
        } catch (SQLException e) {
            showAlert("Erreur", "Problème lors de la génération : " + e.getMessage());
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
