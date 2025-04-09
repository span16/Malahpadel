<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use App\Repository\CompagneRepository;

#[ORM\Entity(repositoryClass: CompagneRepository::class)]
#[ORM\Table(name: 'compagne')]
class Compagne
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $id = null;

    #[ORM\Column(type: 'string', length: 255, nullable: false)]
    private string $nomSponsor;

    #[ORM\Column(type: 'float', nullable: false)]
    private float $tarifs;

    #[ORM\Column(type: 'date', nullable: false)]
    private \DateTimeInterface $dateDebut;

    #[ORM\Column(type: 'date', nullable: false)]
    private \DateTimeInterface $dateFin;

    #[ORM\Column(type: 'string', length: 255, nullable: false)]
    private string $logoCompagne;

    #[ORM\Column(type: 'string', length: 50, nullable: false)]
    private string $status;

    #[ORM\Column(type: 'string', length: 100, nullable: false)]
    private string $typeMarketing;

    #[ORM\ManyToOne(targetEntity: Produit::class, inversedBy: 'compagnes')]
    #[ORM\JoinColumn(
        name: 'id_produit', 
        referencedColumnName: 'id_produit',
        nullable: false,
        onDelete: 'RESTRICT'
    )]
    private Produit $produit;

    // Getters et Setters

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getNomSponsor(): string
    {
        return $this->nomSponsor;
    }

    public function setNomSponsor(string $nomSponsor): self
    {
        $this->nomSponsor = $nomSponsor;
        return $this;
    }

    public function getTarifs(): float
    {
        return $this->tarifs;
    }

    public function setTarifs(float $tarifs): self
    {
        $this->tarifs = $tarifs;
        return $this;
    }

    public function getDateDebut(): \DateTimeInterface
    {
        return $this->dateDebut;
    }

    public function setDateDebut(\DateTimeInterface $dateDebut): self
    {
        $this->dateDebut = $dateDebut;
        return $this;
    }

    public function getDateFin(): \DateTimeInterface
    {
        return $this->dateFin;
    }

    public function setDateFin(\DateTimeInterface $dateFin): self
    {
        $this->dateFin = $dateFin;
        return $this;
    }

    public function getLogoCompagne(): string
    {
        return $this->logoCompagne;
    }

    public function setLogoCompagne(string $logoCompagne): self
    {
        $this->logoCompagne = $logoCompagne;
        return $this;
    }

    public function getStatus(): string
    {
        return $this->status;
    }

    public function setStatus(string $status): self
    {
        $this->status = $status;
        return $this;
    }

    public function getTypeMarketing(): string
    {
        return $this->typeMarketing;
    }

    public function setTypeMarketing(string $typeMarketing): self
    {
        $this->typeMarketing = $typeMarketing;
        return $this;
    }

    public function getProduit(): Produit
    {
        return $this->produit;
    }

    public function setProduit(Produit $produit): self
    {
        $this->produit = $produit;
        return $this;
    }
}