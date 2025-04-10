<?php

namespace App\Repository;

use App\Entity\AnnonceMatch;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<AnnonceMatch>
 */
class AnnonceMatchRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, AnnonceMatch::class);
    }

    // Add your custom repository methods here
} 