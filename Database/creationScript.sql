-- MySQL Script generated by MySQL Workbench
-- Wed Mar 25 23:42:26 2020
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
  `countryCode` VARCHAR(5) NULL,
  `region` VARCHAR(45) NULL,
  `city` VARCHAR(45) NULL,
  `loginCounter` INT NULL DEFAULT 0,
  `picture_id` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_User_picture_idx` (`picture_id` ASC) VISIBLE,
  CONSTRAINT `fk_User_picture`
    FOREIGN KEY (`picture_id`)
    REFERENCES `ip2020`.`Picture` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ip2020`.`Video`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ip2020`.`Video` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `filename` VARCHAR(10) NOT NULL,
  `video` LONGBLOB NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ip2020`.`Post`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ip2020`.`Post` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `User_id` INT NOT NULL,
  `text` VARCHAR(45) NULL,
  `link` VARCHAR(45) NULL,
  `Picture_id` INT NULL,
  `Video_id` INT NULL,
  `youtubeLink` VARCHAR(255) NULL,
  `dateTime` DATETIME NULL,
  PRIMARY KEY (`id`, `User_id`),
  INDEX `fk_Post_Picture1_idx` (`Picture_id` ASC) VISIBLE,
  INDEX `fk_Post_User1_idx` (`User_id` ASC) VISIBLE,
  INDEX `fk_Post_Video1_idx` (`Video_id` ASC) VISIBLE,
  CONSTRAINT `fk_Post_Picture1`
    FOREIGN KEY (`Picture_id`)
    REFERENCES `ip2020`.`Picture` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Post_User1`
    FOREIGN KEY (`User_id`)
    REFERENCES `ip2020`.`User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Post_Video1`
    FOREIGN KEY (`Video_id`)
    REFERENCES `ip2020`.`Video` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ip2020`.`Comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ip2020`.`Comment` (
  `dateTime` DATETIME NOT NULL,
  `comment` VARCHAR(255) NOT NULL,
  `Post_id` INT NOT NULL,
  `User_id` INT NOT NULL,
  `Picture_id` INT NULL,
  INDEX `fk_Comment_User1_idx` (`User_id` ASC) VISIBLE,
  INDEX `fk_Comment_Picture1_idx` (`Picture_id` ASC) VISIBLE,
  CONSTRAINT `fk_Comment_Post1`
    FOREIGN KEY (`Post_id`)
    REFERENCES `ip2020`.`Post` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Comment_User1`
    FOREIGN KEY (`User_id`)
    REFERENCES `ip2020`.`User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Comment_Picture1`
    FOREIGN KEY (`Picture_id`)
    REFERENCES `ip2020`.`Picture` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
