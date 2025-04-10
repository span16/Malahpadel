<?php

namespace App\Controller;

use App\Entity\Equipes;
use App\Form\EquipeType;
use App\Repository\EquipesRepository;
use App\Repository\UserRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class EquipeController extends AbstractController
{
    #[Route('/equipe/new', name: 'equipe_ajouter')]
    public function ajouter(
        Request $request,
        EntityManagerInterface $em,
        EquipesRepository $equipesRepository
    ): Response {
        $equipe = new Equipes();
        $form = $this->createForm(EquipeType::class, $equipe);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $joueur1 = $equipe->getJoueur1();
            $joueur2 = $equipe->getJoueur2();

            // Les deux joueurs doivent être différents
            if ($joueur1 === $joueur2) {
                $this->addFlash('error', 'Les deux joueurs doivent être différents.');
            }
            // Vérifier si un joueur est déjà dans une équipe
            elseif ($equipesRepository->joueurDejaDansEquipe($joueur1) || $equipesRepository->joueurDejaDansEquipe($joueur2)) {
                $this->addFlash('error', 'Un des joueurs est déjà membre d\'une autre équipe.');
            }
            else {
                $em->persist($equipe);
                $em->flush();
                $this->addFlash('success', 'Équipe ajoutée avec succès !');
                return $this->redirectToRoute('equipe_ajouter');
            }
        }

        return $this->render('equipes/new.html.twig', [
            'form' => $form->createView(),
        ]);
    }

    #[Route('/equipe', name: 'equipe_index')]
    public function index(EquipesRepository $equipesRepository): Response
    {
        $equipes = $equipesRepository->findAll();

        return $this->render('equipes/index.html.twig', [
            'equipes' => $equipes,
        ]);
    }
}
