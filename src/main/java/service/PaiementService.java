package service;

import models.paiement;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaiementService implements Iservices<paiement> {
    private Connection cnx;

    public PaiementService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(paiement p) throws SQLException {
        // Vérifier si la réservation existe avant d'ajouter le paiement
        if (!reservationExiste(p.getId_R())) {
            throw new SQLException("❌ Erreur : id_R " + p.getId_R() + " n'existe pas dans reservation !");
        }

        String query = "INSERT INTO paiement (id_R, methode_Paiement, commission, description_Paiement, devise) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            pstmt.setInt(1, p.getId_R());
            pstmt.setString(2, p.getMethode_Paiement());
            pstmt.setFloat(3, p.getCommission());
            pstmt.setString(4, p.getDescription_Paiement());
            pstmt.setString(5, p.getDevise());

            pstmt.executeUpdate();
            System.out.println("✅ Paiement ajouté avec succès !");
        }
    }



    @Override
    public void modifier(paiement p) throws SQLException {
        // La modification ne concerne pas id_R car c'est une clé étrangère et il ne doit pas être modifié
        String sql = "UPDATE paiement SET methode_Paiement = ?, commission = ?, description_Paiement = ?, devise = ? WHERE id_R = ?"; // Mise à jour en utilisant id_R

        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, p.getMethode_Paiement()); // Mise à jour de la méthode de paiement
            stmt.setFloat(2, p.getCommission()); // Mise à jour de la commission
            stmt.setString(3, p.getDescription_Paiement()); // Mise à jour de la description du paiement
            stmt.setString(4, p.getDevise()); // Mise à jour de la devise
            stmt.setInt(5, p.getId_R()); // Utilisation de id_R pour identifier le paiement à modifier

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Paiement mis à jour avec succès.");
            } else {
                System.out.println("⚠️ Aucun paiement trouvé avec l'ID " + p.getId_R());
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la mise à jour du paiement : " + ex.getMessage());
            throw ex;
        }
    }



    public int supprimer(String devise) throws SQLException {
        String query = "DELETE FROM paiement WHERE devise = ?";
        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            pstmt.setString(1, devise);
            return pstmt.executeUpdate();
        }
    }



    @Override
    public List<paiement> recuperer() throws SQLException {
        String sql = "SELECT * FROM paiement";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<paiement> paiements = new ArrayList<>();

        while (rs.next()) {
            paiement p = new paiement(
                    rs.getInt("id_R"), // id_R comme clé étrangère
                    rs.getString("methode_Paiement"),
                    rs.getFloat("commission"),
                    rs.getString("description_Paiement"),
                    rs.getString("devise")
            );
            paiements.add(p);
        }

        return paiements;
    }

    @Override
    public boolean reservationExiste(int id_R) throws SQLException {
        String query = "SELECT COUNT(*) FROM reservation WHERE id_R = ?";
        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            pstmt.setInt(1, id_R);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;  // Retourne true si la réservation existe
                }
            }
        }
        return false;  // Retourne false si la réservation n'existe pas
    }


}
