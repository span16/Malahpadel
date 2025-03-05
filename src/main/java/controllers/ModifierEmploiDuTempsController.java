package controllers;

import com.google.api.services.calendar.model.Event;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import models.EmploiDuTemps;
import models.Événement;
import models.Equipe;
import models.TypeV;
import services.ServiceEmploiDuTemps;
import services.ÉvénementService;
import services.EquipeService;
import services.GoogleCalendarService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ModifierEmploiDuTempsController {

    // --- Nouveau : ComboBox pour choisir l'EmploiDuTemps ---
    @FXML
    private ComboBox<EmploiDuTemps> comboEdt;

    // Formulaire de modification
    @FXML private DatePicker datePicker;
    @FXML private TextField txtPartie;
    @FXML private ComboBox<Événement> comboTournoi;
    @FXML private ComboBox<Equipe> comboEquipe1;
    @FXML private ComboBox<Equipe> comboEquipe2;
    @FXML private Button btnModifier;
    @FXML private Button btnAnnuler;

    // TableView pour afficher les événements Google Calendar
    @FXML private TableView<EdtWrapper> tableEvents;
    @FXML private TableColumn<EdtWrapper, String> colNom;
    @FXML private TableColumn<EdtWrapper, String> colDate;
    @FXML private TableColumn<EdtWrapper, Integer> colPartie;

    // L'EmploiDuTemps actuellement sélectionné
    private EmploiDuTemps emploiDuTemps;

    // Services
    private final ServiceEmploiDuTemps edtService = new ServiceEmploiDuTemps();
    private final ÉvénementService evenementService = new ÉvénementService();
    private final EquipeService equipeService = new EquipeService();

    // Callback à exécuter si la mise à jour est réussie (optionnel)
    private Runnable onUpdateSuccess;

    @FXML
    public void initialize() {
        // Configuration du TableView (s'il est présent dans le FXML)
        if (tableEvents != null) {
            colNom.setCellValueFactory(new PropertyValueFactory<>("nomEvenement"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            colPartie.setCellValueFactory(new PropertyValueFactory<>("partie"));
        }

        // Charger la liste des EmploiDuTemps dans comboEdt
        chargerEmploiDuTemps();

        // Charger la liste des tournois et équipes
        chargerTournois();
        chargerEquipes();

        // Désactiver les dates passées dans le DatePicker
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ff0000;");
                }
            }
        });
    }

    /**
     * Charge la liste des EmploiDuTemps dans comboEdt pour que l'utilisateur puisse
     * en sélectionner un et le modifier.
     */
    private void chargerEmploiDuTemps() {
        try {
            // Récupérer tous les EmploiDuTemps depuis la base
            List<EmploiDuTemps> listeEdt = edtService.recuperer();

            // Injecter la liste dans la comboBox
            comboEdt.setItems(FXCollections.observableArrayList(listeEdt));

            // Définir l'affichage d'un EmploiDuTemps dans la combo (ID + nom de l'événement)
            comboEdt.setConverter(new StringConverter<EmploiDuTemps>() {
                @Override
                public String toString(EmploiDuTemps edt) {
                    if (edt == null) return "";
                    String evtName = (edt.getEvenement() != null) ? edt.getEvenement().getNom() : "Sans évènement";
                    return "ID: " + edt.getId() + " - " + evtName;
                }

                @Override
                public EmploiDuTemps fromString(String string) {
                    // Généralement, on ne convertit pas le texte en EmploiDuTemps dans ce sens
                    return null;
                }
            });

            // Lorsqu'on sélectionne un EmploiDuTemps dans la combo, on met à jour le formulaire
            comboEdt.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    setEmploiDuTemps(newValue);
                }
            });

        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les emplois du temps : " + e.getMessage());
        }
    }

    /**
     * Charge la liste des tournois (Événements de type TOURNOIS) dans la combo correspondante.
     */
    private void chargerTournois() {
        try {
            List<Événement> evenements = evenementService.recuperer();
            evenements.removeIf(e -> e.getType() != TypeV.TOURNOIS);
            comboTournoi.setItems(FXCollections.observableArrayList(evenements));
            comboTournoi.setConverter(new StringConverter<Événement>() {
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

    /**
     * Charge la liste des équipes dans comboEquipe1 et comboEquipe2.
     */
    private void chargerEquipes() {
        try {
            List<Equipe> equipes = equipeService.recupererEquipes();
            comboEquipe1.setItems(FXCollections.observableArrayList(equipes));
            comboEquipe2.setItems(FXCollections.observableArrayList(equipes));
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les équipes : " + e.getMessage());
        }
    }

    /**
     * Préremplit le formulaire avec l'objet EmploiDuTemps à modifier.
     */
    public void setEmploiDuTemps(EmploiDuTemps edt) {
        this.emploiDuTemps = edt;
        if (edt.getDate() != null) {
            // Utilisation de toLocalDate() sur java.sql.Date
            datePicker.setValue(((java.sql.Date) edt.getDate()).toLocalDate());
        } else {
            datePicker.setValue(null);
        }
        txtPartie.setText(String.valueOf(edt.getPartie()));
        comboTournoi.setValue(edt.getEvenement());
        comboEquipe1.setValue(edt.getEquipe1());
        comboEquipe2.setValue(edt.getEquipe2());
    }

    /**
     * Charge et affiche tous les événements depuis Google Calendar dans le TableView.
     */
    @FXML
    public void loadGoogleCalendarEvents() {
        try {
            List<Event> googleEvents = GoogleCalendarService.getAllEvents();
            List<EdtWrapper> wrappers = googleEvents.stream().map(event -> {
                String summary = event.getSummary() != null ? event.getSummary() : "Sans titre";
                String dateStr = "";
                if (event.getStart() != null) {
                    if (event.getStart().getDateTime() != null) {
                        dateStr = event.getStart().getDateTime().toStringRfc3339();
                    } else if (event.getStart().getDate() != null) {
                        dateStr = event.getStart().getDate().toString();
                    }
                }
                return new EdtWrapper(summary, dateStr, 0);
            }).collect(Collectors.toList());
            tableEvents.setItems(FXCollections.observableArrayList(wrappers));
        } catch (IOException | GeneralSecurityException e) {
            showAlert("Erreur", "Erreur lors de la récupération des événements Google Calendar : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Méthode de modification d'un EmploiDuTemps existant.
     * La fenêtre ne se ferme pas après la modification.
     */
    @FXML
    private void modifierEmploiDuTemps() {
        // Vérification que l'objet EmploiDuTemps est bien initialisé
        if (emploiDuTemps == null) {
            showAlert("Erreur", "Aucun emploi du temps sélectionné. Veuillez sélectionner un emploi du temps avant de modifier.");
            return;
        }
        try {
            LocalDate localDate = datePicker.getValue();
            int partie = Integer.parseInt(txtPartie.getText());
            Événement tournoi = comboTournoi.getValue();
            Equipe eq1 = comboEquipe1.getValue();
            Equipe eq2 = comboEquipe2.getValue();

            // Vérification des champs obligatoires
            if (localDate == null || tournoi == null || eq1 == null || eq2 == null || eq1.equals(eq2)) {
                showAlert("Erreur", "Veuillez remplir tous les champs correctement.");
                return;
            }

            // Mettre à jour l'objet
            emploiDuTemps.setDate(java.sql.Date.valueOf(localDate));
            emploiDuTemps.setPartie(partie);
            emploiDuTemps.setEvenement(tournoi);
            emploiDuTemps.setEquipe1(eq1);
            emploiDuTemps.setEquipe2(eq2);

            // Mise à jour dans la base
            edtService.modifier(emploiDuTemps);

            // Mise à jour (ou création) dans Google Calendar
            try {
                if (emploiDuTemps.getGoogleEventId() != null && !emploiDuTemps.getGoogleEventId().isEmpty()) {
                    GoogleCalendarService.updateGoogleCalendarEvent(emploiDuTemps.getGoogleEventId(), emploiDuTemps);
                } else {
                    String googleId = GoogleCalendarService.addEventToGoogleCalendar(emploiDuTemps);
                    emploiDuTemps.setGoogleEventId(googleId);
                    edtService.modifier(emploiDuTemps); // pour sauvegarder le nouvel ID
                }
            } catch (IOException | GeneralSecurityException e) {
                showAlert("Avertissement", "Mise à jour Google Calendar échouée : " + e.getMessage());
            }

            // Affichage du message de succès et exécution du callback, sans fermer la fenêtre
            showAlert("Succès", "Emploi du temps modifié !");
            if (onUpdateSuccess != null) {
                onUpdateSuccess.run();
            }
            // La fenêtre reste ouverte : ne pas appeler fermerFenetre()
        } catch (SQLException | NumberFormatException e) {
            showAlert("Erreur", "Problème lors de la modification : " + e.getMessage());
        }
    }

    /**
     * Ferme la fenêtre courante.
     * (Méthode non utilisée dans ce scénario puisque nous souhaitons garder la fenêtre ouverte.)
     */
    @FXML
    private void fermerFenetre() {
        Stage stage = (Stage) btnModifier.getScene().getWindow();
        stage.close();
    }

    /**
     * Définit un callback à exécuter après mise à jour réussie (optionnel).
     */
    public void setOnUpdateSuccess(Runnable onUpdateSuccess) {
        this.onUpdateSuccess = onUpdateSuccess;
    }

    /**
     * Affiche un message d'information ou d'erreur.
     */
    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Wrapper pour le TableView (affichage simplifié d'un événement Google).
     */
    public static class EdtWrapper {
        private final String nomEvenement;
        private final String date;
        private final int partie;

        public EdtWrapper(String nomEvenement, String date, int partie) {
            this.nomEvenement = nomEvenement;
            this.date = date;
            this.partie = partie;
        }

        public String getNomEvenement() {
            return nomEvenement;
        }

        public String getDate() {
            return date;
        }

        public int getPartie() {
            return partie;
        }
    }
}
