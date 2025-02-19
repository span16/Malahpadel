package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

public class AfficherPaiementController {

    @FXML
    private TextField rIdP;

    @FXML
    private TextField rIdR;

    @FXML
    private TextField rMontant;

    @FXML
    private TextField rStatusP;

    @FXML
    private TextArea rlist;  // Utilisation de TextArea au lieu de TextField

    public void setRIdP(int idP) {
        this.rIdP.setText(String.valueOf(idP));
    }

    public void setRIdR(int idR) {
        this.rIdR.setText(String.valueOf(idR));
    }

    public void setRMontant(float montant) {
        this.rMontant.setText(String.valueOf(montant));
    }

    public void setRStatusP(String statusP) {
        this.rStatusP.setText(statusP);
    }

    public void setRlist(String list) {
        this.rlist.setText(list);  // Affiche la liste dans le TextArea
    }
}
