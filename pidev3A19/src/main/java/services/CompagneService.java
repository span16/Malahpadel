package services;
import models.Compagne;

import models.Produit;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class CompagneService implements IServiceCompagne<Compagne> {
    private Connection cnx;
    public CompagneService(){
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajoutercompagne(Compagne c) throws SQLException {
        String sql = "INSERT INTO compagne(nom_sponsor, date_debut, date_fin, logo_compagne, TypeMarketing, status, tarifs, id_produit) VALUES(?,?,?,?,?,?,?,?)";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setString(1, c.getNom_sponsor());
        st.setDate(2, c.getDate_debut());
        st.setDate(3, c.getDate_fin());
        st.setString(4, c.getLogo_compagne());
        st.setString(5, c.getTypeMarketing());
        st.setString(6, c.getStatus());
        st.setFloat(7, c.getTarifs());
        st.setInt(8, c.getProduit().getId_produit()); // Utiliser l'ID du produit associé

        // Debugging: Print the values being inserted
        System.out.println("Nom Sponsor: " + c.getNom_sponsor());
        System.out.println("Date Début: " + c.getDate_debut());
        System.out.println("Date Fin: " + c.getDate_fin());
        System.out.println("Logo Compagne: " + c.getLogo_compagne());
        System.out.println("Type Marketing: " + c.getTypeMarketing());
        System.out.println("Status: " + c.getStatus());
        System.out.println("Tarifs: " + c.getTarifs());
        System.out.println("Produit ID: " + c.getProduit().getId_produit());

        st.executeUpdate();
        System.out.println("Compagne ajoutée avec succès !");
    }


    @Override
    public void modifiercompagne(Compagne c, int id_compagne) throws SQLException {
        String sql = "UPDATE compagne SET nom_sponsor=?, date_debut=?, date_fin=?, logo_compagne=?, Typemarketing=?, status=?, tarifs=? WHERE id_compagne=?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setString(1, c.getNom_sponsor());
        st.setDate(2, c.getDate_debut());
        st.setDate(3, c.getDate_fin());
        st.setString(4, c.getLogo_compagne());
        st.setString(5, c.getTypeMarketing());
        st.setString(6, c.getStatus());
        st.setFloat(7, c.getTarifs());
        st.setInt(8, id_compagne);  // L'ID de la compagne à mettre à jour

        int rowsUpdated = st.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Compagne mise à jour avec succès !");
        } else {
            System.out.println("Aucune compagne mise à jour. Vérifiez l'ID.");
        }
    }

    @Override
    public void supprimercompagne(Compagne c, String nom_sponsor) throws SQLException {
        String sql = "DELETE FROM compagne WHERE nom_sponsor=?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setString(1, nom_sponsor);

        int rowsDeleted = st.executeUpdate();
        if (rowsDeleted > 0) {
           // System.out.println("Compagne supprimée avec succès !");
        } else {
            System.out.println("Aucune compagne supprimée. Vérifiez le nom du sponsor.");
        }
    }


    @Override
    public List<Compagne> recuperercompagne() throws SQLException {
        String sql = "SELECT c.*, p.id_produit as id_produit, p.nom_produit as produit_nom, p.categorie as produit_categorie, p.image_produit as produit_image, p.description as produit_description, p.prix as produit_prix FROM compagne c JOIN produit p ON c.id_produit = p.id_produit";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);

        List<Compagne> campagnes = new ArrayList<>();
        while (rs.next()) {
            Compagne c = new Compagne();
            c.setId_compagne(rs.getInt("id_compagne"));
            c.setNom_sponsor(rs.getString("nom_sponsor"));
            c.setDate_debut(rs.getDate("date_debut"));
            c.setDate_fin(rs.getDate("date_fin"));
            c.setLogo_compagne(rs.getString("logo_compagne"));
            c.setTypeMarketing(rs.getString("Typemarketing"));
            c.setStatus(rs.getString("status"));
            c.setTarifs(rs.getFloat("tarifs"));

            // Créer un objet Produit avec les attributs corrects
            Produit p = new Produit();
            p.setId_produit(rs.getInt("id_produit"));
            p.setNom_produit(rs.getString("produit_nom"));
            p.setCategorie(rs.getString("produit_categorie"));
            p.setImage_produit(rs.getString("produit_image"));
            p.setDescription(rs.getString("produit_description"));
            p.setPrix(rs.getFloat("produit_prix"));
            p.setStock(rs.getInt("produit_stock")); // Ajout du stock

            c.setProduit(p);  // Associer le produit à la compagne
            campagnes.add(c);
        }
        return campagnes;
    }

}
