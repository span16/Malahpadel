package services;

import models.Role;
import models.User;
import tools.MyDataBase;
import utils.EmailSender;

import java.security.SecureRandom;
import java.sql.*;
import java.util.*;

public class UserService implements Iuser<User> {
    private Connection cnx;
    private final Map<String, String> passwordResetTokens = new HashMap<>();

    public UserService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    // Vérifier si l'email existe déjà
    public boolean emailExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM User WHERE email = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    @Override
    public void ajouter1(User p) throws SQLException {
        String sql = "INSERT INTO User (age, cin, nom, prenom, email, mdp, etat, fonction) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement st = cnx.prepareStatement(sql);

        st.setInt(1, p.getAge());
        st.setInt(2, p.getCin());
        st.setString(3, p.getNom());
        st.setString(4, p.getPrenom());
        st.setString(5, p.getEmail());
        st.setString(6, p.getMdp());
        st.setString(7, p.getEtat());

        // Si fonction est null, on l'initialise ici avant d'utiliser 'name()'
        if (p.getFonction() == null) {
            p.setFonction(Role.USER);  // Assurez-vous que "USER" est un rôle valide dans votre enum
        }

        // Utiliser name() ici pour la conversion du rôle en chaîne
        st.setString(8, p.getFonction().name()); // Convertir l'énum en chaîne ici

        st.executeUpdate();
        System.out.println("Utilisateur ajouté avec succès !");
    }


    @Override
    public void modifier(User user, String ancienEmail) throws SQLException {
        String sql = "UPDATE User SET nom = ?, prenom = ?, age = ?, cin = ?, email = ?, mdp = ?, etat = ?, fonction = ? WHERE email = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setString(1, user.getNom());
            st.setString(2, user.getPrenom());
            st.setInt(3, user.getAge());
            st.setInt(4, user.getCin());
            st.setString(5, user.getEmail());
            st.setString(6, user.getMdp());
            st.setString(7, user.getEtat());

            // Utiliser name() pour la conversion de l'énum en chaîne
            st.setString(8, user.getFonction().name()); // Convertir l'énum en chaîne
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
    public void Delete(int id) throws SQLException {
        String sql = "DELETE FROM User WHERE id = ?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setInt(1, id);
        int rowsDeleted = st.executeUpdate();

        if (rowsDeleted > 0) {
            System.out.println("Utilisateur avec ID " + id + " supprimé avec succès !");
        } else {
            System.out.println("⚠️ Aucun utilisateur trouvé avec l'ID : " + id);
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
            p.setId(rs.getInt("id"));
            p.setAge(rs.getInt("age"));
            p.setCin(rs.getInt("cin"));
            p.setNom(rs.getString("nom"));
            p.setPrenom(rs.getString("prenom"));
            p.setEmail(rs.getString("email"));
            p.setMdp(rs.getString("mdp"));
            p.setEtat(rs.getString("etat"));

            // Convertir la chaîne en Role
            p.setFonction(Role.fromString(rs.getString("fonction"))); // Conversion correcte ici

            users.add(p);
        }
        return users;
    }

    @Override
    public boolean userExists(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM User WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0; // Vérifie si l'utilisateur existe
        }
    }


    public User getUserByEmail(String email) {
        String query = "SELECT * FROM User WHERE email = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("mdp"),
                        rs.getInt("age"),
                        rs.getInt("cin"),
                        Role.fromString(rs.getString("fonction"))
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
        }
        return null; // Retourne null si l'utilisateur n'est pas trouvé
    }

    public void updateUserEtat(int userId, String newEtat) throws SQLException {
        String sql = "UPDATE User SET etat = ? WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, newEtat);  // Mettre à jour l'état
            stmt.setInt(2, userId);  // Spécifier l'ID de l'utilisateur
            stmt.executeUpdate();
            System.out.println("État de l'utilisateur mis à jour avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'état : " + e.getMessage());
            throw e;
        }
    }

    public List<User> searchUsers(String keyword) throws SQLException {
        String sql = "SELECT * FROM User WHERE LOWER(nom) LIKE ? OR LOWER(prenom) LIKE ? OR LOWER(email) LIKE ?";
        PreparedStatement st = cnx.prepareStatement(sql);
        String pattern = "%" + keyword.toLowerCase() + "%";
        st.setString(1, pattern);
        st.setString(2, pattern);
        st.setString(3, pattern);

        ResultSet rs = st.executeQuery();
        List<User> users = new ArrayList<>();

        while (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setNom(rs.getString("nom"));
            user.setPrenom(rs.getString("prenom"));
            user.setAge(rs.getInt("age"));
            user.setCin(rs.getInt("cin"));
            user.setEmail(rs.getString("email"));
            user.setMdp(rs.getString("mdp"));
            user.setEtat(rs.getString("etat"));
            user.setFonction(Role.fromString(rs.getString("fonction")));
            users.add(user);
        }

        return users;
    }


    public boolean sendResetEmail(String email) {
        User user = getUserByEmail(email);
        if (user == null) {
            System.out.println("⚠️ Aucun utilisateur trouvé avec cet email.");
            return false;
        }

        // Génération du token sécurisé
        String token = generateSecureToken();

        // Définir l'expiration (par ex. 15 minutes)
        Timestamp expirationTime = new Timestamp(System.currentTimeMillis() + 15 * 60 * 1000);

        // Enregistrer le token et l'expiration en base
        try {
            String sql = "UPDATE User SET reset_token = ?, token_expiration = ? WHERE email = ?";
            try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
                stmt.setString(1, token);
                stmt.setTimestamp(2, expirationTime);
                stmt.setString(3, email);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'enregistrement du token : " + e.getMessage());
            return false;
        }

        // Envoi de l'email avec le token
        String message = "Bonjour,\n\nVotre code de réinitialisation est : " + token +
                "\n\nVeuillez copier ce code dans l'interface de réinitialisation de l'application.";

        return EmailSender.sendEmail(email, "Réinitialisation de mot de passe", message);
    }

    /**
     * Vérifie que le token fourni correspond bien à celui stocké pour l'email donné.
     */
    public boolean verifyToken(String email, String token) {
        String sql = "SELECT reset_token, token_expiration FROM User WHERE email = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedToken = rs.getString("reset_token");
                Timestamp expirationTime = rs.getTimestamp("token_expiration");

                if (storedToken == null || expirationTime == null) {
                    System.out.println("❌ Aucun token enregistré.");
                    return false;
                }

                // Vérifier si le token est expiré
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                if (currentTime.after(expirationTime)) {
                    System.out.println("❌ Token expiré.");
                    return false;
                }

                // Vérifier que le token saisi correspond
                return storedToken.equals(token);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la vérification du token : " + e.getMessage());
        }
        return false;
    }


    /**
     * Réinitialise le mot de passe si le token est valide.
     */
    public boolean resetPassword(String email, String newPassword, String token) {
        if (!verifyToken(email, token)) {
            System.out.println("❌ Token invalide ou expiré.");
            return false;
        }

        try {
            String hashedPassword = hashPassword(newPassword);

            String sql = "UPDATE User SET mdp = ?, reset_token = NULL, token_expiration = NULL WHERE email = ?";
            try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
                stmt.setString(1, hashedPassword);
                stmt.setString(2, email);
                stmt.executeUpdate();
            }

            System.out.println("✅ Mot de passe mis à jour avec succès !");
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la mise à jour du mot de passe : " + e.getMessage());
        }
        return false;
    }


    /**
     * Génère un token sécurisé pour la réinitialisation.
     */
    private String generateSecureToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /**
     * Hash le mot de passe en utilisant BCrypt.
     */
    private String hashPassword(String password) {
        return org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt(12));
    }
}
