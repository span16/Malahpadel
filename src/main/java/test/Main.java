package test;

import models.paiement;
import models.reservation;
import service.PaiementService;
import service.ReservationService;

import java.sql.Date;

public class Main {
    public static void main(String[] args) {
        ReservationService rs = new ReservationService();
        PaiementService ps = new PaiementService();

        try {
            paiement p = new paiement(24, 10, 582.03F, "reservé");
            //ps.modifier(p, "bienvenue");
            Date date = Date.valueOf("2024-10-13");
            ps.ajouter(p);
           // ps.supprimer(8);
            reservation r = new reservation(2, "esprit", "esprit@gmail.com", new Date(System.currentTimeMillis()), "confirmed");
            reservation updatedReservation = new reservation(1, "wie", "wie.email@example.com", date, "marahbee");

            rs.ajouter(r);  // Décommentez cette ligne si vous voulez ajouter une réservation
            //rs.modifier(updatedReservation, "ninou");
             //rs.supprimer(14);

        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
            e.printStackTrace();  // Affiche la trace complète de l'exception pour mieux comprendre l'erreur
        }
    }
}
