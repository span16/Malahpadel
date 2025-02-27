package models;

public class reservation {
    private int id_R;  // Attribut auto-incrémenté
    private int nombre_places;
    private String type_reservation;
    private int code_confirmation;
    private String remarque;
    private evenement evenement;  // Nouvel attribut de type Evenement

    // Constructeur sans id_R, car il est généré par la base de données
    public reservation(int nombre_places, String type_reservation, int code_confirmation, String remarque, evenement evenement) {
        this.nombre_places = nombre_places;
        this.type_reservation = type_reservation;
        this.code_confirmation = code_confirmation;
        this.remarque = remarque;
        this.evenement = evenement;
    }

    // Constructeur avec id_R pour initialiser à partir de la base de données
    public reservation(int id_R, int nombre_places, String type_reservation, int code_confirmation, String remarque, evenement evenement) {
        this.id_R = id_R;
        this.nombre_places = nombre_places;
        this.type_reservation = type_reservation;
        this.code_confirmation = code_confirmation;
        this.remarque = remarque;
        this.evenement = evenement;
    }

    public int getId_R() {
        return id_R;
    }

    public void setId_R(int id_R) {
        this.id_R = id_R;
    }

    public int getNombre_places() {
        return nombre_places;
    }

    public void setNombre_places(int nombre_places) {
        this.nombre_places = nombre_places;
    }

    public String getType_reservation() {
        return type_reservation;
    }

    public void setType_reservation(String type_reservation) {
        this.type_reservation = type_reservation;
    }

    public int getCode_confirmation() {
        return code_confirmation;
    }

    public void setCode_confirmation(int code_confirmation) {
        this.code_confirmation = code_confirmation;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public evenement getEvenement() {
        return evenement;
    }

    public void setEvenement(evenement evenement) {
        this.evenement = evenement;
    }

    @Override
    public String toString() {
        return "reservation{" +
                "id_R=" + id_R +  // Affichage de l'id_R
                ", nombre_places=" + nombre_places +
                ", type_reservation='" + type_reservation + '\'' +
                ", code_confirmation=" + code_confirmation +
                ", remarque='" + remarque + '\'' +
                ", evenement=" + evenement +  // Affiche l'événement
                '}';
    }
}
