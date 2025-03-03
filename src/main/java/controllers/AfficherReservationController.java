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
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Modifier la réservation");
        dialog.setHeaderText("Modifier les détails de la réservation");

        // Set the button types
        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create the fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField detailsField = new TextField(reservation.getDetails());
        TextField autreChampField = new TextField(reservation.getAutreChamp());

        grid.add(new Label("Détails:"), 0, 0);
        grid.add(detailsField, 1, 0);
        grid.add(new Label("Autre champ:"), 0, 1);
        grid.add(autreChampField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a pair of values when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Pair<>(detailsField.getText(), autreChampField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(newValues -> {
            reservation.setDetails(newValues.getKey());
            reservation.setAutreChamp(newValues.getValue());
            reservationTableView.refresh();
        });
    }





}