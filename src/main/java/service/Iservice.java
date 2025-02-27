package service;

import java.sql.SQLException;
import java.util.List;

public interface Iservice<T> {
    void ajouter(T r) throws SQLException;
    int supprimer(int code_confirmation) throws SQLException;

    int modifier(int id_R, int nombre_places, String type_reservation, int code_confirmation, String remarque) throws SQLException;


    List<T> recuperer() throws SQLException;
    boolean reservationExists(int userId) throws SQLException;

}
