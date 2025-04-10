<?php

namespace App\Repository;

use App\Entity\Compagne;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class CompagneRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Compagne::class);
    }
    public function findByFilters(array $filters)
    {
        $qb = $this->createQueryBuilder('c');
    
        if (!empty($filters['status'])) {
            $qb->andWhere('c.status = :status')
               ->setParameter('status', $filters['status']);
        }
    
        if (!empty($filters['type'])) {
            $qb->andWhere('c.typeMarketing = :type')
               ->setParameter('type', $filters['type']);
        }
    
        if (!empty($filters['search'])) {
            $qb->andWhere('c.nomSponsor LIKE :search')
               ->setParameter('search', '%'.$filters['search'].'%');
        }
    
        // Tri par tarifs (attention au nom du champ qui est tarifs au pluriel)
        $sort = $filters['sort'] ?? 'tarifs_desc';
        if ($sort === 'tarifs_asc') {
            $qb->orderBy('c.tarifs', 'ASC');
        } else {
            $qb->orderBy('c.tarifs', 'DESC');
        }
    
        return $qb->getQuery()->getResult();
    }
}