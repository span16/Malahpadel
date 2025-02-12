package models;

public class Profil {
    private int id;
    private int id_user; // clé étrangère vers l'utilisateur
    private String avatar;
    private String bio;
    private String preferences;

    public Profil(int id, int idUser, String avatar, String bio, String preferences) {
        this.id = id;
        this.id_user = idUser;
        this.avatar = avatar;
        this.bio = bio;
        this.preferences = preferences;
    }

    public Profil(int idUser, String avatar, String bio, String preferences) {
        this.id_user = idUser;
        this.avatar = avatar;
        this.bio = bio;
        this.preferences = preferences;
    }

    public Profil() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return id_user;
    }

    public void setIdUser(int idUser) {
        this.id_user = idUser;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
}

