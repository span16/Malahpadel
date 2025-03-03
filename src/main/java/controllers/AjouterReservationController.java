package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.reservation;
import models.Evenement;
import service.ReservationService;
import service.EvenementService;

import java.io.IOException;
import java.sql.SQLException;



public class AjouterReservationController {

    @FXML
    private TextField txtNombrePlaces;
    @FXML
    private TextField txtTypeReservation;
    @FXML
    private TextField txtCodeConfirmation;
    @FXML
    private TextField txtRemarque;
    @FXML
    private ComboBox<String> comboEvenementNom;  // Utilisation de ComboBox au lieu de txtEvenementNom


    private final EvenementService evenementService = new EvenementService();

    @FXML
    public void initialize() {
        try {
            comboEvenementNom.getItems().addAll(evenementService.getAllEvenementNames());
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des événements : " + e.getMessage());
        }
    }

    @FXML
    void addReservation(ActionEvent event) {
        // Vérification des champs
        if (txtNombrePlaces.getText().isEmpty() || txtTypeReservation.getText().isEmpty() ||
                txtCodeConfirmation.getText().isEmpty() || txtRemarque.getText().isEmpty() ||
                comboEvenementNom.getValue() == null) {  // Vérification du ComboBox
            showAlert("Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        int nombrePlaces, codeConfirmation;
        String evenementNom = comboEvenementNom.getValue();  // Récupérer le nom de l'événement depuis la ComboBox

        try {
            nombrePlaces = Integer.parseInt(txtNombrePlaces.getText());
            codeConfirmation = Integer.parseInt(txtCodeConfirmation.getText());

            if (nombrePlaces <= 0 || codeConfirmation <= 0) {
                showAlert("Erreur", "Les valeurs doivent être des nombres positifs.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur de Saisie", "Les identifiants et autres champs numériques doivent être valides.");
            return;
        }

        String typeReservation = txtTypeReservation.getText().trim();
        String remarque = txtRemarque.getText().trim();

        // Récupérer l'événement par son nom
        Evenement ev;
        try {
            ev = evenementService.getEvenementByNom(evenementNom);
            if (ev == null) {
                showAlert("Erreur", "L'événement spécifié n'existe pas.");
                return;
            }
        } catch (SQLException e) {
            showAlert("Erreur de Base de Données", "Échec de la récupération de l'événement : " + e.getMessage());
            return;
        }

        // Créer la réservation avec l'événement récupéré
        reservation r = new reservation(nombrePlaces, typeReservation, codeConfirmation, remarque, ev);
        ReservationService sr = new ReservationService();

        try {
            sr.ajouter(r);
            showAlert("Succès", "Réservation ajoutée avec succès !");
        } catch (SQLException e) {
            showAlert("Erreur Base de Données", "Échec de l'ajout : " + e.getMessage());
            return;
        }

        // Charger l'affichage des réservations après ajout
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherReservation.fxml"));
            Parent root = loader.load();

            AfficherReservationController ar = loader.getController();
            ar.loadReservations();

            txtTypeReservation.getScene().setRoot(root);
        } catch (IOException e) {
            showAlert("Erreur de Navigation", "Impossible de charger l'affichage des réservations.");
        }
    }



    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }



}