<?php
// src/Form/ProfilType.php

namespace App\Form;

use App\Entity\Profil;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\Extension\Core\Type\FileType;

class ProfilType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            // Pas besoin d'ajouter 'id_user' directement, car la relation est gérée par Doctrine
            ->add('avatar', FileType::class, [
                'label' => 'Avatar (image)',
                'required' => false,  // Ce champ est facultatif
                'mapped' => false,    // Ne pas lier directement à l'entité Profil
                'attr' => ['accept' => 'image/*'],  // Limiter les fichiers aux images lier directement à l'entité Profil
            ])
            ->add('bio', TextareaType::class, [
                'required' => false,
            ])
            ->add('preferences', TextType::class, [
                'required' => false,
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Profil::class,
            'user' => null, // Ajouter l'utilisateur dans les options du formulaire
        ]);
    }
}
