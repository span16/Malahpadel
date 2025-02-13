package tests;
import java.sql.Date;

import models.Compagne;
import models.Produit;
import services.CompagneService;
import services.ProduitService;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        ProduitService ps1= new ProduitService();
        CompagneService ps2= new CompagneService();
        Produit p = new Produit(100, "svr","skincare","svr.jpg","haute gamme",50);
        Compagne c = new Compagne("riovaciar", "2003-08-11", "2025-05-11", "rio.png", "ads", "active", 500);
        try {
           // ps1.ajouter(p);
            //ps2.ajoutercompagne(c);
            System.out.println(ps1.recuperer());
            for (Compagne c1 : ps2.recuperercompagne()) {
                System.out.println("Compagne { id_compagne=" + c1.getId_compagne() +
                        ", nom_sponsor='" + c1.getNom_sponsor() +
                        "', date_debut=" + c1.getDate_debut() +
                        ", date_fin=" + c1.getDate_fin() +
                        ", logo_compagne='" + c1.getLogo_compagne() +
                        "', Typemarketing='" + c1.getTypeMarketing() +
                        "', status='" + c1.getStatus() +
                        "', tarifs=" + c1.getTarifs() + " }");
            }

            Produit p2 = new Produit(40, "vanilla's", "trousse makeup", "vanilla.jpg", "makeup kit", 30);
          ps1.modifier(p2, 8);
            ps1.supprimer(p, "svr");

           Compagne cModif = new Compagne("sugarbaby", "2024-01-01", "2024-12-31", "sugarbaby.png", "digital", "inactive", 200);
           ps2.modifiercompagne(cModif, 11);
            ps2.supprimercompagne(c, "rio");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

}
