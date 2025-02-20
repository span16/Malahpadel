package tn.esprit.models;


import java.time.LocalDateTime;


public class AnnonceMatch {
    private int annonceId;
    private String titre;
    private LocalDateTime date_Heure;
    private String lieu;
    private int joueursRecherches;
    private String niveau;
    private String description;

    public AnnonceMatch(int annonceId, String titre, LocalDateTime date_Heure, String lieu, int joueursRecherches, String niveau, String description) {
        this.annonceId = annonceId;
        this.titre = titre;
        this.date_Heure = date_Heure;
        this.lieu = lieu;
        this.joueursRecherches = joueursRecherches;
        this.niveau = niveau;
        this.description = description;
    }
    public AnnonceMatch( String titre, LocalDateTime date_Heure, String lieu, int joueursRecherches, String niveau, String description) {
        this.titre = titre;
        this.date_Heure = date_Heure;
        this.lieu = lieu;
        this.joueursRecherches = joueursRecherches;
        this.niveau = niveau;
        this.description = description;
    }

    public int getAnnonceId() {
        return annonceId;
    }

    public void setAnnonceId(int annonceId) {
        this.annonceId = annonceId;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public LocalDateTime getDate_Heure() {
        return date_Heure;
    }

    public void setDate_Heure(LocalDateTime dateHeure) {
        this.date_Heure = date_Heure;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public int getJoueursRecherches() {
        return joueursRecherches;
    }

    public void setJoueursRecherches(int joueursRecherches) {
        this.joueursRecherches = joueursRecherches;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "AnnonceMatch{" +
                "annonceId=" + annonceId +
                ", titre='" + titre + '\'' +
                ", dateHeure=" + date_Heure +
                ", lieu='" + lieu + '\'' +
                ", joueursRecherches=" + joueursRecherches +
                ", niveau='" + niveau + '\'' +
                ", description='" + description + '\'' +
                '}';
    }




}