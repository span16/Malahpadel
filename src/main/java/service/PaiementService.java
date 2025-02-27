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
        String checkSql = "SELECT COUNT(*) FROM reservation WHERE id_R = ?";
        try (PreparedStatement checkSt = cnx.prepareStatement(checkSql)) {
            checkSt.setInt(1, p.getId_R());
            ResultSet rs = checkSt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                System.out.println("❌ Erreur : id_R " + p.getId_R() + " n'existe pas dans reservation !");
                return;
            }
        }

        String sql = "INSERT INTO paiement (id_P, id_R, montant, status_P) VALUES (?, ?, ?, ?)";
        try (PreparedStatement st = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, p.getId_P());
            st.setInt(2, p.getId_R());
            st.setFloat(3, p.getMontant());
            st.setString(4, p.getStatus_P());

            int rowsInserted = st.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✅ Paiement ajouté !");
            } else {
                System.out.println("⚠️ Échec de l'ajout du paiement !");
            }
        }
    }





    @Override
    public void modifier(paiement p, String status_P) throws SQLException {
        String sql = "UPDATE paiement SET status_P = ? WHERE id_P = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, status_P);
            stmt.setInt(2, p.getId_P());

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
    @Override
    public int supprimer(int id_P) {
        String sql = "DELETE FROM paiement WHERE id_P = ?";

        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            // Utiliser id_P directement dans la requête
            st.setInt(1, id_P);

            // Exécuter la requête de suppression
            int rowsDeleted = st.executeUpdate();

            // Vérifier si la suppression a été effectuée avec succès
            if (rowsDeleted > 0) {
                System.out.println("✅ Paiement avec ID " + id_P + " supprimé !");
            } else {
                System.out.println("⚠️ Aucun paiement trouvé avec l'ID " + id_P);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la suppression du paiement : " + e.getMessage());
        }
        return id_P;
    }




    @Override
    public List<paiement> recuperer() throws SQLException {
        String sql = "SELECT * FROM paiement";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<paiement> paiements = new ArrayList<>();

        while (rs.next()) {
            paiement p = new paiement(
                    rs.getInt("id_P"),
                    rs.getInt("id_R"),
                    rs.getFloat("montant"),
                    rs.getString("status_P")
            );
            paiements.add(p);
        }

        return paiements;
    }


    public boolean paiementExists(int id_P) throws SQLException {
        String sql = "SELECT COUNT(*) FROM paiement WHERE id_P = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, id_P);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Si le COUNT est > 0, la ligne existe
            }
        }
        return false; // Aucun résultat
    }

}
