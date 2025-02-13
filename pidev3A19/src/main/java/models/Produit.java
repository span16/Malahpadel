package models;

public class Produit {
    private int id_produit, stock;
    private String nom_produit,categorie,image_produit,description;
    private  float prix;
    public Produit() {
    }

    public Produit(int stock, String nom_produit, String categorie,String image_produit,String description,float prix) {
        this.stock = stock;
        this.nom_produit = nom_produit;
        this.categorie = categorie;
        this.image_produit = image_produit;
        this.description = description;
        this.prix = prix;
    }

    public Produit(int id_produit, int stock, String nom_produit, String categorie,String image_produit,String description,float prix) {
        this.id_produit = id_produit;
        this.stock = stock;
        this.nom_produit = nom_produit;
        this.categorie = categorie;
        this.image_produit = image_produit;
        this.description = description;
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "Produit{" +
                "id_produit=" + id_produit +
                ", nom_produit='" + nom_produit + '\'' +
                ", categorie='" + categorie + '\'' +
                ", image_produit='" + image_produit + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", stock=" + stock +
                '}';
    }

    public int getId_produit() {
        return id_produit;
    }

    public void setId_produit(int id_produit) {
        this.id_produit = id_produit;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getNom_produit() {
        return nom_produit;
    }

    public void setNom_produit(String nom_produit) {
        this.nom_produit = nom_produit;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getImage_produit() {
        return image_produit;
    }

    public void setImage_produit(String image_produit) {
        this.image_produit = image_produit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }
}
