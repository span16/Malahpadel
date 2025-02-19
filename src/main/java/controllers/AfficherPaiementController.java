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
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import service.PaiementService;
import models.paiement;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AfficherPaiementController implements Initializable {

    @FXML
    private TableView<paiement> paiementTable;

    @FXML
    private TableColumn<paiement, Integer> idPColumn;

    @FXML
    private TableColumn<paiement, Integer> idRColumn;

    @FXML
    private TableColumn<paiement, Float> montantColumn;

    @FXML
    private TableColumn<paiement, String> statusColumn;

    @FXML
    private TextField pList;

    private final PaiementService paiementService = new PaiementService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns(); // Initialisation des colonnes
        loadPaiements();  // Chargement des paiements
    }

    private void setupTableColumns() {
        idPColumn.setCellValueFactory(new PropertyValueFactory<>("id_P"));
        idRColumn.setCellValueFactory(new PropertyValueFactory<>("id_R"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status_P"));
    }

    public void loadPaiements() {
        try {
            ObservableList<paiement> paiements = FXCollections.observableArrayList(paiementService.recuperer());
            paiementTable.setItems(paiements);
        } catch (SQLException e) {
            System.out.println("Erreur lors du chargement des paiements : " + e.getMessage());
        }
    }

    public void setPList(String list) {
        if (pList != null) {
            pList.setText(list);
        }
    }

    // Méthode pour aller à la fenêtre de modification
    public void goToModifier(ActionEvent actionEvent) {
        try {
            // Charger le FXML pour la scène de modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierPaiement.fxml"));
            Parent root = loader.load();  // This will load the FXML as the root node

            // Utiliser 'actionEvent' au lieu de 'event'
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

            // Set the new scene using the loaded root
            stage.setScene(new Scene(root));  // Use 'root' instead of 'modifierPane'

            // Optionally, you can also set the title of the stage
            stage.setTitle("Modifier Paiement");

            // Show the stage (this is usually not needed as the stage is already visible)
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible de charger la scène de modification.");
        }
    }

    // Méthode pour aller à la fenêtre de réservation
    public void goToReservation(ActionEvent actionEvent) {
        System.out.println("Aller à Reservation");

        try {
            // Charger le fichier FXML de la scène Reservation
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/Reservation.fxml"));
            Parent root = loader.load();

            // Récupérer la fenêtre actuelle et changer la scène
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));  // Mettre à jour la scène
        } catch (IOException e) {
            // Si une erreur se produit lors du chargement de la scène
            System.err.println("Erreur lors du chargement de la scène Reservation : " + e.getMessage());
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
