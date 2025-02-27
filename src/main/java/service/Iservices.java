package service;

import java.sql.SQLException;
import java.util.List;

public interface Iservices<T> {
    void ajouter(T t) throws SQLException;

    int supprimer(int id_P); // Modifier la signature pour prendre un id_P en paramètre

    void modifier(T t, String status) throws SQLException;

    List<T> recuperer() throws SQLException;
}
