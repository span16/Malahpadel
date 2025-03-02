package service;

import models.Evenement;  // Assure-toi que la classe Evenement commence par une majuscule
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvenementService {

    private Connection cnx;

    public EvenementService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    public List<String> getAllEvenementNames() throws SQLException {
        List<String> eventNames = new ArrayList<>();
        String sql = "SELECT nom FROM événement";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            eventNames.add(rs.getString("nom"));
        }

        return eventNames;
    }

    // Dans la classe EvenementService
    public Evenement getEvenementByNom(String nom) throws SQLException {
        String sql = "SELECT * FROM événement WHERE nom = ?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setString(1, nom);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            Evenement evenement = new Evenement();
            evenement.setId(rs.getInt("id"));
            evenement.setNom(rs.getString("nom"));
            evenement.setDate(rs.getDate("date"));
            return evenement;
        } else {
            return null; // Aucun événement trouvé avec ce nom
        }
    }



}
