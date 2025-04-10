<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20250410001311 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql(<<<'SQL'
            CREATE TABLE emploidutemps (id INT AUTO_INCREMENT NOT NULL, evenement_id INT DEFAULT NULL, equipe1_id INT DEFAULT NULL, equipe2_id INT DEFAULT NULL, date DATE NOT NULL, partie INT NOT NULL, google_event_id VARCHAR(500) NOT NULL, INDEX IDX_5152B578FD02F13 (evenement_id), INDEX IDX_5152B5784265900C (equipe1_id), INDEX IDX_5152B57850D03FE2 (equipe2_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB
        SQL);
        $this->addSql(<<<'SQL'
            ALTER TABLE emploidutemps ADD CONSTRAINT FK_5152B578FD02F13 FOREIGN KEY (evenement_id) REFERENCES evenement (id) ON DELETE CASCADE
        SQL);
        $this->addSql(<<<'SQL'
            ALTER TABLE emploidutemps ADD CONSTRAINT FK_5152B5784265900C FOREIGN KEY (equipe1_id) REFERENCES equipes (equipe_id) ON DELETE CASCADE
        SQL);
        $this->addSql(<<<'SQL'
            ALTER TABLE emploidutemps ADD CONSTRAINT FK_5152B57850D03FE2 FOREIGN KEY (equipe2_id) REFERENCES equipes (equipe_id) ON DELETE CASCADE
        SQL);
        $this->addSql(<<<'SQL'
            ALTER TABLE compagne CHANGE status status ENUM('active', 'inactive', 'pending')
        SQL);
        $this->addSql(<<<'SQL'
            ALTER TABLE like_dislike ADD CONSTRAINT FK_ADB6A689FD02F13 FOREIGN KEY (evenement_id) REFERENCES evenement (id)
        SQL);
        $this->addSql(<<<'SQL'
            CREATE UNIQUE INDEX UNIQ_29A5EC27DC2AE7EF ON produit (nom_produit)
        SQL);
        $this->addSql(<<<'SQL'
            ALTER TABLE terrain CHANGE prix_par_personne prix_par_personne DOUBLE PRECISION NOT NULL, CHANGE heure_ouverture heure_ouverture VARCHAR(255) NOT NULL, CHANGE heure_fermeture heure_fermeture VARCHAR(255) NOT NULL
        SQL);
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql(<<<'SQL'
            ALTER TABLE emploidutemps DROP FOREIGN KEY FK_5152B578FD02F13
        SQL);
        $this->addSql(<<<'SQL'
            ALTER TABLE emploidutemps DROP FOREIGN KEY FK_5152B5784265900C
        SQL);
        $this->addSql(<<<'SQL'
            ALTER TABLE emploidutemps DROP FOREIGN KEY FK_5152B57850D03FE2
        SQL);
        $this->addSql(<<<'SQL'
            DROP TABLE emploidutemps
        SQL);
        $this->addSql(<<<'SQL'
            ALTER TABLE compagne CHANGE status status VARCHAR(255) DEFAULT NULL
        SQL);
        $this->addSql(<<<'SQL'
            ALTER TABLE like_dislike DROP FOREIGN KEY FK_ADB6A689FD02F13
        SQL);
        $this->addSql(<<<'SQL'
            DROP INDEX UNIQ_29A5EC27DC2AE7EF ON produit
        SQL);
        $this->addSql(<<<'SQL'
            ALTER TABLE terrain CHANGE prix_par_personne prix_par_personne NUMERIC(10, 0) NOT NULL, CHANGE heure_ouverture heure_ouverture TIME NOT NULL, CHANGE heure_fermeture heure_fermeture TIME NOT NULL
        SQL);
    }
}
