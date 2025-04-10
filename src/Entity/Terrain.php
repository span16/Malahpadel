<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;

#[ORM\Entity]
class Terrain
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: "integer")]
    private ?int $id = null;

    #[ORM\Column(type: "string", length: 255)]
    private string $nom;

    #[ORM\Column(type: "string", length: 255)]
    private string $adresse;

    #[ORM\Column(type: "float")]
    private float $prix_par_personne;

    #[ORM\Column(type: "string")]
    private string $heure_ouverture;

    #[ORM\Column(type: "string")]
    private string $heure_fermeture;

    #[ORM\OneToMany(mappedBy: "terrain", targetEntity: Evenement::class)]
    private Collection $evenements;

    public function __construct()
    {
        $this->evenements = new ArrayCollection();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function setId(int $value): self
    {
        $this->id = $value;
        return $this;
    }

    public function getNom(): string
    {
        return $this->nom;
    }

    public function setNom(string $value): self
    {
        $this->nom = $value;
        return $this;
    }

    public function getAdresse(): string
    {
        return $this->adresse;
    }

    public function setAdresse(string $value): self
    {
        $this->adresse = $value;
        return $this;
    }

    public function getPrixParPersonne(): float
    {
        return $this->prix_par_personne;
    }

    public function setPrixParPersonne(float $value): self
    {
        $this->prix_par_personne = $value;
        return $this;
    }

    public function getHeureOuverture(): string
    {
        return $this->heure_ouverture;
    }

    public function setHeureOuverture(string $value): self
    {
        $this->heure_ouverture = $value;
        return $this;
    }

    public function getHeureFermeture(): string
    {
        return $this->heure_fermeture;
    }

    public function setHeureFermeture(string $value): self
    {
        $this->heure_fermeture = $value;
        return $this;
    }

    public function getEvenements(): Collection
    {
        return $this->evenements;
    }

    public function addEvenement(Evenement $evenement): self
    {
        if (!$this->evenements->contains($evenement)) {
            $this->evenements[] = $evenement;
            $evenement->setTerrain($this);
        }

        return $this;
    }

    public function removeEvenement(Evenement $evenement): self
    {
        if ($this->evenements->removeElement($evenement)) {
            if ($evenement->getTerrain() === $this) {
                $evenement->setTerrain(null);
            }
        }

        return $this;
    }
}
