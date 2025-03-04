package services;

import models.Equipe;

import java.sql.SQLException;
import java.util.List;

public interface IEquipe {
    void ajouterEquipe(Equipe equipe) throws SQLException;
    void modifierEquipe(Equipe equipe) throws SQLException;
    void supprimerEquipe(int equipeId) throws SQLException;
    List<Equipe> recupererEquipes() throws SQLException;
    boolean joueurDejaDansUneEquipe(String email) throws SQLException;
}
