package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.EmploiDuTemps;
import services.ServiceEmploiDuTemps;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class CalendrierController {

    @FXML private DatePicker datePicker;
    @FXML private TableView<EdtWrapper> eventTable;
    @FXML private TableColumn<EdtWrapper, String> colNom;
    @FXML private TableColumn<EdtWrapper, String> colDate;
    @FXML private TableColumn<EdtWrapper, Integer> colPartie;

    @FXML
    public void initialize() {
        // Configuration des colonnes
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomEvenement"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colPartie.setCellValueFactory(new PropertyValueFactory<>("partie"));

        datePicker.setValue(LocalDate.now());
        // Vous pouvez actualiser les données en cas de changement de date, si besoin.
        datePicker.setOnAction(e -> loadEvents());
        loadEvents();
    }

    @FXML
    public void loadEvents() {
        try {
            ServiceEmploiDuTemps service = new ServiceEmploiDuTemps();
            List<EmploiDuTemps> edtList = service.recuperer();

            // Transformation en EdtWrapper pour l'affichage dans la TableView
            List<EdtWrapper> wrappers = edtList.stream()
                    .map(edt -> {
                        // Utilisez edt.getNomEvenement() si ce champ est renseigné, sinon utilisez le nom de l'événement lié
                        String nom = (edt.getNomEvenement() != null && !edt.getNomEvenement().isEmpty())
                                ? edt.getNomEvenement()
                                : (edt.getEvenement() != null ? edt.getEvenement().getNom() : "Sans Nom");
                        String dateStr = edt.getDate().toString();
                        int partie = edt.getPartie();
                        return new EdtWrapper(nom, dateStr, partie);
                    })
                    .collect(Collectors.toList());

            eventTable.setItems(FXCollections.observableArrayList(wrappers));
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la récupération des emplois du temps : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Wrapper pour exposer les données des emplois du temps
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
