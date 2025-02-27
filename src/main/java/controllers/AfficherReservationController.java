package controllers;

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
import javafx.event.ActionEvent;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class AfficherReservationController implements Initializable {

    @FXML
    private TableView<reservation> reservationTable;

    @FXML
    private TableColumn<reservation, Integer> idRColumn;

    @FXML
    private TableColumn<reservation, Integer> idPColumn;

    @FXML
    private TableColumn<reservation, String> nomCColumn;

    @FXML
    private TableColumn<reservation, String> emailColumn;

    @FXML
    private TableColumn<reservation, java.sql.Date> dateRColumn;

    @FXML
    private TableColumn<reservation, String> statusColumn;

    @FXML
    private TextField rlist;

    private final ReservationService reservationService = new ReservationService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        loadReservations();
    }

    private void setupTableColumns() {
        idRColumn.setCellValueFactory(new PropertyValueFactory<>("id_R"));
        idPColumn.setCellValueFactory(new PropertyValueFactory<>("id_P"));
        nomCColumn.setCellValueFactory(new PropertyValueFactory<>("nomC"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        dateRColumn.setCellValueFactory(new PropertyValueFactory<>("dateR"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    public void loadReservations() {
        try {
            ObservableList<reservation> reservations = FXCollections.observableArrayList(reservationService.recuperer());
            reservationTable.setItems(reservations);
        } catch (SQLException e) {
            System.out.println("Erreur lors du chargement des réservations : " + e.getMessage());
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
