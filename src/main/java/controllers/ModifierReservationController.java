package controllers;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Evenement;
import models.reservation;
import service.ReservationService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;



public class ModifierReservationController {
    private int id_R;
    @FXML
    private TableView<reservation> tableView;  // Déclaration de tableView
    @FXML
    private TextField txtNombrePlaces;

    @FXML
    private TextField txtTypeReservation;

    @FXML
    private TextField txtCodeConfirmation;

    @FXML
    private TextField txtRemarque;

    @FXML
    private TextField txtNomEvenement;

    private ReservationService reservationService = new ReservationService();

    @FXML
    void modifierReservation(ActionEvent event) {
        reservation selectedReservation = tableView.getSelectionModel().getSelectedItem();  // Utilisation de tableView
        if (selectedReservation != null) {
            int id_R = selectedReservation.getId_R();  // Récupérer l'ID de la réservation sélectionnée

            try {
                // Vérifier que la réservation existe
                if (reservationService.reservationExists(id_R)) {
                    // Récupération des valeurs saisies
                    int nombrePlaces = Integer.parseInt(txtNombrePlaces.getText());
                    String typeReservation = txtTypeReservation.getText();
                    int codeConfirmation = Integer.parseInt(txtCodeConfirmation.getText());
                    String remarque = txtRemarque.getText();
                    String nomEvenement = txtNomEvenement.getText();  // Récupérer le nom de l'événement

                    // Créer un objet Evenement avec le nom de l'événement
                    Evenement evenement = new Evenement();
                    evenement.setNom(nomEvenement);

                    // Appeler la méthode modifier de ReservationService avec l'objet Evenement
                    int rowsUpdated = reservationService.modifier(id_R, nombrePlaces, typeReservation, codeConfirmation, remarque, evenement);

                    if (rowsUpdated > 0) {
                        System.out.println("✅ Réservation mise à jour avec succès !");
                    } else {
                        System.out.println("⚠️ Aucune réservation trouvée avec l'ID : " + id_R);
                    }
                } else {
                    showError("Réservation introuvable", "Aucune réservation trouvée avec l'ID : " + id_R);
                }
            } catch (NumberFormatException e) {
                showError("Erreur de format", "Veuillez saisir des valeurs valides.");
            } catch (SQLException e) {
                showError("Erreur SQL", "Erreur lors de la mise à jour de la réservation : " + e.getMessage());
            }
        } else {
            showError("Aucune sélection", "Veuillez sélectionner une réservation à modifier.");
        }
    }

    public void goToSupprimer(ActionEvent event) {
        System.out.println("Le bouton a été cliqué !");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SupprimerReservation.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible de charger la scène de suppression.");
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setReservation(reservation selectedReservation) {
        // Stocker l'ID de la réservation
        this.id_R = selectedReservation.getId_R();

        // Remplir les champs avec les valeurs de la réservation sélectionnée
        txtNombrePlaces.setText(String.valueOf(selectedReservation.getNombre_places()));
        txtTypeReservation.setText(selectedReservation.getType_reservation());
        txtCodeConfirmation.setText(String.valueOf(selectedReservation.getCode_confirmation()));
        txtRemarque.setText(selectedReservation.getRemarque());

        // Récupérer le nom de l'événement via l'objet Evenement
        if (selectedReservation.getEvenement() != null) {
            txtNomEvenement.setText(selectedReservation.getEvenement().getNom());
        } else {
            txtNomEvenement.setText("Aucun événement associé");
        }
    }






}