<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;

use App\Repository\EmploidutempRepository;

#[ORM\Entity(repositoryClass: EmploidutempRepository::class)]
#[ORM\Table(name: 'emploidutemps')]
class Emploidutemp
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $id_emplois = null;

    public function getId_emplois(): ?int
    {
        return $this->id_emplois;
    }

    public function setId_emplois(int $id_emplois): self
    {
        $this->id_emplois = $id_emplois;
        return $this;
    }

    #[ORM\Column(type: 'date', nullable: false)]
    private ?\DateTimeInterface $Date = null;

    public function getDate(): ?\DateTimeInterface
    {
        return $this->Date;
    }

    public function setDate(\DateTimeInterface $Date): self
    {
        $this->Date = $Date;
        return $this;
    }

    #[ORM\Column(type: 'integer', nullable: false)]
    private ?int $partie = null;

    public function getPartie(): ?int
    {
        return $this->partie;
    }

    public function setPartie(int $partie): self
    {
        $this->partie = $partie;
        return $this;
    }

    #[ORM\Column(type: 'integer', nullable: false)]
    private ?int $id_Événement = null;

    public function getId_Événement(): ?int
    {
        return $this->id_Événement;
    }

    public function setId_Événement(int $id_Événement): self
    {
        $this->id_Événement = $id_Événement;
        return $this;
    }

    #[ORM\Column(type: 'integer', nullable: false)]
    private ?int $id_equipe = null;

    public function getId_equipe(): ?int
    {
        return $this->id_equipe;
    }

    public function setId_equipe(int $id_equipe): self
    {
        $this->id_equipe = $id_equipe;
        return $this;
    }

    #[ORM\Column(type: 'integer', nullable: false)]
    private ?int $equipe_2 = null;

    public function getEquipe_2(): ?int
    {
        return $this->equipe_2;
    }

    public function setEquipe_2(int $equipe_2): self
    {
        $this->equipe_2 = $equipe_2;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: true)]
    private ?string $google_event_id = null;

    public function getGoogle_event_id(): ?string
    {
        return $this->google_event_id;
    }

    public function setGoogle_event_id(?string $google_event_id): self
    {
        $this->google_event_id = $google_event_id;
        return $this;
    }

}
