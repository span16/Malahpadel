package services;

import models.Profil;
import tools.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfilService implements Iprofil<Profil> {  // Assurez-vous que l'interface IProfil est correctement définie
    private Connection cnx;

    public ProfilService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void addProfil(Profil p) throws SQLException {
        String sql = "INSERT INTO profil (id_user, avatar, bio, preferences) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, p.getIdUser());
            stmt.setString(2, p.getAvatar());
            stmt.setString(3, p.getBio());
            stmt.setString(4, p.getPreferences());
            stmt.executeUpdate();
            System.out.println("Profil ajouté avec succès.");
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'ajout du profil : " + ex.getMessage());
            throw ex;  // Relancer l'exception pour permettre une gestion plus haute dans la pile d'appels
        }
    }

    @Override
    public Profil getProfilByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM profil WHERE id_user = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Profil(
                            rs.getInt("id"),
                            rs.getInt("id_user"),
                            rs.getString("avatar"),
                            rs.getString("bio"),
                            rs.getString("preferences")
                    );
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération du profil : " + ex.getMessage());
            throw ex;
        }
        return null;
    }

    @Override
    public void updateProfil(Profil p) throws SQLException {
        String sql = "UPDATE profil SET avatar = ?, bio = ?, preferences = ? WHERE id_user = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, p.getAvatar());
            stmt.setString(2, p.getBio());
            stmt.setString(3, p.getPreferences());
            stmt.setInt(4, p.getIdUser());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Profil mis à jour avec succès.");
            } else {
                System.out.println("Aucun profil trouvé pour cet utilisateur.");
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la mise à jour du profil : " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void deleteProfil(int userId) throws SQLException {
        String sql = "DELETE FROM profil WHERE id_user = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Profil supprimé avec succès.");
            } else {
                System.out.println("Aucun profil trouvé pour cet utilisateur.");
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la suppression du profil : " + ex.getMessage());
            throw ex;
        }
    }
}
