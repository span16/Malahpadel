package service;

import models.reservation;
import tools.MyDataBase;
import models.Evenement;
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
        String sql = "INSERT INTO reservation (nombre_places, type_reservation, code_confirmation, remarque, nom) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement st = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        st.setInt(1, r.getNombre_places());
        st.setString(2, r.getType_reservation());
        st.setInt(3, r.getCode_confirmation());
        st.setString(4, r.getRemarque());
        st.setString(5, r.getEvenement().getNom());  // Utiliser le nom de l'événement

        int rowsInserted = st.executeUpdate();

        // Récupérer l'ID auto-généré (id_R)
        if (rowsInserted > 0) {
            ResultSet generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id_R = generatedKeys.getInt(1);  // Récupère l'ID généré
                r.setId_R(id_R);  // Associe l'ID à l'objet reservation
                System.out.println("✅ Réservation ajoutée avec ID " + id_R + " !");
            }
        }
    }

    @Override
    public int supprimer(int code_confirmation) throws SQLException {
        String query = "DELETE FROM reservation WHERE code_confirmation = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, code_confirmation);
            return statement.executeUpdate();
        }
    }

    @Override
    public int modifier(int id_R, int nombre_places, String type_reservation, int code_confirmation, String remarque, Evenement evenement) throws SQLException {
        String query = "UPDATE reservation SET nombre_places = ?, type_reservation = ?, code_confirmation = ?, remarque = ?, nom = ? WHERE id_R = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, nombre_places);
            statement.setString(2, type_reservation);
            statement.setInt(3, code_confirmation);
            statement.setString(4, remarque);
            statement.setString(5, evenement.getNom());  // Utiliser le nom de l'événement
            statement.setInt(6, id_R);  // Utiliser l'ID pour identifier la réservation à mettre à jour
            return statement.executeUpdate();
        }
    }
    @Override
    public List<reservation> recuperer() throws SQLException {
        String sql = "SELECT id_R, nombre_places, type_reservation, code_confirmation, remarque, nom FROM reservation";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<reservation> reservations = new ArrayList<>();

        while (rs.next()) {
            // Crée l'objet Evenement associé
            Evenement evenement = new Evenement();
            evenement.setNom(rs.getString("nom"));  // Récupérer le nom de l'événement

            // Crée la réservation avec l'événement récupéré
            reservation r = new reservation(
                    rs.getInt("id_R"),
                    rs.getInt("nombre_places"),
                    rs.getString("type_reservation"),
                    rs.getInt("code_confirmation"),
                    rs.getString("remarque"),
                    evenement  // Associe l'événement à la réservation
            );

            reservations.add(r);
        }

        return reservations;
    }

    @Override
    public boolean reservationExists(int code_confirmation) throws SQLException {
        String query = "SELECT COUNT(*) FROM reservation WHERE code_confirmation = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setInt(1, code_confirmation);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Retourne true si la réservation existe
            }
        }
        return false; // Retourne false si la réservation n'existe pas
    }
    public List<reservation> filterByCodeConfirmation(String code) throws SQLException {
        String sql = "SELECT r.id_R, r.nombre_places, r.type_reservation, r.code_confirmation, r.remarque, r.nom " +
                "FROM reservation r " +
                "WHERE r.code_confirmation LIKE ?";
        PreparedStatement pstmt = cnx.prepareStatement(sql);
        pstmt.setString(1, "%" + code + "%");
        ResultSet rs = pstmt.executeQuery();

        List<reservation> reservations = new ArrayList<>();

        while (rs.next()) {
            // Crée l'objet Evenement associé
            Evenement evenement = new Evenement();
            evenement.setNom(rs.getString("nom"));  // Récupérer le nom de l'événement

            // Crée la réservation avec l'événement récupéré
            reservation r = new reservation(
                    rs.getInt("id_R"),
                    rs.getInt("nombre_places"),
                    rs.getString("type_reservation"),
                    rs.getInt("code_confirmation"),
                    rs.getString("remarque"),
                    evenement  // Associe l'événement à la réservation
            );
            reservations.add(r);
        }

        return reservations;
    }
}