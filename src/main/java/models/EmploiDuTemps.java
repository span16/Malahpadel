package models;

import java.util.Date;

public class EmploiDuTemps {
    private int id;               // Correspond à id_emplois
    private String nomEvenement;  // Correspond à Nom_d'evenement
    private Date date;            // Date du match
    private int partie;           // Numéro de la partie dans la journée

    // Clés étrangères vers l'événement et les équipes (pour un match opposant 2 équipes)
    private Événement evenement;  // L'événement (clé étrangère id_Événement)
    private Equipe equipe1;       // Première équipe (clé étrangère id_equipe)
    private Equipe equipe2;       // Deuxième équipe (clé étrangère equipe_2)

    public EmploiDuTemps() {
    }

    public EmploiDuTemps(String nomEvenement, Date date, int partie,
                         Événement evenement, Equipe equipe1, Equipe equipe2) {
        this.nomEvenement = nomEvenement;
        this.date = date;
        this.partie = partie;
        this.evenement = evenement;
        this.equipe1 = equipe1;
        this.equipe2 = equipe2;
    }

    // Getters et setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNomEvenement() {
        return nomEvenement;
    }
    public void setNomEvenement(String nomEvenement) {
        this.nomEvenement = nomEvenement;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public int getPartie() {
        return partie;
    }
    public void setPartie(int partie) {
        this.partie = partie;
    }
    public Événement getEvenement() {
        return evenement;
    }
    public void setEvenement(Événement evenement) {
        this.evenement = evenement;
    }
    public Equipe getEquipe1() {
        return equipe1;
    }
    public void setEquipe1(Equipe equipe1) {
        this.equipe1 = equipe1;
    }
    public Equipe getEquipe2() {
        return equipe2;
    }
    public void setEquipe2(Equipe equipe2) {
        this.equipe2 = equipe2;
    }
}
