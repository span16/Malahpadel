package models;

import java.util.Date;

public class Événement {
    private int id;
    private String nom;
    private TypeV type;
    private Date date;
    private Terrain terrain;

    public Événement() {
    }

    public Événement( String nom, TypeV type, Date date,  Terrain terrain) {

        this.nom = nom;
        this.type = type;
        this.date = date;
        this.terrain = terrain;
    }

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

    public Terrain getTerrain() { // 🔥 Getter pour terrain
        return terrain;
    }

    public void setTerrain(Terrain terrain) { // 🔥 Setter pour terrain
        this.terrain = terrain;
    }

    @Override
    public String toString() {
        return "Événement{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", type=" + type +
                ", date=" + date +
                ", terrain=" + (terrain != null ? terrain.getNom() : "Aucun") +
                '}'; // 🔥 Afficher le nom du terrain
    }


}