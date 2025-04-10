<?php

namespace App\Repository;

use App\Entity\Reclamation;
use App\Entity\Reservation;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Reclamation>
 *
 * @method Reclamation|null find($id, $lockMode = null, $lockVersion = null)
 * @method Reclamation|null findOneBy(array $criteria, array $orderBy = null)
 * @method Reclamation[]    findAll()
 * @method Reclamation[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class ReclamationRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Reclamation::class);
    }

    public function save(Reclamation $entity, bool $flush = false): void
    {
        $this->getEntityManager()->persist($entity);

        if ($flush) {
            $this->getEntityManager()->flush();
        }
    }

    public function remove(Reclamation $entity, bool $flush = false): void
    {
        $this->getEntityManager()->remove($entity);

        if ($flush) {
            $this->getEntityManager()->flush();
        }
    }

    /**
     * Récupère toutes les réclamations triées par date (plus récentes d'abord)
     */
    public function findAllOrderedByDate(): array
    {
        return $this->createQueryBuilder('r')
            ->orderBy('r.date', 'DESC')
            ->getQuery()
            ->getResult();
    }

    /**
     * Trouve les réclamations par réservation
     */
    public function findByReservation(Reservation $reservation): array
    {
        return $this->createQueryBuilder('r')
            ->andWhere('r.reservation = :reservation')
            ->setParameter('reservation', $reservation)
            ->orderBy('r.date', 'DESC')
            ->getQuery()
            ->getResult();
    }

    /**
     * Trouve les réclamations par état
     */
    public function findByEtat(string $etat): array
    {
        return $this->createQueryBuilder('r')
            ->andWhere('r.etat = :etat')
            ->setParameter('etat', $etat)
            ->orderBy('r.date', 'DESC')
            ->getQuery()
            ->getResult();
    }

    /**
     * Compte le nombre de réclamations par état
     */
    public function countByEtat(): array
    {
        return $this->createQueryBuilder('r')
            ->select('r.etat, COUNT(r.id_reclamation) as count')
            ->groupBy('r.etat')
            ->getQuery()
            ->getResult();
    }

    /**
     * Trouve les réclamations entre deux dates
     */
    public function findBetweenDates(\DateTimeInterface $startDate, \DateTimeInterface $endDate): array
    {
        return $this->createQueryBuilder('r')
            ->andWhere('r.date BETWEEN :start AND :end')
            ->setParameter('start', $startDate)
            ->setParameter('end', $endDate)
            ->orderBy('r.date', 'DESC')
            ->getQuery()
            ->getResult();
    }

    /**
     * Trouve la dernière réclamation pour une réservation
     */
    public function findLastForReservation(Reservation $reservation): ?Reclamation
    {
        return $this->createQueryBuilder('r')
            ->andWhere('r.reservation = :reservation')
            ->setParameter('reservation', $reservation)
            ->orderBy('r.date', 'DESC')
            ->setMaxResults(1)
            ->getQuery()
            ->getOneOrNullResult();
    }
}