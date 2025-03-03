package service;

import java.sql.SQLException;
import java.util.List;
import models.Evenement;

public interface Iservice<T> {
    void ajouter(T r) throws SQLException;
    int supprimer(int code_confirmation) throws SQLException;

    int modifier(T t) throws SQLException;


    int modifier(int id_R, int nombrePlaces, String typeReservation, int codeConfirmation, String remarque, Evenement evenement) throws SQLException;

    List<T> recuperer() throws SQLException;
    boolean reservationExists(int userId) throws SQLException;

}
