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
        setupTableColumns(); // Initialisation des colonnes
        loadReservations();  // Chargement des réservations
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

    // Méthode pour aller à la fenêtre de modification
    public void goToModifier(ActionEvent actionEvent) {
        try {
            // Charger le FXML pour la scène de modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReservation.fxml"));
            Parent root = loader.load();  // This will load the FXML as the root node

            // Utiliser 'actionEvent' au lieu de 'event'
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

            // Set the new scene using the loaded root
            stage.setScene(new Scene(root));  // Use 'root' instead of 'modifierPane'

            // Optionally, you can also set the title of the stage
            stage.setTitle("Modifier Reservation");

            // Show the stage (this is usually not needed as the stage is already visible)
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible de charger la scène de modification.");
        }
    }




    // Méthode pour aller à la fenêtre de paiement
    public void goToPaiement(ActionEvent actionEvent) {
        System.out.println("Aller à Paiement");

        try {
            // Charger le fichier FXML de la scène Paiement
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/Paiement.fxml"));
            Parent root = loader.load();

            // Récupérer la fenêtre actuelle et changer la scène
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));  // Mettre à jour la scène
        } catch (IOException e) {
            // Si une erreur se produit lors du chargement de la scène
            System.err.println("Erreur lors du chargement de la scène Paiement : " + e.getMessage());
        }
    }

    // Méthode pour afficher une alerte d'erreur
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
