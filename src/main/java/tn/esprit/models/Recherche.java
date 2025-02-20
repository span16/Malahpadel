package tn.esprit.models;

public class Recherche {
    private int userId;
    private String nom;
    private String niveau;
    private String annonces;

    public Recherche(int userId, String nom, String niveau, String annonces) {
        this.userId = userId;
        this.nom = nom;
        this.niveau = niveau;
        this.annonces = annonces;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public String getAnnonces() {
        return annonces;
    }

    public void setAnnonces(String annonces) {
        this.annonces = annonces;
    }

    @Override
    public String toString() {
        return "Recherche{" +
                "userId=" + userId +
                ", nom='" + nom + '\'' +
                ", niveau='" + niveau + '\'' +
                ", annonces='" + annonces + '\'' +
                '}';
    }
}
