package models;

public class paiement {
    private int id_P;
    private int id_R;
    private float montant;
    private String status_P;

    public paiement() {
    }

    public paiement(int id_R, float montant, String status_P) {
        this.id_R = id_R;
        this.montant = montant;
        this.status_P = status_P;
    }

    public paiement(int id_P, int id_R, float montant, String status_P) {
        this.id_P = id_P;
        this.id_R = id_R;
        this.montant = montant;
        this.status_P = status_P;
    }

    @Override
    public String toString() {
        return "Paiement{" +
                "id_P=" + id_P +
                ", id_R=" + id_R +
                ", montant=" + montant +
                ", status_P='" + status_P + '\'' +
                '}';
    }

    public int getId_P() {
        return id_P;
    }

    public void setId_P(int id_P) {
        this.id_P = id_P;
    }

    public int getId_R() {
        return id_R;
    }

    public void setId_R(int id_R) {
        this.id_R = id_R;
    }

    public float getMontant() {
        return montant;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }

    public String getStatus_P() {
        return status_P;
    }

    public void setStatus_P(String status_P) {
        this.status_P = status_P;
    }
}
