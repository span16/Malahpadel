package tn.esprit.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.models.AnnonceMatch;
import tn.esprit.services.AnnonceService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class AnnonceMatchController implements Initializable {

    @FXML private VBox annonceContainer;
    @FXML private Label statusLabel;

    private final AnnonceService annonceService = new AnnonceService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chargerAnnonces();
    }

    public void chargerAnnonces() {
        annonceContainer.getChildren().clear();
        try {
            List<AnnonceMatch> annonces = annonceService.recuperer();
            for (AnnonceMatch annonce : annonces) {
                HBox annonceBox = new HBox(10);
                annonceBox.setStyle("-fx-padding: 10px; -fx-border-color: black; -fx-background-color: #f0f0f0;");

                // Labels des informations de l'annonce
                Label titreLabel = new Label("Titre: " + annonce.getTitre());
                Label dateLabel = new Label("Date: " + annonce.getDate_Heure().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                Label lieuLabel = new Label("Lieu: " + annonce.getLieu());
                Label joueursLabel = new Label("Joueurs: " + annonce.getJoueursRecherches());
                Label niveauLabel = new Label("Niveau: " + annonce.getNiveau());
                Label descriptionLabel = new Label("Description: " + annonce.getDescription());

                // Boutons Modifier et Supprimer
                Button modifierBtn = new Button("Modifier");
                modifierBtn.setOnAction(e -> ouvrirModifierPage(annonce));

                Button supprimerBtn = new Button("Supprimer");
                supprimerBtn.setOnAction(e -> supprimer(annonce));

                annonceBox.getChildren().addAll(titreLabel, dateLabel, lieuLabel, joueursLabel, niveauLabel, descriptionLabel, modifierBtn, supprimerBtn);
                annonceContainer.getChildren().add(annonceBox);
            }
        } catch (SQLException e) {
            statusLabel.setText("Erreur de chargement !");
            e.printStackTrace();
        }
    }

    @FXML
    private void ouvrirModifierPage(AnnonceMatch annonce) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Edit.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modifier l'annonce");

            Scene scene = new Scene(loader.load());
            ModifierPageController controller = loader.getController();
            controller.initData(annonce, this);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void supprimer(AnnonceMatch annonce) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer cette annonce ?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmation de suppression");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    annonceService.supprimer(annonce);
                    chargerAnnonces();
                    statusLabel.setText("Annonce supprimée !");
                } catch (SQLException e) {
                    statusLabel.setText("Erreur lors de la suppression !");
                    e.printStackTrace();
                }
            }
        });
    }
}
