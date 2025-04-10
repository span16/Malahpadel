<?php

namespace App\Entity;

use App\Repository\AnnonceMatchRepository;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: AnnonceMatchRepository::class)]
#[ORM\Table(name: 'annoncematch')]
class AnnonceMatch
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(name: 'annonce_id')]
    private ?int $annonceId = null;

    #[ORM\Column(length: 25)]
    private ?string $titre = null;

    #[ORM\Column(name: 'date_heure', type: Types::DATE_MUTABLE)]
    private ?\DateTimeInterface $dateHeure = null;

    #[ORM\Column(length: 25, nullable: true)]
    private ?string $lieu = null;

    #[ORM\Column(name: 'joueurs_recherches')]
    private ?int $joueursRecherches = null;

    #[ORM\Column(length: 25)]
    private ?string $niveau = null;

    #[ORM\Column(length: 25)]
    private ?string $description = null;

    public function getAnnonceId(): ?int
    {
        return $this->annonceId;
    }

    public function getTitre(): ?string
    {
        return $this->titre;
    }

    public function setTitre(string $titre): static
    {
        $this->titre = $titre;

        return $this;
    }

    public function getDateHeure(): ?\DateTimeInterface
    {
        return $this->dateHeure;
    }

    public function setDateHeure(\DateTimeInterface $dateHeure): static
    {
        $this->dateHeure = $dateHeure;

        return $this;
    }

    public function getLieu(): ?string
    {
        return $this->lieu;
    }

    public function setLieu(?string $lieu): static
    {
        $this->lieu = $lieu;

        return $this;
    }

    public function getJoueursRecherches(): ?int
    {
        return $this->joueursRecherches;
    }

    public function setJoueursRecherches(int $joueursRecherches): static
    {
        $this->joueursRecherches = $joueursRecherches;

        return $this;
    }

    public function getNiveau(): ?string
    {
        return $this->niveau;
    }

    public function setNiveau(string $niveau): static
    {
        $this->niveau = $niveau;

        return $this;
    }

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function setDescription(string $description): static
    {
        $this->description = $description;

        return $this;
    }
} 