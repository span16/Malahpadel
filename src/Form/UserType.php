<?php

namespace App\Form;

use App\Entity\User;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\Form\Extension\Core\Type\PasswordType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\OptionsResolver\OptionsResolver;

class UserType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('nom', TextType::class, [
                'label' => 'Nom',
            ])
            ->add('prenom', TextType::class, [
                'label' => 'Prénom',
            ])
            ->add('age', null, [
                'label' => 'Âge',
            ])
            ->add('email', null, [
                'label' => 'Email',
            ])
            ->add('mdp', PasswordType::class, [
                'label' => 'Mot de passe',
                'required' => false, // facultatif lors de la modification
            ])
            ->add('cin', null, [
                'label' => 'CIN',
            ])
            ->add('etat', TextType::class, [
                'label' => 'État',
            ])
            ->add('fonction', ChoiceType::class, [
                'label' => 'Fonction',
                'choices' => [
                    'Administrateur' => 'admin',
                    'Utilisateur' => 'utilisateur',
                ],
                'expanded' => false, // false = menu déroulant
                'multiple' => false, // une seule option possible
            
           
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => User::class,
        ]);
    }
}
