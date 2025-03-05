package models;

import java.util.Date;

public class EmploiDuTemps {
    private int id;               // id_emplois
    private String nomEvenement;  // Nom de l'événement
    private Date date;            // Date du match
    private int partie;           // Numéro de la partie

    // Clés étrangères
    private Événement evenement;
    private Equipe equipe1;
    private Equipe equipe2;

    // Nouveau champ pour l'ID Google
    private String googleEventId;

    public EmploiDuTemps() { }

    public EmploiDuTemps(String nomEvenement, Date date, int partie,
                         Événement evenement, Equipe equipe1, Equipe equipe2) {
        this.nomEvenement = nomEvenement;
        this.date = date;
        this.partie = partie;
        this.evenement = evenement;
        this.equipe1 = equipe1;
        this.equipe2 = equipe2;
    }

    // Getters et setters existants...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNomEvenement() { return nomEvenement; }
    public void setNomEvenement(String nomEvenement) { this.nomEvenement = nomEvenement; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public int getPartie() { return partie; }
    public void setPartie(int partie) { this.partie = partie; }
    public Événement getEvenement() { return evenement; }
    public void setEvenement(Événement evenement) { this.evenement = evenement; }
    public Equipe getEquipe1() { return equipe1; }
    public void setEquipe1(Equipe equipe1) { this.equipe1 = equipe1; }
    public Equipe getEquipe2() { return equipe2; }
    public void setEquipe2(Equipe equipe2) { this.equipe2 = equipe2; }

    // Nouveau getter/setter pour Google Event ID
    public String getGoogleEventId() { return googleEventId; }
    public void setGoogleEventId(String googleEventId) { this.googleEventId = googleEventId; }
}
