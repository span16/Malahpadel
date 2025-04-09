<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use App\Repository\PaiementRepository;

#[ORM\Entity(repositoryClass: PaiementRepository::class)]
#[ORM\Table(name: 'paiement')]
class Paiement
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $id = null;

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $methode_Paiement = null;

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $commission = null;

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $description_Paiement = null;

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $devise = null;

    #[ORM\ManyToOne(targetEntity: Reservation::class, inversedBy: 'paiements')]
    #[ORM\JoinColumn(name: 'id_R', referencedColumnName: 'id_R')]
    private ?Reservation $reservation = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getMethode_Paiement(): ?string
    {
        return $this->methode_Paiement;
    }

    public function setMethode_Paiement(string $methode_Paiement): self
    {
        $this->methode_Paiement = $methode_Paiement;
        return $this;
    }

    public function getCommission(): ?string
    {
        return $this->commission;
    }

    public function setCommission(string $commission): self
    {
        $this->commission = $commission;
        return $this;
    }

    public function getDescription_Paiement(): ?string
    {
        return $this->description_Paiement;
    }

    public function setDescription_Paiement(string $description_Paiement): self
    {
        $this->description_Paiement = $description_Paiement;
        return $this;
    }

    public function getDevise(): ?string
    {
        return $this->devise;
    }

    public function setDevise(string $devise): self
    {
        $this->devise = $devise;
        return $this;
    }

    public function getReservation(): ?Reservation
    {
        return $this->reservation;
    }

    public function setReservation(?Reservation $reservation): self
    {
        $this->reservation = $reservation;
        return $this;
    }
}
