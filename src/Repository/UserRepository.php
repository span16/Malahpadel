<?php

// src/Repository/UserRepository.php

namespace App\Repository;

use App\Entity\User;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class UserRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, User::class);
    }

    // Ajouter cette méthode pour vérifier l'unicité de l'email
    public function findByEmail(string $email): ?User
    {
        return $this->findOneBy(['email' => $email]);
    }
     /**
     * Retourne les utilisateurs qui ne sont dans aucune équipe.
     */
    public function findUsersNotInEquipes(): array
    {
        return $this->createQueryBuilder('u')
            ->leftJoin('u.equipesJoueur1', 'eq1')
            ->leftJoin('u.equipesJoueur2', 'eq2')
            ->where('eq1 IS NULL')
            ->andWhere('eq2 IS NULL')
            ->getQuery()
            ->getResult();
    }
}

