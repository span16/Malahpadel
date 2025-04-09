<?php
// src/Service/RecaptchaService.php

namespace App\Service;

use Symfony\Contracts\HttpClient\HttpClientInterface;

class RecaptchaService
{
    private $client;
    private $secretKey;

    public function __construct(HttpClientInterface $client, string $secretKey)
    {
        $this->client = $client;
        $this->secretKey = $secretKey;
    }

    public function verify(string $recaptchaResponse, string $clientIp): bool
    {
        $response = $this->client->request('POST', 'https://www.google.com/recaptcha/api/siteverify', [
            'headers' => [
                'Content-Type' => 'application/x-www-form-urlencoded',
            ],
            'body' => [
                'secret' => $this->secretKey,
                'response' => $recaptchaResponse,
                'remoteip' => $clientIp,
            ],
        ]);

        $data = $response->toArray();
        return $data['success'] ?? false;
    }
}
