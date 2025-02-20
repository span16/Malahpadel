package services;

import models.Terrain;
import models.Événement;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TerrainService implements IService<Terrain> {
    private final Connection cnx;

    public TerrainService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    // ✅ Ajouter un terrain et récupérer l'ID après insertion
    @Override
    public void ajouter(Terrain t) throws SQLException {
        if (t.getNom() == null || t.getNom().trim().isEmpty()) {
            throw new SQLException("Le nom du terrain ne peut pas être vide.");
        }

        String sql = "INSERT INTO terrain (nom, adresse, prix_par_personne, heure_ouverture, heure_fermeture) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement st = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, t.getNom());
            st.setString(2, t.getAdresse());
            st.setDouble(3, t.getPrixParPersonne());
            st.setTime(4, t.getHeureOuverture());
            st.setTime(5, t.getHeureFermeture());
            st.executeUpdate();

            // ✅ Récupérer l'ID généré
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    t.setId(rs.getInt(1));
                }
            }

            System.out.println("✅ Terrain ajouté avec ID : " + t.getId());
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de l'ajout du terrain : " + ex.getMessage());
            throw ex;
        }
    }

    // ✅ Supprimer un terrain
    @Override
    public void supprimer(Terrain t) throws SQLException {
        String sql = "DELETE FROM terrain WHERE id = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setInt(1, t.getId());
            int affectedRows = st.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✅ Terrain supprimé");
            } else {
                System.out.println("❌ Aucun terrain trouvé avec cet ID.");
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de la suppression du terrain : " + ex.getMessage());
            throw ex;
        }
    }

    // ✅ Récupérer la liste des terrains
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
                t.setPrixParPersonne(rs.getDouble("prix_par_personne"));
                t.setHeureOuverture(rs.getTime("heure_ouverture"));
                t.setHeureFermeture(rs.getTime("heure_fermeture"));
                terrains.add(t);
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de la récupération des terrains : " + ex.getMessage());
            throw ex;
        }

        return terrains;
    }

    // ✅ Modifier un terrain dans la base de données
    @Override
    public void modifier(Terrain t, int id) throws SQLException {
        String sql = "UPDATE terrain SET nom = ?, adresse = ?, prix_par_personne = ?, heure_ouverture = ?, heure_fermeture = ? WHERE id = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setString(1, t.getNom());
            st.setString(2, t.getAdresse());
            st.setDouble(3, t.getPrixParPersonne());
            st.setTime(4, t.getHeureOuverture());
            st.setTime(5, t.getHeureFermeture());
            st.setInt(6, id);

            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Terrain modifié avec succès !");
            } else {
                System.out.println("❌ Aucun terrain modifié !");
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de la modification du terrain : " + ex.getMessage());
            throw ex;
        }
    }

    // ✅ Méthodes inutiles supprimées
    @Override
    public void modifier(Terrain t) throws SQLException {
        throw new UnsupportedOperationException("Utilisez modifier(Terrain t, int id)");
    }

    @Override
    public void modifier(Terrain t, String nom) throws SQLException {
        throw new UnsupportedOperationException("Utilisez modifier(Terrain t, int id)");
    }

    @Override
    public void modifier(Événement e, int id) throws SQLException {
        throw new UnsupportedOperationException("Méthode non implémentée.");
    }
}
