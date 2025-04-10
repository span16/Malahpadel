<?php

namespace App\Form;

use App\Entity\Reclamation;
use App\Entity\Reservation;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;

class ReclamationType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
{
    $builder
        ->add('date', DateType::class, [
            'label' => 'Date de réclamation',
            'widget' => 'single_text',
            'required' => true,
            'attr' => ['class' => 'form-control']
        ])
        ->add('raison', TextareaType::class, [
            'label' => 'Raison de la réclamation',
            'required' => true,
            'attr' => [
                'class' => 'form-control',
                'rows' => 5
            ]
        ])
        ->add('etat', ChoiceType::class, [
            'label' => 'État',
            'choices' => [
                'Nouvelle' => 'nouvelle',
                'En cours' => 'en cours',
                'Traitée' => 'traitée',
                'Rejetée' => 'rejetée'
            ],
            'required' => true,
            'attr' => ['class' => 'form-control']
        ]);

    if ($options['include_reservation_field']) {
        $builder->add('reservation', EntityType::class, [
            'class' => Reservation::class,
            'label' => 'Réservation associée',
            'required' => false,
            'choice_label' => function(Reservation $reservation) {
                return sprintf('#%d - %s', $reservation->getId(), $reservation->getNom());
            },
            'attr' => ['class' => 'form-control']
        ]);
    }
}

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Reclamation::class,
            'include_reservation_field' => true
        ]);
    }
}