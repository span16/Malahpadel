<?php
namespace App\Controller;

use App\Entity\AnnonceMatch;
use App\Repository\AnnonceMatchRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/annonce')]
class AnnonceMatchController extends AbstractController
{
    // Index Route (Display all annonces)
    #[Route('/', name: 'app_annonce_match_index', methods: ['GET'])]
    public function index(AnnonceMatchRepository $annonceMatchRepository): Response
    {
        return $this->render('annonce_match/index.html.twig', [
            'annonce_matches' => $annonceMatchRepository->findAll(),
        ]);
    }

    // New Annonce Route
    #[Route('/new', name: 'app_annonce_match_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $annonceMatch = new AnnonceMatch();
        $form = $this->createFormBuilder($annonceMatch)
            ->add('titre')
            ->add('dateHeure')
            ->add('lieu')
            ->add('joueursRecherches')
            ->add('niveau')
            ->add('description')
            ->getForm();

        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($annonceMatch);
            $entityManager->flush();

            return $this->redirectToRoute('app_annonce_match_index');
        }

        return $this->render('annonce_match/new.html.twig', [
            'annonce_match' => $annonceMatch,
            'form' => $form->createView(),
        ]);
    }

    // Show Annonce Route
    #[Route('/{annonceId}', name: 'app_annonce_match_show', methods: ['GET'])]
    public function show(AnnonceMatch $annonceMatch): Response
    {
        return $this->render('annonce_match/show.html.twig', [
            'annonce_match' => $annonceMatch,
        ]);
    }

    // Edit Annonce Route
    #[Route('/{annonceId}/edit', name: 'app_annonce_match_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, AnnonceMatch $annonceMatch, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createFormBuilder($annonceMatch)
            ->add('titre')
            ->add('dateHeure')
            ->add('lieu')
            ->add('joueursRecherches')
            ->add('niveau')
            ->add('description')
            ->getForm();

        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();

            // Redirect to the show page after editing
            return $this->redirectToRoute('app_annonce_match_show', [
                'annonceId' => $annonceMatch->getAnnonceId()
            ]);
        }

        return $this->render('annonce_match/edit.html.twig', [
            'annonce_match' => $annonceMatch,
            'form' => $form->createView(),
        ]);
    }

    // Delete Annonce Route
    #[Route('/{annonceId}', name: 'app_annonce_match_delete', methods: ['POST'])]
    public function delete(Request $request, AnnonceMatch $annonceMatch, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete' . $annonceMatch->getAnnonceId(), $request->request->get('_token'))) {
            $entityManager->remove($annonceMatch);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_annonce_match_index');
    }
}
