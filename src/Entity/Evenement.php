<?php

namespace App\Entity;

use App\Entity\TypeV;
use App\Entity\Terrain;
use App\Entity\Emploidutemps;
use App\Entity\LikeDislike;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity]
class Evenement
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: "integer")]
    private ?int $id = null;

    #[ORM\Column(type: "string", length: 50)]
    private string $nom;

    #[ORM\Column(enumType: TypeV::class)]
    private TypeV $type;

    #[ORM\Column(type: "date")]
    private \DateTimeInterface $date;

    #[ORM\Column(type: "string", length: 500)]
    private string $image_url;

    #[ORM\ManyToOne(targetEntity: Terrain::class, inversedBy: "evenements")]
    #[ORM\JoinColumn(name: "terrain_id", referencedColumnName: "id", onDelete: "CASCADE")]
    private ?Terrain $terrain = null;

    #[ORM\OneToMany(mappedBy: "evenement", targetEntity: Emploidutemps::class, cascade: ['persist', 'remove'])]
    private Collection $emploidutempss;

    #[ORM\OneToMany(mappedBy: "evenement", targetEntity: LikeDislike::class, cascade: ['persist', 'remove'])]
    private Collection $likesDislikes;

    public function __construct()
    {
        $this->emploidutempss = new ArrayCollection();
        $this->likesDislikes = new ArrayCollection();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getNom(): string
    {
        return $this->nom;
    }

    public function setNom(string $nom): self
    {
        $this->nom = $nom;
        return $this;
    }

    public function getType(): TypeV
    {
        return $this->type;
    }

    public function setType(TypeV $type): self
    {
        $this->type = $type;
        return $this;
    }

    public function getDate(): \DateTimeInterface
    {
        return $this->date;
    }

    public function setDate(\DateTimeInterface $date): self
    {
        $this->date = $date;
        return $this;
    }

    public function getImageUrl(): string
    {
        return $this->image_url;
    }

    public function setImageUrl(string $imageUrl): self
    {
        $this->image_url = $imageUrl;
        return $this;
    }

    public function getTerrain(): ?Terrain
    {
        return $this->terrain;
    }

    public function setTerrain(?Terrain $terrain): self
    {
        $this->terrain = $terrain;
        return $this;
    }

    public function getEmploidutempss(): Collection
    {
        return $this->emploidutempss;
    }

    public function addEmploidutemps(Emploidutemps $emploi): self
    {
        if (!$this->emploidutempss->contains($emploi)) {
            $this->emploidutempss[] = $emploi;
            $emploi->setEvenement($this);
        }
        return $this;
    }

    public function removeEmploidutemps(Emploidutemps $emploi): self
    {
        if ($this->emploidutempss->removeElement($emploi) && $emploi->getEvenement() === $this) {
            $emploi->setEvenement(null);
        }
        return $this;
    }

    public function getLikesDislikes(): Collection
    {
        return $this->likesDislikes;
    }

    public function addLikeDislike(LikeDislike $likeDislike): self
    {
        if (!$this->likesDislikes->contains($likeDislike)) {
            $this->likesDislikes[] = $likeDislike;
            $likeDislike->setEvenement($this);
        }
        return $this;
    }

    public function removeLikeDislike(LikeDislike $likeDislike): self
    {
        if ($this->likesDislikes->removeElement($likeDislike) && $likeDislike->getEvenement() === $this) {
            $likeDislike->setEvenement(null);
        }
        return $this;
    }
}
