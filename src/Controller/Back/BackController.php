<?php

namespace App\Controller\Back;

use App\Repository\AnnonceMatchRepository;
use App\Repository\RechercheRepository;
use App\Repository\InvitationRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/back')]
class BackController extends AbstractController
{
    #[Route('/', name: 'back_home')]
    public function index(
        AnnonceMatchRepository $annonceRepository,
        RechercheRepository $rechercheRepository,
        InvitationRepository $invitationRepository
    ): Response {
        return $this->render('back/index.html.twig', [
            'annonces' => $annonceRepository->findAll(),
            'recherches' => $rechercheRepository->findAll(),
            'invitations' => $invitationRepository->findAll(),
        ]);
    }
} 