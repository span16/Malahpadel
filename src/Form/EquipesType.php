<?php

namespace App\Form;

use App\Entity\Equipes;
use App\Entity\User;
use App\Repository\UserRepository;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class EquipeType extends AbstractType
{
    private UserRepository $userRepository;

    public function __construct(UserRepository $userRepository)
    {
        $this->userRepository = $userRepository;
    }

    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        // Utilise directement la méthode propre
        $joueursDispo = $this->userRepository->findUsersNotInEquipes();

        $builder
            ->add('nom_equipe', TextType::class, [
                'label' => 'Nom de l\'équipe',
            ])
            ->add('joueur1', EntityType::class, [
                'class' => User::class,
                'choices' => $joueursDispo,
                'choice_label' => 'email',
                'label' => 'Joueur 1',
                'placeholder' => 'Choisir un joueur',
            ])
            ->add('joueur2', EntityType::class, [
                'class' => User::class,
                'choices' => $joueursDispo,
                'choice_label' => 'email',
                'label' => 'Joueur 2',
                'placeholder' => 'Choisir un joueur',
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Equipes::class,
        ]);
    }
}
