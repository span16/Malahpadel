<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Session\SessionInterface;
use Symfony\Component\Routing\Annotation\Route;
use App\Repository\ProduitRepository;
use App\Entity\Produit;

class PanierController extends AbstractController
{
    #[Route('/panier', name: 'app_panier_index')]
    public function index(SessionInterface $session, ProduitRepository $produitRepository): Response
    {
        $panier = $session->get('panier', []);
        
        $panierData = [];
        $total = 0;

        foreach ($panier as $id => $quantite) {
            $produit = $produitRepository->find($id);
            if ($produit) {
                $panierData[] = [
                    'produit' => $produit,
                    'quantite' => $quantite,
                    'sousTotal' => $produit->getPrix() * $quantite
                ];
                $total += $produit->getPrix() * $quantite;
            }
        }

        return $this->render('panier/index.html.twig', [
            'items' => $panierData,
            'total' => $total
        ]);
    }

    #[Route('/panier/add/{id}', name: 'app_panier_add')]
    public function add($id, Request $request, SessionInterface $session): Response
    {
        $panier = $session->get('panier', []);

        if (!empty($panier[$id])) {
            $panier[$id]++;
        } else {
            $panier[$id] = 1;
        }

        $session->set('panier', $panier);
        $this->addFlash('success', 'Produit ajouté au panier');

        return $this->redirectToRoute('app_panier_index');
    }

    #[Route('/panier/remove/{id}', name: 'app_panier_remove')]
    public function remove($id, SessionInterface $session): Response
    {
        $panier = $session->get('panier', []);

        if (!empty($panier[$id])) {
            if ($panier[$id] > 1) {
                $panier[$id]--;
            } else {
                unset($panier[$id]);
            }
        }

        $session->set('panier', $panier);
        $this->addFlash('success', 'Produit retiré du panier');

        return $this->redirectToRoute('app_panier_index');
    }

    #[Route('/checkout', name: 'app_checkout')]
    public function checkout(SessionInterface $session, ProduitRepository $produitRepository): Response
    {
        $panier = $session->get('panier', []);
        
        if (empty($panier)) {
            $this->addFlash('warning', 'Votre panier est vide');
            return $this->redirectToRoute('app_panier_index');
        }
        
        return $this->render('panier/checkout.html.twig');
    }

    #[Route('/panier/clear', name: 'app_panier_clear')]
    public function clear(SessionInterface $session): Response
    {
        $session->remove('panier');
        $this->addFlash('success', 'Le panier a été vidé');
        return $this->redirectToRoute('app_panier_index');
    }

    #[Route('/panier/remove-all/{id}', name: 'app_panier_remove_all')]
    public function removeAll($id, SessionInterface $session): Response
    {
        $panier = $session->get('panier', []);
        
        if (!empty($panier[$id])) {
            unset($panier[$id]);
        }

        $session->set('panier', $panier);
        $this->addFlash('success', 'Produit retiré du panier');
        
        return $this->redirectToRoute('app_panier_index');
    }
}