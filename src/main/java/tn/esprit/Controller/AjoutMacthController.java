package tn.esprit.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.models.AnnonceMatch;
import tn.esprit.services.AnnonceService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AjoutMacthController {
    @FXML
    private TextField titreField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField heureField;

    @FXML
    private TextField lieuField;

    @FXML
    private TextField joueursRecherchesField;

    @FXML
    private ComboBox<String> niveauComboBox;

    @FXML
    private TextArea descriptionField;

    private final AnnonceService annonceService = new AnnonceService();

    @FXML
    private void ajouterAnnonce() {
        try {
            // Récupérer les valeurs des champs
            String titre = titreField.getText().trim();
            LocalDate date = datePicker.getValue();
            String heureText = heureField.getText().trim();
            String lieu = lieuField.getText().trim();
            String joueursText = joueursRecherchesField.getText().trim();
            String niveau = niveauComboBox.getValue();
            String description = descriptionField.getText().trim();

            // Vérifier que tous les champs sont remplis
            if (titre.isEmpty() || date == null || heureText.isEmpty() || lieu.isEmpty() ||
                    joueursText.isEmpty() || niveau == null || description.isEmpty()) {
                showAlert("Erreur", "Tous les champs doivent être remplis !");
                return;
            }

            // Convertir l'heure
            LocalTime heure;
            try {
                heure = LocalTime.parse(heureText);
            } catch (Exception e) {
                showAlert("Format d'heure invalide", "Veuillez saisir une heure au format HH:MM");
                return;
            }

            // Convertir le nombre de joueurs recherchés
            int joueursRecherches;
            try {
                joueursRecherches = Integer.parseInt(joueursText);
                if (joueursRecherches <= 0) {
                    showAlert("Nombre invalide", "Le nombre de joueurs doit être supérieur à zéro.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Nombre invalide", "Veuillez saisir un nombre valide pour les joueurs recherchés.");
                return;
            }

            // Créer une annonce
            LocalDateTime dateHeure = LocalDateTime.of(date, heure);
            AnnonceMatch annonce = new AnnonceMatch(titre, dateHeure, lieu, joueursRecherches, niveau, description);

            // Ajouter l'annonce via le service
            annonceService.ajouter(annonce);

            // Afficher un message de succès
            showAlert("Succès", "Annonce ajoutée avec succès !");

            // Réinitialiser le formulaire
            resetForm();

        } catch (SQLException e) {
            showAlert("Erreur SQL", "Une erreur est survenue lors de l'ajout de l'annonce.");
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void resetForm() {
        titreField.clear();
        datePicker.setValue(null);
        heureField.clear();
        lieuField.clear();
        joueursRecherchesField.clear();
        niveauComboBox.getSelectionModel().clearSelection();
        descriptionField.clear();
    }
}
