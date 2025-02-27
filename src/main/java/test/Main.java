package test;

import models.evenement;
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
            // 📅 Date pour le paiement
            Date date = Date.valueOf("2024-10-13");

            // 💳 Création et ajout d'un paiement
            paiement p = new paiement(24, 10, 582.03F, "reservé");
            //ps.ajouter(p);

            // 🎉 Création d'un événement (avec id_evenement auto-incrémenté)
            evenement ev = new evenement(3); // Supposons que l'id_evenement = 3 existe déjà dans la base

            // 📝 Création d'une nouvelle réservation
            reservation r = new reservation(
                    4,               // nombre_places
                    "Couple",      // type_reservation
                    95104,           // code_confirmation
                    "seconde réservation", // remarque
                    ev               // évènement lié
            );
            //rs.ajouter(r);  // ➕ Ajout de la réservation

            // 🛠️ Mise à jour d'une réservation existante
            evenement evUpdated = new evenement(3); // id_evenement mis à jour
            reservation updatedReservation = new reservation(
                    2,               // nombre_places
                    "VIP",           // type_reservation
                    54321,           // code_confirmation
                    "Réservation modifiée", // remarque
                    evUpdated        // nouvel événement lié
            );
//modification par id
            // Mise à jour de la réservation avec id_R existant (par exemple, id_R = 1)
            //rs.modifier(
                   // 1, // id_R de la réservation à modifier
                    //updatedReservation.getNombre_places(),
                    //updatedReservation.getType_reservation(),
                    //updatedReservation.getCode_confirmation(),
                   // updatedReservation.getRemarque()
            //);


           // System.out.println("Réservation modifiée avec succès !");

            // 🗑️ Suppression d'une réservation par id_R
            rs.supprimer(95104);
        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}

