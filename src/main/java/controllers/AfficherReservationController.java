package controllers;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import service.ReservationService;
import models.reservation;
import models.Evenement;
import javafx.beans.property.SimpleStringProperty;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
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
    private TableColumn<reservation, String> evenementColumn;

    @FXML
    private TextField rlist;

    @FXML
    private Button importerButton; // Bouton "Importer PDF"

    private final ReservationService reservationService = new ReservationService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        loadReservations();
    }

    private void setupTableColumns() {
        nombrePlacesColumn.setCellValueFactory(new PropertyValueFactory<>("nombre_places"));
        typeReservationColumn.setCellValueFactory(new PropertyValueFactory<>("type_reservation"));
        codeConfirmationColumn.setCellValueFactory(new PropertyValueFactory<>("code_confirmation"));
        remarqueColumn.setCellValueFactory(new PropertyValueFactory<>("remarque"));

        evenementColumn.setCellValueFactory(cellData -> {
            Evenement ev = cellData.getValue().getEvenement();
            return new SimpleStringProperty(ev != null ? ev.getNom() : "Aucun");
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

    @FXML
    public void filterReservations() {
        String filterText = rlist.getText().trim();

        try {
            ObservableList<reservation> filteredReservations;

            if (filterText.isEmpty()) {
                filteredReservations = FXCollections.observableArrayList(reservationService.recuperer());
            } else {
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



    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void onImporterButtonClick() {
        reservation selectedReservation = reservationTable.getSelectionModel().getSelectedItem();

        if (selectedReservation != null) {
            genererPDF(
                    String.valueOf(selectedReservation.getNombre_places()),
                    selectedReservation.getType_reservation(),
                    selectedReservation.getRemarque()
            );
        } else {
            showError("Aucune sélection", "Veuillez sélectionner une réservation pour générer un PDF.");
        }
    }

    @FXML
    public void genererPDF(String nombrePlaces, String typeReservation, String remarque) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                PdfWriter writer = new PdfWriter(file.getAbsolutePath());
                PdfDocument pdfDoc = new PdfDocument(writer);
                Document document = new Document(pdfDoc);

                document.add(new Paragraph("Confirmation de Réservation"));
                document.add(new Paragraph("Nombre de place: " + nombrePlaces));
                document.add(new Paragraph("Type de réservation: " + typeReservation));
                document.add(new Paragraph("Remarque: " + remarque));

                document.close();
                System.out.println("PDF généré avec succès !");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openModificationDialog(reservation reservation) {
        Dialog<Pair<Integer, String>> dialog = new Dialog<>();
        dialog.setTitle("Modifier la réservation");
        dialog.setHeaderText("Modifier les détails de la réservation");

        // Boutons de la boîte de dialogue
        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Champs de saisie
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nombrePlacesField = new TextField(String.valueOf(reservation.getNombre_places()));
        TextField typeReservationField = new TextField(reservation.getType_reservation());
        TextField remarqueField = new TextField(reservation.getRemarque());

        grid.add(new Label("Nombre de places:"), 0, 0);
        grid.add(nombrePlacesField, 1, 0);
        grid.add(new Label("Type de réservation:"), 0, 1);
        grid.add(typeReservationField, 1, 1);
        grid.add(new Label("Remarque:"), 0, 2);
        grid.add(remarqueField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Gestion du bouton "Enregistrer"
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Pair<>(Integer.parseInt(nombrePlacesField.getText()), typeReservationField.getText());
            }
            return null;
        });

        Optional<Pair<Integer, String>> result = dialog.showAndWait();

        result.ifPresent(newValues -> {
            reservation.setNombre_places(newValues.getKey());
            reservation.setType_reservation(newValues.getValue());
            reservation.setRemarque(remarqueField.getText());

            try {
                reservationService.modifier(reservation); // Met à jour la BD
                reservationTable.refresh(); // Rafraîchir la TableView
            } catch (SQLException e) {
                showError("Erreur de mise à jour", "Impossible de modifier la réservation !");
            }
        });
    }


    @FXML
    public void onModifierButtonClick() {
        reservation selectedReservation = reservationTable.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            openModificationDialog(selectedReservation);
        } else {
            showError("Aucune sélection", "Veuillez sélectionner une réservation à modifier.");
        }
    }

    @FXML
    public void goToPaiement(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPaiement.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la scène Paiement : " + e.getMessage());
        }
    }




}