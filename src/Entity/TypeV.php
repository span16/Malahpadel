<?php
namespace App\Entity;

enum TypeV: string
{
    case TOURNOIS = 'TOURNOIS';
    case MATCH = 'MATCH';
    case AUTRE = 'AUTRE';
}
