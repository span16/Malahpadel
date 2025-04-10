<?php
// src/Entity/User.php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Symfony\Bridge\Doctrine\Validator\Constraints\UniqueEntity;
use Symfony\Component\Security\Core\User\UserInterface;
use Symfony\Component\Security\Core\User\PasswordAuthenticatedUserInterface;
use App\Repository\UserRepository;
use Symfony\Component\Validator\Constraints as Assert;
use App\Entity\Equipes;

#[ORM\Entity(repositoryClass: UserRepository::class)]
#[ORM\Table(name: 'user')]
#[UniqueEntity(fields: ['email'], message: 'There is already an account with this email')] // Validation of unique email
class User implements UserInterface, PasswordAuthenticatedUserInterface
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $id = null;

    #[ORM\Column(type: 'string', nullable: false)]
    #[Assert\NotBlank(message: "Le nom est requis.")]
    private ?string $nom = null;

    #[ORM\Column(type: 'string', nullable: false)]
    #[Assert\NotBlank(message: "Le prénom est requis.")]
    private ?string $prenom = null;

    #[ORM\Column(type: 'integer', nullable: false)]
    #[Assert\NotBlank(message: "L'âge est requis.")]
    #[Assert\Range(min: 18, max: 120, notInRangeMessage: 'L\'âge doit être compris entre {{ min }} et {{ max }} ans.')]
    private ?int $age = null;

    #[ORM\Column(type: 'string', unique: true)]
    #[Assert\NotBlank(message: "L'email est requis.")]
    #[Assert\Email(message: "L'email '{{ value }}' n'est pas valide.")]
    private ?string $email = null;

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $mdp = null;

    #[ORM\Column(type: 'integer', nullable: false)]
    #[Assert\NotBlank(message: "Le CIN est requis.")]
    private ?int $cin = null;

    #[ORM\Column(type: 'string', length: 20)]
    private string $etat = 'Inactif';

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $fonction = null;

    #[ORM\Column(type: 'string', nullable: true)]
    private ?string $reset_token = null;

    #[ORM\Column(type: 'datetime', nullable: true)]
    private ?\DateTimeInterface $token_expiration = null;

    #[ORM\OneToMany(targetEntity: Loginhistory::class, mappedBy: 'user')]
    private Collection $loginhistorys;

    #[ORM\Column]
    private bool $isVerified = false;

    private ?string $plainPassword = null;

    #[ORM\OneToOne(mappedBy: 'user', targetEntity: Profil::class, cascade: ['persist', 'remove'])]
    private ?Profil $profil = null;

    #[ORM\OneToMany(mappedBy: 'joueur1', targetEntity: Equipes::class)]
    private Collection $equipesJoueur1;

    #[ORM\OneToMany(mappedBy: 'joueur2', targetEntity: Equipes::class)]
    private Collection $equipesJoueur2;

    public function __construct()
    {
        $this->equipesJoueur1 = new ArrayCollection();
        $this->equipesJoueur2 = new ArrayCollection();
        $this->loginhistorys = new ArrayCollection();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getNom(): ?string
    {
        return $this->nom;
    }

    public function setNom(string $nom): self
    {
        $this->nom = $nom;
        return $this;
    }

    public function getPrenom(): ?string
    {
        return $this->prenom;
    }

    public function setPrenom(string $prenom): self
    {
        $this->prenom = $prenom;
        return $this;
    }

    public function getAge(): ?int
    {
        return $this->age;
    }

    public function setAge(int $age): self
    {
        $this->age = $age;
        return $this;
    }

    public function getEmail(): ?string
    {
        return $this->email;
    }

    public function setEmail(string $email): self
    {
        $this->email = $email;
        return $this;
    }

    public function getMdp(): ?string
    {
        return $this->mdp;
    }

    public function setMdp(string $mdp): self
    {
        $this->mdp = $mdp;
        return $this;
    }

    public function getCin(): ?int
    {
        return $this->cin;
    }

    public function setCin(int $cin): self
    {
        $this->cin = $cin;
        return $this;
    }

    public function getEtat(): ?string
    {
        return $this->etat;
    }

    public function setEtat(string $etat): self
    {
        $this->etat = $etat;
        return $this;
    }

    public function getFonction(): ?string
    {
        return $this->fonction;
    }

    public function setFonction(string $fonction): self
    {
        $this->fonction = $fonction;
        return $this;
    }

    public function getReset_token(): ?string
    {
        return $this->reset_token;
    }

    public function setReset_token(?string $reset_token): self
    {
        $this->reset_token = $reset_token;
        return $this;
    }

    public function getToken_expiration(): ?\DateTimeInterface
    {
        return $this->token_expiration;
    }

    public function setToken_expiration(?\DateTimeInterface $token_expiration): self
    {
        $this->token_expiration = $token_expiration;
        return $this;
    }

    public function getProfil(): ?Profil
    {
        return $this->profil;
    }

    public function setProfil(?Profil $profil): self
    {
        // assure la cohérence de la relation bidirectionnelle
        if ($profil === null && $this->profil !== null) {
            $this->profil->setUser(null);
        }

        if ($profil !== null && $profil->getUser() !== $this) {
            $profil->setUser($this);
        }

        $this->profil = $profil;

        return $this;
    }
    public function getUserIdentifier(): string
    {
        return $this->email;
    }

    public function getPassword(): string
    {
        return $this->mdp;
    }
    
        public function getRoles(): array
        {
            // On utilise la colonne 'fonction' pour attribuer le rôle
            $fonction = strtolower($this->getFonction());
        
            return match ($fonction) {
                'admin' => ['ROLE_ADMIN'],
                'utilisateur' => ['ROLE_USER'],
                default => ['ROLE_USER'], // rôle par défaut si inconnu
            };
        }
        
    

    public function eraseCredentials(): void
    {
        $this->plainPassword = null;
    }

    public function isVerified(): bool
    {
        return $this->isVerified;
    }

    public function setIsVerified(bool $isVerified): self
    {
        $this->isVerified = $isVerified;
        return $this;
    }

    public function getEquipesJoueur1(): Collection { return $this->equipesJoueur1; }
    public function getEquipesJoueur2(): Collection { return $this->equipesJoueur2; }



}
