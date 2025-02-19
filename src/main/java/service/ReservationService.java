package service;

import models.reservation;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationService implements Iservice<reservation> {
    private Connection cnx;

    public ReservationService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(reservation r) throws SQLException {
        String sql = "INSERT INTO reservation (id_P, nomC, email, dateR, status) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement st = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        st.setInt(1, r.getId_P());
        st.setString(2, r.getNomC());
        st.setString(3, r.getEmail());
        st.setDate(4, r.getDateR() != null ? new java.sql.Date(r.getDateR().getTime()) : new java.sql.Date(new java.util.Date().getTime()));
        st.setString(5, r.getStatus());

        st.executeUpdate();
        System.out.println("✅ Réservation ajoutée !");
    }

    @Override
    public int supprimer(int id) throws SQLException {
        String sql = "DELETE FROM reservation WHERE id_R = ?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setInt(1, id);

        int rowsDeleted = st.executeUpdate();

        if (rowsDeleted > 0) {
            System.out.println("✅ Réservation avec ID " + id + " supprimée !");
        } else {
            System.out.println("⚠️ Aucune réservation trouvée avec l'ID : " + id);
        }
        return rowsDeleted;
    }

    @Override
    public int modifier(reservation r, String nomC) throws SQLException {
        String sql = "UPDATE reservation SET id_P = ?, nomC = ?, email = ?, dateR = ?, status = ? WHERE nomC = ?";
        PreparedStatement st = cnx.prepareStatement(sql);

        st.setInt(1, r.getId_P());
        st.setString(2, r.getNomC());
        st.setString(3, r.getEmail());
        st.setDate(4, new java.sql.Date(r.getDateR().getTime()));
        st.setString(5, r.getStatus());
        st.setString(6, nomC);

        int rowsUpdated = st.executeUpdate();

        if (rowsUpdated > 0) {
            System.out.println("✅ Réservation modifiée !");
        } else {
            System.out.println("⚠️ Aucune réservation trouvée avec le nom : " + nomC);
        }
        return rowsUpdated;
    }

    @Override
    public List<reservation> recuperer() throws SQLException {
        String sql = "SELECT * FROM reservation";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<reservation> reservations = new ArrayList<>();

        while (rs.next()) {
            reservation r = new reservation(
                    rs.getInt("id_R"),
                    rs.getInt("id_P"),
                    rs.getString("nomC"),
                    rs.getString("email"),
                    rs.getDate("dateR"),
                    rs.getString("status")
            );
            reservations.add(r);
        }

        return reservations;
    }

    public boolean reservationExists(int id_R) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservation WHERE id_R = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, id_R);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public void insert(reservation reservation) {

    }


}
