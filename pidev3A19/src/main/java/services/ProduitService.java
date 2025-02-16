package services;
import models.Produit;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ProduitService implements IServiceProduit<Produit>{
    private Connection cnx;
    public ProduitService(){
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Produit p) throws SQLException {
        String sql = "INSERT INTO produit (nom_produit, categorie, prix, stock, description, image_produit) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement st = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        st.setString(1, p.getNom_produit());
        st.setString(2, p.getCategorie());
        st.setFloat(3, p.getPrix());
        st.setInt(4, p.getStock());
        st.setString(5, p.getDescription());
        st.setString(6, p.getImage_produit());

        int rowsInserted = st.executeUpdate();

        if (rowsInserted > 0) {
            // Récupérer l'ID généré
            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                int idGenere = rs.getInt(1);
                p.setId_produit(idGenere);  // Mettre à jour l'objet produit avec l'ID généré
            }
            rs.close();
        }
        st.close();
        System.out.println("Produit ajouté avec succès !");
    }


    @Override
    public void modifier(Produit p, int id_produit) throws SQLException {
        String sql = "UPDATE produit SET nom_produit=?, categorie=?, prix=?, stock=?, description=?, image_produit=? WHERE id_produit=?";
        PreparedStatement st = cnx.prepareStatement(sql);

        st.setString(1, p.getNom_produit());
        st.setString(2, p.getCategorie());
        st.setFloat(3, p.getPrix());
        st.setInt(4, p.getStock());
        st.setString(5, p.getDescription());
        st.setString(6, p.getImage_produit());
        st.setInt(7, id_produit); // Utilisation correcte de l'ID fourni

        int rowsUpdated = st.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Produit mis à jour avec succès !");
        } else {
            System.out.println("Aucun produit mis à jour. Vérifiez l'ID.");
        }
    }

    @Override
    public void supprimer(Produit p, String nom_produit) throws SQLException {
        String sql = "DELETE FROM produit WHERE nom_produit=?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setString(1, nom_produit);

        int rowsDeleted = st.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("Produit supprimé avec succès !");
        } else {
            System.out.println("Aucun produit supprimé. Vérifiez le nom.");
        }
    }


    @Override
    public List<Produit> recuperer() throws SQLException {
        String sql ="select * from produit";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Produit> produits = new ArrayList<>();
        while(rs.next()){
            Produit p = new Produit();
            p.setNom_produit(rs.getString("nom_produit"));
            p.setCategorie(rs.getString("categorie"));
            p.setPrix(rs.getFloat("prix"));
            p.setStock(rs.getInt("stock"));
            p.setDescription(rs.getString("description"));
            p.setImage_produit(rs.getString("image_produit"));
            produits.add(p);
        }

        return produits;
    }
}
