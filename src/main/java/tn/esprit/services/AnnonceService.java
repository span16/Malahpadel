package tn.esprit.services;

import tn.esprit.models.AnnonceMatch;
import tn.esprit.tools.MyDataBase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

        public class AnnonceService implements IServiceAnnonce<AnnonceMatch> {
    private Connection cnx;

    public AnnonceService() {
        cnx = MyDataBase.getInstance().getCnx();


    }

    @Override
    public void ajouter(AnnonceMatch A) throws SQLException {
        String sql = "INSERT INTO annoncematch (titre, date_Heure, lieu, joueurs_Recherches, niveau, description) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setString(1, A.getTitre());
        st.setObject(2, A.getDate_Heure());
        st.setString(3, A.getLieu());
        st.setInt(4, A.getJoueursRecherches());
        st.setString(5, A.getNiveau());
        st.setString(6, A.getDescription());
        st.executeUpdate();
        System.out.println("Annonce ajoutée");
    }


    @Override
    public void supprimer(AnnonceMatch A) throws SQLException {
        String sql = "DELETE FROM annoncematch WHERE annonce_id = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setInt(1, A.getAnnonceId());
            int rowsDeleted = st.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Annonce supprimée");
            } else {
                System.out.println("Aucune annonce trouvée avec cet ID");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'annonce : " + e.getMessage());
        }
    }
        @Override
            public void modifier(AnnonceMatch A) throws SQLException {
                String sql = "UPDATE annoncematch SET titre = ?, date_Heure = ?, lieu = ?, joueurs_Recherches = ?, niveau = ?, description = ? WHERE annonce_id = ?";
                PreparedStatement st = cnx.prepareStatement(sql);

                // Mise à jour de tous les champs de l'annonce
                st.setString(1, A.getTitre());
                st.setObject(2, A.getDate_Heure());  // On suppose que 'date_Heure' est un LocalDateTime et qu'il est correctement converti
                st.setString(3, A.getLieu());
                st.setInt(4, A.getJoueursRecherches());
                st.setString(5, A.getNiveau());
                st.setString(6, A.getDescription());
                st.setInt(7, A.getAnnonceId());  // Assurez-vous que 'annonce_id' est la clé primaire dans la table

                int rowsUpdated = st.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Annonce modifiée");
                } else {
                    System.out.println("Aucune annonce trouvée avec cet ID");
                }
            }



            @Override
    public List<AnnonceMatch> recuperer() throws SQLException {
        String sql = "SELECT * FROM annoncematch";
        List<AnnonceMatch> annonces = new ArrayList<>();

        try (PreparedStatement st = cnx.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                int annonceId = rs.getInt("annonce_id");
                String titre = rs.getString("titre");
                LocalDateTime dateHeure = rs.getTimestamp("date_heure").toLocalDateTime();
                String lieu = rs.getString("lieu");
                int joueursRecherches = rs.getInt("joueurs_recherches");
                String niveau = rs.getString("niveau");
                String description = rs.getString("description");

                AnnonceMatch annonce = new AnnonceMatch(annonceId, titre, dateHeure, lieu, joueursRecherches, niveau, description);
                annonces.add(annonce);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des annonces : " + e.getMessage());
            throw e; // Re-throw the exception after logging
        }

        return annonces;
    }
}
