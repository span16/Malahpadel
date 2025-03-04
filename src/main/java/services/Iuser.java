package services;

import models.User;
import java.sql.SQLException;
import java.util.List;

public interface Iuser<T> {
    void ajouter1(T t) throws SQLException;
    void modifier(T t, String ancienEmail) throws SQLException;
    void Delete(int id) throws SQLException;
    List<T> recuperer() throws SQLException;
    boolean userExists(int userId) throws SQLException; // Méthode à implémenter

   // boolean checkUserExists(int userId);
}
