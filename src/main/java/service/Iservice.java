package service;

import models.reservation;

import java.sql.SQLException;
import java.util.List;

public interface Iservice<T> {
    void ajouter(T r) throws SQLException;
    int supprimer(int id_R) throws SQLException;

    int modifier(reservation r, String nomC) throws SQLException;

    List<T> recuperer() throws SQLException;
    boolean reservationExists(int userId) throws SQLException;
}
