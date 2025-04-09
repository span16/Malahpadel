<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;

use App\Repository\AnnoncematchRepository;

#[ORM\Entity(repositoryClass: AnnoncematchRepository::class)]
#[ORM\Table(name: 'annoncematch')]
class Annoncematch
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $annonce_id = null;

    public function getAnnonce_id(): ?int
    {
        return $this->annonce_id;
    }

    public function setAnnonce_id(int $annonce_id): self
    {
        $this->annonce_id = $annonce_id;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $titre = null;

    public function getTitre(): ?string
    {
        return $this->titre;
    }

    public function setTitre(string $titre): self
    {
        $this->titre = $titre;
        return $this;
    }

    #[ORM\Column(type: 'date', nullable: false)]
    private ?\DateTimeInterface $date_heure = null;

    public function getDate_heure(): ?\DateTimeInterface
    {
        return $this->date_heure;
    }

    public function setDate_heure(\DateTimeInterface $date_heure): self
    {
        $this->date_heure = $date_heure;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: true)]
    private ?string $lieu = null;

    public function getLieu(): ?string
    {
        return $this->lieu;
    }

    public function setLieu(?string $lieu): self
    {
        $this->lieu = $lieu;
        return $this;
    }

    #[ORM\Column(type: 'integer', nullable: false)]
    private ?int $joueurs_recherches = null;

    public function getJoueurs_recherches(): ?int
    {
        return $this->joueurs_recherches;
    }

    public function setJoueurs_recherches(int $joueurs_recherches): self
    {
        $this->joueurs_recherches = $joueurs_recherches;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $niveau = null;

    public function getNiveau(): ?string
    {
        return $this->niveau;
    }

    public function setNiveau(string $niveau): self
    {
        $this->niveau = $niveau;
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

}
