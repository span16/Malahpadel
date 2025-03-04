package models;

public class Equipe {
    private int equipeId;
    private String nomEquipe;
    private String emailJoueur1;
    private String emailJoueur2;
    private User user; // L'utilisateur qui a créé l'équipe

    public Equipe(int equipeId, String nomEquipe, String emailJoueur1, String emailJoueur2, User user) {
        this.equipeId = equipeId;
        this.nomEquipe = nomEquipe;
        this.emailJoueur1 = emailJoueur1;
        this.emailJoueur2 = emailJoueur2;
        this.user = user;
    }

    public Equipe(String nomEquipe, String emailJoueur1, String emailJoueur2, User user) {
        this.nomEquipe = nomEquipe;
        this.emailJoueur1 = emailJoueur1;
        this.emailJoueur2 = emailJoueur2;
        this.user = user;
    }

    public Equipe() {}

    public int getEquipeId() {
        return equipeId;
    }

    public void setEquipeId(int equipeId) {
        this.equipeId = equipeId;
    }

    public String getNomEquipe() {
        return nomEquipe;
    }

    public void setNomEquipe(String nomEquipe) {
        this.nomEquipe = nomEquipe;
    }

    public String getEmailJoueur1() {
        return emailJoueur1;
    }

    public void setEmailJoueur1(String emailJoueur1) {
        this.emailJoueur1 = emailJoueur1;
    }

    public String getEmailJoueur2() {
        return emailJoueur2;
    }

    public void setEmailJoueur2(String emailJoueur2) {
        this.emailJoueur2 = emailJoueur2;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
