<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Http\Authentication\AuthenticationUtils;
use App\Service\ParameterService; 

use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mime\Email;
use Symfony\Component\Routing\Generator\UrlGeneratorInterface;
use App\Repository\UserRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

use App\Service\RecaptchaService; 
use Psr\Log\LoggerInterface;

class SecurityController extends AbstractController
{
    private $recaptchaService;

    public function __construct(RecaptchaService $recaptchaService)
    {
        $this->recaptchaService = $recaptchaService;
    }

    #[Route(path: '/login', name: 'app_login')]
    public function login(AuthenticationUtils $authenticationUtils, Request $request): Response
    {
        // Si l'utilisateur est déjà authentifié, rediriger vers la page d'administration
        if ($this->getUser()) {
            return $this->redirectToRoute('app_admin');
        }

        // Vérification reCAPTCHA (si la méthode est POST)
        if ($request->isMethod('POST')) {
            $recaptchaResponse = $request->request->get('g-recaptcha-response');
            $clientIp = $request->getClientIp();

            // Appel au service de validation reCAPTCHA
            $isValid = $this->recaptchaService->verify($recaptchaResponse, $clientIp);

            if (!$isValid) {
                $this->addFlash('error', 'La validation reCAPTCHA a échoué.');
                return $this->redirectToRoute('app_login'); // Rediriger si échec
            }
        }

        // Récupération des erreurs d'authentification et du dernier nom d'utilisateur
        $error = $authenticationUtils->getLastAuthenticationError();
        $lastUsername = $authenticationUtils->getLastUsername();

        // Rendu de la vue avec les paramètres nécessaires
        return $this->render('security/login.html.twig', [
            'last_username' => $lastUsername, 
            'error' => $error,
            'recaptcha_site_key' => $this->getParameter('recaptcha.site_key'), // Clé publique reCAPTCHA
        ]);
    }

    #[Route(path: '/logout', name: 'app_logout')]
    public function logout(): void
    {
        throw new \LogicException('This method can be blank - it will be intercepted by the logout key on your firewall.');
    }


    #[Route('/forgot-password', name: 'app_forgot_password')]
    public function forgotPassword(
        Request $request, 
        MailerInterface $mailer, 
        UserRepository $userRepository, 
        EntityManagerInterface $entityManager
    ): Response {
        if ($request->isMethod('POST')) {
            $email = $request->request->get('email');
            $user = $userRepository->findOneBy(['email' => $email]);

            if ($user) {
                $token = bin2hex(random_bytes(32));
                $user->setReset_token($token);
                $user->setToken_expiration(new \DateTime('+1 hour'));
                $entityManager->flush();

                $emailMessage = (new Email())
                    ->from('noreply@votresite.com')
                    ->to($email)
                    ->subject('Réinitialisation de votre mot de passe')
                    ->html("<p>Pour réinitialiser votre mot de passe, cliquez sur ce lien : <a href='" . $this->generateUrl('app_reset_password', ['token' => $token], UrlGeneratorInterface::ABSOLUTE_URL) . "'>Réinitialiser mon mot de passe</a></p>");

                $mailer->send($emailMessage);

                $this->addFlash('success', 'Un email de réinitialisation a été envoyé à votre adresse email.');
                return $this->redirectToRoute('app_login');
            } else {
                $this->addFlash('error', 'Aucun compte trouvé avec cet email.');
            }
        }

        return $this->render('security/forgot_password.html.twig');
    }

    #[Route('/reset-password/{token}', name: 'app_reset_password')]
    public function resetPassword(
        Request $request, 
        string $token, 
        UserRepository $userRepository, 
        EntityManagerInterface $entityManager, 
        UserPasswordHasherInterface $userPasswordHasher
    ): Response {
        $user = $userRepository->findOneBy(['reset_token' => $token]);

        if (!$user) {
            $this->addFlash('error', 'Token de réinitialisation invalide.');
            return $this->redirectToRoute('app_login');
        }

        $now = new \DateTime();
        if ($user->getToken_expiration() < $now) {
            $this->addFlash('error', 'Le token de réinitialisation a expiré.');
            return $this->redirectToRoute('app_login');
        }

        if ($request->isMethod('POST')) {
            $newPassword = $request->request->get('password');

            if (empty($newPassword)) {
                $this->addFlash('error', 'Le mot de passe ne peut pas être vide.');
                return $this->redirectToRoute('app_reset_password', ['token' => $token]);
            }

            $hashedPassword = $userPasswordHasher->hashPassword($user, $newPassword);
            $user->setMdp($hashedPassword);

            $user->setReset_token(null);
            $user->setToken_expiration(null);

            $entityManager->flush();

            $this->addFlash('success', 'Votre mot de passe a été réinitialisé avec succès. Vous pouvez maintenant vous connecter.');
            return $this->redirectToRoute('app_login');
        }

        return $this->render('security/reset_password.html.twig', [
            'token' => $token,
        ]);
    }



}