package models;

public class User {
    private int id;
    private int age;
    private int cin;
    private String nom;
    private String prenom;
    private String email;
    private String mdp;
    private String etat;
    private Role fonction;

    // Constructor complet
    public User(String nom, String prenom, String email, String mdp, int age, int cin, Role fonction) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.age = age;
        this.cin = cin;
        this.fonction = fonction;
    }

    // Autre constructeur par défaut
    public User() {}

    public User(int age, int cin, String nom, String prenom, String email, String mdp, String etat, Role fonction) {
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCin() {
        return cin;
    }

    public void setCin(int cin) {
        this.cin = cin;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Role getFonction() {
        return fonction;
    }

    public void setFonction(Role fonction) {
        this.fonction = fonction;
    }
}
