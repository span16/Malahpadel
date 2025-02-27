package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModifierPaiementController {

    @FXML private TextField txtId_P;
    @FXML private TextField txtId_R;
    @FXML private TextField txtMontant;
    @FXML private TextField txtStatus_P;
    @FXML private Button btnSupprimer;

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void modifierPaiement(ActionEvent actionEvent) {
        try {
            int id_P = Integer.parseInt(txtId_P.getText().trim());

            if (id_P <= 0) {
                showAlert(Alert.AlertType.ERROR, "ID invalide", "L'ID du paiement doit être un entier positif.");
                return;
            }

            String url = "jdbc:mysql://localhost:3306/pidev";
            String username = "root";
            String password = "";

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String query = "SELECT * FROM paiement WHERE id_P = ?";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setInt(1, id_P);
                    ResultSet resultSet = stmt.executeQuery();

                    if (resultSet.next()) {
                        float montant = Float.parseFloat(txtMontant.getText().trim());
                        String status_P = txtStatus_P.getText().trim();

                        String updateQuery = "UPDATE paiement SET montant = ?, status_P = ? WHERE id_P = ?";
                        try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                            updateStmt.setFloat(1, montant);
                            updateStmt.setString(2, status_P);
                            updateStmt.setInt(3, id_P);

                            int affectedRows = updateStmt.executeUpdate();
                            if (affectedRows > 0) {
                                showAlert(Alert.AlertType.INFORMATION, "Succès", "Paiement modifié avec succès !");
                            } else {
                                showAlert(Alert.AlertType.WARNING, "Erreur", "Le paiement n'a pas été trouvé.");
                            }
                        }
                    } else {
                        showAlert(Alert.AlertType.WARNING, "Erreur", "Aucun paiement trouvé avec cet ID.");
                    }
                }
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de format", "Veuillez entrer des valeurs valides.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Une erreur est survenue lors de la modification : " + e.getMessage());
        }
    }

    @FXML
    private void goToSupprimer(ActionEvent event) {
        try {
            int id_P = Integer.parseInt(txtId_P.getText().trim());

            if (id_P <= 0) {
                showAlert(Alert.AlertType.ERROR, "ID invalide", "L'ID du paiement doit être un entier positif.");
                return;
            }

            String url = "jdbc:mysql://localhost:3306/pidev";
            String username = "root";
            String password = "";

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String query = "DELETE FROM paiement WHERE id_P = ?";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setInt(1, id_P);
                    int affectedRows = stmt.executeUpdate();

                    if (affectedRows > 0) {
                        showAlert(Alert.AlertType.INFORMATION, "Succès", "Paiement supprimé avec succès !");
                        Stage stage = (Stage) btnSupprimer.getScene().getWindow();
                        stage.close();
                    } else {
                        showAlert(Alert.AlertType.WARNING, "Erreur", "Aucun paiement trouvé avec cet ID.");
                    }
                }
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Une erreur est survenue lors de la suppression : " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de format", "Veuillez entrer un ID valide.");
        }
    }
}
