package services;

import models.Produit;

import java.sql.SQLException;
import java.util.List;

public interface IServiceProduit<T> {
    public void ajouter(T p) throws SQLException;
    public void modifier(T p , int id_produit) throws SQLException;
    public void supprimer(T p,String nom_produit) throws SQLException;
    public List<T> recuperer() throws SQLException;

}
