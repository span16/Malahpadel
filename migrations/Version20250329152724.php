<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20250329152724 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql(<<<'SQL'
            CREATE TABLE annoncematch (annonce_id INT AUTO_INCREMENT NOT NULL, titre VARCHAR(255) NOT NULL, date_heure DATE NOT NULL, lieu VARCHAR(255) DEFAULT NULL, joueurs_recherches INT NOT NULL, niveau VARCHAR(255) NOT NULL, description VARCHAR(255) NOT NULL, PRIMARY KEY(annonce_id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB
        SQL);
        $this->addSql(<<<'SQL'
            CREATE TABLE compagne (id INT AUTO_INCREMENT NOT NULL, id_produit INT NOT NULL, nom_sponsor VARCHAR(255) NOT NULL, tarifs DOUBLE PRECISION NOT NULL, date_debut DATE NOT NULL, date_fin DATE NOT NULL, logo_compagne VARCHAR(255) NOT NULL, status VARCHAR(50) NOT NULL, type_marketing VARCHAR(100) NOT NULL, INDEX IDX_3A4264BF7384557 (id_produit), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB
        SQL);
        $this->addSql(<<<'SQL'
            CREATE TABLE emploidutemps (id_emplois INT AUTO_INCREMENT NOT NULL, date DATE NOT NULL, partie INT NOT NULL, id_Événement INT NOT NULL, id_equipe INT NOT NULL, equipe_2 INT NOT NULL, google_event_id VARCHAR(255) DEFAULT NULL, PRIMARY KEY(id_emplois)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB
        SQL);
        $this->addSql(<<<'SQL'
            CREATE TABLE equipes (equipe_id INT AUTO_INCREMENT NOT NULL, nom_equipe VARCHAR(255) NOT NULL, email_joueur1 VARCHAR(255) NOT NULL, email_joueur2 VARCHAR(255) NOT NULL, PRIMARY KEY(equipe_id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB
        SQL);
        $this->addSql(<<<'SQL'
            CREATE TABLE evenement (id INT AUTO_INCREMENT NOT NULL, nom VARCHAR(255) NOT NULL, type VARCHAR(255) NOT NULL, date DATE NOT NULL, terrain_id INT DEFAULT NULL, image_url VARCHAR(255) NOT NULL, PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB
        SQL);
        $this->addSql(<<<'SQL'
            CREATE TABLE invitation (id INT AUTO_INCREMENT NOT NULL, sender_id INT NOT NULL, receiver_id INT NOT NULL, date_envoi DATE NOT NULL, statut VARCHAR(255) NOT NULL, PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB
        SQL);
        $this->addSql(<<<'SQL'
            CREATE TABLE loginhistory (id INT AUTO_INCREMENT NOT NULL, user_id INT DEFAULT NULL, login_time DATETIME NOT NULL, ip_address VARCHAR(255) DEFAULT NULL, device_info VARCHAR(255) DEFAULT NULL, INDEX IDX_29EDB052A76ED395 (user_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB
        SQL);
        $this->addSql(<<<'SQL'
            CREATE TABLE paiement (id INT AUTO_INCREMENT NOT NULL, methode_paiement VARCHAR(255) NOT NULL, commission VARCHAR(255) NOT NULL, description_paiement VARCHAR(255) NOT NULL, devise VARCHAR(255) NOT NULL, id_R INT DEFAULT NULL, INDEX IDX_B1DC7A1E90F2752A (id_R), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB
        SQL);
        $this->addSql(<<<'SQL'
            CREATE TABLE produit (id_produit INT AUTO_INCREMENT NOT NULL, nom_produit VARCHAR(255) NOT NULL, categorie VARCHAR(255) NOT NULL, prix NUMERIC(10, 0) NOT NULL, stock INT NOT NULL, description VARCHAR(255) NOT NULL, image_produit VARCHAR(255) NOT NULL, PRIMARY KEY(id_produit)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB
        SQL);
        $this->addSql(<<<'SQL'
            CREATE TABLE profil (id INT AUTO_INCREMENT NOT NULL, id_user INT DEFAULT NULL, avatar VARCHAR(255) DEFAULT NULL, bio LONGTEXT DEFAULT NULL, preferences VARCHAR(255) DEFAULT NULL, PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB
        SQL);
        $this->addSql(<<<'SQL'
            CREATE TABLE recherche (user_id INT AUTO_INCREMENT NOT NULL, nom VARCHAR(255) NOT NULL, niveau VARCHAR(255) NOT NULL, annonces VARCHAR(255) NOT NULL, PRIMARY KEY(user_id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB
        SQL);
        $this->addSql(<<<'SQL'
            CREATE TABLE reservation (id_r INT AUTO_INCREMENT NOT NULL, nombre_places INT NOT NULL, type_reservation VARCHAR(255) NOT NULL, code_confirmation INT NOT NULL, remarque VARCHAR(255) NOT NULL, nom VARCHAR(255) DEFAULT NULL, PRIMARY KEY(id_r)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB
        SQL);
        $this->addSql(<<<'SQL'
            CREATE TABLE terrain (id INT AUTO_INCREMENT NOT NULL, nom VARCHAR(255) NOT NULL, adresse VARCHAR(255) NOT NULL, prix_par_personne NUMERIC(10, 0) NOT NULL, heure_ouverture TIME NOT NULL, heure_fermeture TIME NOT NULL, PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB
        SQL);
        $this->addSql(<<<'SQL'
            CREATE TABLE user (id INT AUTO_INCREMENT NOT NULL, nom VARCHAR(255) NOT NULL, prenom VARCHAR(255) NOT NULL, age INT NOT NULL, email VARCHAR(255) NOT NULL, mdp VARCHAR(255) NOT NULL, cin INT NOT NULL, etat VARCHAR(255) NOT NULL, fonction VARCHAR(255) NOT NULL, reset_token VARCHAR(255) DEFAULT NULL, token_expiration DATETIME DEFAULT NULL, is_verified TINYINT(1) NOT NULL, UNIQUE INDEX UNIQ_8D93D649E7927C74 (email), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB
        SQL);
        $this->addSql(<<<'SQL'
            ALTER TABLE compagne ADD CONSTRAINT FK_3A4264BF7384557 FOREIGN KEY (id_produit) REFERENCES produit (id_produit) ON DELETE RESTRICT
        SQL);
        $this->addSql(<<<'SQL'
            ALTER TABLE loginhistory ADD CONSTRAINT FK_29EDB052A76ED395 FOREIGN KEY (user_id) REFERENCES user (id)
        SQL);
        $this->addSql(<<<'SQL'
            ALTER TABLE paiement ADD CONSTRAINT FK_B1DC7A1E90F2752A FOREIGN KEY (id_R) REFERENCES reservation (id_R)
        SQL);
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql(<<<'SQL'
            ALTER TABLE compagne DROP FOREIGN KEY FK_3A4264BF7384557
        SQL);
        $this->addSql(<<<'SQL'
            ALTER TABLE loginhistory DROP FOREIGN KEY FK_29EDB052A76ED395
        SQL);
        $this->addSql(<<<'SQL'
            ALTER TABLE paiement DROP FOREIGN KEY FK_B1DC7A1E90F2752A
        SQL);
        $this->addSql(<<<'SQL'
            DROP TABLE annoncematch
        SQL);
        $this->addSql(<<<'SQL'
            DROP TABLE compagne
        SQL);
        $this->addSql(<<<'SQL'
            DROP TABLE emploidutemps
        SQL);
        $this->addSql(<<<'SQL'
            DROP TABLE equipes
        SQL);
        $this->addSql(<<<'SQL'
            DROP TABLE evenement
        SQL);
        $this->addSql(<<<'SQL'
            DROP TABLE invitation
        SQL);
        $this->addSql(<<<'SQL'
            DROP TABLE loginhistory
        SQL);
        $this->addSql(<<<'SQL'
            DROP TABLE paiement
        SQL);
        $this->addSql(<<<'SQL'
            DROP TABLE produit
        SQL);
        $this->addSql(<<<'SQL'
            DROP TABLE profil
        SQL);
        $this->addSql(<<<'SQL'
            DROP TABLE recherche
        SQL);
        $this->addSql(<<<'SQL'
            DROP TABLE reservation
        SQL);
        $this->addSql(<<<'SQL'
            DROP TABLE terrain
        SQL);
        $this->addSql(<<<'SQL'
            DROP TABLE user
        SQL);
    }
}
