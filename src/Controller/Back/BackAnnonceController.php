<?php

namespace App\Controller\Back;

use App\Entity\AnnonceMatch;
use App\Form\AnnonceMatchType;
use App\Repository\AnnonceMatchRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/back/annonce')]
class BackAnnonceController extends AbstractController
{
    #[Route('/', name: 'back_annonce_index', methods: ['GET'])]
    public function index(AnnonceMatchRepository $annonceMatchRepository): Response
    {
        return $this->render('back/annonce/index.html.twig', [
            'annonce_matches' => $annonceMatchRepository->findAll(),
        ]);
    }

    #[Route('/new', name: 'back_annonce_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $annonceMatch = new AnnonceMatch();
        $form = $this->createForm(AnnonceMatchType::class, $annonceMatch);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($annonceMatch);
            $entityManager->flush();

            return $this->redirectToRoute('back_annonce_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('back/annonce/new.html.twig', [
            'annonce_match' => $annonceMatch,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'back_annonce_show', methods: ['GET'])]
    public function show(AnnonceMatch $annonceMatch): Response
    {
        return $this->render('back/annonce/show.html.twig', [
            'annonce_match' => $annonceMatch,
        ]);
    }

    #[Route('/{id}/edit', name: 'back_annonce_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, AnnonceMatch $annonceMatch, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(AnnonceMatchType::class, $annonceMatch);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();

            return $this->redirectToRoute('back_annonce_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('back/annonce/edit.html.twig', [
            'annonce_match' => $annonceMatch,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'back_annonce_delete', methods: ['POST'])]
    public function delete(Request $request, AnnonceMatch $annonceMatch, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$annonceMatch->getId(), $request->request->get('_token'))) {
            $entityManager->remove($annonceMatch);
            $entityManager->flush();
        }

        return $this->redirectToRoute('back_annonce_index', [], Response::HTTP_SEE_OTHER);
    }
} 