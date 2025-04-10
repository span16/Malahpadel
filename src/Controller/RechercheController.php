<?php

namespace App\Controller;

use App\Entity\Recherche;
use App\Form\RechercheType;
use App\Repository\RechercheRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/recherche')]
class RechercheController extends AbstractController
{
    #[Route('/', name: 'app_recherche_index', methods: ['GET'])]
    public function index(RechercheRepository $rechercheRepository): Response
    {
        return $this->render('recherche/index.html.twig', [
            'recherches' => $rechercheRepository->findAll(),
        ]);
    }

    #[Route('/new', name: 'app_recherche_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $recherche = new Recherche();
        $form = $this->createForm(RechercheType::class, $recherche);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($recherche);
            $entityManager->flush();

            return $this->redirectToRoute('app_recherche_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('recherche/new.html.twig', [
            'recherche' => $recherche,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_recherche_show', methods: ['GET'])]
    public function show(int $id, RechercheRepository $rechercheRepository): Response
    {
        $recherche = $rechercheRepository->find($id);
        
        if (!$recherche) {
            throw $this->createNotFoundException('Recherche not found');
        }

        return $this->render('recherche/show.html.twig', [
            'recherche' => $recherche,
        ]);
    }

    #[Route('/{id}/edit', name: 'app_recherche_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, int $id, RechercheRepository $rechercheRepository, EntityManagerInterface $entityManager): Response
    {
        $recherche = $rechercheRepository->find($id);
        
        if (!$recherche) {
            throw $this->createNotFoundException('Recherche not found');
        }

        $form = $this->createForm(RechercheType::class, $recherche);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();

            return $this->redirectToRoute('app_recherche_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('recherche/edit.html.twig', [
            'recherche' => $recherche,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_recherche_delete', methods: ['POST'])]
    public function delete(Request $request, int $id, RechercheRepository $rechercheRepository, EntityManagerInterface $entityManager): Response
    {
        $recherche = $rechercheRepository->find($id);
        
        if (!$recherche) {
            throw $this->createNotFoundException('Recherche not found');
        }

        if ($this->isCsrfTokenValid('delete'.$id, $request->request->get('_token'))) {
            $entityManager->remove($recherche);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_recherche_index', [], Response::HTTP_SEE_OTHER);
    }
} 