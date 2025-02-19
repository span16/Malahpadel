package service;

import java.sql.SQLException;
import java.util.List;

public interface Iservices<T> {
    // Méthode pour ajouter une entité
    void ajouter(T t) throws SQLException;

    // Méthode pour supprimer une entité en utilisant un id
    int supprimer(int id_P); // Modifier la signature pour prendre un id_P en paramètre

    // Méthode pour modifier une entité
    void modifier(T t, String status) throws SQLException;

    // Méthode pour récupérer toutes les entités
    List<T> recuperer() throws SQLException;
}
