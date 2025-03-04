package services;

import models.User;

import java.sql.SQLException;
import java.util.List;

public interface Iuser <T>{
    void ajouter1(T p) throws SQLException;
    void modifier(T user,String ancienEmail) throws SQLException;
    void Delete(int id) throws SQLException;

    List<T> recuperer() throws SQLException;
    boolean userExists(int userId) throws SQLException;

    User recupererParEmail(String emailJoueur1);

}
