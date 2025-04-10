<?php

namespace App\Repository;

use App\Entity\Emploidutemps;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class EmploidutempsRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Emploidutemps::class);
    }

    // Add custom methods as needed
}