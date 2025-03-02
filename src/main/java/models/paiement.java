package models;

public class paiement {
    private int id_R; // Clé étrangère vers Reservation
    private String methode_Paiement;
    private float commission;
    private String description_Paiement;
    private String devise;

    public paiement() {
    }

    public paiement(String methode_Paiement, float commission, String description_Paiement, String devise) {
        this.methode_Paiement = methode_Paiement;
        this.commission = commission;
        this.description_Paiement = description_Paiement;
        this.devise = devise;
    }


    public paiement(int id_R, String methode_Paiement, float commission, String description_Paiement, String devise) {
        this.id_R = id_R;
        this.methode_Paiement = methode_Paiement;
        this.commission = commission;
        this.description_Paiement = description_Paiement;
        this.devise = devise;
    }

    @Override
    public String toString() {
        return "Paiement{" +
                "id_R=" + id_R +
                ", methode_Paiement='" + methode_Paiement + '\'' +
                ", commission=" + commission +
                ", description_Paiement='" + description_Paiement + '\'' +
                ", devise='" + devise + '\'' +
                '}';
    }

    public int getId_R() {
        return id_R;
    }

    public void setId_R(int id_R) {
        this.id_R = id_R;
    }

    public String getMethode_Paiement() {
        return methode_Paiement;
    }

    public void setMethode_Paiement(String methode_Paiement) {
        this.methode_Paiement = methode_Paiement;
    }

    public float getCommission() {
        return commission;
    }

    public void setCommission(float commission) {
        this.commission = commission;
    }

    public String getDescription_Paiement() {
        return description_Paiement;
    }

    public void setDescription_Paiement(String description_Paiement) {
        this.description_Paiement = description_Paiement;
    }

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }
}
