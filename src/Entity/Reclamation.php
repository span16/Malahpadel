<?php

namespace App\Entity;

use App\Repository\ReclamationRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: ReclamationRepository::class)]
class Reclamation
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(name: "id_reclamation", type: "integer")]
    private ?int $id = null;

    #[ORM\Column(type: "date")]
    #[Assert\NotBlank(message: "La date de réclamation est obligatoire")]
    #[Assert\Type("\DateTimeInterface", message: "La date doit être une valeur valide")]
    private ?\DateTimeInterface $date = null;

    #[ORM\Column(length: 25)]
    #[Assert\NotBlank(message: "La raison est obligatoire")]
    #[Assert\Length(
        min: 3,
        max: 25,
        minMessage: "La raison doit contenir au moins {{ limit }} caractères",
        maxMessage: "La raison ne peut pas dépasser {{ limit }} caractères"
    )]
    #[Assert\Regex(
        pattern: "/^[a-zA-Z0-9éèêëàâäôöûüç' -]+$/",
        message: "La raison ne peut contenir que des lettres, chiffres et certains caractères spéciaux"
    )]
    private ?string $raison = null;

    #[ORM\Column(length: 25)]
    #[Assert\NotBlank(message: "L'état est obligatoire")]
    #[Assert\Length(
        min: 2,
        max: 25,
        minMessage: "L'état doit contenir au moins {{ limit }} caractères",
        maxMessage: "L'état ne peut pas dépasser {{ limit }} caractères"
    )]
    #[Assert\Choice(
        choices: ["en cours", "traitée", "rejetée", "nouvelle"],
        message: "L'état doit être une valeur valide (en cours, traitée, rejetée, nouvelle)"
    )]
    private ?string $etat = null;

    #[ORM\ManyToOne(targetEntity: Reservation::class)]
    #[ORM\JoinColumn(name: "id_R", referencedColumnName: "id_R")]
    #[Assert\NotNull(message: "Une réservation doit être associée à la réclamation")]
    private ?Reservation $reservation = null;

    // Getters et Setters...

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getDate(): ?\DateTimeInterface
    {
        return $this->date;
    }

    public function setDate(\DateTimeInterface $date): self
    {
        $this->date = $date;
        return $this;
    }

    public function getRaison(): ?string
    {
        return $this->raison;
    }

    public function setRaison(string $raison): self
    {
        $this->raison = $raison;
        return $this;
    }

    public function getEtat(): ?string
    {
        return $this->etat;
    }

    public function setEtat(string $etat): self
    {
        $this->etat = $etat;
        return $this;
    }

    public function getReservation(): ?Reservation
    {
        return $this->reservation;
    }

    public function setReservation(?Reservation $reservation): self
    {
        $this->reservation = $reservation;
        return $this;
    }
}