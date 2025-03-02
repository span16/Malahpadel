package service;

import models.paiement;

import java.sql.SQLException;
import java.util.List;

public interface Iservices<T> {
    void ajouter(paiement p) throws SQLException;
    boolean reservationExiste(int id_R) throws SQLException;

    int supprimer(String devise) throws SQLException; // ✅ Correction ici
    void modifier(T t) throws SQLException;

    List<T> recuperer() throws SQLException;
}
