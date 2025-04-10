<?php

namespace App\Controller;

use App\Entity\Reclamation;
use App\Entity\Reservation;
use App\Form\ReclamationType;
use App\Repository\ReclamationRepository;
use App\Repository\ReservationRepository;
use Doctrine\ORM\EntityManagerInterface;
use Psr\Log\LoggerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;
use Symfony\Component\Validator\Validator\ValidatorInterface;

class ReclamationController extends AbstractController
{
    public function __construct(
        private readonly EntityManagerInterface $entityManager,
        private readonly ReservationRepository $reservationRepository,
        private readonly ReclamationRepository $reclamationRepository,
        private readonly LoggerInterface $logger,
        private readonly ValidatorInterface $validator
    ) {}

    /**************************
     * PARTIE FRONT
     **************************/
    #[Route('/reclamation', name: 'app_reclamation_index', methods: ['GET'])]
   public function index(): Response
{
    $reclamation = new Reclamation();
    $form = $this->createForm(ReclamationType::class, $reclamation);

    return $this->render('reclamation/index.html.twig', [
        'reclamations' => $this->reclamationRepository->findAllOrderedByDate(),
        'form' => $form->createView()
    ]);
}


    #[Route('/reclamation', name: 'app_reclamation_new')]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $reclamation = new Reclamation();

        $form = $this->createForm(ReclamationType::class, $reclamation);

        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($reclamation);
            $entityManager->flush();

            $this->addFlash('reclamation_success', [
                'title' => 'Réclamation envoyée',
                'message' => 'Votre demande a bien été enregistrée.',
                'details' => 'Nous vous contacterons dans les plus brefs délais.',
                'is_cancellation' => false
            ]);

            return $this->redirectToRoute('app_reclamation_new');
        }

        return $this->render('reclamation/index.html.twig', [
            'form' => $form->createView()
        ]);
    }

    #[Route('/reclamation/{id}/edit', name: 'app_reclamation_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Reclamation $reclamation): Response
    {
        $form = $this->createForm(ReclamationType::class, $reclamation);
        $form->handleRequest($request);

        if ($form->isSubmitted()) {
            $errors = $this->validator->validate($reclamation);
            
            if ($form->isValid() && count($errors) === 0) {
                $this->entityManager->flush();
                $this->addFlash('success', 'Réclamation mise à jour avec succès');
                return $this->redirectToRoute('app_reclamation_index');
            } else {
                foreach ($errors as $error) {
                    $this->addFlash('error', $error->getMessage());
                }
            }
        }

        return $this->render('reclamation/edit.html.twig', [
            'reclamation' => $reclamation,
            'form' => $form->createView(),
            'errors' => isset($errors) ? $errors : null
        ]);
    }

    #[Route('/reclamation/{id}', name: 'app_reclamation_delete', methods: ['POST'])]
    public function delete(Request $request, Reclamation $reclamation): Response
    {
        if ($this->isCsrfTokenValid('delete'.$reclamation->getId(), $request->request->get('_token'))) {
            $this->entityManager->remove($reclamation);
            $this->entityManager->flush();
            $this->addFlash('success', 'Réclamation supprimée avec succès');
        }

        return $this->redirectToRoute('app_reclamation_index');
    }

    /**************************
     * PARTIE ADMIN
     **************************/

    #[Route('/admin/reclamations', name: 'admin_reclamation_index')]
    public function adminIndex(): Response
    {
        $reclamations = $this->reclamationRepository->findAllOrderedByDate();
        $reclamation = new Reclamation();
        
        $form = $this->createForm(ReclamationType::class, $reclamation, [
            'action' => $this->generateUrl('admin_reclamation_new')
        ]);
        
        return $this->render('back/reclamationback.html.twig', [
            'reclamations' => $reclamations,
            'form' => $form->createView(),
            'form_title' => 'Gestion des réclamations',
            'button_label' => 'Créer',
            'total_reclamations' => count($reclamations),
            'show_list' => true
        ]);
    }

    #[Route('/admin/reclamation/new', name: 'admin_reclamation_new', methods: ['GET', 'POST'])]
    public function adminNew(Request $request): Response
    {
        $reclamation = new Reclamation();
        $form = $this->createForm(ReclamationType::class, $reclamation, [
            'action' => $this->generateUrl('admin_reclamation_new')
        ]);

        $form->handleRequest($request);

        if ($form->isSubmitted()) {
            $errors = $this->validator->validate($reclamation);
            
            if ($form->isValid() && count($errors) === 0) {
                try {
                    $this->initializeReclamation($reclamation, $request);
                    $this->entityManager->persist($reclamation);
                    $this->entityManager->flush();

                    $this->addFlash('success', 'Réclamation créée avec succès !');
                    return $this->redirectToRoute('admin_reclamation_index');
                } catch (\Exception $e) {
                    $this->logger->error('Erreur création réclamation: ' . $e->getMessage());
                    $this->addFlash('error', 'Erreur lors de la création');
                }
            } else {
                foreach ($errors as $error) {
                    $this->addFlash('error', $error->getMessage());
                }
            }
        }

        return $this->render('back/reclamationback.html.twig', [
            'form' => $form->createView(),
            'form_title' => 'Nouvelle réclamation',
            'button_label' => 'Confirmer',
            'reclamations' => $this->reclamationRepository->findAllOrderedByDate(),
            'total_reclamations' => count($this->reclamationRepository->findAll()),
            'show_list' => false,
            'errors' => isset($errors) ? $errors : null
        ]);
    }

    #[Route('/admin/reclamation/{id}/edit', name: 'admin_reclamation_edit', methods: ['GET', 'POST'])]
    public function adminEdit(Request $request, Reclamation $reclamation): Response
    {
        $form = $this->createForm(ReclamationType::class, $reclamation);

        $form->handleRequest($request);

        if ($form->isSubmitted()) {
            $errors = $this->validator->validate($reclamation);
            
            if ($form->isValid() && count($errors) === 0) {
                try {
                    $this->entityManager->flush();
                    $this->addFlash('success', 'Réclamation mise à jour avec succès !');
                    return $this->redirectToRoute('admin_reclamation_index');
                } catch (\Exception $e) {
                    $this->logger->error('Erreur modification: ' . $e->getMessage());
                    $this->addFlash('error', 'Erreur lors de la modification');
                }
            } else {
                foreach ($errors as $error) {
                    $this->addFlash('error', $error->getMessage());
                }
            }
        }

        return $this->render('back/reclamationback.html.twig', [
            'form' => $form->createView(),
            'reclamation' => $reclamation,
            'reclamations' => $this->reclamationRepository->findAllOrderedByDate(),
            'form_title' => 'Modifier réclamation',
            'button_label' => 'Mettre à jour',
            'total_reclamations' => count($this->reclamationRepository->findAll()),
            'show_list' => false,
            'errors' => isset($errors) ? $errors : null
        ]);
    }

    #[Route('/admin/reclamation/{id}/delete', name: 'admin_reclamation_delete', methods: ['POST'])]
    public function adminDelete(Request $request, Reclamation $reclamation): Response
    {
        if ($this->isCsrfTokenValid('delete'.$reclamation->getId(), $request->request->get('_token'))) {
            try {
                $this->entityManager->remove($reclamation);
                $this->entityManager->flush();
                $this->addFlash('success', 'Réclamation supprimée avec succès !');
            } catch (\Exception $e) {
                $this->logger->error('Erreur suppression: ' . $e->getMessage());
                $this->addFlash('error', 'Erreur lors de la suppression');
            }
        }

        return $this->redirectToRoute('admin_reclamation_index');
    }

    /**************************
     * METHODES PRIVEES
     **************************/

    private function initializeReclamation(Reclamation $reclamation, Request $request): void
    {
        $reclamation->setDate(new \DateTime());
        $reclamation->setEtat('Nouvelle');

        $reservationId = $request->query->getInt('reservation_id');
        if ($reservationId > 0) {
            /** @var Reservation $reservation */
            $reservation = $this->reservationRepository->find($reservationId);
            if ($reservation) {
                $reclamation->setReservation($reservation);
                if ($request->query->getBoolean('is_cancellation')) {
                    $reclamation->setRaison('Annulation de réservation #'.$reservation->getId());
                    $this->logger->info('Init réclamation annulation', ['id' => $reservation->getId()]);
                }
            }
        }
    }

    private function processReclamation(Reclamation $reclamation, bool $isCancellation): void
    {
        try {
            // Validation finale avant persistance
            $errors = $this->validator->validate($reclamation);
            if (count($errors) > 0) {
                throw new \RuntimeException('Validation failed before persistence');
            }

            $this->entityManager->persist($reclamation);
            $this->entityManager->flush();

            $this->logger->info(($isCancellation ? 'Annulation' : 'Réclamation').' enregistrée', [
                'id' => $reclamation->getId(),
                'user' => $this->getUser()?->getUserIdentifier()
            ]);
            
        } catch (\Exception $e) {
            $this->logger->error('Erreur enregistrement', [
                'error' => $e->getMessage(),
                'trace' => $e->getTraceAsString()
            ]);
            $this->addFlash('error', "Une erreur est survenue lors de l'enregistrement");
        }
    }
}