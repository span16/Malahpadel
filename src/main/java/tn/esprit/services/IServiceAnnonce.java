package tn.esprit.services;


import java.sql.SQLException;
import java.util.List;

public interface IServiceAnnonce <T>{
    void ajouter(T A) throws SQLException;
    void supprimer(T A)throws SQLException;
    void modifier(T A) throws SQLException;
    List<T> recuperer() throws SQLException;
}

