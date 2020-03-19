CREATE DATABASE  IF NOT EXISTS `planer_obaveza`;
USE `planer_obaveza`;

DROP TABLE IF EXISTS `obaveza`;

DROP TABLE iF EXISTS `korisnik`;

CREATE TABLE `korisnik` (
  `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `ime` varchar(45) NOT NULL,
  `prezime` varchar(45) NOT NULL
);


CREATE TABLE `obaveza` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `opis` varchar(45) NOT NULL,
  `datum` date NOT NULL,
  `kategorija` varchar(45) NOT NULL,
  `adresaKorisnika` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `korisnik` VALUES (1, 'admin', 'admin', 'Demo', 'Aplikacija');
INSERT INTO `obaveza` VALUES (1,'opis','2018-01-17','neka','adresa'),(2,'Ovo je nova obaveza','2018-01-20','poslovna obaveza','Neka');