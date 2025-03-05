package controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.TypeV;
import models.Événement;
import services.ÉvénementService;
import tests.MainFX;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class Afficherevnet {

    @FXML
    private WebView mapView; // WebView pour la carte Google Maps
    @FXML
    private ComboBox<TypeV> comboFiltreType; // ComboBox pour filtrer par type
    @FXML
    private FlowPane flowPane; // FlowPane qui contiendra les cartes d'événements
    @FXML
    private ScrollPane scrollPane; // Pour permettre le scroll si nécessaire
    @FXML
    private TextField txtRecherche; // Champ de recherche (correspond à fx:id="txtRecherche")

    private ÉvénementService eventService;

    @FXML
    public void initialize() {
        eventService = new ÉvénementService();

        // Initialisation du filtre par type
        comboFiltreType.setItems(FXCollections.observableArrayList(TypeV.values()));
        comboFiltreType.setOnAction(e -> afficherEvenementsFiltres());

        // Affichage initial de tous les événements
        afficherEvenementsFiltres();

        // Charger la carte Google Maps pour "Paris" par défaut
        afficherCarte("Paris");
    }

    // Charge la carte Google Maps dans le WebView pour l'adresse spécifiée
    private void afficherCarte(String adresse) {
        if (mapView == null) {
            System.out.println("❌ WebView (mapView) est null !");
            return;
        }
        WebEngine webEngine = mapView.getEngine();
        String googleMapsUrl = "https://www.google.com/maps/search/?api=1&query=" + adresse.replace(" ", "+");
        webEngine.load(googleMapsUrl);
    }

    // Récupère et affiche les événements en fonction du type sélectionné
    private void afficherEvenementsFiltres() {
        flowPane.getChildren().clear();
        TypeV typeSelectionne = comboFiltreType.getValue();
        try {
            List<Événement> allEvents = eventService.recuperer();
            if (typeSelectionne != null) {
                allEvents = allEvents.stream()
                        .filter(evt -> evt.getType() == typeSelectionne)
                        .collect(Collectors.toList());
            }
            for (Événement evt : allEvents) {
                VBox carte = creerCarteEvenement(evt);
                flowPane.getChildren().add(carte);
            }
        } catch (SQLException ex) {
            System.out.println("❌ Erreur récupération événements : " + ex.getMessage());
        }
    }

    // Crée une "carte" (VBox) pour afficher un événement
    private VBox creerCarteEvenement(Événement evt) {
        VBox vbox = new VBox(5);
        vbox.setStyle("-fx-border-color: #ccc; -fx-padding: 10; -fx-background-color: #f9f9f9;");
        vbox.setPrefWidth(120);

        // Afficher l'image associée à l'événement (si disponible)
        ImageView imgEvt = new ImageView();
        imgEvt.setFitWidth(100);
        imgEvt.setFitHeight(80);
        if (evt.getImageUrl() != null && !evt.getImageUrl().isEmpty()) {
            try {
                Image img = new Image(evt.getImageUrl(), true);
                imgEvt.setImage(img);
            } catch (Exception e) {
                System.out.println("⚠ Impossible de charger l'image : " + e.getMessage());
            }
        }

        Label lblNom = new Label("Nom : " + evt.getNom());
        Label lblDate = new Label("Date : " + evt.getDate());
        Label lblType = new Label("Type : " + evt.getType());
        Label lblTerrain = new Label("Terrain : " +
                (evt.getTerrain() != null ? evt.getTerrain().getNom() : "Aucun"));

        Button btnModifier = new Button("Modifier");
        btnModifier.setOnAction(e -> modifierEvenement(evt));

        Button btnSupprimer = new Button("Supprimer");
        btnSupprimer.setOnAction(e -> supprimerEvenement(evt));

        vbox.getChildren().addAll(imgEvt, lblNom, lblDate, lblType, lblTerrain, btnModifier, btnSupprimer);
        return vbox;
    }

    // Ouvre la fenêtre de modification pour l'événement sélectionné
    private void modifierEvenement(Événement evt) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modifierevent.fxml"));
            Parent root = loader.load();
            Modifierevent controller = loader.getController();
            controller.setÉvénement(evt);
            controller.setOnUpdateSuccess(() -> afficherEvenementsFiltres());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Événement");
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Supprime l'événement après confirmation
    private void supprimerEvenement(Événement evt) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer l'événement " + evt.getNom() + " ?");
        ButtonType btnOui = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnNon = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmation.getButtonTypes().setAll(btnOui, btnNon);

        confirmation.showAndWait().ifPresent(response -> {
            if (response == btnOui) {
                try {
                    eventService.supprimer(evt);
                    afficherEvenementsFiltres();
                } catch (SQLException ex) {
                    System.out.println("❌ Erreur lors de la suppression : " + ex.getMessage());
                }
            }
        });
    }

    // Méthode appelée par le bouton "Retour"
    @FXML
    void retourAjouterEvent() {
        MainFX.switchToAjouterevent();
    }

    // Méthode pour rechercher des événements par nom (appelée depuis le FXML)
    @FXML
    void rechercherEvenements(ActionEvent event) {
        String motCle = txtRecherche.getText();
        if (motCle == null || motCle.trim().isEmpty()) {
            // Si le champ est vide, on affiche tous les événements
            afficherEvenementsFiltres();
            return;
        }
        try {
            List<Événement> events = eventService.rechercherParNom(motCle);
            flowPane.getChildren().clear();
            for (Événement evt : events) {
                VBox carte = creerCarteEvenement(evt);
                flowPane.getChildren().add(carte);
            }
        } catch (SQLException ex) {
            System.out.println("❌ Erreur lors de la recherche des événements : " + ex.getMessage());
        }
    }

    // Méthode pour trier les événements par ordre alphabétique (appelée depuis le FXML)
    @FXML
    void trierAlphabetique(ActionEvent event) {
        try {
            List<Événement> events = eventService.trierParAlphabet();
            flowPane.getChildren().clear();
            for (Événement evt : events) {
                VBox carte = creerCarteEvenement(evt);
                flowPane.getChildren().add(carte);
            }
        } catch (SQLException ex) {
            System.out.println("❌ Erreur lors du tri alphabétique : " + ex.getMessage());
        }
    }

    // Méthode statique pour un éventuel ajout immédiat (non utilisée ici)
    public static void ajouterEvenement(Événement event) {
        // Dans cette version, l'affichage se recharge entièrement via afficherEvenementsFiltres()
    }
}
