<?php

namespace App\Repository;

use App\Entity\Equipes;
use App\Entity\User;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class EquipesRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Equipes::class);
    }

    public function joueurDejaDansEquipe(User $user): bool
    {
        $qb = $this->createQueryBuilder('e');
        $qb->where('e.joueur1 = :user OR e.joueur2 = :user')
           ->setParameter('user', $user);

        return (count($qb->getQuery()->getResult()) > 0);
    }
}
