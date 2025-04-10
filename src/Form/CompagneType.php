<?php

namespace App\Form;

use App\Entity\Compagne;
use App\Entity\Produit;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\NumberType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Validator\Constraints as Assert;

class CompagneType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
        ->add('nomSponsor', TextType::class, [
            'label' => 'Nom du Sponsor',
            'error_bubbling' => false, // Important
            'constraints' => [
                new Assert\NotBlank(['message' => 'Le nom du sponsor est obligatoire']),
                new Assert\Length([
                    'max' => 9,
                    'maxMessage' => 'Le nom ne doit pas dépasser {{ limit }} caractères'
                ])
            ]
        ])
            ->add('tarifs', NumberType::class, [
                'label' => 'Tarifs',
                'scale' => 2,
                'constraints' => [
                    new Assert\NotBlank([
                        'message' => 'Le tarif est obligatoire'
                    ]),
                    new Assert\Positive([
                        'message' => 'Le tarif doit être positif'
                    ]),
                    new Assert\Type([
                        'type' => 'float',
                        'message' => 'Le tarif doit être un nombre'
                    ])
                ],
                'attr' => [
                    'pattern' => null, // Supprime la validation HTML5
                    'oninput' => 'this.value = this.value.replace(/[^0-9.]/g, "").replace(/(\..*)\./g, "$1");'
                ]
            ])
            ->add('dateDebut', DateType::class, [
                'widget' => 'single_text',
                'label' => 'Date de début',
                'constraints' => [
                    new Assert\NotBlank([
                        'message' => 'La date de début est obligatoire'
                    ]),
                    new Assert\LessThan([
                        'propertyPath' => 'parent.all[dateFin].data',
                        'message' => 'La date de début doit être antérieure à la date de fin'
                    ])
                ]
            ])
            ->add('dateFin', DateType::class, [
                'widget' => 'single_text',
                'label' => 'Date de fin',
                'constraints' => [
                    new Assert\NotBlank([
                        'message' => 'La date de fin est obligatoire'
                    ]),
                    new Assert\GreaterThan([
                        'propertyPath' => 'parent.all[dateDebut].data',
                        'message' => 'La date de fin doit être postérieure à la date de début'
                    ])
                ]
            ])
            ->add('logoCompagne', FileType::class, [
                'label' => 'Logo',
                'required' => false,
                'mapped' => false,
                'constraints' => [
                    new Assert\File([
                        'maxSize' => '2M',
                        'mimeTypes' => ['image/jpeg', 'image/png', 'image/gif'],
                        'mimeTypesMessage' => 'Veuillez uploader une image valide (JPEG, PNG ou GIF)',
                        'maxSizeMessage' => 'L\'image est trop lourde ({{ size }} {{ suffix }}). La taille maximale est {{ limit }} {{ suffix }}'
                    ])
                ],
                'attr' => [
                    'onchange' => 'previewImage(this)',
                    'accept' => 'image/*'
                ]
            ])
            ->add('status', ChoiceType::class, [
                'label' => 'Statut',
                'choices' => [
                    'Active' => 'active',
                    'Inactive' => 'inactive',
                    'Pending' => 'pending'
                ],
                'constraints' => [
                    new Assert\NotBlank([
                        'message' => 'Le statut est obligatoire'
                    ])
                ]
            ])
            ->add('typeMarketing', ChoiceType::class, [
                'label' => 'Type Marketing',
                'choices' => [
                    'Email' => 'Email',
                    'Réseaux sociaux' => 'Réseaux sociaux'
                ],
                'constraints' => [
                    new Assert\NotBlank([
                        'message' => 'Le type marketing est obligatoire'
                    ])
                ]
            ])
            ->add('produit', EntityType::class, [
                'class' => Produit::class,
                'choice_label' => 'nomProduit',
                'label' => 'Produit associé',
                'required' => false
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Compagne::class,
            'attr' => [
                'novalidate' => 'novalidate' // Désactive la validation HTML5
            ]
        ]);
    }
}