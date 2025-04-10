<?php

namespace App\Form;

use App\Entity\Evenement;
use App\Entity\TypeV; // ✅ CORRECT : TypeV est bien dans Entity
use App\Entity\Terrain;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\UrlType;
use Symfony\Component\Form\Extension\Core\Type\EnumType;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class EvenementType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $builder
            ->add('nom', TextType::class, [
                'label' => 'Nom de l\'événement',
                'attr' => ['class' => 'form-control']
            ])
            ->add('type', EnumType::class, [
                'class' => TypeV::class,
                'choices' => TypeV::cases(), // ✅ essentiel pour les enums
                'label' => 'Type',
                'choice_label' => fn (TypeV $choice) => $choice->value,
                'attr' => ['class' => 'form-control']
            ])
            ->add('date', DateType::class, [
                'label' => 'Date',
                'widget' => 'single_text',
                'attr' => ['class' => 'form-control'],
                'html5' => true,
            ])
            ->add('terrain', EntityType::class, [
                'class' => Terrain::class,
                'choice_label' => 'nom',
                'label' => 'Terrain',
                'attr' => ['class' => 'form-control']
            ])
            ->add('image_url', UrlType::class, [
                'label' => 'Lien de l\'image',
                'attr' => ['class' => 'form-control']
            ]);
    }

    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults([
            'data_class' => Evenement::class,
        ]);
    }
}
