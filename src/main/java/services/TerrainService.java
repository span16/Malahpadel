package services;

import models.Terrain;
import models.Événement;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TerrainService implements IService<Terrain> {
    private Connection cnx;

    public TerrainService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Terrain t) throws SQLException {
        String sql = "INSERT INTO terrain (nom, adresse, prix_par_personne, heure_ouverture, heure_fermeture) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setString(1, t.getNom());
            st.setString(2, t.getAdresse());
            st.setDouble(3, t.getPrix_par_personne());
            st.setTime(4, t.getHeure_ouverture());
            st.setTime(5, t.getHeure_fermeture());
            st.executeUpdate();
            System.out.println("✅ Terrain ajouté avec succès.");
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de l'ajout du terrain : " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void supprimer(Terrain t) throws SQLException {
        String sql = "DELETE FROM terrain WHERE id = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setInt(1, t.getId());
            int affectedRows = st.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✅ Terrain supprimé avec succès.");
            } else {
                System.out.println("❌ Aucun terrain trouvé avec cet ID.");
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de la suppression du terrain : " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void modifier(Terrain t, String ancienNom) throws SQLException {
        String sql = "UPDATE terrain SET nom = ?, adresse = ?, prix_par_personne = ?, heure_ouverture = ?, heure_fermeture = ? WHERE nom = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setString(1, t.getNom());
            st.setString(2, t.getAdresse());
            st.setDouble(3, t.getPrix_par_personne());
            st.setTime(4, t.getHeure_ouverture());
            st.setTime(5, t.getHeure_fermeture());
            st.setString(6, ancienNom);
            int affectedRows = st.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✅ Terrain modifié avec succès.");
            } else {
                System.out.println("❌ Aucun terrain trouvé avec le nom spécifié pour la modification.");
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de la modification du terrain : " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void modifier(Événement e, int id) throws SQLException {

    }

    @Override
    public List<Terrain> recuperer() throws SQLException {
        String sql = "SELECT * FROM terrain";
        List<Terrain> terrains = new ArrayList<>();
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Terrain t = new Terrain();
                t.setId(rs.getInt("id"));
                t.setNom(rs.getString("nom"));
                t.setAdresse(rs.getString("adresse"));
                t.setPrix_par_personne(rs.getDouble("prix_par_personne"));
                t.setHeure_ouverture(rs.getTime("heure_ouverture"));
                t.setHeure_fermeture(rs.getTime("heure_fermeture"));
                terrains.add(t);
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de la récupération des terrains : " + ex.getMessage());
            throw ex;
        }
        return terrains;
    }

    // Nouvelle méthode pour récupérer un terrain par son ID
    public Terrain recupererParId(int id) throws SQLException {
        String sql = "SELECT * FROM terrain WHERE id = ?";
        Terrain terrain = null;
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    terrain = new Terrain();
                    terrain.setId(rs.getInt("id"));
                    terrain.setNom(rs.getString("nom"));
                    terrain.setAdresse(rs.getString("adresse"));
                    terrain.setPrix_par_personne(rs.getDouble("prix_par_personne"));
                    terrain.setHeure_ouverture(rs.getTime("heure_ouverture"));
                    terrain.setHeure_fermeture(rs.getTime("heure_fermeture"));
                }
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de la récupération du terrain par ID : " + ex.getMessage());
            throw ex;
        }
        return terrain;
    }
}
