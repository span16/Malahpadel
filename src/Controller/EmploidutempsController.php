<?php

namespace App\Controller;

use App\Entity\Emploidutemps;
use App\Form\EmploidutempsType;
use App\Repository\EquipesRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class EmploidutempsController extends AbstractController
{
    #[Route('/emploidutemps/new', name: 'emploi_du_temps_new')]
    public function new(Request $request, EntityManagerInterface $em, EquipesRepository $equipesRepo): Response
    {
        $emploi = new Emploidutemps();
        $form = $this->createForm(EmploidutempsType::class, $emploi);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $equipes = $equipesRepo->findAll();

            if (count($equipes) < 4) {
                $this->addFlash('error', 'Il faut au moins 4 équipes pour générer un match.');
            } else {
                shuffle($equipes);
                $equipe1 = $equipes[0];
                $equipe2 = $equipes[1];

                if ($equipe1 === $equipe2) {
                    $this->addFlash('error', 'Équipes identiques, réessayez.');
                } elseif ($emploi->getDate() < new \DateTime('today')) {
                    $this->addFlash('error', 'La date ne peut pas être antérieure à aujourd\'hui.');
                } else {
                    $emploi->setEquipe1($equipe1);
                    $emploi->setEquipe2($equipe2);

                    // ✅ Génère automatiquement un ID pour Google Calendar si besoin
                    $emploi->setGoogleEventId('event-' . uniqid());

                    $em->persist($emploi);
                    $em->flush();

                    $this->addFlash('success', 'Match généré automatiquement avec 2 équipes.');
                    return $this->redirectToRoute('emploi_du_temps_new');
                }
            }
        }

        return $this->render('emploidutemps/new.html.twig', [
            'form' => $form->createView(),
        ]);
    }

    #[Route('/emploidutemps/calendrier', name: 'emploi_du_temps_calendrier')]
    public function calendrier(): Response
    {
        return $this->render('emploidutemps/calendrier.html.twig');
    }

    #[Route('/emploidutemps/show', name: 'emploi_du_temps_show')]
    public function show(): Response
    {
        return $this->render('emploidutemps/show.html.twig');
    }

    #[Route('/emploidutemps/api', name: 'emploi_du_temps_api')]
    public function api(EntityManagerInterface $em): JsonResponse
    {
        $emplois = $em->getRepository(Emploidutemps::class)->findAll();
        $events = [];

        foreach ($emplois as $emploi) {
            $events[] = [
                'title' => $emploi->getEvenement()->getNom() . ' - ' .
                           $emploi->getEquipe1()->getNomEquipe() . ' vs ' .
                           $emploi->getEquipe2()->getNomEquipe(),
                'start' => $emploi->getDate()->format('Y-m-d'),
            ];
        }

        return new JsonResponse($events);
    }
}
