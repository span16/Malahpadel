<?php
namespace App\Form;

use App\Entity\Reservation;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\IntegerType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Validator\Constraints\NotBlank;
use Symfony\Component\Validator\Constraints\Length;
use Symfony\Component\Validator\Constraints\IsTrue;

class ReservationType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $builder
            ->add('nom', ChoiceType::class, [
                'label' => 'Nom de l\'événement *',
                'choices' => $options['evenement_names'],
                'placeholder' => 'Choisissez un événement',
                'constraints' => [
                    new NotBlank(['message' => 'Une sélection est requise'])
                ]
            ])
            ->add('typeReservation', ChoiceType::class, [
                'label' => 'Type de réservation *',
                'choices' => [
                    'Réservation simple' => 'simple',
                    'Réservation en duo' => 'duo',
                    'Réservation en groupe' => 'groupe',
                    'Réservation VIP' => 'vip',
                    'Réservation tournoi' => 'tournoi',
                    'Location du terrain' => 'location',
                    'Cours avec coach' => 'coach',
                ],
                'placeholder' => 'Choisissez un type',
                'constraints' => [
                    new NotBlank(['message' => 'Le type de réservation est obligatoire'])
                ]
            ])
            ->add('nombrePlaces', IntegerType::class, [
                'label' => 'Nombre de places *',
                'attr' => ['min' => 1, 'max' => 100],
                'constraints' => [
                    new NotBlank(['message' => 'Le nombre de places est obligatoire'])
                ]
            ])
            ->add('remarque', TextareaType::class, [
                'label' => 'Remarques (optionnel)',
                'required' => false,
                'attr' => [
                    'placeholder' => 'Informations supplémentaires...',
                    'rows' => 5
                ],
                'constraints' => [
                    new Length([
                        'min' => 5,
                        'max' => 500,
                        'minMessage' => 'La remarque doit contenir au moins {{ limit }} caractères',
                        'maxMessage' => 'La remarque ne peut pas dépasser {{ limit }} caractères'
                    ])
                ]
            ]);
    }

    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults([
            'data_class' => Reservation::class,
            'evenement_names' => []
        ]);
    }
}