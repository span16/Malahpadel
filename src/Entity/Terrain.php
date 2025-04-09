<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;

use App\Repository\TerrainRepository;

#[ORM\Entity(repositoryClass: TerrainRepository::class)]
#[ORM\Table(name: 'terrain')]
class Terrain
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $id = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function setId(int $id): self
    {
        $this->id = $id;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $nom = null;

    public function getNom(): ?string
    {
        return $this->nom;
    }

    public function setNom(string $nom): self
    {
        $this->nom = $nom;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $adresse = null;

    public function getAdresse(): ?string
    {
        return $this->adresse;
    }

    public function setAdresse(string $adresse): self
    {
        $this->adresse = $adresse;
        return $this;
    }

    #[ORM\Column(type: 'decimal', nullable: false)]
    private ?float $prix_par_personne = null;

    public function getPrix_par_personne(): ?float
    {
        return $this->prix_par_personne;
    }

    public function setPrix_par_personne(float $prix_par_personne): self
    {
        $this->prix_par_personne = $prix_par_personne;
        return $this;
    }

    #[ORM\Column(type: 'time', nullable: false)]
    private ?string $heure_ouverture = null;

    public function getHeure_ouverture(): ?string
    {
        return $this->heure_ouverture;
    }

    public function setHeure_ouverture(string $heure_ouverture): self
    {
        $this->heure_ouverture = $heure_ouverture;
        return $this;
    }

    #[ORM\Column(type: 'time', nullable: false)]
    private ?string $heure_fermeture = null;

    public function getHeure_fermeture(): ?string
    {
        return $this->heure_fermeture;
    }

    public function setHeure_fermeture(string $heure_fermeture): self
    {
        $this->heure_fermeture = $heure_fermeture;
        return $this;
    }

}
