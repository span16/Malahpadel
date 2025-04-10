<?php

namespace App\Controller;

use App\Entity\Evenement;
use App\Entity\LikeDislike;
use App\Entity\TypeV;
use App\Form\EvenementType;
use App\Form\LikeDislikeType;
use App\Repository\LikeDislikeRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class EvenementController extends AbstractController
{
    private EntityManagerInterface $entityManager;
    private LikeDislikeRepository $likeDislikeRepository;

    public function __construct(EntityManagerInterface $entityManager, LikeDislikeRepository $likeDislikeRepository)
    {
        $this->entityManager = $entityManager;
        $this->likeDislikeRepository = $likeDislikeRepository;
    }

    #[Route('/evenement', name: 'evenement_list')]
    public function list(): Response
    {
        $evenements = $this->entityManager->getRepository(Evenement::class)->findAll();

        return $this->render('evenement/show.html.twig', [
            'evenements' => $evenements,
        ]);
    }

    #[Route('/evenement/back', name: 'evenement_list_back')]
    public function listBack(Request $request): Response
    {
        $search = $request->query->get('search');
        $type = $request->query->get('type');
        $date = $request->query->get('date');

        $queryBuilder = $this->entityManager->getRepository(Evenement::class)->createQueryBuilder('e');

        if ($search) {
            $queryBuilder->andWhere('e.nom LIKE :search OR e.type LIKE :search')
                         ->setParameter('search', '%' . $search . '%');
        }

        if ($type && in_array($type, ['TOURNOIS', 'MATCH'])) {
            $queryBuilder->andWhere('e.type = :type')
                         ->setParameter('type', $type);
        }

        if ($date) {
            $queryBuilder->andWhere('e.date = :date')
                         ->setParameter('date', new \DateTime($date));
        }

        $evenements = $queryBuilder->getQuery()->getResult();

        return $this->render('evenement/list_evenement_back.html.twig', [
            'evenements' => $evenements,
        ]);
    }

    #[Route('/evenement/create', name: 'evenement_create')]
    public function create(Request $request): Response
    {
        $evenement = new Evenement();
        $form = $this->createForm(EvenementType::class, $evenement);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $existingEvent = $this->entityManager->getRepository(Evenement::class)->findOneBy(['nom' => $evenement->getNom()]);
            $now = new \DateTime('today');

            if ($existingEvent) {
                $form->get('nom')->addError(new \Symfony\Component\Form\FormError('Un événement avec ce nom existe déjà.'));
            } elseif ($evenement->getDate() < $now) {
                $form->get('date')->addError(new \Symfony\Component\Form\FormError('La date ne peut pas être antérieure à aujourd’hui.'));
            } else {
                $this->entityManager->persist($evenement);
                $this->entityManager->flush();
                $this->addFlash('success', "L'événement a été créé avec succès.");
                return $this->redirectToRoute('evenement_list_back');
            }
        }

        return $this->render('evenement/create.html.twig', [
            'form' => $form->createView(),
        ]);
    }

    #[Route('/evenement/update/{id}', name: 'evenement_update', methods: ['POST'])]
    public function update(Request $request, Evenement $evenement): JsonResponse
    {
        $nom = $request->request->get('nom');
        $type = $request->request->get('type');
        $date = $request->request->get('date');

        if ($nom && $nom !== $evenement->getNom()) {
            $existingEvent = $this->entityManager->getRepository(Evenement::class)->findOneBy(['nom' => $nom]);
            if ($existingEvent) {
                return new JsonResponse(['success' => false, 'message' => 'Ce nom est déjà utilisé.']);
            }
            $evenement->setNom($nom);
        }

        if ($type && in_array($type, ['TOURNOIS', 'MATCH'])) {
            $evenement->setType(TypeV::from($type));
        }

        if ($date) {
            $newDate = new \DateTime($date);
            $now = new \DateTime('today');
            if ($newDate < $now) {
                return new JsonResponse(['success' => false, 'message' => 'La date ne peut pas être dans le passé.']);
            }
            $evenement->setDate($newDate);
        }

        $this->entityManager->flush();

        return new JsonResponse(['success' => true]);
    }

    #[Route('/evenement/show/{id}', name: 'evenement_show')]
    public function show(Request $request, int $id): Response
    {
        $evenement = $this->entityManager->getRepository(Evenement::class)->find($id);

        if (!$evenement) {
            throw $this->createNotFoundException("L'événement n'existe pas.");
        }

        $terrain = $evenement->getTerrain();

        $likeDislike = new LikeDislike();
        $likeDislike->setEvenement($evenement);

        $form = $this->createForm(LikeDislikeType::class, $likeDislike);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $this->entityManager->persist($likeDislike);
            $this->entityManager->flush();
            $this->addFlash('success', 'Merci pour votre note et votre avis !');
            return $this->redirectToRoute('evenement_show', ['id' => $id]);
        }

        $stats = $this->likeDislikeRepository->getStatsByEvenement($evenement->getId());

        return $this->render('evenement/details.html.twig', [
            'evenement' => $evenement,
            'terrain' => $terrain,
            'form' => $form->createView(),
            'stats' => $stats,
        ]);
    }

    #[Route('/evenement/dashboard', name: 'dashboard_powerbi')]
    public function dashboardPowerBI(): Response
    {
        return $this->render('evenement/dashboard.html.twig');
    }
}
