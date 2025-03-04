package services;

import models.Equipe;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipeService implements IEquipe {
    private final Connection cnx;

    public EquipeService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouterEquipe(Equipe equipe) throws SQLException {
        if (joueurDejaDansUneEquipe(equipe.getEmailJoueur1()) || joueurDejaDansUneEquipe(equipe.getEmailJoueur2())) {
            throw new SQLException("Un des joueurs est déjà dans une autre équipe.");
        }

        String sql = "INSERT INTO equipes (nom_equipe, email_joueur1, email_joueur2) VALUES (?, ?, ?)";
        try (PreparedStatement st = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, equipe.getNomEquipe());
            st.setString(2, equipe.getEmailJoueur1());
            st.setString(3, equipe.getEmailJoueur2());
            st.executeUpdate();

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    equipe.setEquipeId(rs.getInt(1));
                }
            }

            System.out.println("✅ Équipe ajoutée avec ID : " + equipe.getEquipeId());
        }
    }

    @Override
    public void modifierEquipe(Equipe equipe) throws SQLException {
        String sql = "UPDATE equipes SET nom_equipe = ?, email_joueur1 = ?, email_joueur2 = ? WHERE equipe_id = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setString(1, equipe.getNomEquipe());
            st.setString(2, equipe.getEmailJoueur1());
            st.setString(3, equipe.getEmailJoueur2());
            st.setInt(4, equipe.getEquipeId());

            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Équipe modifiée !");
            } else {
                System.out.println("⚠️ Aucune équipe trouvée !");
            }
        }
    }

    @Override
    public void supprimerEquipe(int equipeId) throws SQLException {
        String sql = "DELETE FROM equipes WHERE equipe_id = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setInt(1, equipeId);
            int affectedRows = st.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✅ Équipe supprimée !");
            } else {
                System.out.println("⚠️ Aucune équipe trouvée avec cet ID.");
            }
        }
    }

    @Override
    public List<Equipe> recupererEquipes() throws SQLException {
        String sql = "SELECT * FROM equipes";
        List<Equipe> equipes = new ArrayList<>();

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Equipe equipe = new Equipe();
                equipe.setEquipeId(rs.getInt("equipe_id"));
                equipe.setNomEquipe(rs.getString("nom_equipe"));
                equipe.setEmailJoueur1(rs.getString("email_joueur1"));
                equipe.setEmailJoueur2(rs.getString("email_joueur2"));
                equipes.add(equipe);
            }
        }
        return equipes;
    }

    @Override
    public boolean joueurDejaDansUneEquipe(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM equipes WHERE email_joueur1 = ? OR email_joueur2 = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setString(1, email);
            st.setString(2, email);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * Récupère une équipe à partir de son ID.
     *
     * @param id l'ID de l'équipe (colonne equipe_id)
     * @return l'objet Equipe correspondant, ou null si aucune équipe trouvée
     * @throws SQLException en cas d'erreur SQL
     */
    public Equipe recupererParId(int id) throws SQLException {
        String sql = "SELECT * FROM equipes WHERE equipe_id = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Equipe equipe = new Equipe();
                    equipe.setEquipeId(rs.getInt("equipe_id"));
                    equipe.setNomEquipe(rs.getString("nom_equipe"));
                    equipe.setEmailJoueur1(rs.getString("email_joueur1"));
                    equipe.setEmailJoueur2(rs.getString("email_joueur2"));
                    return equipe;
                }
            }
        }
        return null;
    }
}
