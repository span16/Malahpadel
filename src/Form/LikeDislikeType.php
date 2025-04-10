<?php

namespace App\Form;

use App\Entity\LikeDislike;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\CheckboxType;
use Symfony\Component\Form\Extension\Core\Type\HiddenType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class LikeDislikeType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('isLike', CheckboxType::class, [
                'label' => 'J’aime cet événement',
                'required' => false,
            ])
            ->add('rating', HiddenType::class, [
                'required' => false,
                'attr' => ['id' => 'ratingInput']
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => LikeDislike::class,
        ]);
    }
}
