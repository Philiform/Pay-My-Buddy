/* BASE DE DONNEES MYSQL 8 */

START TRANSACTION;

-- ======================= SUPPRIMER LES CLES ETRANGERES =======================

ALTER TABLE `bank_account` DROP CONSTRAINT `FK3jn4nan3fmvegqlbw3npkumhb`;
ALTER TABLE `credit_card` DROP CONSTRAINT `FKotvkdsvfmsckokst7nslfpfcv`;
ALTER TABLE `external_bank_account` DROP CONSTRAINT `FK2wcuk4rtvt3s6aml9iphacmai`;
ALTER TABLE `external_bank_account` DROP CONSTRAINT `FK7wh2wpqbm0kpetl3hgjm6kvo`;
ALTER TABLE `external_bank_account` DROP CONSTRAINT `FKr69xevnhumvqj2u2nilo6il4s`;
ALTER TABLE `internal_bank_account` DROP CONSTRAINT `FK8cobk9a1019my512ueqq3mqw8`;
ALTER TABLE `money_transfer` DROP CONSTRAINT `FK9e7d1fy1rteef5ytvx6b8pph9`;
ALTER TABLE `money_transfer` DROP CONSTRAINT `FKc1a4l6jv6cxylblrlrfx2f73w`;
ALTER TABLE `user` DROP CONSTRAINT `FKiiwid70c2rc5ib4xtpidehupl`;
ALTER TABLE `user` DROP CONSTRAINT `FKn8m89sk3rd3pinwkviy67253s`;
ALTER TABLE `user_connection` DROP CONSTRAINT `FK7nqmrc4b8ffv76f55cdpo2kgl`;
ALTER TABLE `user_connection` DROP CONSTRAINT `FKfe2o18riq31xic2f3l7al0q69`;
ALTER TABLE `user_role` DROP CONSTRAINT `FKa68196081fvovjhkek5m97n3y`;
ALTER TABLE `user_role` DROP CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o`;

-- ======================= CREER LES TABLES =======================

DROP TABLE IF EXISTS `bank_account`;
CREATE TABLE IF NOT EXISTS `bank_account` (
  `bank_account_id` int NOT NULL AUTO_INCREMENT,
  `deleted` tinyint(1) DEFAULT '0',
  `number` varchar(27) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `external_bank_account_id` int DEFAULT NULL,
  PRIMARY KEY (`bank_account_id`),
  UNIQUE KEY `UK_17vmv222i0bofso3sint89xga` (`number`),
  KEY `FK7r1ofuo9blnk54dpxpiqpp6r7` (`external_bank_account_id`)
);

ALTER TABLE `bank_account` AUTO_INCREMENT = 3;

-- --------------------------------------------------------

DROP TABLE IF EXISTS `credit_card`;
CREATE TABLE IF NOT EXISTS `credit_card` (
  `credit_card_id` int NOT NULL AUTO_INCREMENT,
  `deleted` tinyint(1) DEFAULT '0',
  `expires_end_month` varchar(2) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `expires_end_year` varchar(4) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `number` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `validation_value` varchar(3) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `external_bank_account_id` int DEFAULT NULL,
  PRIMARY KEY (`credit_card_id`),
  KEY `FK4wkh36vsxldgbtosvh2f9xwob` (`external_bank_account_id`)
);

ALTER TABLE `credit_card` AUTO_INCREMENT = 3;

-- --------------------------------------------------------

DROP TABLE IF EXISTS `external_bank_account`;
CREATE TABLE IF NOT EXISTS `external_bank_account` (
  `external_bank_account_id` int NOT NULL AUTO_INCREMENT,
  `deleted` tinyint(1) DEFAULT '0',
  `bank_account_id` int DEFAULT NULL,
  `credit_card_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`external_bank_account_id`),
  UNIQUE KEY `UKtc0ydej0npqab3pntp3a6qajt` (`user_id`,`bank_account_id`,`credit_card_id`),
  UNIQUE KEY `UK_fg46sr266x5tlb44y0o622dat` (`bank_account_id`),
  UNIQUE KEY `UK_ml2rmkkht9uh3xokjhihypj0n` (`credit_card_id`),
  UNIQUE KEY `UK_kc4gs370f559t2wi0bwo03xbs` (`user_id`)
);

ALTER TABLE `external_bank_account` AUTO_INCREMENT = 5;

-- --------------------------------------------------------

DROP TABLE IF EXISTS `internal_bank_account`;
CREATE TABLE IF NOT EXISTS `internal_bank_account` (
  `internal_bank_account_id` int NOT NULL AUTO_INCREMENT,
  `bank_balance` float DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`internal_bank_account_id`),
  UNIQUE KEY `UK_t3ubk2c0a5yp1pxxbsipjcj86` (`user_id`)
);

ALTER TABLE `internal_bank_account` AUTO_INCREMENT = 5;

-- --------------------------------------------------------

DROP TABLE IF EXISTS `money_transfer`;
CREATE TABLE IF NOT EXISTS `money_transfer` (
  `money_transfer_id` int NOT NULL AUTO_INCREMENT,
  `amount` int DEFAULT NULL,
  `commission` float DEFAULT NULL,
  `date` datetime(6) DEFAULT NULL,
  `description` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `external_type_operation` enum('BANK_ACCOUNT','CREDIT_CARD') COLLATE utf8mb4_general_ci DEFAULT NULL,
  `internal_type_operation` enum('CREDIT','DEBIT','TRANSFER') COLLATE utf8mb4_general_ci DEFAULT NULL,
  `invoice` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `invoice_sent` tinyint(1) DEFAULT '0',
  `rate_id` int DEFAULT NULL,
  `user_connection_id` int DEFAULT NULL,
  PRIMARY KEY (`money_transfer_id`),
  KEY `FKc1a4l6jv6cxylblrlrfx2f73w` (`rate_id`),
  KEY `FK9e7d1fy1rteef5ytvx6b8pph9` (`user_connection_id`)
);

ALTER TABLE `money_transfer` AUTO_INCREMENT = 11;

-- --------------------------------------------------------

DROP TABLE IF EXISTS `rate`;
CREATE TABLE IF NOT EXISTS `rate` (
  `rate_id` int NOT NULL AUTO_INCREMENT,
  `date` datetime(6) DEFAULT NULL,
  `rate` float DEFAULT NULL,
  PRIMARY KEY (`rate_id`),
  UNIQUE KEY `UK2ivldkm407w44kap5hy5vavq3` (`rate`,`date`)
);

ALTER TABLE `rate` AUTO_INCREMENT = 3;

-- --------------------------------------------------------

DROP TABLE IF EXISTS `role`;
CREATE TABLE IF NOT EXISTS `role` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `role` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `UK_bjxn5ii7v7ygwx39et0wawu0q` (`role`)
);

ALTER TABLE `role` AUTO_INCREMENT = 4;

-- --------------------------------------------------------

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `deleted` tinyint(1) DEFAULT '0',
  `email` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `first_name` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `last_name` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password` varchar(80) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `external_bank_account_id` int DEFAULT NULL,
  `internal_bank_account_id` int DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_g8p9d7nkluepyln2m4lo7onec` (`external_bank_account_id`),
  UNIQUE KEY `UK_xn48npfhwd35rbo40ak9r2j4` (`internal_bank_account_id`)
);

ALTER TABLE `user` AUTO_INCREMENT = 7;

-- --------------------------------------------------------

DROP TABLE IF EXISTS `user_connection`;
CREATE TABLE IF NOT EXISTS `user_connection` (
  `user_connection_id` int NOT NULL AUTO_INCREMENT,
  `deleted` tinyint(1) DEFAULT '0',
  `pseudo` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '-',
  `user_id_recipient` int DEFAULT NULL,
  `user_id_sender` int DEFAULT NULL,
  PRIMARY KEY (`user_connection_id`),
  UNIQUE KEY `UKenuubfsd1x25g10qg8rbpbmnd` (`user_id_sender`,`user_id_recipient`),
  KEY `FK7nqmrc4b8ffv76f55cdpo2kgl` (`user_id_recipient`)
);

ALTER TABLE `user_connection` AUTO_INCREMENT = 11;

-- --------------------------------------------------------

DROP TABLE IF EXISTS `user_role`;
CREATE TABLE IF NOT EXISTS `user_role` (
  `user_id` int NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`),
  KEY `FK859n2jvi8ivhui0rl0esws6o` (`user_id`)
);

ALTER TABLE `user_role` AUTO_INCREMENT = 7;

-- ======================= AJOUTER LES VALEURS DE DEPART =======================

INSERT INTO `rate` (`rate_id`, `rate`, `date`) VALUES
(1, 0, '2023-05-20 00:00:00.000000'),
(2, 0.005, '2023-05-21 00:00:00.000000');

-- --------------------------------------------------------

INSERT INTO `role` (`role_id`, `role`) VALUES
(1, 'ADMIN'),
(2, 'ACCOUNTING'),
(3, 'USER');

-- --------------------------------------------------------

INSERT INTO `user` (`user_id`, `deleted`, `email`, `first_name`, `last_name`, `password`, `external_bank_account_id`, `internal_bank_account_id`) VALUES
(1, 0, 'a@email.com', 'Albert', 'ADMIN', '$2a$10$vJGHPB2HO591KFUUtNttxOu.E7agMq7iBkS7d.jgjyjZz/eVwDBNi', NULL, NULL),
(2, 0, 'b@email.com', 'Sylvie', 'COMPTA', '$2a$10$pXK7f78NFGW0fkmAueaMo.7sn5KVC4P0jXeQ1utY33745x9txJzeq', NULL, NULL);

-- --------------------------------------------------------

INSERT INTO `bank_account` (`bank_account_id`, `deleted`, `number`, `external_bank_account_id`) VALUES
(1, 0, 'FR7600020002000200020002022', NULL),
(2, 0, 'FR7600030003000300030003033', NULL);

-- --------------------------------------------------------

INSERT INTO `credit_card` (`credit_card_id`, `deleted`, `expires_end_month`, `expires_end_year`, `number`, `validation_value`, `external_bank_account_id`) VALUES
(1, 0, '1', '2024', '3001000100010001', '111', NULL),
(2, 0, '3', '2025', '4003000300030003', '333', NULL);

-- --------------------------------------------------------

INSERT INTO `external_bank_account` (`external_bank_account_id`, `deleted`, `bank_account_id`, `credit_card_id`, `user_id`) VALUES
(1, 0, NULL, 1, NULL),
(2, 0, 1, NULL, NULL),
(3, 0, 2, 2, NULL),
(4, 0, NULL, NULL, NULL);

-- --------------------------------------------------------

INSERT INTO `internal_bank_account` (`internal_bank_account_id`, `bank_balance`, `deleted`, `user_id`) VALUES
(1, 49, 0, NULL),
(2, 37.87, 0, NULL),
(3, 30.37, 0, NULL),
(4, 81.85, 0, NULL);

-- --------------------------------------------------------

INSERT INTO `user` (`user_id`, `deleted`, `email`, `first_name`, `last_name`, `password`, `external_bank_account_id`, `internal_bank_account_id`) VALUES
(3, 0, 'c@email.com', 'Marcel', 'DUPUIT', '$2a$10$Q6bh/zASkTHdfK0STpdR9u5si3Af6vy4bGY0Fpyzjd7TGgPZf8K7W', 1, 1),
(4, 0, 'd@email.com', 'Tom', 'DUCHEMIN', '$2a$10$qixUaZC360c/QGiSmQ9IneIbyXMrmqJUQSRUdOWERRYRftmlcykPS', 2, 2),
(5, 0, 'e@email.com', 'Jocelyne', 'DUJARDIN', '$2a$10$rFqnZXawjT3aZP/0K/n3du5HS6.ROm9CHZgZg5v2ZrciEpOgfgBjS', 3, 3),
(6, 0, 'f@email.com', 'Henry', 'DUPONT', '$2a$10$Hk/c.K491/FQqW1Fp9ZXEujwfbo6uGNIBkP4DjO1YX3wEnAohHowe', 4, 4);

-- --------------------------------------------------------

INSERT INTO `user_role` (`user_id`, `role_id`) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 3),
(5, 3),
(6, 3);

-- --------------------------------------------------------

INSERT INTO `user_connection` (`user_connection_id`, `deleted`, `pseudo`, `user_id_recipient`, `user_id_sender`) VALUES
(1, 0, 'Me', 3, 3),
(2, 0, 'Me', 4, 4),
(3, 0, 'Me', 5, 5),
(4, 0, 'Me', 6, 6),
(5, 0, 'Marcel', 3, 4),
(6, 0, 'Tata', 5, 4),
(7, 0, 'Henry', 6, 5),
(8, 0, 'Marcel', 3, 5),
(9, 0, 'Tom', 4, 6),
(10, 0, 'Marcel', 3, 6);

-- --------------------------------------------------------

INSERT INTO `money_transfer` (`money_transfer_id`, `amount`, `commission`, `date`, `description`, `external_type_operation`, `internal_type_operation`, `invoice`, `invoice_sent`, `rate_id`, `user_connection_id`) VALUES
(1, 10, 0, '2023-05-22 00:00:00.000000', 'Crédit compte', 'CREDIT_CARD', 'CREDIT', NULL, 0, 1, 1),
(2, 40, 0, '2023-05-23 00:00:00.000000', 'Crédit compte', 'BANK_ACCOUNT', 'CREDIT', NULL, 0, 1, 2),
(3, 300, 0, '2023-05-24 00:00:00.000000', 'Crédit compte', 'BANK_ACCOUNT', 'CREDIT', NULL, 0, 1, 3),
(4, 18, 0.09, '2023-07-24 23:51:42.582719', 'Ciné', NULL, 'TRANSFER', NULL, 0, 2, 5),
(5, 8, 0.04, '2023-07-24 23:55:25.569378', 'courses', NULL, 'TRANSFER', NULL, 0, 2, 6),
(6, 112, 0.56, '2023-07-24 23:57:28.357209', 'Meuble', NULL, 'TRANSFER', NULL, 0, 2, 7),
(7, 15, 0.07, '2023-07-25 00:00:19.515282', '', NULL, 'TRANSFER', NULL, 0, 2, 8),
(8, 6, 0.03, '2023-07-25 00:02:59.274692', 'Pain', NULL, 'TRANSFER', NULL, 0, 2, 10),
(9, 24, 0.12, '2023-07-25 00:03:42.689296', '', NULL, 'TRANSFER', NULL, 0, 2, 9),
(10, 50, 0, '2023-07-25 00:04:59.014541', 'DEBIT', 'CREDIT_CARD', 'DEBIT', NULL, 0, 1, 3);

-- --------------------------------------------------------

UPDATE `bank_account` SET `external_bank_account_id` = 2 WHERE `bank_account_id` = 1;
UPDATE `bank_account` SET `external_bank_account_id` = 3 WHERE `bank_account_id` = 2;

-- --------------------------------------------------------

UPDATE `credit_card` SET `external_bank_account_id` = 1 WHERE `credit_card_id` = 1;
UPDATE `credit_card` SET `external_bank_account_id` = 3 WHERE `credit_card_id` = 2;

-- --------------------------------------------------------

UPDATE `external_bank_account` SET `user_id` = 3 WHERE `external_bank_account_id` = 1;
UPDATE `external_bank_account` SET `user_id` = 4 WHERE `external_bank_account_id` = 2;
UPDATE `external_bank_account` SET `user_id` = 5 WHERE `external_bank_account_id` = 3;
UPDATE `external_bank_account` SET `user_id` = 6 WHERE `external_bank_account_id` = 4;

-- --------------------------------------------------------

UPDATE `internal_bank_account` SET `user_id` = 3 WHERE `internal_bank_account_id` = 1;
UPDATE `internal_bank_account` SET `user_id` = 4 WHERE `internal_bank_account_id` = 2;
UPDATE `internal_bank_account` SET `user_id` = 5 WHERE `internal_bank_account_id` = 3;
UPDATE `internal_bank_account` SET `user_id` = 6 WHERE `internal_bank_account_id` = 4;

-- --------------------------------------------------------

UPDATE `money_transfer` SET `user_connection_id` = 1 WHERE `money_transfer_id` = 1;
UPDATE `money_transfer` SET `user_connection_id` = 2 WHERE `money_transfer_id` = 2;
UPDATE `money_transfer` SET `user_connection_id` = 3 WHERE `money_transfer_id` = 3;

-- ======================= AJOUTER LES CONTRAINTES =======================

ALTER TABLE `bank_account`
  ADD CONSTRAINT `FK3jn4nan3fmvegqlbw3npkumhb` FOREIGN KEY (`external_bank_account_id`) REFERENCES `external_bank_account` (`external_bank_account_id`);

-- --------------------------------------------------------

ALTER TABLE `credit_card`
  ADD CONSTRAINT `FKotvkdsvfmsckokst7nslfpfcv` FOREIGN KEY (`external_bank_account_id`) REFERENCES `external_bank_account` (`external_bank_account_id`);

-- --------------------------------------------------------

ALTER TABLE `external_bank_account`
  ADD CONSTRAINT `FK2wcuk4rtvt3s6aml9iphacmai` FOREIGN KEY (`bank_account_id`) REFERENCES `bank_account` (`bank_account_id`),
  ADD CONSTRAINT `FK7wh2wpqbm0kpetl3hgjm6kvo` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  ADD CONSTRAINT `FKr69xevnhumvqj2u2nilo6il4s` FOREIGN KEY (`credit_card_id`) REFERENCES `credit_card` (`credit_card_id`);

-- --------------------------------------------------------

ALTER TABLE `internal_bank_account`
  ADD CONSTRAINT `FK8cobk9a1019my512ueqq3mqw8` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);

-- --------------------------------------------------------

ALTER TABLE `money_transfer`
  ADD CONSTRAINT `FK9e7d1fy1rteef5ytvx6b8pph9` FOREIGN KEY (`user_connection_id`) REFERENCES `user_connection` (`user_connection_id`),
  ADD CONSTRAINT `FKc1a4l6jv6cxylblrlrfx2f73w` FOREIGN KEY (`rate_id`) REFERENCES `rate` (`rate_id`);

-- --------------------------------------------------------

ALTER TABLE `user`
  ADD CONSTRAINT `FKiiwid70c2rc5ib4xtpidehupl` FOREIGN KEY (`external_bank_account_id`) REFERENCES `external_bank_account` (`external_bank_account_id`),
  ADD CONSTRAINT `FKn8m89sk3rd3pinwkviy67253s` FOREIGN KEY (`internal_bank_account_id`) REFERENCES `internal_bank_account` (`internal_bank_account_id`);

-- --------------------------------------------------------

ALTER TABLE `user_connection`
  ADD CONSTRAINT `FK7nqmrc4b8ffv76f55cdpo2kgl` FOREIGN KEY (`user_id_recipient`) REFERENCES `user` (`user_id`),
  ADD CONSTRAINT `FKfe2o18riq31xic2f3l7al0q69` FOREIGN KEY (`user_id_sender`) REFERENCES `user` (`user_id`);

-- --------------------------------------------------------

ALTER TABLE `user_role`
  ADD CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`),
  ADD CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);

-- ===============================================

COMMIT;
