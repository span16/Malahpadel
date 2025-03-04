package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Role;
import models.User;
import services.UserService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class Admin implements Initializable {
    @FXML
    private Button sortBtn;

    @FXML
    private ChoiceBox<String> filterChoiceBox;

    @FXML
    private Button filterBtn;

    // Navigation section (gère la visibilité des sections)
    @FXML
    private AnchorPane homeform;  // Section "Home"

    @FXML
    private AnchorPane useform;   // Section "User"

    @FXML
    private Button homeBtn;

    @FXML
    private Button userBtn;

    @FXML
    private Button logout;

    @FXML
    private Label nom;  // Label pour afficher "Welcome, [Nom]"

    // User Form fields (pour la partie "User")
    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPrenom;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtAge;

    @FXML
    private TextField txtCin;

    @FXML
    private TextField txtMdp;
    @FXML
    private ChoiceBox<Role> txtfonction; // Pour sélectionner le rôle (ADMIN, USER, etc.)

    @FXML
    private Button addBtn;

    @FXML
    private Button updateBtn;

    @FXML
    private Button deleteBtn;
    @FXML
    private TextField txtSearch;


    // TableView et ses colonnes (pour afficher la liste des utilisateurs)
    @FXML
    private TableView<User> tableUser;

    @FXML
    private TableColumn<User, String> col_MDP;

    @FXML
    private TableColumn<User, String> col_nom;

    @FXML
    private TableColumn<User, String> col_prenom;

    @FXML
    private TableColumn<User, Integer> col_age;

    @FXML
    private TableColumn<User, Integer> cin_col;

    @FXML
    private TableColumn<User, String> email_col;

    @FXML
    private TableColumn<User, Role> fonction_col;

    private final UserService userService = new UserService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialisation de la navigation : afficher "Home" par défaut
        homeform.setVisible(true);
        useform.setVisible(false);

        // Optionnel : afficher le message de bienvenue dans le Label "nom"
        // Par exemple, si vous avez stocké l'utilisateur connecté dans une session :
        // nom.setText("Bienvenue, " + SessionManager.getCurrentUser().getNom());

        // Initialisation des colonnes du TableView
        col_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        col_prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        col_age.setCellValueFactory(new PropertyValueFactory<>("age"));
        cin_col.setCellValueFactory(new PropertyValueFactory<>("cin"));
        email_col.setCellValueFactory(new PropertyValueFactory<>("email"));
        fonction_col.setCellValueFactory(new PropertyValueFactory<>("fonction"));
        col_MDP.setCellValueFactory(new PropertyValueFactory<>("mdp"));
        filterChoiceBox.getItems().addAll("Tous", "ADMIN", "USER");
        filterChoiceBox.setValue("Tous");

        // Initialiser la ChoiceBox des rôles
        txtfonction.getItems().addAll(Role.ADMIN, Role.USER);

        // Charger la liste des utilisateurs dans la TableView
        loadUsers();
        setupSearch();

        // Remplir le formulaire quand un utilisateur est sélectionné dans la TableView
        tableUser.setOnMouseClicked(e -> {
            User selected = tableUser.getSelectionModel().getSelectedItem();
            if (selected != null) {
                fillForm(selected);
            }
        });
    }
    private void setupSearch() {
        // Ajoutez un listener sur txtSearch pour déclencher la recherche dès que l'utilisateur tape
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                List<User> results = userService.searchUsers(newValue);
                ObservableList<User> data = FXCollections.observableArrayList(results);
                tableUser.setItems(data);
            } catch (SQLException e) {
                showAlert("Erreur SQL", "Erreur lors de la recherche : " + e.getMessage());
            }
        });
    }

    // Méthodes de navigation
    @FXML
    private void showHome() {
        homeform.setVisible(true);
        useform.setVisible(false);
    }

    @FXML
    private void showUser() {
        homeform.setVisible(false);
        useform.setVisible(true);
    }

    @FXML
    private void logoutAction() {
        System.out.println("Déconnexion en cours...");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
            Parent root = loader.load();
            Scene loginScene = new Scene(root);
            Stage stage = (Stage) logout.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de rediriger vers la page de login : " + e.getMessage());
        }
    }

    // Méthode pour charger la liste des utilisateurs depuis la base de données
    private void loadUsers() {
        try {
            List<User> users = userService.recuperer();
            ObservableList<User> data = FXCollections.observableArrayList(users);
            tableUser.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la liste des utilisateurs : " + e.getMessage());
        }
    }

    // Remplir le formulaire avec les informations de l'utilisateur sélectionné
    private void fillForm(User user) {
        txtNom.setText(user.getNom());
        txtPrenom.setText(user.getPrenom());
        txtEmail.setText(user.getEmail());
        txtAge.setText(String.valueOf(user.getAge()));
        txtCin.setText(String.valueOf(user.getCin()));
        txtfonction.setValue(user.getFonction());
    }

    // Nettoyer les champs du formulaire
    private void clearForm() {
        txtNom.clear();
        txtPrenom.clear();
        txtEmail.clear();
        txtAge.clear();
        txtCin.clear();
        txtfonction.setValue(null);
    }

    // Méthode pour ajouter un utilisateur
    @FXML
    private void addUser() {
        // Vérifier que tous les champs sont remplis, y compris le mot de passe
        if (txtNom.getText().isEmpty() || txtPrenom.getText().isEmpty() || txtEmail.getText().isEmpty() ||
                txtAge.getText().isEmpty() || txtCin.getText().isEmpty() || txtfonction.getValue() == null || txtMdp.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        // Vérifier que l'email est valide
        String email = txtEmail.getText();
        if (!isValidEmail(email)) {
            showAlert("Erreur", "L'email n'est pas valide !");
            return;
        }

        // Vérifier que l'email est unique
        try {
            if (userService.emailExists(email)) {
                showAlert("Erreur", "L'email est déjà utilisé !");
                return;
            }
        } catch (SQLException e) {
            showAlert("Erreur SQL", "Erreur lors de la vérification de l'email : " + e.getMessage());
            return;
        }

        // Vérifier que l'âge est un nombre valide et positif
        int age;
        try {
            age = Integer.parseInt(txtAge.getText());
            if (age <= 0) {
                showAlert("Erreur", "L'âge doit être un nombre positif !");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'âge doit être un nombre valide !");
            return;
        }

        // Vérifier que le CIN est un nombre valide et positif
        int cin;
        try {
            cin = Integer.parseInt(txtCin.getText());
            if (cin <= 0) {
                showAlert("Erreur", "Le CIN doit être un nombre positif !");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le CIN doit être un nombre valide !");
            return;
        }

        // Récupérer le rôle et le mot de passe saisis par l'utilisateur
        Role fonction = txtfonction.getValue();
        String password = txtMdp.getText();

        // Si tout est valide, ajouter l'utilisateur
        try {
            String nom = txtNom.getText();
            String prenom = txtPrenom.getText();

            // Créer un nouvel objet User
            User newUser = new User();
            newUser.setNom(nom);
            newUser.setPrenom(prenom);
            newUser.setEmail(email);
            newUser.setAge(age);
            newUser.setCin(cin);
            newUser.setMdp(password); // Utilisation du mot de passe fourni par l'utilisateur
            newUser.setEtat("Actif");   // Etat par défaut
            newUser.setFonction(fonction);  // Affectation du rôle

            // Ajouter l'utilisateur à la base de données
            userService.ajouter1(newUser);
            loadUsers();
            // Afficher un message de succès
            showAlert("Succès", "Utilisateur ajouté avec succès !");

            // Réinitialiser les champs après l'ajout
            clearForm(); // ou clearFields(), selon votre méthode existante

            // Passer directement à la fenêtre de l'utilisateur ajouté

        } catch (SQLException e) {
            showAlert("Erreur SQL", "Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }

    // Méthode pour modifier un utilisateur
    @FXML
    private void updateUser() {
        User selected = tableUser.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner un utilisateur à modifier !");
            return;
        }
        try {
            if (txtNom.getText().isEmpty() || txtPrenom.getText().isEmpty() || txtEmail.getText().isEmpty()
                    || txtAge.getText().isEmpty() || txtCin.getText().isEmpty() || txtfonction.getValue() == null) {
                showAlert("Erreur", "Veuillez remplir tous les champs !");
                return;
            }
            int age = Integer.parseInt(txtAge.getText());
            int cin = Integer.parseInt(txtCin.getText());
            Role role = txtfonction.getValue();
            String ancienEmail = selected.getEmail();

            selected.setNom(txtNom.getText());
            selected.setPrenom(txtPrenom.getText());
            selected.setEmail(txtEmail.getText());
            selected.setAge(age);
            selected.setCin(cin);
            selected.setFonction(role);

            userService.modifier(selected, ancienEmail);
            loadUsers();
            clearForm();
            showAlert("Succès", "Utilisateur modifié avec succès !");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'âge et le CIN doivent être des nombres valides !");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de modifier l'utilisateur : " + e.getMessage());
        }
    }

    // Méthode pour supprimer un utilisateur
    @FXML
    private void deleteUser() {
        User selected = tableUser.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner un utilisateur à supprimer !");
            return;
        }
        try {
            userService.Delete(selected.getId());
            loadUsers();
            clearForm();
            showAlert("Succès", "Utilisateur supprimé avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de supprimer l'utilisateur : " + e.getMessage());
        }
    }

    // Méthode pour afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
    @FXML
    private void filterUsers() {
        String filterRole = filterChoiceBox.getValue();

        // Si "Tous" est sélectionné, rechargez tous les utilisateurs
        if ("Tous".equals(filterRole)) {
            loadUsers();
            return;
        }

        try {
            // Récupérer la liste complète des utilisateurs depuis le service
            List<User> users = userService.recuperer();

            // Filtrer la liste par rôle en vérifiant que getFonction() n'est pas null
            List<User> filteredUsers = users.stream()
                    .filter(u -> u.getFonction() != null && u.getFonction().name().equalsIgnoreCase(filterRole))
                    .toList();

            ObservableList<User> data = FXCollections.observableArrayList(filteredUsers);
            tableUser.setItems(data);
        } catch (SQLException e) {
            showAlert("Erreur SQL", "Erreur lors du filtrage : " + e.getMessage());
        }
    }

    @FXML
    private void sortByName() {
        // Récupère les données actuelles de la TableView
        ObservableList<User> userList = tableUser.getItems();

        // Trie la liste par nom (insensible à la casse)
        FXCollections.sort(userList, (u1, u2) ->
                u1.getNom().compareToIgnoreCase(u2.getNom())
        );
    }
}
