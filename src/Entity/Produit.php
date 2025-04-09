<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;

use App\Repository\ProduitRepository;

#[ORM\Entity(repositoryClass: ProduitRepository::class)]
#[ORM\Table(name: 'produit')]
class Produit
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $id_produit = null;

    public function getId_produit(): ?int
    {
        return $this->id_produit;
    }

    public function setId_produit(int $id_produit): self
    {
        $this->id_produit = $id_produit;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $nom_produit = null;

    public function getNom_produit(): ?string
    {
        return $this->nom_produit;
    }

    public function setNom_produit(string $nom_produit): self
    {
        $this->nom_produit = $nom_produit;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $categorie = null;

    public function getCategorie(): ?string
    {
        return $this->categorie;
    }

    public function setCategorie(string $categorie): self
    {
        $this->categorie = $categorie;
        return $this;
    }

    #[ORM\Column(type: 'decimal', nullable: false)]
    private ?float $prix = null;

    public function getPrix(): ?float
    {
        return $this->prix;
    }

    public function setPrix(float $prix): self
    {
        $this->prix = $prix;
        return $this;
    }

    #[ORM\Column(type: 'integer', nullable: false)]
    private ?int $stock = null;

    public function getStock(): ?int
    {
        return $this->stock;
    }

    public function setStock(int $stock): self
    {
        $this->stock = $stock;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $description = null;

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function setDescription(string $description): self
    {
        $this->description = $description;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $image_produit = null;

    public function getImage_produit(): ?string
    {
        return $this->image_produit;
    }

    public function setImage_produit(string $image_produit): self
    {
        $this->image_produit = $image_produit;
        return $this;
    }

    #[ORM\OneToMany(targetEntity: Compagne::class, mappedBy: 'produit')]
    private Collection $compagnes;

    /**
     * @return Collection<int, Compagne>
     */
    public function getCompagnes(): Collection
    {
        if (!$this->compagnes instanceof Collection) {
            $this->compagnes = new ArrayCollection();
        }
        return $this->compagnes;
    }

    public function addCompagne(Compagne $compagne): self
    {
        if (!$this->getCompagnes()->contains($compagne)) {
            $this->getCompagnes()->add($compagne);
        }
        return $this;
    }

    public function removeCompagne(Compagne $compagne): self
    {
        $this->getCompagnes()->removeElement($compagne);
        return $this;
    }

}
