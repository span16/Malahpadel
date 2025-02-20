package services;

import models.Terrain;
import models.Événement;
import java.sql.SQLException;
import java.util.List;

public interface IService<T> {
    void ajouter(T p) throws SQLException;
    void supprimer(T p) throws SQLException;
    void modifier(T p, String nom) throws SQLException;
    void modifier(Terrain t) throws SQLException;
    void modifier(Terrain t, int id) throws SQLException;
    void modifier(Événement e, int id) throws SQLException;
    List<T> recuperer() throws SQLException;
}
