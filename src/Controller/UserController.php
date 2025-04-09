<?php

namespace App\Controller;

use App\Entity\User;
use App\Form\UserType;
use App\Repository\UserRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;
use Symfony\Component\Routing\Attribute\Route;

#[Route('/user')]
final class UserController extends AbstractController
{
    #[Route('/dashboard', name: 'app_user_dashboard', methods: ['GET'])]
    public function dashboard(Request $request, UserRepository $userRepository): Response
    {
        // Récupération des critères de recherche depuis la query string
        $nom = $request->query->get('nom');
        $email = $request->query->get('email');
        $fonction = $request->query->get('fonction');
        $etat = $request->query->get('etat');
        
        // Préparation d'un QueryBuilder pour construire la requête selon les critères renseignés
        $qb = $userRepository->createQueryBuilder('u');
        
        if ($nom) {
            $qb->andWhere('u.nom LIKE :nom')
               ->setParameter('nom', '%' . $nom . '%');
        }
        
        if ($email) {
            $qb->andWhere('u.email LIKE :email')
               ->setParameter('email', '%' . $email . '%');
        }
        
        if ($fonction) {
            // Ici on suppose que la valeur dans "fonction" correspond à celle dans la base (ex. "admin" ou "utilisateur")
            $qb->andWhere('u.fonction = :fonction')
               ->setParameter('fonction', $fonction);
        }
        
        if ($etat) {
            $qb->andWhere('u.etat = :etat')
               ->setParameter('etat', $etat);
        }
        
        // Exécute la requête et récupère le résultat
        $users = $qb->getQuery()->getResult();
        
        // Calcul de quelques statistiques
        $totalUsers = count($users);
        $adminCount = count(array_filter($users, fn($u) => in_array('ROLE_ADMIN', $u->getRoles())));
        $userCount = count(array_filter($users, fn($u) => in_array('ROLE_USER', $u->getRoles())));
        
        return $this->render('user/dashboard.html.twig', [
            'users' => $users,
            // On transmet aussi les critères pour les réafficher dans le formulaire (optionnel)
            'nom' => $nom,
            'email' => $email,
            'fonction' => $fonction,
            'etat' => $etat,
            'totalUsers' => $totalUsers,
            'adminCount' => $adminCount,
            'userCount' => $userCount,
        ]);
    }

   
    #[Route(name: 'app_user_index', methods: ['GET'])]
public function index(): Response
{
    return $this->redirectToRoute('app_user_dashboard');
}
#[Route('/new', name: 'app_user_new', methods: ['GET', 'POST'])]
public function new(Request $request, EntityManagerInterface $entityManager, UserPasswordHasherInterface $hasher): Response
{
    $user = new User();
    // Optionnel : définir un groupe de validation si besoin pour l'inscription
    $form = $this->createForm(UserType::class, $user, [
        'validation_groups' => ['Registration'],
    ]);
    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {
        if ($user->getMdp()) {
            $hashedPassword = $hasher->hashPassword($user, $user->getMdp());
            $user->setMdp($hashedPassword);
        }

        $entityManager->persist($user);
        $entityManager->flush();

        return $this->redirectToRoute('app_user_dashboard');
    }

    return $this->render('user/new.html.twig', [
        'form' => $form->createView(),
    ]);
}


    #[Route('/{id}', name: 'app_user_show', methods: ['GET'])]
    public function show(User $user): Response
    {
        return $this->render('user/show.html.twig', [
            'user' => $user,
        ]);
    }

    #[Route('/{id}/edit', name: 'app_user_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, User $user, EntityManagerInterface $entityManager, UserPasswordHasherInterface $hasher): Response
    {
        $form = $this->createForm(UserType::class, $user);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // Si un nouveau mot de passe est fourni, on le hash
            if ($user->getMdp()) {
                $hashedPassword = $hasher->hashPassword($user, $user->getMdp());
                $user->setMdp($hashedPassword);
            }

            $entityManager->flush();

            return $this->redirectToRoute('app_user_dashboard');
        }

        return $this->render('user/edit.html.twig', [
            'user' => $user,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_user_delete', methods: ['POST'])]
    public function delete(Request $request, User $user, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete' . $user->getId(), $request->request->get('_token'))) {
            $entityManager->remove($user);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_user_dashboard');
    }
}
