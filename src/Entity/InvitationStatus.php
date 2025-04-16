<?php

namespace App\Entity;

enum InvitationStatus: string
{
    case EN_ATTENTE = 'en attente';
    case ACCEPTEE = 'acceptée';
    case REFUSEE = 'refusée';
} 