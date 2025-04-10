<?php

namespace App\Entity;

use App\Entity\User;
use App\Entity\Emploidutemps;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity]
#[ORM\Table(name: "equipes")]
class Equipes
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(name: "equipe_id", type: "integer")]
    private ?int $equipe_id = null;

    #[ORM\Column(name: "nom_equipe", type: "string", length: 50)]
    private string $nom_equipe;

    #[ORM\ManyToOne(targetEntity: User::class, inversedBy: "equipesJoueur1")]
    #[ORM\JoinColumn(name: "joueur1_id", referencedColumnName: "id", onDelete: "CASCADE", nullable: false)]
    private ?User $joueur1 = null;

    #[ORM\ManyToOne(targetEntity: User::class, inversedBy: "equipesJoueur2")]
    #[ORM\JoinColumn(name: "joueur2_id", referencedColumnName: "id", onDelete: "CASCADE", nullable: false)]
    private ?User $joueur2 = null;

    #[ORM\OneToMany(mappedBy: "equipe1", targetEntity: Emploidutemps::class, cascade: ['persist', 'remove'])]
    private Collection $emploisEquipe1;

    #[ORM\OneToMany(mappedBy: "equipe2", targetEntity: Emploidutemps::class, cascade: ['persist', 'remove'])]
    private Collection $emploisEquipe2;

    public function __construct()
    {
        $this->emploisEquipe1 = new ArrayCollection();
        $this->emploisEquipe2 = new ArrayCollection();
    }

    public function getEquipeId(): ?int
    {
        return $this->equipe_id;
    }

    public function setEquipeId(int $id): self
    {
        $this->equipe_id = $id;
        return $this;
    }

    public function getNomEquipe(): string
    {
        return $this->nom_equipe;
    }

    public function setNomEquipe(string $nom): self
    {
        $this->nom_equipe = $nom;
        return $this;
    }

    public function getJoueur1(): ?User
    {
        return $this->joueur1;
    }

    public function setJoueur1(?User $user): self
    {
        $this->joueur1 = $user;
        return $this;
    }

    public function getJoueur2(): ?User
    {
        return $this->joueur2;
    }

    public function setJoueur2(?User $user): self
    {
        $this->joueur2 = $user;
        return $this;
    }

    public function getEmploisEquipe1(): Collection
    {
        return $this->emploisEquipe1;
    }

    public function addEmploiEquipe1(Emploidutemps $emploi): self
    {
        if (!$this->emploisEquipe1->contains($emploi)) {
            $this->emploisEquipe1[] = $emploi;
            $emploi->setEquipe1($this);
        }
        return $this;
    }

    public function removeEmploiEquipe1(Emploidutemps $emploi): self
    {
        if ($this->emploisEquipe1->removeElement($emploi) && $emploi->getEquipe1() === $this) {
            $emploi->setEquipe1(null);
        }
        return $this;
    }

    public function getEmploisEquipe2(): Collection
    {
        return $this->emploisEquipe2;
    }

    public function addEmploiEquipe2(Emploidutemps $emploi): self
    {
        if (!$this->emploisEquipe2->contains($emploi)) {
            $this->emploisEquipe2[] = $emploi;
            $emploi->setEquipe2($this);
        }
        return $this;
    }

    public function removeEmploiEquipe2(Emploidutemps $emploi): self
    {
        if ($this->emploisEquipe2->removeElement($emploi) && $emploi->getEquipe2() === $this) {
            $emploi->setEquipe2(null);
        }
        return $this;
    }
}
