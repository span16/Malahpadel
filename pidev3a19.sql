-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : mer. 12 fév. 2025 à 23:33
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `pidev3a19`
--

-- --------------------------------------------------------

--
-- Structure de la table `campagne_produit`
--

CREATE TABLE `campagne_produit` (
  `id_compagne` int(11) NOT NULL,
  `id_produit` int(11) NOT NULL,
  `quantite` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `compagne`
--

CREATE TABLE `compagne` (
  `id_compagne` int(11) NOT NULL,
  `nom_sponsor` varchar(30) NOT NULL,
  `tarifs` float NOT NULL,
  `date_debut` date NOT NULL,
  `date_fin` date NOT NULL,
  `logo_compagne` varchar(255) NOT NULL,
  `status` enum('active','inactive','pending') NOT NULL,
  `TypeMarketing` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `compagne`
--

INSERT INTO `compagne` (`id_compagne`, `nom_sponsor`, `tarifs`, `date_debut`, `date_fin`, `logo_compagne`, `status`, `TypeMarketing`) VALUES
(2, 'sweettotes', 700, '2024-01-01', '2024-12-31', 'sweettotes.png', 'pending', 'digital'),
(3, 'sugarbaby', 200, '2024-01-01', '2024-12-31', 'sugarbaby.png', 'inactive', 'digital'),
(10, 'sugarbaby', 200, '2024-01-01', '2024-12-31', 'sugarbaby.png', 'inactive', 'digital'),
(11, 'sugarbaby', 200, '2024-01-01', '2024-12-31', 'sugarbaby.png', 'inactive', 'digital'),
(12, 'riovaciar', 500, '2003-08-11', '2025-05-11', 'rio.png', 'active', 'ads'),
(13, 'riovaciar', 500, '2003-08-11', '2025-05-11', 'rio.png', 'active', 'ads');

-- --------------------------------------------------------

--
-- Structure de la table `paiement`
--

CREATE TABLE `paiement` (
  `id_P` int(11) NOT NULL,
  `id_reservation` int(11) NOT NULL,
  `montant` float NOT NULL,
  `status_P` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `produit`
--

CREATE TABLE `produit` (
  `id_produit` int(11) NOT NULL,
  `nom_produit` varchar(25) NOT NULL,
  `categorie` varchar(25) NOT NULL,
  `prix` decimal(10,2) NOT NULL,
  `stock` int(11) NOT NULL,
  `description` varchar(255) NOT NULL,
  `image_produit` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `produit`
--

INSERT INTO `produit` (`id_produit`, `nom_produit`, `categorie`, `prix`, `stock`, `description`, `image_produit`) VALUES
(8, 'vanilla\'s', 'trousse makeup', 30.00, 40, 'makeup kit', 'vanilla.jpg');

-- --------------------------------------------------------

--
-- Structure de la table `profil`
--

CREATE TABLE `profil` (
  `id` int(11) NOT NULL,
  `id_user` int(11) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `bio` text DEFAULT NULL,
  `preferences` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `profil`
--

INSERT INTO `profil` (`id`, `id_user`, `avatar`, `bio`, `preferences`) VALUES
(18, 124, 'hxhshshs', 'jhhhhhhhhhhh', 'jjjjjjjjjj'),
(19, 125, 'yzyz', 'dgdgd', 'dgdg');

-- --------------------------------------------------------

--
-- Structure de la table `reservation`
--

CREATE TABLE `reservation` (
  `id` int(11) NOT NULL,
  `nomC` varchar(25) NOT NULL,
  `email` varchar(25) NOT NULL,
  `dateR` date NOT NULL,
  `status` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `terrain`
--

CREATE TABLE `terrain` (
  `id` int(11) NOT NULL,
  `nom` varchar(255) NOT NULL,
  `adresse` varchar(255) NOT NULL,
  `prix_par_personne` decimal(10,2) NOT NULL,
  `heure_ouverture` time NOT NULL,
  `heure_fermeture` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `terrain`
--

INSERT INTO `terrain` (`id`, `nom`, `adresse`, `prix_par_personne`, `heure_ouverture`, `heure_fermeture`) VALUES
(1, 'uuuuuuuu', 'wwa', 20.00, '04:04:04', '08:08:08'),
(2, '[value-2]', '[value-3]', 0.00, '00:00:00', '00:00:00'),
(3, 'llll', 'llk', 20.00, '20:20:20', '14:14:20'),
(4, 'mmmm', 'rets', 25.00, '20:20:20', '20:14:14');

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `nom` varchar(50) NOT NULL,
  `prenom` varchar(50) NOT NULL,
  `age` int(11) NOT NULL,
  `email` varchar(50) NOT NULL,
  `mdp` varchar(50) NOT NULL,
  `cin` int(11) NOT NULL,
  `etat` varchar(50) NOT NULL,
  `fonction` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`id`, `nom`, `prenom`, `age`, `email`, `mdp`, `cin`, `etat`, `fonction`) VALUES
(122, '[value-2]', '[value-3]', 0, '[value-5]', '[value-6]', 0, '[value-8]', '[value-9]'),
(123, 'yassmine', 'pppp', 25, 'jj@email.com', '555', 888, 'actif', 'admin'),
(124, 'ii', 'ninou', 30, 'hdhdgdgdgdgdgdg@email.com', '9999999', 666, 'actif', 'admin'),
(125, 'NouveauNom', 'NouveauPrenom', 30, 'nouveau@email.com', 'newpass', 12345678, 'actif', 'admin'),
(126, 'yassmine', 'pppp', 25, 'jj@email.com', '555', 888, 'actif', 'admin'),
(127, 'ii', 'ninou', 30, 'hdhdgdgdgdgdgdg@email.com', '9999999', 666, 'actif', 'admin'),
(128, 'NouveauNom', 'NouveauPrenom', 30, 'nouveau@email.com', 'newpass', 12345678, 'actif', 'admin'),
(129, 'yassmine', 'pppp', 25, 'jj@email.com', '555', 888, 'actif', 'admin'),
(130, 'ii', 'ninou', 30, 'hdhdgdgdgdgdgdg@email.com', '9999999', 666, 'actif', 'admin'),
(131, 'NouveauNom', 'NouveauPrenom', 30, 'nouveau@email.com', 'newpass', 12345678, 'actif', 'admin'),
(132, 'yassmine', 'pppp', 25, 'jj@email.com', '555', 888, 'actif', 'admin'),
(133, 'ii', 'ninou', 30, 'hdhdgdgdgdgdgdg@email.com', '9999999', 666, 'actif', 'admin'),
(134, 'NouveauNom', 'NouveauPrenom', 30, 'nouveau@email.com', 'newpass', 12345678, 'actif', 'admin'),
(135, 'yassmine', 'pppp', 25, 'jj@email.com', '555', 888, 'actif', 'admin'),
(136, 'ii', 'ninou', 30, 'hdhdgdgdgdgdgdg@email.com', '9999999', 666, 'actif', 'admin'),
(137, 'NouveauNom', 'NouveauPrenom', 30, 'nouveau@email.com', 'newpass', 12345678, 'actif', 'admin'),
(138, 'yassmine', 'pppp', 25, 'jj@email.com', '555', 888, 'actif', 'admin'),
(139, 'ii', 'ninou', 30, 'hdhdgdgdgdgdgdg@email.com', '9999999', 666, 'actif', 'admin'),
(140, 'NouveauNom', 'NouveauPrenom', 30, 'nouveau@email.com', 'newpass', 12345678, 'actif', 'admin'),
(141, 'yassmine', 'pppp', 25, 'jj@email.com', '555', 888, 'actif', 'admin'),
(142, 'ii', 'ninou', 30, 'hdhdgdgdgdgdgdg@email.com', '9999999', 666, 'actif', 'admin'),
(143, 'NouveauNom', 'NouveauPrenom', 30, 'nouveau@email.com', 'newpass', 12345678, 'actif', 'admin'),
(144, 'yassmine', 'pppp', 25, 'jj@email.com', '555', 888, 'actif', 'admin'),
(145, 'ii', 'ninou', 30, 'hdhdgdgdgdgdgdg@email.com', '9999999', 666, 'actif', 'admin'),
(146, 'walou', 'mimi', 55, 'oula@gmail.com', 'yass11', 44, 'yassou', 'hh'),
(147, 'yassmine', 'pppp', 25, 'jj@email.com', '555', 888, 'actif', 'admin'),
(148, 'ii', 'ninou', 30, 'hdhdgdgdgdgdgdg@email.com', '9999999', 666, 'actif', 'admin'),
(149, 'walou', 'mimi', 55, 'oula@gmail.com', 'yass11', 44, 'yassou', 'hh'),
(150, 'yassmine', 'pppp', 25, 'jj@email.com', '555', 888, 'actif', 'admin'),
(151, 'ii', 'ninou', 30, 'hdhdgdgdgdgdgdg@email.com', '9999999', 666, 'actif', 'admin'),
(153, 'yassmo', 'uuu', 21, 'yasss@gmail.', 'iii', 21, 'yass', 'yass');

-- --------------------------------------------------------

--
-- Structure de la table `événement`
--

CREATE TABLE `événement` (
  `id` int(11) NOT NULL,
  `nom` varchar(50) NOT NULL,
  `type` enum('TOURNOIS','MATCH') NOT NULL,
  `date` date NOT NULL DEFAULT current_timestamp(),
  `terrain_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `événement`
--

INSERT INTO `événement` (`id`, `nom`, `type`, `date`, `terrain_id`) VALUES
(2, 'Conee', 'MATCH', '2025-02-12', NULL),
(3, 'Conee', 'MATCH', '2025-02-12', NULL),
(6, 'Conee', 'MATCH', '2025-02-12', NULL),
(23, 'WASIM', 'MATCH', '2025-02-12', NULL),
(24, 'WASIM', 'MATCH', '2025-02-12', NULL),
(25, 'YASSMINE', 'MATCH', '2025-02-12', NULL),
(27, 'DOFUS', 'MATCH', '2025-02-12', NULL),
(29, 'zaar', 'MATCH', '2025-02-12', NULL),
(30, 'CHAMES', 'TOURNOIS', '2025-02-12', NULL),
(34, 'MARSAFOLLE', 'MATCH', '2025-05-22', 1);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `campagne_produit`
--
ALTER TABLE `campagne_produit`
  ADD PRIMARY KEY (`id_compagne`,`id_produit`),
  ADD KEY `id_produit` (`id_produit`);

--
-- Index pour la table `compagne`
--
ALTER TABLE `compagne`
  ADD PRIMARY KEY (`id_compagne`);

--
-- Index pour la table `paiement`
--
ALTER TABLE `paiement`
  ADD PRIMARY KEY (`id_P`);

--
-- Index pour la table `produit`
--
ALTER TABLE `produit`
  ADD PRIMARY KEY (`id_produit`);

--
-- Index pour la table `profil`
--
ALTER TABLE `profil`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_user` (`id_user`);

--
-- Index pour la table `reservation`
--
ALTER TABLE `reservation`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `terrain`
--
ALTER TABLE `terrain`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `événement`
--
ALTER TABLE `événement`
  ADD PRIMARY KEY (`id`),
  ADD KEY `terrain_id` (`terrain_id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `compagne`
--
ALTER TABLE `compagne`
  MODIFY `id_compagne` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT pour la table `produit`
--
ALTER TABLE `produit`
  MODIFY `id_produit` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT pour la table `profil`
--
ALTER TABLE `profil`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT pour la table `terrain`
--
ALTER TABLE `terrain`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=154;

--
-- AUTO_INCREMENT pour la table `événement`
--
ALTER TABLE `événement`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `campagne_produit`
--
ALTER TABLE `campagne_produit`
  ADD CONSTRAINT `campagne_produit_ibfk_1` FOREIGN KEY (`id_compagne`) REFERENCES `compagne` (`id_compagne`),
  ADD CONSTRAINT `campagne_produit_ibfk_2` FOREIGN KEY (`id_produit`) REFERENCES `produit` (`id_produit`);

--
-- Contraintes pour la table `paiement`
--
ALTER TABLE `paiement`
  ADD CONSTRAINT `paiement_ibfk_1` FOREIGN KEY (`id_P`) REFERENCES `reservation` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `profil`
--
ALTER TABLE `profil`
  ADD CONSTRAINT `profil_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `événement`
--
ALTER TABLE `événement`
  ADD CONSTRAINT `événement_ibfk_1` FOREIGN KEY (`terrain_id`) REFERENCES `terrain` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
