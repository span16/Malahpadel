package models;

import java.sql.Time;

public class Terrain {
    private int id;
    private String nom;
    private String adresse;
    private double prix_par_personne;
    private Time heure_ouverture;
    private Time heure_fermeture;

    // Constructeurs
    public Terrain() {
    }

    public Terrain(int id, String nom, String adresse, double prix_par_personne, Time heure_ouverture, Time heure_fermeture) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.prix_par_personne = prix_par_personne;
        this.heure_ouverture = heure_ouverture;
        this.heure_fermeture = heure_fermeture;
    }

    public Terrain(String nom, String adresse, double prix_par_personne, Time heure_ouverture, Time heure_fermeture) {
        this.nom = nom;
        this.adresse = adresse;
        this.prix_par_personne = prix_par_personne;
        this.heure_ouverture = heure_ouverture;
        this.heure_fermeture = heure_fermeture;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public double getPrix_par_personne() {
        return prix_par_personne;
    }

    public void setPrix_par_personne(double prix_par_personne) {
        this.prix_par_personne = prix_par_personne;
    }

    public Time getHeure_ouverture() {
        return heure_ouverture;
    }

    public void setHeure_ouverture(Time heure_ouverture) {
        this.heure_ouverture = heure_ouverture;
    }

    public Time getHeure_fermeture() {
        return heure_fermeture;
    }

    public void setHeure_fermeture(Time heure_fermeture) {
        this.heure_fermeture = heure_fermeture;
    }

    @Override
    public String toString() {
        return "Terrain{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", prix_par_personne=" + prix_par_personne +
                ", heure_ouverture=" + heure_ouverture +
                ", heure_fermeture=" + heure_fermeture +
                '}';
    }
}
