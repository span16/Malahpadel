<?php

namespace App\Repository;

use App\Entity\LikeDislike;
use App\Entity\Evenement;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class LikeDislikeRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, LikeDislike::class);
    }

    public function countLikes(Evenement $evenement): int
    {
        return (int) $this->createQueryBuilder('l')
            ->select('COUNT(l.id)')
            ->where('l.isLike = true')
            ->andWhere('l.evenement = :evenement')
            ->setParameter('evenement', $evenement)
            ->getQuery()
            ->getSingleScalarResult();
    }

    public function getAverageRating(Evenement $evenement): float
    {
        return (float) $this->createQueryBuilder('l')
            ->select('AVG(l.rating)')
            ->where('l.evenement = :evenement')
            ->setParameter('evenement', $evenement)
            ->getQuery()
            ->getSingleScalarResult();
    }
    // src/Repository/LikeDislikeRepository.php

public function getStatsByEvenement(int $evenementId): array
{
    $qb = $this->createQueryBuilder('l')
        ->select('COUNT(l.id) as total', 'AVG(l.rating) as moyenne')
        ->andWhere('l.evenement = :evenementId')
        ->setParameter('evenementId', $evenementId);

    return $qb->getQuery()->getOneOrNullResult();
}

}
