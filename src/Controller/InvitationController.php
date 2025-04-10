<?php

namespace App\Controller;

use App\Entity\Invitation;
use App\Entity\InvitationStatus;
use App\Repository\InvitationRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;

#[Route('/invitation')]
class InvitationController extends AbstractController
{
    // Display all invitations
    #[Route('/', name: 'app_invitation_index', methods: ['GET'])]
    public function index(InvitationRepository $invitationRepository): Response
    {
        return $this->render('invitation/index.html.twig', [
            'invitations' => $invitationRepository->findAll(),
        ]);
    }

    // Create a new invitation
    #[Route('/new', name: 'app_invitation_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $invitation = new Invitation();
        $form = $this->createFormBuilder($invitation)
            ->add('senderId')
            ->add('receiverId')
            ->add('dateEnvoi')
            ->add('statut', ChoiceType::class, [
                'choices' => [
                    'En attente' => InvitationStatus::EN_ATTENTE,
                    'Acceptée' => InvitationStatus::ACCEPTEE,
                    'Refusée' => InvitationStatus::REFUSEE,
                ]
            ])
            ->getForm();

        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($invitation);
            $entityManager->flush();

            return $this->redirectToRoute('app_invitation_index');
        }

        return $this->render('invitation/new.html.twig', [
            'invitation' => $invitation,
            'form' => $form->createView(),
        ]);
    }

    // Show an individual invitation
    #[Route('/{id}', name: 'app_invitation_show', methods: ['GET'])]
    public function show(Invitation $invitation): Response
    {
        return $this->render('invitation/show.html.twig', [
            'invitation' => $invitation,
        ]);
    }

    // Edit an invitation
    #[Route('/{id}/edit', name: 'app_invitation_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Invitation $invitation, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createFormBuilder($invitation)
            ->add('senderId')
            ->add('receiverId')
            ->add('dateEnvoi')
            ->add('statut', ChoiceType::class, [
                'choices' => [
                    'En attente' => InvitationStatus::EN_ATTENTE,
                    'Acceptée' => InvitationStatus::ACCEPTEE,
                    'Refusée' => InvitationStatus::REFUSEE,
                ]
            ])
            ->getForm();

        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();

            return $this->redirectToRoute('app_invitation_index');
        }

        return $this->render('invitation/edit.html.twig', [
            'invitation' => $invitation,
            'form' => $form->createView(),
        ]);
    }

    // Delete an invitation
    #[Route('/{id}', name: 'app_invitation_delete', methods: ['POST'])]
    public function delete(Request $request, Invitation $invitation, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$invitation->getId(), $request->request->get('_token'))) {
            $entityManager->remove($invitation);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_invitation_index');
    }
}
