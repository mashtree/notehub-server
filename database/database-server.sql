-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.7.14 - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL Version:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for notehub
CREATE DATABASE IF NOT EXISTS `notehub` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `notehub`;

-- Dumping structure for table notehub.notes
CREATE TABLE IF NOT EXISTS `notes` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `note_title` varchar(255) NOT NULL,
  `description` text,
  `owner` int(11) unsigned NOT NULL,
  `content` longtext,
  `institution` varchar(255) DEFAULT NULL,
  `institution_address` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_notes_users` (`owner`),
  CONSTRAINT `FK_note_user` FOREIGN KEY (`owner`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.
-- Dumping structure for table notehub.note_change
CREATE TABLE IF NOT EXISTS `note_change` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `version` int(10) unsigned NOT NULL DEFAULT '1',
  `id_user` int(10) unsigned DEFAULT NULL,
  `id_note` int(10) unsigned DEFAULT NULL,
  `change_type` varchar(50) DEFAULT NULL,
  `row_change` int(11) DEFAULT NULL,
  `old` longtext,
  `new` longtext,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_note_change_users` (`id_user`),
  KEY `FK_note_change_notes` (`id_note`),
  CONSTRAINT `FK_notechange_note` FOREIGN KEY (`id_note`) REFERENCES `notes` (`id`),
  CONSTRAINT `FK_notechange_user` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.
-- Dumping structure for table notehub.roles
CREATE TABLE IF NOT EXISTS `roles` (
  `id` int(10) unsigned NOT NULL,
  `role_name` varchar(100) DEFAULT NULL,
  `description` text,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.
-- Dumping structure for table notehub.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `password` varchar(200) NOT NULL,
  `plain_password` varchar(200) NOT NULL,
  `email` varchar(200) DEFAULT NULL,
  `last_connect` datetime DEFAULT NULL,
  `ip_address` varchar(20) DEFAULT '0.0.0.0',
  `remember_token` varchar(20) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15559 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.
-- Dumping structure for table notehub.users_to_notes
CREATE TABLE IF NOT EXISTS `users_to_notes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_user` int(11) unsigned DEFAULT NULL,
  `id_note` int(11) unsigned DEFAULT NULL,
  `role` int(11) unsigned DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_users_to_notes_users` (`id_user`),
  KEY `FK_users_to_notes_notes` (`id_note`),
  KEY `FK_users_to_notes_roles` (`role`),
  CONSTRAINT `FK_usertonote_note` FOREIGN KEY (`id_note`) REFERENCES `notes` (`id`),
  CONSTRAINT `FK_usertonote_role` FOREIGN KEY (`role`) REFERENCES `roles` (`id`),
  CONSTRAINT `FK_usertonote_user` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
