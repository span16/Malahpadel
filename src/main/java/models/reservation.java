package models;

import java.sql.Date;

public class reservation {
    private int id_R;
    private int id_P;          // 🔹 Clé étrangère vers paiement
    private String nomC;
    private String email;
    private Date dateR;
    private String status;

    public reservation(int id_R, int id_P, String nomC, String email, Date dateR, String status) {
        this.id_R = id_R;
        this.id_P = id_P;
        this.nomC = nomC;
        this.email = email;
        this.dateR = dateR;
        this.status = status;
    }

    public reservation(int id_P, String nomC, String email, Date dateR, String status) {
        this.id_P = id_P;
        this.nomC = nomC;
        this.email = email;
        this.dateR = dateR;
        this.status = status;
    }

    public int getId_R() {
        return id_R;
    }

    public void setId_R(int id_R) {
        this.id_R = id_R;
    }

    public int getId_P() {
        return id_P;
    }

    public void setId_P(int id_P) {
        this.id_P = id_P;
    }

    public String getNomC() {
        return nomC;
    }

    public void setNomC(String nomC) {
        this.nomC = nomC;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateR() {
        return dateR;
    }

    public void setDateR(Date dateR) {
        this.dateR = dateR;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "reservation{id_R=" + id_R + ", id_P=" + id_P + ", nomC='" + nomC + "', email='" + email + "', dateR=" + dateR + ", status='" + status + "'}";
    }
}
