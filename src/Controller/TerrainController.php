<?php

namespace App\Controller;

use App\Entity\Terrain;
use App\Form\TerrainType;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/terrain')]
class TerrainController extends AbstractController
{
    // Afficher la liste des terrains
    #[Route('/', name: 'app_terrain_index', methods: ['GET'])]
    public function index(EntityManagerInterface $entityManager): Response
    {
        // Récupérer tous les terrains
        $terrains = $entityManager
            ->getRepository(Terrain::class)
            ->findAll();

        // Rendre la vue avec la liste des terrains
        return $this->render('terrain/index.html.twig', [
            'terrains' => $terrains,
        ]);
    }

    // Créer un nouveau terrain
    #[Route('/new', name: 'app_terrain_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        // Créer une nouvelle instance de Terrain
        $terrain = new Terrain();

        // Créer le formulaire basé sur le TerrainType
        $form = $this->createForm(TerrainType::class, $terrain);

        // Gérer la soumission du formulaire
        $form->handleRequest($request);

        // Si le formulaire est soumis et valide, enregistrer le terrain
        if ($form->isSubmitted() && $form->isValid()) {
            // Persister l'entité Terrain dans la base de données
            $entityManager->persist($terrain);
            $entityManager->flush();

            // Rediriger vers la page de liste des terrains
            return $this->redirectToRoute('app_terrain_index', [], Response::HTTP_SEE_OTHER);
        }

        // Rendre la vue du formulaire pour la création
        return $this->renderForm('terrain/new.html.twig', [
            'terrain' => $terrain,
            'form' => $form,
        ]);
    }

    // Afficher un terrain spécifique
    #[Route('/{id}', name: 'app_terrain_show', methods: ['GET'])]
    public function show(Terrain $terrain): Response
    {
        // Rendre la vue pour afficher un terrain
        return $this->render('terrain/show.html.twig', [
            'terrain' => $terrain,
        ]);
    }

    // Modifier un terrain existant
    #[Route('/{id}/edit', name: 'app_terrain_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Terrain $terrain, EntityManagerInterface $entityManager): Response
    {
        // Créer un formulaire pour modifier le terrain
        $form = $this->createForm(TerrainType::class, $terrain);
        $form->handleRequest($request);

        // Si le formulaire est soumis et valide, enregistrer les modifications
        if ($form->isSubmitted() && $form->isValid()) {
            // Enregistrer les changements dans la base de données
            $entityManager->flush();

            // Rediriger vers la liste des terrains après la modification
            return $this->redirectToRoute('app_terrain_index', [], Response::HTTP_SEE_OTHER);
        }

        // Rendre la vue avec le formulaire de modification
        return $this->renderForm('terrain/edit.html.twig', [
            'terrain' => $terrain,
            'form' => $form,
        ]);
    }

    // Supprimer un terrain
    #[Route('/{id}', name: 'app_terrain_delete', methods: ['POST'])]
    public function delete(Request $request, Terrain $terrain, EntityManagerInterface $entityManager): Response
    {
        // Vérifier le jeton CSRF pour la suppression
        if ($this->isCsrfTokenValid('delete' . $terrain->getId(), $request->request->get('_token'))) {
            // Supprimer le terrain de la base de données
            $entityManager->remove($terrain);
            $entityManager->flush();
        }

        // Rediriger vers la liste des terrains après la suppression
        return $this->redirectToRoute('app_terrain_index', [], Response::HTTP_SEE_OTHER);
    }
}
