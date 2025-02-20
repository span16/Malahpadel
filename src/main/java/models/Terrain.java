package models;

import java.sql.Time;

public class Terrain {
    private int id;
    private String nom;
    private String adresse;
    private double prixParPersonne;
    private Time heureOuverture;
    private Time heureFermeture;

    // ✅ Constructeur par défaut
    public Terrain() {}

    // ✅ Constructeur principal (avec ID)
    public Terrain(int id, String nom, String adresse, double prixParPersonne, Time heureOuverture, Time heureFermeture) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.prixParPersonne = prixParPersonne;
        this.heureOuverture = heureOuverture;
        this.heureFermeture = heureFermeture;
    }

    // ✅ Constructeur principal (sans ID)
    public Terrain(String nom, String adresse, double prixParPersonne, Time heureOuverture, Time heureFermeture) {
        this.nom = nom;
        this.adresse = adresse;
        this.prixParPersonne = prixParPersonne;
        this.heureOuverture = heureOuverture;
        this.heureFermeture = heureFermeture;
    }

    // ✅ Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public double getPrixParPersonne() { return prixParPersonne; }
    public void setPrixParPersonne(double prixParPersonne) { this.prixParPersonne = prixParPersonne; }

    public Time getHeureOuverture() { return heureOuverture; }
    public void setHeureOuverture(Time heureOuverture) { this.heureOuverture = heureOuverture; }

    public Time getHeureFermeture() { return heureFermeture; }
    public void setHeureFermeture(Time heureFermeture) { this.heureFermeture = heureFermeture; }

    @Override
    public String toString() {
        return "Terrain{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", prixParPersonne=" + prixParPersonne +
                ", heureOuverture=" + heureOuverture +
                ", heureFermeture=" + heureFermeture +
                '}';
    }
}
