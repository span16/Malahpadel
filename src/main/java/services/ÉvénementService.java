package services;

import models.Événement;
import models.Terrain;
import models.TypeV;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ÉvénementService implements IService<Événement> {
    private Connection cnx;

    public ÉvénementService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    // Ajoute un événement et récupère l'ID généré
    @Override
    public void ajouter(Événement e) throws SQLException {
        String sql = "INSERT INTO événement (nom, type, terrain_id, date, image_url) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement st = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, e.getNom());
            st.setString(2, e.getType().name());
            st.setInt(3, e.getTerrain() != null ? e.getTerrain().getId() : 0);
            st.setDate(4, e.getDate() != null ? new java.sql.Date(e.getDate().getTime()) : null);
            st.setString(5, e.getImageUrl());
            st.executeUpdate();

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    e.setId(rs.getInt(1));
                }
            }
            System.out.println("✅ Événement ajouté avec ID : " + e.getId());
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de l'ajout de l'événement : " + ex.getMessage());
            throw ex;
        }
    }

    // Supprime un événement
    @Override
    public void supprimer(Événement e) throws SQLException {
        String sql = "DELETE FROM événement WHERE id = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setInt(1, e.getId());
            int affectedRows = st.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✅ Événement supprimé");
            } else {
                System.out.println("❌ Aucun événement trouvé avec l'ID " + e.getId());
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de la suppression de l'événement : " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void modifier(Événement p, String nom) throws SQLException {
        // Méthode non implémentée
    }

    // Modifier un événement (avec vérification d'existence)
    @Override
    public void modifier(Événement e, int id) throws SQLException {
        if (!existe(id)) {
            System.out.println("❌ Aucun événement trouvé avec l'ID " + id);
            return;
        }

        String sql = "UPDATE événement SET nom = ?, type = ?, date = ?, terrain_id = ?, image_url = ? WHERE id = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setString(1, e.getNom());
            st.setString(2, e.getType().name());
            st.setDate(3, e.getDate() != null ? new java.sql.Date(e.getDate().getTime()) : null);
            st.setInt(4, e.getTerrain() != null ? e.getTerrain().getId() : 0);
            st.setString(5, e.getImageUrl());
            st.setInt(6, id);

            int affectedRows = st.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✅ Événement modifié !");
            } else {
                System.out.println("❌ Aucun événement trouvé avec l'ID spécifié !");
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de la modification de l'événement : " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void modifier(models.Terrain t) throws SQLException {
        // Méthode non implémentée
    }

    @Override
    public void modifier(models.Terrain t, int id) throws SQLException {
        // Méthode non implémentée
    }

    // Vérifie si un événement existe
    public boolean existe(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM événement WHERE id = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de la vérification de l'existence de l'événement : " + ex.getMessage());
            throw ex;
        }
        return false;
    }

    // Récupère la liste des événements avec l'objet Terrain et l'image
    @Override
    public List<Événement> recuperer() throws SQLException {
        String sql = "SELECT * FROM événement";
        List<Événement> événements = new ArrayList<>();

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Événement e = new Événement();
                e.setId(rs.getInt("id"));
                e.setNom(rs.getString("nom"));
                e.setType(TypeV.valueOf(rs.getString("type")));
                e.setDate(rs.getDate("date"));

                int terrainId = rs.getInt("terrain_id");
                Terrain terrain = recupererTerrainParId(terrainId);
                e.setTerrain(terrain);

                e.setImageUrl(rs.getString("image_url"));
                événements.add(e);
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de la récupération des événements : " + ex.getMessage());
            throw ex;
        }
        return événements;
    }

    // Méthode pour rechercher des événements dont le nom contient un mot-clé (insensible à la casse)
    public List<Événement> rechercherParNom(String motCle) throws SQLException {
        String sql = "SELECT * FROM événement WHERE LOWER(nom) LIKE ?";
        List<Événement> événements = new ArrayList<>();

        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setString(1, "%" + motCle.toLowerCase() + "%");
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Événement e = new Événement();
                    e.setId(rs.getInt("id"));
                    e.setNom(rs.getString("nom"));
                    e.setType(TypeV.valueOf(rs.getString("type")));
                    e.setDate(rs.getDate("date"));

                    int terrainId = rs.getInt("terrain_id");
                    Terrain terrain = recupererTerrainParId(terrainId);
                    e.setTerrain(terrain);

                    e.setImageUrl(rs.getString("image_url"));
                    événements.add(e);
                }
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de la recherche des événements : " + ex.getMessage());
            throw ex;
        }
        return événements;
    }

    // Récupère la liste des événements triée par ordre alphabétique (selon le nom)
    public List<Événement> trierParAlphabet() throws SQLException {
        String sql = "SELECT * FROM événement ORDER BY nom ASC";
        List<Événement> événements = new ArrayList<>();

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Événement e = new Événement();
                e.setId(rs.getInt("id"));
                e.setNom(rs.getString("nom"));
                e.setType(TypeV.valueOf(rs.getString("type")));
                e.setDate(rs.getDate("date"));

                int terrainId = rs.getInt("terrain_id");
                Terrain terrain = recupererTerrainParId(terrainId);
                e.setTerrain(terrain);

                e.setImageUrl(rs.getString("image_url"));
                événements.add(e);
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors du tri des événements : " + ex.getMessage());
            throw ex;
        }
        return événements;
    }

    // Récupère un terrain par son ID
    private Terrain recupererTerrainParId(int terrainId) throws SQLException {
        if (terrainId == 0) {
            return null;
        }

        String sql = "SELECT * FROM terrain WHERE id = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setInt(1, terrainId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Terrain terrain = new Terrain();
                    terrain.setId(rs.getInt("id"));
                    terrain.setNom(rs.getString("nom"));
                    terrain.setAdresse(rs.getString("adresse"));
                    return terrain;
                } else {
                    System.out.println("⚠ Aucun terrain trouvé avec l'ID " + terrainId);
                }
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de la récupération du terrain : " + ex.getMessage());
            throw ex;
        }
        return null;
    }

    // Récupère un événement par son ID
    public Événement recupererParId(int id) throws SQLException {
        String sql = "SELECT * FROM événement WHERE id = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Événement e = new Événement();
                    e.setId(rs.getInt("id"));
                    e.setNom(rs.getString("nom"));
                    e.setType(TypeV.valueOf(rs.getString("type")));
                    e.setDate(rs.getDate("date"));

                    int terrainId = rs.getInt("terrain_id");
                    Terrain terrain = recupererTerrainParId(terrainId);
                    e.setTerrain(terrain);

                    e.setImageUrl(rs.getString("image_url"));
                    return e;
                }
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de la récupération de l'événement par ID : " + ex.getMessage());
            throw ex;
        }
        return null;
    }
}
