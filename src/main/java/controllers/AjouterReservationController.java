package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.reservation;
import service.ReservationService;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.regex.Pattern;

public class AjouterReservationController {

    @FXML private TextField txtIdR;
    @FXML private TextField txtIdP;
    @FXML private TextField txtNomC;
    @FXML private TextField txtEmail;
    @FXML private TextField txtDateR;
    @FXML private TextField txtStatus;

    // 🔔 Affiche une alerte avec un message donné
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // 📧 Vérifie le format de l'email avec une regex
    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return Pattern.matches(emailRegex, email);
    }

    // 📅 Vérifie si la date est valide et retourne un java.sql.Date si elle l'est
    private java.sql.Date parseDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false); // Désactive la tolérance des dates invalides
        try {
            java.util.Date utilDate = sdf.parse(dateString);
            return new java.sql.Date(utilDate.getTime());
        } catch (ParseException e) {
            showAlert("Erreur de Date", "Le format de la date doit être 'yyyy-MM-dd'.");
            return null;
        }
    }

    @FXML
    void addReservation(ActionEvent event) {
        // ✅ Vérification des champs vides
        if (txtIdR.getText().isEmpty() || txtIdP.getText().isEmpty() || txtNomC.getText().isEmpty() ||
                txtEmail.getText().isEmpty() || txtDateR.getText().isEmpty() || txtStatus.getText().isEmpty()) {
            showAlert("Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        // 🆔 Vérification des IDs (doivent être des entiers positifs)
        int id_R, id_P;
        try {
            id_R = Integer.parseInt(txtIdR.getText());
            id_P = Integer.parseInt(txtIdP.getText());
            if (id_R <= 0 || id_P <= 0) {
                showAlert("Erreur d'ID", "Les identifiants doivent être des nombres positifs.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur d'ID", "Les identifiants doivent être des nombres valides.");
            return;
        }

        // 🧑 Nom du client non vide et longueur valide
        String nomC = txtNomC.getText().trim();
        if (nomC.length() < 2) {
            showAlert("Erreur de Nom", "Le nom du client doit contenir au moins 2 caractères.");
            return;
        }

        // 📧 Vérification de l'email
        String email = txtEmail.getText().trim();
        if (!isValidEmail(email)) {
            showAlert("Erreur d'Email", "Veuillez saisir une adresse email valide.");
            return;
        }

        // 📅 Vérification de la date
        String dateString = txtDateR.getText().trim();
        java.sql.Date sqlDate = parseDate(dateString);
        if (sqlDate == null) return;  // Erreur déjà affichée si null

        // 📝 Vérification du statut
        String status = txtStatus.getText().trim();
        if (status.isEmpty()) {
            showAlert("Erreur de Statut", "Le statut ne peut pas être vide.");
            return;
        }

        // ✅ Si tout est valide, créer et ajouter la réservation
        reservation r = new reservation(id_R, id_P, nomC, email, sqlDate, status);
        ReservationService sr = new ReservationService();

        try {
            sr.ajouter(r);
            showAlert("Succès", "Réservation ajoutée avec succès !");
        } catch (SQLException e) {
            showAlert("Erreur Base de Données", "Échec de l'ajout : " + e.getMessage());
            return;
        }

        // 🔄 Navigation vers l'affichage des réservations après ajout
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherReservation.fxml"));
            Parent root = loader.load();

            AfficherReservationController ar = loader.getController();
            ar.loadReservations(); // Actualise le TableView avec les dernières réservations

            txtNomC.getScene().setRoot(root); // Remplace la scène actuelle par la nouvelle
        } catch (IOException e) {
            showAlert("Erreur de Navigation", "Impossible de charger l'affichage des réservations.");
        }
    }

    // 🔄 Navigation vers ModifierReservation.fxml
    public void goToModifier(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ModifierReservation.fxml")));
            ((Stage) txtIdR.getScene().getWindow()).setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Erreur de Navigation", "Impossible d'accéder à la modification des réservations.");
        }
    }

    // 🔄 Navigation vers SupprimerReservation.fxml
    public void goToSupprimer(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/SupprimerReservation.fxml"));
            ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Erreur de Navigation", "Impossible d'accéder à la suppression des réservations.");
        }
    }

    // 🔄 Navigation vers AjouterPaiement.fxml
    @FXML
    public void goToPaiement(ActionEvent actionEvent) {
        System.out.println("Aller à Paiement");

        try {
            // Charger le fichier FXML de la scène Paiement
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPaiement.fxml"));
            Parent root = loader.load();

            // Récupérer la fenêtre actuelle et changer la scène
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));  // Mettre à jour la scène
        } catch (IOException e) {
            // Si une erreur se produit lors du chargement de la scène
            System.err.println("Erreur lors du chargement de la scène Paiement : " + e.getMessage());
        }
    }
}
