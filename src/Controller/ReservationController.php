<?php

namespace App\Controller;

use App\Entity\Reservation;
use App\Form\ReservationType;
use App\Repository\ReservationRepository;
use App\Repository\EvenementRepository;
use Doctrine\ORM\EntityManagerInterface;
use Psr\Log\LoggerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;
use Symfony\Component\Security\Csrf\CsrfTokenManagerInterface;

class ReservationController extends AbstractController
{
    public function __construct(
        private EntityManagerInterface $entityManager,
        private ReservationRepository $reservationRepository,
        private EvenementRepository $evenementRepository,
        private CsrfTokenManagerInterface $csrfTokenManager,
        private LoggerInterface $logger
    ) {}

    /**************************
     * PARTIE FRONT
     **************************/

    #[Route('/', name: 'app_home')]
    public function home(): Response
    {
        return $this->redirectToRoute('app_reservation');
    }

    #[Route('/reservation', name: 'app_reservation', methods: ['GET', 'POST'])]
    public function index(Request $request): Response
    {
        $reservation = new Reservation();
        $form = $this->createForm(ReservationType::class, $reservation, [
            'evenement_names' => $this->getEventNames()
        ]);

        $form->handleRequest($request);

        if ($form->isSubmitted()) {
            if ($form->isValid()) {
                try {
                    if (!$reservation->getCodeConfirmation()) {
                        $reservation->generateConfirmationCode();
                    }

                    $this->entityManager->persist($reservation);
                    $this->entityManager->flush();

                    $this->addFlash(
                        'success',
                        sprintf('Réservation créée avec succès ! Votre code: %s', $reservation->getCodeConfirmation())
                    );
                    
                    return $this->redirectToRoute('app_reservation');
                } catch (\Exception $e) {
                    $this->logger->error('Erreur création réservation: ' . $e->getMessage());
                    $this->addFlash('error', 'Une erreur est survenue lors de la création');
                }
            } else {
                foreach ($form->getErrors(true) as $error) {
                    $this->logger->error('Erreur validation: '.$error->getMessage());
                }
            }
        }

        return $this->render('reservation/reservation.html.twig', [
            'form' => $form->createView(),
            'reservations' => $this->reservationRepository->findAllOrderedById()
        ]);
    }

    #[Route('/reservation/{id}', name: 'app_reservation_show', methods: ['GET'])]
    public function show(Reservation $reservation): Response
    {
        return $this->render('reservation/show.html.twig', [
            'reservation' => $reservation
        ]);
    }

    #[Route('/reservation/{id}/edit', name: 'app_reservation_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Reservation $reservation): Response
    {
        $form = $this->createForm(ReservationType::class, $reservation, [
            'evenement_names' => $this->getEventNames()
        ]);

        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            try {
                $this->entityManager->flush();
                
                if ($request->isXmlHttpRequest()) {
                    return $this->jsonResponse($reservation);
                }

                $this->addFlash('success', 'Réservation mise à jour avec succès !');
                return $this->redirectToRoute('app_reservation');
            } catch (\Exception $e) {
                $this->logger->error('Erreur modification: ' . $e->getMessage());
                $this->addFlash('error', 'Erreur lors de la modification');
            }
        }

        return $this->render('reservation/edit.html.twig', [
            'form' => $form->createView(),
            'reservation' => $reservation
        ]);
    }

    #[Route('/reservation/{id}/cancel', name: 'app_reservation_cancel', methods: ['POST'])]
    public function cancel(Request $request, Reservation $reservation): Response
    {
        if (!$this->isCsrfTokenValid('cancel'.$reservation->getId(), $request->request->get('_token'))) {
            $this->addFlash('error', 'Token invalide');
            return $this->redirectToRoute('app_reservation_show', ['id' => $reservation->getId()]);
        }
    
        return $this->redirectToRoute('app_reclamation_new', [
            'reservation_id' => $reservation->getId(),
            'is_cancellation' => 1
        ], Response::HTTP_SEE_OTHER);
    }

    #[Route('/reservation/{id}', name: 'app_reservation_delete', methods: ['POST'])]
    public function delete(Request $request, Reservation $reservation): Response
    {
        if ($this->isCsrfTokenValid('delete'.$reservation->getId(), $request->request->get('_token'))) {
            try {
                $this->entityManager->remove($reservation);
                $this->entityManager->flush();

                if ($request->isXmlHttpRequest()) {
                    return new JsonResponse(['success' => true]);
                }

                $this->addFlash('success', 'Réservation supprimée !');
            } catch (\Exception $e) {
                $this->logger->error('Erreur suppression: ' . $e->getMessage());
                $this->addFlash('error', 'Erreur lors de la suppression');
            }
        }

        return $this->redirectToRoute('app_reservation');
    }

    /**************************
     * PARTIE ADMIN
     **************************/

     #[Route('/admin/reservations', name: 'admin_reservation_index')]
     public function adminIndex(): Response
     {
         $reservations = $this->reservationRepository->findAllOrderedById();
         $reservation = new Reservation();
         
         $form = $this->createForm(ReservationType::class, $reservation, [
             'evenement_names' => $this->getEventNames(),
             'action' => $this->generateUrl('admin_reservation_new')
         ]);
         
         return $this->render('back/reservationback.html.twig', [
             'reservations' => $reservations,
             'form' => $form->createView(), // Ne passez pas null ici
             'form_title' => 'Gestion des réservations',
             'button_label' => 'Créer',
             'total_reservations' => count($reservations),
             'show_list' => true
         ]);
     }

    #[Route('/admin/reservation/new', name: 'admin_reservation_new', methods: ['GET', 'POST'])]
public function adminNew(Request $request): Response
{
    $reservation = new Reservation();
    $form = $this->createForm(ReservationType::class, $reservation, [
        'evenement_names' => $this->getEventNames(),
        'action' => $this->generateUrl('admin_reservation_new') // Ajout important
    ]);

    $form->handleRequest($request);

    if ($form->isSubmitted()) {
        if ($form->isValid()) {
            try {
                if (!$reservation->getCodeConfirmation()) {
                    $reservation->generateConfirmationCode();
                }

                $this->entityManager->persist($reservation);
                $this->entityManager->flush();

                $this->addFlash('success', 'Réservation créée avec succès !');
                return $this->redirectToRoute('admin_reservation_index');
            } catch (\Exception $e) {
                $this->logger->error('Erreur création réservation: ' . $e->getMessage());
                $this->addFlash('error', 'Erreur lors de la création');
                
                // Réafficher le formulaire avec erreurs
                return $this->render('back/reservationback.html.twig', [
                    'form' => $form->createView(),
                    'form_title' => 'Nouvelle réservation',
                    'button_label' => 'Confirmer',
                    'reservations' => $this->reservationRepository->findAllOrderedById(),
                    'total_reservations' => count($this->reservationRepository->findAll()),
                    'show_list' => false
                ]);
            }
        }
    }

    return $this->render('back/reservationback.html.twig', [
        'form' => $form->createView(),
        'form_title' => 'Nouvelle réservation',
        'button_label' => 'Confirmer',
        'reservations' => $this->reservationRepository->findAllOrderedById(),
        'total_reservations' => count($this->reservationRepository->findAll()),
        'show_list' => false
    ]);
}

    #[Route('/admin/reservation/{id}/edit', name: 'admin_reservation_edit', methods: ['GET', 'POST'])]
    public function adminEdit(Request $request, Reservation $reservation): Response
    {
        $form = $this->createForm(ReservationType::class, $reservation, [
            'evenement_names' => $this->getEventNames()
        ]);

        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            try {
                $this->entityManager->flush();
                $this->addFlash('success', 'Réservation mise à jour avec succès !');
                return $this->redirectToRoute('admin_reservation_index');
            } catch (\Exception $e) {
                $this->logger->error('Erreur modification: ' . $e->getMessage());
                $this->addFlash('error', 'Erreur lors de la modification');
            }
        }

        return $this->render('back/reservationback.html.twig', [
            'form' => $form->createView(),
            'reservation' => $reservation,
            'reservations' => $this->reservationRepository->findAllOrderedById(),
            'form_title' => 'Modifier réservation',
            'button_label' => 'Mettre à jour',
            'total_reservations' => count($this->reservationRepository->findAll()),
            'show_list' => false
        ]);
    }

    #[Route('/admin/reservation/{id}/delete', name: 'admin_reservation_delete', methods: ['POST'])]
    public function adminDelete(Request $request, Reservation $reservation): Response
    {
        if ($this->isCsrfTokenValid('delete'.$reservation->getId(), $request->request->get('_token'))) {
            try {
                $this->entityManager->remove($reservation);
                $this->entityManager->flush();
                $this->addFlash('success', 'Réservation supprimée avec succès !');
            } catch (\Exception $e) {
                $this->logger->error('Erreur suppression: ' . $e->getMessage());
                $this->addFlash('error', 'Erreur lors de la suppression');
            }
        }

        return $this->redirectToRoute('admin_reservation_index');
    }

    /**************************
     * METHODES COMMUNES
     **************************/

    private function jsonResponse(Reservation $reservation): JsonResponse
    {
        return new JsonResponse([
            'success' => true,
            'id' => $reservation->getId(),
            'nom' => $reservation->getNom(),
            'codeConfirmation' => $reservation->getCodeConfirmation(),
            'csrf_token' => $this->csrfTokenManager->getToken('delete'.$reservation->getId())->getValue()
        ]);
    }

    private function getEventNames(): array
    {
        $evenementNamesRaw = $this->evenementRepository->findAllEventNames();
        return array_combine($evenementNamesRaw, $evenementNamesRaw);
    }
}