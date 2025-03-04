package Session;
import models.User;

public class SessionManger
{
    private static User currentUser = null;

    // Méthode pour récupérer l'utilisateur connecté
    public static User getCurrentUser() {
        return currentUser;
    }

    // Méthode pour définir l'utilisateur connecté
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    // Méthode pour vérifier si un utilisateur est connecté
    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    // Méthode pour déconnecter l'utilisateur
    public static void logout() {
        currentUser = null;
    }
}
