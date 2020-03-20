-- MySQL Script generated by MySQL Workbench
-- Fri Mar 20 12:15:02 2020
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema ip2020
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema ip2020
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ip2020` DEFAULT CHARACTER SET utf8 ;
USE `ip2020` ;

-- -----------------------------------------------------
-- Table `ip2020`.`Picture`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ip2020`.`Picture` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `fileName` VARCHAR(45) NOT NULL,
  `picture` LONGBLOB NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ip2020`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ip2020`.`User` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `country` VARCHAR(45) NULL,
  `region` VARCHAR(45) NULL,
  `city` VARCHAR(45) NULL,
  `picture_id` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_User_picture_idx` (`picture_id` ASC) VISIBLE,
  CONSTRAINT `fk_User_picture`
    FOREIGN KEY (`picture_id`)
    REFERENCES `ip2020`.`Picture` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;