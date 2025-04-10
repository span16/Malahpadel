<?php

namespace App\Controller;

use App\Entity\AnnonceMatch;
use App\Entity\Invitation;
use App\Entity\InvitationStatus;
use App\Entity\Recherche;
use App\Repository\AnnonceMatchRepository;
use App\Repository\InvitationRepository;
use App\Repository\RechercheRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class TestController extends AbstractController
{
    #[Route('/test-all', name: 'test_all')]
    public function testAllConnections(
        AnnonceMatchRepository $annonceRepository,
        InvitationRepository $invitationRepository,
        RechercheRepository $rechercheRepository,
        EntityManagerInterface $entityManager
    ): Response {
        $results = [];
        
        try {
            // Test AnnonceMatch
            $annonces = $annonceRepository->findAll();
            $results[] = "AnnonceMatch: Found " . count($annonces) . " records";
            
            // Test Invitation
            $invitations = $invitationRepository->findAll();
            $results[] = "Invitation: Found " . count($invitations) . " records";
            
            // Test Recherche
            $recherches = $rechercheRepository->findAll();
            $results[] = "Recherche: Found " . count($recherches) . " records";
            
            // Try to create new records
            $newAnnonce = new AnnonceMatch();
            $newAnnonce->setTitre('Test Annonce');
            $newAnnonce->setDateHeure(new \DateTime());
            $newAnnonce->setLieu('Test Location');
            $newAnnonce->setJoueursRecherches(2);
            $newAnnonce->setNiveau('Débutant');
            $newAnnonce->setDescription('Test Description');
            $entityManager->persist($newAnnonce);
            
            $newInvitation = new Invitation();
            $newInvitation->setSenderId(1);
            $newInvitation->setReceiverId(2);
            $newInvitation->setDateEnvoi(new \DateTime());
            $newInvitation->setStatut(InvitationStatus::EN_ATTENTE);
            $entityManager->persist($newInvitation);
            
            $newRecherche = new Recherche();
            $newRecherche->setNom('Test User');
            $newRecherche->setNiveau('Intermédiaire');
            $newRecherche->setAnnonces('Looking for match');
            $entityManager->persist($newRecherche);
            
            $entityManager->flush();
            $results[] = "Successfully created new test records for all entities";
            
            return new Response(
                '<h1>Database Connection Test Results</h1>' .
                '<ul><li>' . implode('</li><li>', $results) . '</li></ul>'
            );
        } catch (\Exception $e) {
            return new Response(
                '<h1>Database Connection Test Failed</h1>' .
                '<p>Error: ' . $e->getMessage() . '</p>' .
                '<p>Stack Trace: ' . $e->getTraceAsString() . '</p>'
            );
        }
    }
} 