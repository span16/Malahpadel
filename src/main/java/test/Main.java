package test;

import models.Evenement;
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

            // 🎉 Création d'un événement (avec id_evenement auto-incrémenté)
            Evenement ev = new Evenement("Conee");
            // 📝 Création d'une nouvelle réservation

            reservation r = new reservation(5, "VIP", 12345, "Aucune remarque", ev);             // évènement lié


            // Vérifier si la réservation existe avant d'ajouter un paiement
            boolean reservationExiste = rs.reservationExists(r.getCode_confirmation()); // Utilisez rs.reservationExists
            if (reservationExiste) {
                // 💳 Création et ajout d'un paiement
                paiement p = new paiement("Carte", 5.0F, "Paiement de réservation", "EUR");
                ps.ajouter(p);
                System.out.println("✅ Paiement ajouté avec succès !");
            } else {
                System.out.println("⚠️ La réservation avec le code de confirmation " + r.getCode_confirmation() + " n'existe pas.");
            }

            // 🛠️ Mise à jour d'une réservation existante
            Evenement evUpdated = new Evenement(39, "Nouveau Nom", Date.valueOf("2023-12-31"));
            reservation updatedReservation = new reservation(
                    10,               // nombre_places
                    "perso",           // type_reservation
                    852031,           // code_confirmation
                    "reservation pour famille ", // remarque
                    evUpdated        // nouvel événement lié
            );

            // Mise à jour de la réservation avec id_R existant
            /*rs.modifier(
                    53, // id_R de la réservation à modifier
                    updatedReservation.getNombre_places(),
                    updatedReservation.getType_reservation(),
                    updatedReservation.getCode_confirmation(),
                    updatedReservation.getRemarque()
            );
            System.out.println("Réservation modifiée avec succès !");*/

            // 🗑️ Suppression d'une réservation par id_R
            //rs.supprimer(98765);
        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
        //System.out.println("Réservation supprimée avec succès !");
    }
}