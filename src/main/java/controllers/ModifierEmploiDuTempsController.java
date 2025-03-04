package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.EmploiDuTemps;
import models.Événement;
import models.Equipe;
import models.TypeV;
import services.ServiceEmploiDuTemps;
import services.ÉvénementService;
import services.EquipeService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class ModifierEmploiDuTempsController {

    @FXML private TextField txtNomEvenement;
    @FXML private DatePicker datePicker;
    @FXML private TextField txtPartie;
    @FXML private ComboBox<Événement> comboTournoi;
    @FXML private ComboBox<Equipe> comboEquipe1;
    @FXML private ComboBox<Equipe> comboEquipe2;
    @FXML private Button btnModifier;
    @FXML private Button btnAnnuler;

    private EmploiDuTemps emploiDuTemps;
    private final ServiceEmploiDuTemps edtService = new ServiceEmploiDuTemps();
    private final ÉvénementService evenementService = new ÉvénementService();
    private final EquipeService equipeService = new EquipeService();
    private Runnable onUpdateSuccess;

    @FXML
    public void initialize() {
        chargerTournois();
        chargerEquipes();
    }

    private void chargerTournois() {
        try {
            List<Événement> evenements = evenementService.recuperer();
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

    private void chargerEquipes() {
        try {
            List<Equipe> equipes = equipeService.recupererEquipes();
            comboEquipe1.setItems(FXCollections.observableArrayList(equipes));
            comboEquipe2.setItems(FXCollections.observableArrayList(equipes));
            comboEquipe1.setConverter(new javafx.util.StringConverter<Equipe>() {
                @Override
                public String toString(Equipe equipe) {
                    return equipe != null ? equipe.getNomEquipe() : "";
                }
                @Override
                public Equipe fromString(String string) {
                    return comboEquipe1.getItems().stream()
                            .filter(e -> e.getNomEquipe().equals(string))
                            .findFirst().orElse(null);
                }
            });
            comboEquipe2.setConverter(new javafx.util.StringConverter<Equipe>() {
                @Override
                public String toString(Equipe equipe) {
                    return equipe != null ? equipe.getNomEquipe() : "";
                }
                @Override
                public Equipe fromString(String string) {
                    return comboEquipe2.getItems().stream()
                            .filter(e -> e.getNomEquipe().equals(string))
                            .findFirst().orElse(null);
                }
            });
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les équipes : " + e.getMessage());
        }
    }

    public void setEmploiDuTemps(EmploiDuTemps edt) {
        this.emploiDuTemps = edt;
        txtNomEvenement.setText(edt.getNomEvenement());
        // Conversion correcte de java.util.Date en LocalDate
        datePicker.setValue(edt.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        txtPartie.setText(String.valueOf(edt.getPartie()));
        comboTournoi.setValue(edt.getEvenement());
        comboEquipe1.setValue(edt.getEquipe1());
        comboEquipe2.setValue(edt.getEquipe2());
    }

    @FXML
    private void modifierEmploiDuTemps() {
        try {
            String nomEvenement = txtNomEvenement.getText();
            LocalDate localDate = datePicker.getValue();
            int partie = Integer.parseInt(txtPartie.getText());
            Événement tournoi = comboTournoi.getValue();
            Equipe eq1 = comboEquipe1.getValue();
            Equipe eq2 = comboEquipe2.getValue();

            if (nomEvenement.isEmpty() || localDate == null || tournoi == null || eq1 == null || eq2 == null || eq1.equals(eq2)) {
                showAlert("Erreur", "Veuillez remplir tous les champs correctement.");
                return;
            }

            emploiDuTemps.setNomEvenement(nomEvenement);
            emploiDuTemps.setDate(java.sql.Date.valueOf(localDate));
            emploiDuTemps.setPartie(partie);
            emploiDuTemps.setEvenement(tournoi);
            emploiDuTemps.setEquipe1(eq1);
            emploiDuTemps.setEquipe2(eq2);

            edtService.modifier(emploiDuTemps);
            showAlert("Succès", "Emploi du temps modifié !");
            if (onUpdateSuccess != null) {
                onUpdateSuccess.run();
            }
            fermerFenetre();
        } catch (SQLException | NumberFormatException e) {
            showAlert("Erreur", "Problème lors de la modification : " + e.getMessage());
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
