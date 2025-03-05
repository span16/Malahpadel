package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Événement;
import models.Terrain;
import models.TypeV;
import services.ÉvénementService;
import services.TerrainService;
import javafx.collections.FXCollections;
import javafx.util.StringConverter;
import java.sql.SQLException;
import java.time.LocalDate;

public class Modifierevent {

    @FXML
    private TextField txtNom;
    @FXML
    private ComboBox<TypeV> comboType;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<Terrain> comboTerrain;
    // Nouveau champ pour l'image
    @FXML
    private TextField txtImageUrl;

    @FXML
    private Button btnModifier;
    @FXML
    private Button btnAnnuler;

    private ÉvénementService événementService = new ÉvénementService();
    private Événement événement;
    private Runnable onUpdateSuccess;

    public void setÉvénement(Événement événement) {
        this.événement = événement;
        txtNom.setText(événement.getNom());
        comboType.setValue(événement.getType());

        // Conversion de java.sql.Date en LocalDate
        if (événement.getDate() != null) {
            datePicker.setValue(((java.sql.Date) événement.getDate()).toLocalDate());
        } else {
            datePicker.setValue(null);
        }

        comboTerrain.setValue(événement.getTerrain());
        // Remplir le champ image avec l'URL actuelle
        txtImageUrl.setText(événement.getImageUrl());
    }

    public void setOnUpdateSuccess(Runnable runnable) {
        this.onUpdateSuccess = runnable;
    }

    @FXML
    public void initialize() {
        comboType.setItems(FXCollections.observableArrayList(TypeV.values()));
        initTerrainComboBox();

        // Désactiver les dates passées dans le DatePicker (similaire à l'ajout)
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });

        btnModifier.setOnAction(event -> modifierÉvénement());
        btnAnnuler.setOnAction(event -> fermerFenetre());
    }

    private void initTerrainComboBox() {
        TerrainService terrainService = new TerrainService();
        try {
            comboTerrain.setItems(FXCollections.observableArrayList(terrainService.recuperer()));
            comboTerrain.setConverter(new StringConverter<Terrain>() {
                @Override
                public String toString(Terrain terrain) {
                    return terrain != null ? terrain.getNom() : "";
                }
                @Override
                public Terrain fromString(String string) {
                    return comboTerrain.getItems().stream()
                            .filter(t -> t.getNom().equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des terrains : " + e.getMessage());
        }
    }

    private void modifierÉvénement() {
        try {
            // Vérification simple : vous pouvez ajouter ici une validation similaire pour la date si nécessaire
            événement.setNom(txtNom.getText());
            événement.setType(comboType.getValue());
            événement.setDate(java.sql.Date.valueOf(datePicker.getValue()));
            événement.setTerrain(comboTerrain.getValue());
            // Mise à jour de l'image via le champ txtImageUrl
            événement.setImageUrl(txtImageUrl.getText());

            événementService.modifier(événement, événement.getId());

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement modifié !");
            fermerFenetre();

            if (onUpdateSuccess != null) {
                onUpdateSuccess.run();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la modification !");
            e.printStackTrace();
        }
    }

    private void fermerFenetre() {
        Stage stage = (Stage) btnModifier.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
