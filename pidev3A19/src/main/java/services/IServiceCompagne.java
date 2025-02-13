package services;
import models.Compagne;

import java.sql.SQLException;
import java.util.List;
public interface IServiceCompagne <T>{
    public void ajoutercompagne(T C) throws SQLException;
    public void modifiercompagne(T C , int id_compagne) throws SQLException;
    public void supprimercompagne(T C,String nom_sponsor) throws SQLException;
    public List<T> recuperercompagne() throws SQLException;

}
