package utils;

import models.User;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;

    private SessionManager() {
        // Constructeur privé pour éviter l'instanciation externe
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void clearSession() {
        currentUser = null;
    }
}
