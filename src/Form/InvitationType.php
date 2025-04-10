<?php

namespace App\Form;

use App\Entity\Invitation;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\DateTimeType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;

class InvitationType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('senderId', TextType::class, [
                'label' => 'ID Expéditeur',
                'attr' => ['class' => 'form-control']
            ])
            ->add('receiverId', TextType::class, [
                'label' => 'ID Destinataire',
                'attr' => ['class' => 'form-control']
            ])
            ->add('dateEnvoi', DateTimeType::class, [
                'label' => 'Date d\'envoi',
                'widget' => 'single_text',
                'attr' => ['class' => 'form-control']
            ])
            ->add('statut', ChoiceType::class, [
                'label' => 'Statut',
                'choices' => [
                    'En attente' => 'en_attente',
                    'Acceptée' => 'acceptee',
                    'Refusée' => 'refusee'
                ],
                'attr' => ['class' => 'form-control']
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Invitation::class,
        ]);
    }
} 