package models;

import java.sql.Date;

public class Evenement {
    private int id;
    private String nom;
    private Date date;

    // Constructeur par défaut (sans arguments)
    public Evenement() {
        this.id = 0; // Valeur par défaut pour l'id
        this.nom = ""; // Nom vide par défaut
        this.date = null; // Date non définie par défaut
    }

    // Constructeur avec id, nom et date
    public Evenement(int id, String nom, Date date) {
        this.id = id;
        this.nom = nom;
        this.date = date;
    }

    // Constructeur avec uniquement le nom
    public Evenement(String nom) {
        this.id = 0; // Valeur par défaut pour l'id
        this.nom = nom;
        this.date = null; // Date non définie par défaut
    }

    // Getters et setters
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", date=" + date +
                '}';
    }
}