<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use App\Repository\ReservationRepository;

#[ORM\Entity(repositoryClass: ReservationRepository::class)]
#[ORM\Table(name: 'reservation')]
class Reservation
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $id_R = null;

    #[ORM\Column(type: 'integer', nullable: false)]
    private ?int $nombre_places = null;

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $type_reservation = null;

    #[ORM\Column(type: 'integer', nullable: false)]
    private ?int $code_confirmation = null;

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $remarque = null;

    #[ORM\Column(type: 'string', nullable: true)]
    private ?string $nom = null;

    #[ORM\OneToMany(targetEntity: Paiement::class, mappedBy: 'reservation', cascade: ['persist', 'remove'])]
    private Collection $paiements;

    public function __construct()
    {
        $this->paiements = new ArrayCollection();
    }

    public function getId_R(): ?int
    {
        return $this->id_R;
    }

    public function setId_R(int $id_R): self
    {
        $this->id_R = $id_R;
        return $this;
    }

    public function getNombre_places(): ?int
    {
        return $this->nombre_places;
    }

    public function setNombre_places(int $nombre_places): self
    {
        $this->nombre_places = $nombre_places;
        return $this;
    }

    public function getType_reservation(): ?string
    {
        return $this->type_reservation;
    }

    public function setType_reservation(string $type_reservation): self
    {
        $this->type_reservation = $type_reservation;
        return $this;
    }

    public function getCode_confirmation(): ?int
    {
        return $this->code_confirmation;
    }

    public function setCode_confirmation(int $code_confirmation): self
    {
        $this->code_confirmation = $code_confirmation;
        return $this;
    }

    public function getRemarque(): ?string
    {
        return $this->remarque;
    }

    public function setRemarque(string $remarque): self
    {
        $this->remarque = $remarque;
        return $this;
    }

    public function getNom(): ?string
    {
        return $this->nom;
    }

    public function setNom(?string $nom): self
    {
        $this->nom = $nom;
        return $this;
    }

    /**
     * @return Collection<int, Paiement>
     */
    public function getPaiements(): Collection
    {
        return $this->paiements;
    }

    public function addPaiement(Paiement $paiement): self
    {
        if (!$this->paiements->contains($paiement)) {
            $this->paiements[] = $paiement;
            $paiement->setReservation($this); // Lier la réservation au paiement
        }
        return $this;
    }

    public function removePaiement(Paiement $paiement): self
    {
        if ($this->paiements->removeElement($paiement)) {
            // On dissocie la réservation du paiement
            if ($paiement->getReservation() === $this) {
                $paiement->setReservation(null);
            }
        }
        return $this;
    }
}
