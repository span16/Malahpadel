package controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import service.ReservationService;
import models.reservation;
import models.Evenement;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AfficherReservationController implements Initializable {

    @FXML
    private TableView<reservation> reservationTable;

    @FXML
    private TableColumn<reservation, Integer> nombrePlacesColumn;

    @FXML
    private TableColumn<reservation, String> typeReservationColumn;

    @FXML
    private TableColumn<reservation, Integer> codeConfirmationColumn;

    @FXML
    private TableColumn<reservation, String> remarqueColumn;

    @FXML
    private TableColumn<reservation, String> evenementColumn;  // Colonne pour afficher le nom de l'événement

    @FXML
    private TextField rlist; // Champ de texte pour filtrer

    private final ReservationService reservationService = new ReservationService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        loadReservations(); // Charger toutes les réservations au début
    }

    private void setupTableColumns() {
        // Définir les colonnes de la table
        nombrePlacesColumn.setCellValueFactory(new PropertyValueFactory<>("nombre_places"));
        typeReservationColumn.setCellValueFactory(new PropertyValueFactory<>("type_reservation"));
        codeConfirmationColumn.setCellValueFactory(new PropertyValueFactory<>("code_confirmation"));
        remarqueColumn.setCellValueFactory(new PropertyValueFactory<>("remarque"));

        // Modifier la colonne 'evenementColumn' pour afficher le nom de l'événement
        evenementColumn.setCellValueFactory(cellData -> {
            Evenement ev = cellData.getValue().getEvenement(); // Récupère l'objet evenement associé à la réservation
            // Retourne un SimpleStringProperty qui contient le nom de l'événement
            return new SimpleStringProperty(ev != null ? ev.getNom() : "Aucun");  // Utilise "Aucun" si ev est null
        });
    }

    public void loadReservations() {
        try {
            ObservableList<reservation> reservations = FXCollections.observableArrayList(reservationService.recuperer());
            reservationTable.setItems(reservations);
        } catch (SQLException e) {
            System.out.println("Erreur lors du chargement des réservations : " + e.getMessage());
        }
    }

    // Méthode pour filtrer les réservations en fonction de l'input dans rlist
    @FXML
    public void filterReservations() {
        String filterText = rlist.getText().trim();

        try {
            ObservableList<reservation> filteredReservations;

            if (filterText.isEmpty()) {
                // Si le champ est vide, afficher toutes les réservations
                filteredReservations = FXCollections.observableArrayList(reservationService.recuperer());
            } else {
                // Sinon, filtrer les réservations par code_confirmation
                filteredReservations = FXCollections.observableArrayList(reservationService.filterByCodeConfirmation(filterText));
            }

            reservationTable.setItems(filteredReservations);
        } catch (SQLException e) {
            System.out.println("Erreur lors du filtrage des réservations : " + e.getMessage());
        }
    }

    public void setRlist(String list) {
        if (rlist != null) {
            rlist.setText(list);
        }
    }

    public void goToModifier(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReservation.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));

            stage.setTitle("Modifier Reservation");

            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible de charger la scène de modification.");
        }
    }

    public void goToPaiement(ActionEvent actionEvent) {
        System.out.println("Aller à Paiement");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/Paiement.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la scène Paiement : " + e.getMessage());
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
