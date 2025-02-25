package models;

import java.sql.Date;

public class Compagne {
    private String nom_sponsor;
    private int id_compagne;
    private Date date_debut;
    private Date date_fin;
    private String logo_compagne;
    private String TypeMarketing;
    private String status;
    private float tarifs;
    private Produit produit;

    // Constructors
    public Compagne() {
        this.produit = new Produit();
    }

    public Compagne(String nom_sponsor, int id_compagne, Date date_debut, Date date_fin, String logo_compagne, String TypeMarketing, String status, float tarifs) {
        this.nom_sponsor = nom_sponsor;
        this.id_compagne = id_compagne;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.logo_compagne = logo_compagne;
        this.TypeMarketing = TypeMarketing;
        this.status = status;
        this.tarifs = tarifs;
        this.produit = new Produit();
    }

    public Compagne(String nom_sponsor, String date_debut, String date_fin, String logo_compagne, String typeMarketing, String status, float tarifs, Produit produit) {
        this.nom_sponsor = nom_sponsor;
        this.date_debut = Date.valueOf(date_debut);
        this.date_fin = Date.valueOf(date_fin);
        this.logo_compagne = logo_compagne;
        this.TypeMarketing = typeMarketing;
        this.status = status;
        this.tarifs = tarifs;
        this.produit = produit;
    }

    public Compagne(String riovaciar, Date date, Date date1, String image, String digital, String active, double v, int idProduit) {
    }

    // Getters and setters
    public String getNom_sponsor() {
        return nom_sponsor;
    }

    public void setNom_sponsor(String nom_sponsor) {
        this.nom_sponsor = nom_sponsor;
    }

    public int getId_compagne() {
        return id_compagne;
    }

    public void setId_compagne(int id_compagne) {
        this.id_compagne = id_compagne;
    }

    public Date getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(Date date_debut) {
        this.date_debut = date_debut;
    }

    public Date getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(Date date_fin) {
        this.date_fin = date_fin;
    }

    public String getLogo_compagne() {
        return logo_compagne;
    }

    public void setLogo_compagne(String logo_compagne) {
        this.logo_compagne = logo_compagne;
    }

    public String getTypeMarketing() {
        return TypeMarketing;
    }

    public void setTypeMarketing(String typeMarketing) {
        this.TypeMarketing = typeMarketing;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getTarifs() {
        return tarifs;
    }

    public void setTarifs(float tarifs) {
        this.tarifs = tarifs;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    @Override
    public String toString() {
        return "Compagne{" +
                "id_compagne=" + id_compagne +
                ", nom_sponsor='" + nom_sponsor + '\'' +
                ", date_debut=" + date_debut +
                ", date_fin=" + date_fin +
                ", logo_compagne='" + logo_compagne + '\'' +
                ", TypeMarketing='" + TypeMarketing + '\'' +
                ", status='" + status + '\'' +
                ", tarifs=" + tarifs +
                ", produit=" + produit +
                '}';
    }
}