<?php

namespace App\Entity;

use App\Repository\ReservationRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: ReservationRepository::class)]
#[ORM\HasLifecycleCallbacks]
class Reservation
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(name: "id_R", type: "integer")]
    private ?int $id = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "Vous devez sélectionner un événement")]
    private ?string $nom = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "Le type de réservation est obligatoire")]
    #[Assert\Choice(
        choices: ['simple', 'duo', 'groupe', 'vip', 'tournoi', 'location', 'coach'],
        message: "Type de réservation invalide"
    )]
    private ?string $typeReservation = null;

    #[ORM\Column]
    #[Assert\NotBlank(message: "Le nombre de places est obligatoire")]
    #[Assert\Range(
        min: 1,
        max: 100,
        notInRangeMessage: "Le nombre de places doit être entre {{ min }} et {{ max }}"
    )]
    private ?int $nombrePlaces = null;

    #[ORM\Column(type: 'text', nullable: true)]
    #[Assert\Length(
        min: 5,
        minMessage: "La remarque doit contenir au moins {{ limit }} caractères",
        max: 500,
        maxMessage: "La remarque ne peut pas dépasser {{ limit }} caractères"
    )]
    private ?string $remarque = null;

    #[ORM\Column(unique: true)]
    private ?int $codeConfirmation = null;

    #[ORM\PrePersist]
    public function generateConfirmationCode(): void
    {
        error_log('PrePersist callback executed - Generating confirmation code');
        if ($this->codeConfirmation === null) {
            $this->codeConfirmation = random_int(100000, 999999);
            error_log('Generated code: '.$this->codeConfirmation);
        }
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getNom(): ?string
    {
        return $this->nom;
    }

    public function setNom(string $nom): self
    {
        $this->nom = $nom;
        return $this;
    }

    public function getTypeReservation(): ?string
    {
        return $this->typeReservation;
    }

    public function setTypeReservation(string $typeReservation): self
    {
        $this->typeReservation = $typeReservation;
        return $this;
    }

    public function getNombrePlaces(): ?int
    {
        return $this->nombrePlaces;
    }

    public function setNombrePlaces(int $nombrePlaces): self
    {
        $this->nombrePlaces = $nombrePlaces;
        return $this;
    }

    public function getRemarque(): ?string
    {
        return $this->remarque;
    }

    public function setRemarque(?string $remarque): self
    {
        $this->remarque = $remarque;
        return $this;
    }

    public function getCodeConfirmation(): ?int
    {
        return $this->codeConfirmation;
    }

    public function setCodeConfirmation(int $codeConfirmation): self
    {
        $this->codeConfirmation = $codeConfirmation;
        return $this;
    }

    public function __toString(): string
    {
        return sprintf(
            'Réservation #%s - %s (%s)',
            $this->id ?? 'Nouvelle',
            $this->nom ?? 'Aucun événement',
            $this->typeReservation ?? 'Aucun type'
        );
    }
}