package services;

import models.User;
import tools.MyDataBase;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.sql.*;
import java.sql.PreparedStatement;

public class UserService implements Iuser <User>
{
    private Connection cnx;
    public UserService(){
        cnx = MyDataBase.getInstance().getCnx();
    }



    @Override
    public void ajouter1(User p) throws SQLException {
        String sql = "INSERT INTO User ( age, cin, nom, prenom, email, mdp, etat, fonction) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement st = cnx.prepareStatement(sql);

        st.setInt(1, p.getAge());
        st.setInt(2, p.getCin());
        st.setString(3, p.getNom());
        st.setString(4, p.getPrenom());
        st.setString(5, p.getEmail());
        st.setString(6, p.getMdp());
        st.setString(7, p.getEtat());
        st.setString(8, p.getFonction());

        st.executeUpdate();
        System.out.println("Utilisateur ajouté avec succès !");
    }
@Override
    public void modifier(User user, String ancienEmail) throws SQLException {
    String sql = "UPDATE user SET nom = ?, prenom = ?, age = ?, cin = ?, email = ?, mdp = ?, etat = ?, fonction = ? WHERE email = ?";

    try (PreparedStatement st = cnx.prepareStatement(sql)) {
        st.setString(1, user.getNom());
        st.setString(2, user.getPrenom());
        st.setInt(3, user.getAge());
        st.setInt(4, user.getCin());
        st.setString(5, user.getEmail());
        st.setString(6, user.getMdp());
        st.setString(7, user.getEtat());
        st.setString(8, user.getFonction());
        st.setString(9, ancienEmail); // Condition WHERE sur l'ancien email

        int affectedRows = st.executeUpdate();

        if (affectedRows > 0) {
            System.out.println("✅ Utilisateur modifié avec succès !");
        } else {
            System.out.println("⚠️ Aucun utilisateur trouvé avec l'email : " + ancienEmail);
        }
    } catch (SQLException ex) {
        System.err.println("❌ Erreur lors de la modification de l'utilisateur : " + ex.getMessage());
        throw ex;
    }
    }





    @Override
    public void Delete(int id) throws SQLException
    {String sql = "DELETE FROM User WHERE id = ?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setInt(1, id);

        int rowsDeleted = st.executeUpdate();

        if (rowsDeleted > 0) {
            System.out.println(" Réservation avec ID " + id + " supprimée avec succès !");
        } else {
            System.out.println("⚠️ Aucune réservation trouvée avec l'ID : " + id);
        }
    }
    @Override
    public List<User> recuperer() throws SQLException {
        String sql = "SELECT * FROM User";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<User> users = new ArrayList<>();

        while (rs.next()) {
            User p = new User();
            p.setId(rs.getInt("id")); // **Ajoute cette ligne pour récupérer l'ID**
            p.setAge(rs.getInt("age"));
            p.setCin(rs.getInt("cin"));
            p.setNom(rs.getString("nom"));
            p.setPrenom(rs.getString("prenom"));
            p.setEmail(rs.getString("email"));
            p.setMdp(rs.getString("mdp"));
            p.setEtat(rs.getString("etat"));
            p.setFonction(rs.getString("fonction"));

            System.out.println("Utilisateur chargé : ID=" + p.getId() + ", Nom=" + p.getNom());
            users.add(p);
        }
        return users;
    }

    public boolean checkUserExists(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM user WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Retourne vrai si l'utilisateur existe
                }
            }
        }
        return false; // Si aucune ligne n'est trouvée
    }



    // Méthode pour vérifier si un utilisateur existe par ID
    public boolean userExists(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM User WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
}

