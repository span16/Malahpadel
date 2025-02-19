package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {
    // Définition des variables statiques pour la connexion
    private static final String URL = "jdbc:mysql://localhost:3306/pidev"; // Assurez-vous que c'est le bon nom de base de données
    private static final String USER = "root";
    private static final String PWD = "";

    // Déclaration de la connexion
    private Connection cnx;
    // Instance pour le pattern Singleton
    private static MyDataBase instance;

    // Constructeur privé pour initialiser la connexion
    private MyDataBase() {
        try {
            // Chargement du driver MySQL (optionnel pour MySQL 8+)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connexion à la base de données
            cnx = DriverManager.getConnection(URL, USER, PWD);
            System.out.println("✅ Connexion établie avec succès !");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL introuvable !");
        } catch (SQLException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
        }
    }

    // Méthode statique pour obtenir l'instance de la connexion (Singleton)
    public static MyDataBase getInstance() {
        if (instance == null) {
            instance = new MyDataBase();
        }
        return instance;
    }

    // Méthode pour obtenir la connexion
    public Connection getCnx() {
        return cnx;
    }
}
