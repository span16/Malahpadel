<?php

namespace App\Controller\Back;

use App\Entity\Recherche;
use App\Form\RechercheType;
use App\Repository\RechercheRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/back/recherche')]
class BackRechercheController extends AbstractController
{
    #[Route('/', name: 'back_recherche_index', methods: ['GET'])]
    public function index(RechercheRepository $rechercheRepository): Response
    {
        return $this->render('back/recherche/index.html.twig', [
            'recherches' => $rechercheRepository->findAll(),
        ]);
    }

    #[Route('/new', name: 'back_recherche_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $recherche = new Recherche();
        $form = $this->createForm(RechercheType::class, $recherche);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($recherche);
            $entityManager->flush();

            return $this->redirectToRoute('back_recherche_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('back/recherche/new.html.twig', [
            'recherche' => $recherche,
            'form' => $form,
        ]);
    }

    #[Route('/{userId}', name: 'back_recherche_show', methods: ['GET'])]
    public function show(Recherche $recherche): Response
    {
        return $this->render('back/recherche/show.html.twig', [
            'recherche' => $recherche,
        ]);
    }

    #[Route('/{userId}/edit', name: 'back_recherche_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Recherche $recherche, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(RechercheType::class, $recherche);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();

            return $this->redirectToRoute('back_recherche_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('back/recherche/edit.html.twig', [
            'recherche' => $recherche,
            'form' => $form,
        ]);
    }

    #[Route('/{userId}', name: 'back_recherche_delete', methods: ['POST'])]
    public function delete(Request $request, Recherche $recherche, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$recherche->getUserId(), $request->request->get('_token'))) {
            $entityManager->remove($recherche);
            $entityManager->flush();
        }

        return $this->redirectToRoute('back_recherche_index', [], Response::HTTP_SEE_OTHER);
    }
} 