package service;

import models.evenement;
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
        String sql = "INSERT INTO reservation (nombre_places, type_reservation, code_confirmation, remarque, id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement st = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        st.setInt(1, r.getNombre_places());
        st.setString(2, r.getType_reservation());
        st.setInt(3, r.getCode_confirmation());
        st.setString(4, r.getRemarque());
        st.setInt(5, r.getEvenement().getId());  // Utilisation de l'ID de l'événement

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

        // Utilisation de la connexion existante de MyDataBase
        try (Connection connection = MyDataBase.getInstance().getCnx();  // pas de nouvelle ouverture, utilise l'instance existante
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Paramétrage de la requête avec code_confirmation
            statement.setInt(1, code_confirmation);

            // Exécution de la suppression
            return statement.executeUpdate();
        }
    }



    @Override
    public int modifier(int id_R, int nombre_places, String type_reservation, int code_confirmation, String remarque) throws SQLException {
        String query = "UPDATE reservation SET nombre_places = ?, type_reservation = ?, code_confirmation = ?, remarque = ? WHERE id_R = ?";

        try (Connection connection = MyDataBase.getInstance().getCnx(); // Utilisation de MyDataBase
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Paramétrage de la requête
            statement.setInt(1, nombre_places);
            statement.setString(2, type_reservation);
            statement.setInt(3, code_confirmation);
            statement.setString(4, remarque);
            statement.setInt(5, id_R);

            // Exécution de la mise à jour
            return statement.executeUpdate();
        }
    }










    @Override
    public List<reservation> recuperer() throws SQLException {
        String sql = "SELECT * FROM reservation";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<reservation> reservations = new ArrayList<>();

        while (rs.next()) {
            // Suppose que l'ID de l'événement est récupéré
            int evenementId = rs.getInt("evenement_id");
            evenement evenement = new evenement(evenementId);  // Créez un événement avec l'ID récupéré

            reservation r = new reservation(
                    rs.getInt("id_R"),  // Récupération de l'ID auto-généré
                    rs.getInt("nombre_places"),
                    rs.getString("type_reservation"),
                    rs.getInt("code_confirmation"),
                    rs.getString("remarque"),
                    evenement  // Associez l'événement à la réservation
            );
            reservations.add(r);
        }

        return reservations;
    }

    public boolean reservationExists(int code_confirmation) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservation WHERE code_confirmation = ?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setInt(1, code_confirmation);

        ResultSet rs = st.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;  // Retourne true si une réservation existe avec ce code de confirmation
    }



}
