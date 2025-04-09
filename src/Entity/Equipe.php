<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;

use App\Repository\EquipeRepository;

#[ORM\Entity(repositoryClass: EquipeRepository::class)]
#[ORM\Table(name: 'equipes')]
class Equipe
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $equipe_id = null;

    public function getEquipe_id(): ?int
    {
        return $this->equipe_id;
    }

    public function setEquipe_id(int $equipe_id): self
    {
        $this->equipe_id = $equipe_id;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $nom_equipe = null;

    public function getNom_equipe(): ?string
    {
        return $this->nom_equipe;
    }

    public function setNom_equipe(string $nom_equipe): self
    {
        $this->nom_equipe = $nom_equipe;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $email_joueur1 = null;

    public function getEmail_joueur1(): ?string
    {
        return $this->email_joueur1;
    }

    public function setEmail_joueur1(string $email_joueur1): self
    {
        $this->email_joueur1 = $email_joueur1;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $email_joueur2 = null;

    public function getEmail_joueur2(): ?string
    {
        return $this->email_joueur2;
    }

    public function setEmail_joueur2(string $email_joueur2): self
    {
        $this->email_joueur2 = $email_joueur2;
        return $this;
    }

}
