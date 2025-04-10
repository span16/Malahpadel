<?php

namespace App\Entity;

use App\Entity\Produit;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Bridge\Doctrine\Validator\Constraints\UniqueEntity;

#[ORM\Entity(repositoryClass: "App\Repository\CompagneRepository")]
#[ORM\Table(name: 'compagne')]
#[UniqueEntity(
    fields: ['nomSponsor'],
    message: 'Ce nom de sponsor est déjà utilisé'
)]
class Compagne
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(name: 'id_compagne', type: 'integer')]
    private ?int $idCompagne = null;

    #[ORM\Column(name: 'nom_sponsor', type: 'string', length: 30, unique: true)]
    #[Assert\NotBlank(message: 'Le nom du sponsor est obligatoire')]
    #[Assert\Length(
        max: 30,
        maxMessage: 'Le nom ne doit pas dépasser {{ limit }} caractères'
    )]
    private ?string $nomSponsor = null;

    #[ORM\Column(name: 'tarifs', type: 'float')]
    #[Assert\NotBlank(message: 'Le tarif est obligatoire')]
    #[Assert\Positive(message: 'Le tarif doit être positif')]
    private ?float $tarifs = null;

    #[ORM\Column(name: 'date_debut', type: 'date')]
    #[Assert\NotBlank(message: 'La date de début est obligatoire')]
    private ?\DateTimeInterface $dateDebut = null;

    #[ORM\Column(name: 'date_fin', type: 'date')]
    #[Assert\NotBlank(message: 'La date de fin est obligatoire')]
    #[Assert\GreaterThan(
        propertyPath: 'dateDebut',
        message: 'La date de fin doit être postérieure à la date de début'
    )]
    private ?\DateTimeInterface $dateFin = null;

    #[ORM\Column(name: 'logo_compagne', type: 'string', length: 255, nullable: false)]
    private ?string $logoCompagne = 'default-logo.png';

    #[ORM\Column(name: 'status', type: 'string', columnDefinition: "ENUM('active', 'inactive', 'pending')")]
    #[Assert\NotBlank(message: 'Le statut est obligatoire')]
    #[Assert\Choice(
        choices: ['active', 'inactive', 'pending'],
        message: 'Choisissez un statut valide'
    )]
    private ?string $status = 'active';

    #[ORM\Column(name: 'TypeMarketing', type: 'string', length: 255)]
    #[Assert\NotBlank(message: 'Le type marketing est obligatoire')]
    #[Assert\Choice(
        choices: ['Email', 'Réseaux sociaux'],
        message: 'Choisissez un type marketing valide'
    )]
    private ?string $typeMarketing = null;

    #[ORM\ManyToOne(targetEntity: Produit::class)]
    #[ORM\JoinColumn(name: 'id_produit', referencedColumnName: 'id_produit', nullable: true)]
    private ?Produit $produit = null;

    

    // Getters et Setters
    public function getIdCompagne(): ?int { return $this->idCompagne; }

    public function getNomSponsor(): ?string { return $this->nomSponsor; }
    public function setNomSponsor(string $nomSponsor): static { $this->nomSponsor = $nomSponsor; return $this; }

    public function getTarifs(): ?float { return $this->tarifs; }
    public function setTarifs(float $tarifs): static { $this->tarifs = $tarifs; return $this; }

    public function getDateDebut(): ?\DateTimeInterface { return $this->dateDebut; }
    public function setDateDebut(\DateTimeInterface $dateDebut): static { $this->dateDebut = $dateDebut; return $this; }

    public function getDateFin(): ?\DateTimeInterface { return $this->dateFin; }
    public function setDateFin(\DateTimeInterface $dateFin): static { $this->dateFin = $dateFin; return $this; }

    public function getLogoCompagne(): ?string { return $this->logoCompagne; }
    public function setLogoCompagne(?string $logoCompagne): static { $this->logoCompagne = $logoCompagne; return $this; }

    public function getStatus(): ?string { return $this->status; }
    public function setStatus(string $status): static { $this->status = $status; return $this; }

    public function getTypeMarketing(): ?string { return $this->typeMarketing; }
    public function setTypeMarketing(string $typeMarketing): static { $this->typeMarketing = $typeMarketing; return $this; }

    public function getProduit(): ?Produit { return $this->produit; }
    public function setProduit(?Produit $produit): static { $this->produit = $produit; return $this; }
}