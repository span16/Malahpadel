<?php

namespace App\Form;

use App\Entity\Produit;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\NumberType;
use Symfony\Component\Form\Extension\Core\Type\IntegerType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Validator\Constraints as Assert;


class ProduitType extends AbstractType
{
public function buildForm(FormBuilderInterface $builder, array $options): void
{
    $builder
        ->add('nom_produit', TextType::class, [
            'label' => 'Nom du produit',
            'attr' => ['class' => 'form-control'],
            'constraints' => [
                new Assert\NotBlank(['message' => 'Le nom du produit est obligatoire']),
                new Assert\Length([
                    'min' => 3,
                    'minMessage' => 'Le nom doit contenir au moins {{ limit }} caractères'
                ])
            ]
        ])
        ->add('categorie', TextType::class, [
            'label' => 'Catégorie',
            'attr' => ['class' => 'form-control'],
            'constraints' => [
                new Assert\NotBlank(['message' => 'La catégorie est obligatoire'])
            ]
        ])
        ->add('prix', NumberType::class, [
            'label' => 'Prix',
            'scale' => 2,
            'attr' => ['class' => 'form-control'],
            'constraints' => [
                new Assert\NotBlank(['message' => 'Le prix est obligatoire']),
                new Assert\Positive(['message' => 'Le prix doit être positif'])
            ]
        ])
        ->add('stock', IntegerType::class, [
            'label' => 'Stock',
            'attr' => ['class' => 'form-control'],
            'constraints' => [
                new Assert\NotBlank(['message' => 'Le stock est obligatoire']),
                new Assert\PositiveOrZero(['message' => 'Le stock ne peut pas être négatif'])
            ]
        ])
        ->add('description', TextareaType::class, [
            'label' => 'Description',
            'attr' => ['class' => 'form-control', 'rows' => 5],
            'required' => false
        ])
        ->add('image_produit', FileType::class, [
            'label' => 'Image du produit',
            'required' => false,
            'mapped' => false,
            'attr' => ['class' => 'form-control'],
            'constraints' => [
                new Assert\File([
                    'maxSize' => '2M',
                    'mimeTypes' => ['image/jpeg', 'image/png'],
                    'mimeTypesMessage' => 'Veuillez uploader une image valide (JPEG ou PNG)',
                    'maxSizeMessage' => 'L\'image ne doit pas dépasser {{ limit }}'
                ])
            ]
        ]);}}