package tn.esprit.services;

import tn.esprit.models.Recherche;
import tn.esprit.tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RechercheService implements IServiceAnnonce<Recherche> {

    private Connection cnx;

    public RechercheService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    // Ajouter une recherche
    public void ajouter(Recherche recherche) throws SQLException {
        String sql = "INSERT INTO recherche (nom, niveau, annonces) VALUES (?, ?, ?)";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setString(1, recherche.getNom());
            st.setString(2, recherche.getNiveau());
            st.setString(3, recherche.getAnnonces());
            st.executeUpdate();
            System.out.println("Recherche ajoutée");
        }
    }

    // Supprimer une recherche
    public void supprimer(Recherche recherche) throws SQLException {
        String sql = "DELETE FROM recherche WHERE user_id = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setInt(1, recherche.getUserId());
            st.executeUpdate();
            System.out.println("Recherche supprimée");
        }
    }

    // Modifier une recherche
    public void modifier(Recherche recherche) throws SQLException {
        String sql = "UPDATE recherche SET nom = ?, niveau = ?, annonces = ? WHERE user_id = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setString(1, recherche.getNom());
            st.setString(2, recherche.getNiveau());
            st.setString(3, recherche.getAnnonces());
            st.setInt(4, recherche.getUserId());
            st.executeUpdate();
            System.out.println("Recherche modifiée");
        }
    }

    // Récupérer toutes les recherches
    public List<Recherche> recuperer() throws SQLException {
        String sql = "SELECT * FROM recherche";
        List<Recherche> recherches = new ArrayList<>();

        try (PreparedStatement st = cnx.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String nom = rs.getString("nom");
                String niveau = rs.getString("niveau");
                String annonces = rs.getString("annonces");
                Recherche recherche = new Recherche(userId, nom, niveau, annonces);
                recherches.add(recherche);
            }
        }

        return recherches;
    }
}
