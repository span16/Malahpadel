package models;

import java.util.Date;

public class Événement {
    private int id;
    private String nom;
    private TypeV type;
    private Date date;
    private Terrain terrain;
    // Nouvel attribut pour l'image (l'URL ou chemin)
    private String imageUrl;

    public Événement() {
    }

    public Événement(String nom, TypeV type, Date date, Terrain terrain, String imageUrl) {
        this.nom = nom;
        this.type = type;
        this.date = date;
        this.terrain = terrain;
        this.imageUrl = imageUrl;
    }

    // Getters et setters existants...
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
    public TypeV getType() {
        return type;
    }
    public void setType(TypeV type) {
        this.type = type;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public Terrain getTerrain() {
        return terrain;
    }
    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    // Getters et setters pour l'image
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Événement{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", type=" + type +
                ", date=" + date +
                ", terrain=" + (terrain != null ? terrain.getNom() : "Aucun") +
                ", imageUrl=" + imageUrl +
                '}';
    }
}
