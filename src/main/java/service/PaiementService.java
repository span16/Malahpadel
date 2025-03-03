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
        String query = "INSERT INTO paiement (methode_Paiement, commission, description_Paiement, devise) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            pstmt.setString(1, p.getMethode_Paiement());
            pstmt.setFloat(2, p.getCommission());
            pstmt.setString(3, p.getDescription_Paiement());
            pstmt.setString(4, p.getDevise());

            pstmt.executeUpdate();
            System.out.println("✅ Paiement ajouté avec succès !");
        }
    }

    @Override
    public void modifier(paiement p) throws SQLException {
        String sql = "UPDATE paiement SET methode_Paiement = ?, commission = ?, description_Paiement = ?, devise = ? WHERE id_P = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, p.getMethode_Paiement());
            stmt.setFloat(2, p.getCommission());
            stmt.setString(3, p.getDescription_Paiement());
            stmt.setString(4, p.getDevise());
            stmt.setInt(5, p.getId_P());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Paiement mis à jour avec succès.");
            } else {
                System.out.println("⚠️ Aucun paiement trouvé avec l'ID " + p.getId_P());
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
                    rs.getInt("id_R"),
                    rs.getString("methode_Paiement"),
                    rs.getFloat("commission"),
                    rs.getString("description_Paiement"),
                    rs.getString("devise")
            );
            paiements.add(p);
        }

        return paiements;
    }
}