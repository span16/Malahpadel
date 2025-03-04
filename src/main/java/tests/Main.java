package tests;
import models.Role;
import models.User;
import models.Profil;
import services.UserService;
import services.ProfilService;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {
        // Initialisation des services
        UserService userService = new UserService();
        ProfilService profilService = new ProfilService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Affichage du menu des options
            System.out.println("\n=== Menu ===");
            System.out.println("1. Ajouter un utilisateur");
            System.out.println("2. Ajouter un profil");
            System.out.println("3. Modifier un utilisateur");
            System.out.println("4. Modifier un profil");
            System.out.println("5. Supprimer un utilisateur");
            System.out.println("6. Supprimer un profil");
            System.out.println("7. Quitter");
            System.out.print("Choisissez une option : ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne laissée par nextInt()

            switch (choice) {
                case 1:
                    ajouterUtilisateur(userService, scanner);
                    break;
                case 2:
                    ajouterProfil(profilService, userService, scanner);
                    break;
                case 3:
                    modifierUtilisateur(userService, scanner);
                    break;
                case 4:
                    modifierProfil(profilService, scanner);
                    break;
                case 5:
                    supprimerUtilisateur(userService, scanner);
                    break;
                case 6:
                    supprimerProfil(profilService, scanner);
                    break;
                case 7:
                    System.out.println("Au revoir!");
                    return;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
                    break;
            }
        }
    }

    private static void ajouterUtilisateur(UserService userService, Scanner scanner) {
        System.out.println("\n=== Ajouter un utilisateur ===");
        System.out.print("Nom : ");
        String nom = scanner.nextLine();
        System.out.print("Prénom : ");
        String prenom = scanner.nextLine();
        System.out.print("Email : ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String mdp = scanner.nextLine();
        System.out.print("Age : ");
        int age = scanner.nextInt();
        System.out.print("CIN : ");
        int cin = scanner.nextInt();
        scanner.nextLine(); // Consommer la ligne restante
        System.out.print("Etat : ");
        String etat = scanner.nextLine();
        System.out.print("Fonction : ");
        Role fonction = Role.valueOf(scanner.nextLine()); // Cela crée une erreur


        User user = new User(age, cin, nom, prenom, email, mdp, etat, fonction);
        try {
            userService.ajouter1(user);  // Utilisation de la méthode ajouter1 de l'interface Iuser
            System.out.println("Utilisateur ajouté avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }

    private static void ajouterProfil(ProfilService profilService, UserService userService, Scanner scanner) throws SQLException {
        // Récupérer la liste des utilisateurs existants
        List<User> users = userService.recuperer();

        // Si aucun utilisateur n'existe, afficher un message et quitter
        if (users.isEmpty()) {
            System.out.println("Aucun utilisateur trouvé dans la base de données.");
            return;
        }

        // Afficher les utilisateurs existants
        System.out.println("\n=== Ajouter un profil ===");
        System.out.println("Liste des utilisateurs existants :");
        for (User user : users) {
            System.out.println("ID: " + user.getId() + ", Nom: " + user.getNom() + " " + user.getPrenom());
        }

        System.out.print("Entrez l'ID de l'utilisateur pour lequel vous voulez ajouter un profil : ");
        int idUser = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne après nextInt()

        // Vérifier si l'utilisateur existe dans la base de données
        boolean userExists = userService.userExists(idUser);
        if (!userExists) {
            System.out.println("Aucun utilisateur trouvé avec cet ID.");
            return;
        }

        // Demander les informations du profil à ajouter
        System.out.print("Entrez l'avatar : ");
        String avatar = scanner.nextLine();
        System.out.print("Entrez la bio : ");
        String bio = scanner.nextLine();
        System.out.print("Entrez les préférences : ");
        String preferences = scanner.nextLine();

        // Créer le profil à ajouter
        Profil profil = new Profil(idUser, avatar, bio, preferences);

        try {
            profilService.addProfil(profil);
            System.out.println("Profil ajouté avec succès pour l'utilisateur ID " + idUser + ".");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du profil : " + e.getMessage());
        }
    }

    private static void modifierUtilisateur(UserService userService, Scanner scanner) {
        System.out.println("\n=== Modifier un utilisateur ===");
        System.out.print("Entrez l'email de l'utilisateur à modifier : ");
        String ancienEmail = scanner.nextLine();

        // Vérifier si l'utilisateur existe
        try {
            List<User> users = userService.recuperer();
            User userToUpdate = null;
            for (User user : users) {
                if (user.getEmail().equals(ancienEmail)) {
                    userToUpdate = user;
                    break;
                }
            }

            if (userToUpdate == null) {
                System.out.println("Aucun utilisateur trouvé avec cet email.");
                return;
            }

            // Demander les nouvelles informations pour l'utilisateur
            System.out.print("Nouveau nom : ");
            String nom = scanner.nextLine();
            System.out.print("Nouveau prénom : ");
            String prenom = scanner.nextLine();
            System.out.print("Nouvelle email : ");
            String email = scanner.nextLine();
            System.out.print("Nouveau mot de passe : ");
            String mdp = scanner.nextLine();
            System.out.print("Nouvel age : ");
            int age = scanner.nextInt();
            System.out.print("Nouveau CIN : ");
            int cin = scanner.nextInt();
            scanner.nextLine(); // Consommer la ligne restante
            System.out.print("Nouvel état : ");
            String etat = scanner.nextLine();
            System.out.print("Nouvelle fonction : ");
            Role fonction = Role.valueOf(scanner.nextLine());

            User updatedUser = new User(age, cin, nom, prenom, email, mdp, etat, fonction);
            updatedUser.setId(userToUpdate.getId());
            userService.modifier(updatedUser, ancienEmail);  // Utilisation de la méthode modifier de l'interface Iuser
            System.out.println("Utilisateur modifié avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de l'utilisateur : " + e.getMessage());
        }
    }

    private static void modifierProfil(ProfilService profilService, Scanner scanner) {
        System.out.println("\n=== Modifier un profil ===");
        System.out.print("Entrez l'ID du profil à modifier : ");
        int idUser = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne

        try {
            Profil profil = profilService.getProfilByUserId(idUser);
            if (profil == null) {
                System.out.println("Aucun profil trouvé pour cet utilisateur.");
                return;
            }

            // Demander les nouvelles informations pour le profil
            System.out.print("Nouvel avatar : ");
            String avatar = scanner.nextLine();
            System.out.print("Nouvelle bio : ");
            String bio = scanner.nextLine();
            System.out.print("Nouvelles préférences : ");
            String preferences = scanner.nextLine();

            profil.setAvatar(avatar);
            profil.setBio(bio);
            profil.setPreferences(preferences);

            profilService.updateProfil(profil);
            System.out.println("Profil mis à jour avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du profil : " + e.getMessage());
        }
    }

    private static void supprimerUtilisateur(UserService userService, Scanner scanner) {
        System.out.println("\n=== Supprimer un utilisateur ===");
        System.out.print("Entrez l'ID de l'utilisateur à supprimer : ");
        int idUser = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne

        try {
            userService.Delete(idUser);  // Utilisation de la méthode Delete de l'interface Iuser
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }
    }

    private static void supprimerProfil(ProfilService profilService, Scanner scanner) {
        System.out.println("\n=== Supprimer un profil ===");
        System.out.print("Entrez l'ID de l'utilisateur pour lequel vous souhaitez supprimer le profil : ");
        int idUser = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne

        try {
            profilService.deleteProfil(idUser);  // Utilisation de la méthode deleteProfil de ProfilService
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du profil : " + e.getMessage());
        }
    }
}
