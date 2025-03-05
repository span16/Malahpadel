package services;

import models.EmploiDuTemps;
import models.Événement;
import models.Equipe;
import tools.MyDataBase;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class ServiceEmploiDuTemps implements IEmploiDuTemps {

    private Connection cnx;

    public ServiceEmploiDuTemps() {
        this.cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(EmploiDuTemps edt) throws SQLException {
        String sql = "INSERT INTO emploidutemps (Date, partie, id_Événement, id_equipe, equipe_2) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement st = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setDate(1, new java.sql.Date(edt.getDate().getTime()));
            st.setInt(2, edt.getPartie());
            st.setInt(3, edt.getEvenement().getId());
            st.setInt(4, edt.getEquipe1().getEquipeId());
            st.setInt(5, edt.getEquipe2().getEquipeId());
            st.executeUpdate();
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    edt.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void modifier(EmploiDuTemps edt) throws SQLException {
        String sql = "UPDATE emploidutemps SET Date = ?, partie = ?, id_Événement = ?, id_equipe = ?, equipe_2 = ?, google_event_id = ? WHERE id_emplois = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setDate(1, new java.sql.Date(edt.getDate().getTime()));
            st.setInt(2, edt.getPartie());
            st.setInt(3, edt.getEvenement().getId());
            st.setInt(4, edt.getEquipe1().getEquipeId());
            st.setInt(5, edt.getEquipe2().getEquipeId());
            st.setString(6, edt.getGoogleEventId()); // Mise à jour de l'ID Google
            st.setInt(7, edt.getId());
            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Aucun emploi du temps trouvé avec l'ID " + edt.getId());
            }
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM emploidutemps WHERE id_emplois = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setInt(1, id);
            int rowsDeleted = st.executeUpdate();
            if (rowsDeleted == 0) {
                throw new SQLException("Aucun emploi du temps trouvé avec l'ID " + id);
            }
        }
    }

    @Override
    public List<EmploiDuTemps> recuperer() throws SQLException {
        List<EmploiDuTemps> liste = new ArrayList<>();
        String sql = "SELECT * FROM emploidutemps";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                EmploiDuTemps edt = new EmploiDuTemps();
                edt.setId(rs.getInt("id_emplois"));
                edt.setDate(rs.getDate("Date"));
                edt.setPartie(rs.getInt("partie"));
                int idEvent = rs.getInt("id_Événement");
                int idEq1 = rs.getInt("id_equipe");
                int idEq2 = rs.getInt("equipe_2");
                ÉvénementService evtService = new ÉvénementService();
                edt.setEvenement(evtService.recupererParId(idEvent));
                EquipeService eqService = new EquipeService();
                edt.setEquipe1(eqService.recupererParId(idEq1));
                edt.setEquipe2(eqService.recupererParId(idEq2));
                // Récupération de l'ID Google s'il existe
                edt.setGoogleEventId(rs.getString("google_event_id"));
                liste.add(edt);
            }
        }
        return liste;
    }

    @Override
    public EmploiDuTemps recupererParId(int id) throws SQLException {
        EmploiDuTemps edt = null;
        String sql = "SELECT * FROM emploidutemps WHERE id_emplois = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    edt = new EmploiDuTemps();
                    edt.setId(rs.getInt("id_emplois"));
                    edt.setDate(rs.getDate("Date"));
                    edt.setPartie(rs.getInt("partie"));
                    int idEvent = rs.getInt("id_Événement");
                    int idEq1 = rs.getInt("id_equipe");
                    int idEq2 = rs.getInt("equipe_2");
                    ÉvénementService evtService = new ÉvénementService();
                    edt.setEvenement(evtService.recupererParId(idEvent));
                    EquipeService eqService = new EquipeService();
                    edt.setEquipe1(eqService.recupererParId(idEq1));
                    edt.setEquipe2(eqService.recupererParId(idEq2));
                    edt.setGoogleEventId(rs.getString("google_event_id"));
                }
            }
        }
        return edt;
    }

    // Génération automatique d'emplois du temps avec insertion dans Google Calendar
    public List<EmploiDuTemps> genererEmploiDuTempsAuto(Événement event, Date matchDate, int nombreParties) throws SQLException {
        List<Equipe> equipes = new EquipeService().recupererEquipes();
        if (equipes.size() < nombreParties * 2) {
            throw new SQLException("Pas assez d'équipes pour générer " + nombreParties + " parties.");
        }
        Collections.shuffle(equipes);
        List<EmploiDuTemps> emplois = new ArrayList<>();
        for (int i = 0; i < nombreParties; i++) {
            Equipe eq1 = equipes.remove(0);
            Equipe eq2 = equipes.remove(0);
            EmploiDuTemps edt = new EmploiDuTemps();
            edt.setDate(matchDate);
            edt.setPartie(i + 1);
            edt.setEvenement(event);
            edt.setEquipe1(eq1);
            edt.setEquipe2(eq2);
            // Ajout dans la base
            ajouter(edt);
            emplois.add(edt);
            // Insertion dans Google Calendar et mise à jour de l'ID dans la base
            try {
                String googleId = GoogleCalendarService.addEventToGoogleCalendar(edt);
                edt.setGoogleEventId(googleId);
                updateGoogleEventId(edt.getId(), googleId);
            } catch (IOException | GeneralSecurityException e) {
                e.printStackTrace();
            }
        }
        return emplois;
    }

    // Méthode privée pour mettre à jour l'ID Google dans la base
    private void updateGoogleEventId(int edtId, String googleEventId) throws SQLException {
        String sql = "UPDATE emploidutemps SET google_event_id = ? WHERE id_emplois = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setString(1, googleEventId);
            st.setInt(2, edtId);
            st.executeUpdate();
        }
    }
}
