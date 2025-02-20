package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Terrain;
import services.TerrainService;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;

public class Afficherterrain {

    @FXML
    private TableView<Terrain> tableTerrains;
    @FXML
    private TableColumn<Terrain, String> colNom;
    @FXML
    private TableColumn<Terrain, String> colAdresse;
    @FXML
    private TableColumn<Terrain, String> colPrix;
    @FXML
    private TableColumn<Terrain, String> colHeureOuverture;
    @FXML
    private TableColumn<Terrain, String> colHeureFermeture;
    @FXML
    private Button btnModifier;
    @FXML
    private Button btnSupprimer;

    private TerrainService terrainService = new TerrainService();
    private static final ObservableList<Terrain> listeTerrains = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colPrix.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() ->
                String.format("%.2f TND", cellData.getValue().getPrixParPersonne())));
        colHeureOuverture.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() ->
                formatTime(cellData.getValue().getHeureOuverture())));
        colHeureFermeture.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() ->
                formatTime(cellData.getValue().getHeureFermeture())));

        tableTerrains.setItems(listeTerrains);
    }

    // ✅ Ajouter un terrain dans la liste affichée
    public static void ajouterTerrain(Terrain terrain) {
        listeTerrains.add(terrain);
    }

    private String formatTime(Time time) {
        return time != null ? time.toString() : "Non spécifié";
    }

    @FXML
    void modifierTerrain() {
        Terrain selectedTerrain = tableTerrains.getSelectionModel().getSelectedItem();
        if (selectedTerrain == null) {
            showAlert(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner un terrain à modifier.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modifierterrain.fxml"));
            Parent root = loader.load();

            Modifierterrain controller = loader.getController();
            controller.setTerrain(selectedTerrain);
            controller.setOnUpdateSuccess(() -> tableTerrains.refresh());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Terrain");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void supprimerTerrain() {
        Terrain selectedTerrain = tableTerrains.getSelectionModel().getSelectedItem();
        if (selectedTerrain == null) {
            showAlert(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner un terrain à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer le terrain " + selectedTerrain.getNom() + " ?");
        ButtonType btnOui = new ButtonType("Oui");
        ButtonType btnNon = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmation.getButtonTypes().setAll(btnOui, btnNon);

        confirmation.showAndWait().ifPresent(response -> {
            if (response == btnOui) {
                try {
                    terrainService.supprimer(selectedTerrain); // ✅ Suppression de la base de données
                    listeTerrains.remove(selectedTerrain); // ✅ Suppression de la liste affichée
                    tableTerrains.refresh(); // ✅ Rafraîchir l'affichage
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Terrain supprimé avec succès !");
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression du terrain : " + e.getMessage());
                }
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
