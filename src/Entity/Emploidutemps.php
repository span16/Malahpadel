<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use App\Entity\Evenement;
use App\Entity\Equipes;

#[ORM\Entity]
class Emploidutemps
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: "integer")]
    private int $id;

    #[ORM\ManyToOne(targetEntity: Evenement::class, inversedBy: "emploidutempss")]
    #[ORM\JoinColumn(name: "evenement_id", referencedColumnName: "id", onDelete: "CASCADE")]
    private ?Evenement $evenement = null;

    #[ORM\ManyToOne(targetEntity: Equipes::class, inversedBy: "emploisEquipe1")]
    #[ORM\JoinColumn(name: "equipe1_id", referencedColumnName: "equipe_id", onDelete: "CASCADE")]
    private ?Equipes $equipe1 = null;

    #[ORM\ManyToOne(targetEntity: Equipes::class, inversedBy: "emploisEquipe2")]
    #[ORM\JoinColumn(name: "equipe2_id", referencedColumnName: "equipe_id", onDelete: "CASCADE")]
    private ?Equipes $equipe2 = null;

    #[ORM\Column(type: "date")]
    private \DateTimeInterface $date;

    #[ORM\Column(type: "integer")]
    private int $partie;

    #[ORM\Column(type: "string", length: 500)]
    private string $google_event_id;

    public function getId(): int
    {
        return $this->id;
    }

    public function getEvenement(): ?Evenement
    {
        return $this->evenement;
    }

    public function setEvenement(?Evenement $evenement): self
    {
        $this->evenement = $evenement;
        return $this;
    }

    public function getEquipe1(): ?Equipes
    {
        return $this->equipe1;
    }

    public function setEquipe1(?Equipes $equipe1): self
    {
        $this->equipe1 = $equipe1;
        return $this;
    }

    public function getEquipe2(): ?Equipes
    {
        return $this->equipe2;
    }

    public function setEquipe2(?Equipes $equipe2): self
    {
        $this->equipe2 = $equipe2;
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

    public function getPartie(): int
    {
        return $this->partie;
    }

    public function setPartie(int $partie): self
    {
        $this->partie = $partie;
        return $this;
    }

    public function getGoogleEventId(): string
    {
        return $this->google_event_id;
    }

    public function setGoogleEventId(string $google_event_id): self
    {
        $this->google_event_id = $google_event_id;
        return $this;
    }
}
