<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class BackController extends AbstractController
{
    #[Route('/back', name: 'app_back')]
    public function index(): Response
    {
        return $this->render('back.html.twig', []);
    }

    #[Route('/', name: 'app_home')]
    public function home(): Response
    {
        return $this->redirectToRoute('app_back');
    }

    // Add all missing routes below
    #[Route('/buttons', name: 'app_buttons')]
    public function buttons(): Response
    {
        return $this->render('back/buttons.html.twig');
    }

    #[Route('/dropdowns', name: 'app_dropdowns')]
    public function dropdowns(): Response
    {
        return $this->render('back/dropdowns.html.twig');
    }

    #[Route('/typography', name: 'app_typography')]
    public function typography(): Response
    {
        return $this->render('back/typography.html.twig');
    }

    #[Route('/form-elements', name: 'app_form_elements')]
    public function formElements(): Response
    {
        return $this->render('back/form_elements.html.twig');
    }

  

    #[Route('/compagneback', name: 'app_compagneback')]
    public function compagneback(): Response
    {
        return $this->render('back/compagneback.html.twig');
    }

    #[Route('/icons', name: 'app_icons')]
    public function icons(): Response
    {
        return $this->render('back/icons.html.twig');
    }

    #[Route('/blank-page', name: 'app_blank_page')]
    public function blankPage(): Response
    {
        return $this->render('back/blank_page.html.twig');
    }

    #[Route('/error-404', name: 'app_error_404')]
    public function error404(): Response
    {
        return $this->render('back/error_404.html.twig');
    }

    #[Route('/error-500', name: 'app_error_500')]
    public function error500(): Response
    {
        return $this->render('back/error_500.html.twig');
    }

    #[Route('/login', name: 'app_login')]
    public function login(): Response
    {
        return $this->render('back/login.html.twig');
    }

    #[Route('/register', name: 'app_register')]
    public function register(): Response
    {
        return $this->render('back/register.html.twig');
    }
}