<?php
// src/Entity/Session.php
namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * @ORM\Entity
 * @ORM\Table(name="sessions")
 */
class Session
{
    /**
     * @ORM\Id
     * @ORM\Column(type="string", length=255)
     */
    private $id;

    /**
     * @ORM\Column(type="blob")
     */
    private $data;

    /**
     * @ORM\Column(type="integer")
     */
    private $lifetime;

    /**
     * @ORM\Column(type="integer")
     */
    private $time;

    // Getters and setters...
}
