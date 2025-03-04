package models;

public enum Role {
    ADMIN,
    USER;

    // Méthode pour convertir la chaîne en énumération
    public static Role fromString(String role) {
        try {
            return Role.valueOf(role.toUpperCase()); // Convertit la chaîne en majuscules et obtient l'énum
        } catch (IllegalArgumentException e) {
            return null; // Retourne null si la chaîne n'est pas valide
        }
    }
}
