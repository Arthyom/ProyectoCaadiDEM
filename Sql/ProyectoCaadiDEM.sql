-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema CaadiIntegrada
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema CaadiIntegrada
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `CaadiIntegrada` DEFAULT CHARACTER SET utf8 ;
USE `CaadiIntegrada` ;

-- -----------------------------------------------------
-- Table `CaadiIntegrada`.`Teachers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CaadiIntegrada`.`Teachers` (
  `employeeNumber` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NULL DEFAULT NULL,
  `firstLastName` VARCHAR(255) NOT NULL,
  `gender` VARCHAR(255) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `secondLastName` VARCHAR(255) NOT NULL,
  `visible` BIT(1) NULL DEFAULT b'1',
  PRIMARY KEY (`employeeNumber`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CaadiIntegrada`.`Periods`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CaadiIntegrada`.`Periods` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `actual` TINYINT(1) NULL DEFAULT '0',
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `end` DATETIME NOT NULL,
  `start` DATETIME NOT NULL,
  `visible` BIT(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CaadiIntegrada`.`Groups`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CaadiIntegrada`.`Groups` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `identifier` VARCHAR(255) NULL DEFAULT NULL,
  `learningUnit` VARCHAR(255) NULL DEFAULT NULL,
  `level` VARCHAR(255) NULL DEFAULT NULL,
  `employeeNumber` VARCHAR(255) NULL DEFAULT NULL,
  `periodId` INT(11) NULL DEFAULT NULL,
  `visible` BIT(1) NULL DEFAULT b'1',
  PRIMARY KEY (`id`),
  INDEX `FK_Groups_employeeNumber` (`employeeNumber` ASC),
  INDEX `FK_Groups_periodId` (`periodId` ASC),
  CONSTRAINT `FK_Groups_employeeNumber`
    FOREIGN KEY (`employeeNumber`)
    REFERENCES `CaadiIntegrada`.`Teachers` (`employeeNumber`),
  CONSTRAINT `FK_Groups_periodId`
    FOREIGN KEY (`periodId`)
    REFERENCES `CaadiIntegrada`.`Periods` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CaadiIntegrada`.`Students`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CaadiIntegrada`.`Students` (
  `nua` VARCHAR(255) NOT NULL,
  `birthday` DATETIME NULL DEFAULT NULL,
  `email` VARCHAR(255) NULL DEFAULT NULL,
  `firstLastName` VARCHAR(255) NOT NULL,
  `gender` VARCHAR(255) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `program` VARCHAR(255) NULL DEFAULT NULL,
  `secondLastName` VARCHAR(255) NOT NULL,
  `visible` BIT(1) NULL DEFAULT b'1',
  PRIMARY KEY (`nua`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CaadiIntegrada`.`GroupMembers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CaadiIntegrada`.`GroupMembers` (
  `nua` VARCHAR(255) NOT NULL,
  `groupId` INT(11) NOT NULL,
  `visible` BIT(2) NULL DEFAULT NULL,
  PRIMARY KEY (`nua`, `groupId`),
  INDEX `FK_GroupMembers_groupId` (`groupId` ASC),
  CONSTRAINT `FK_GroupMembers_groupId`
    FOREIGN KEY (`groupId`)
    REFERENCES `CaadiIntegrada`.`Groups` (`id`),
  CONSTRAINT `FK_GroupMembers_nua`
    FOREIGN KEY (`nua`)
    REFERENCES `CaadiIntegrada`.`Students` (`nua`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CaadiIntegrada`.`Visit`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CaadiIntegrada`.`Visit` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `end` DATETIME NULL DEFAULT NULL,
  `skill` VARCHAR(255) NULL DEFAULT NULL,
  `start` DATETIME NULL DEFAULT NULL,
  `nua` VARCHAR(255) NULL DEFAULT NULL,
  `periodId` INT(11) NULL DEFAULT NULL,
  `visible` BIT(1) NULL DEFAULT b'1',
  PRIMARY KEY (`id`),
  INDEX `FK_Visit_periodId` (`periodId` ASC),
  INDEX `FK_Visit_nua` (`nua` ASC),
  CONSTRAINT `FK_Visit_nua`
    FOREIGN KEY (`nua`)
    REFERENCES `CaadiIntegrada`.`Students` (`nua`),
  CONSTRAINT `FK_Visit_periodId`
    FOREIGN KEY (`periodId`)
    REFERENCES `CaadiIntegrada`.`Periods` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 11
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CaadiIntegrada`.`Visits`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CaadiIntegrada`.`Visits` (
  `id` BIGINT(20) NOT NULL,
  `end` DATETIME NULL DEFAULT NULL,
  `nua` VARCHAR(255) NULL DEFAULT NULL,
  `skill` VARCHAR(255) NULL DEFAULT NULL,
  `start` DATETIME NULL DEFAULT NULL,
  `periodid` INT(11) NULL DEFAULT NULL,
  `visible` BIT(2) NULL DEFAULT b'0',
  PRIMARY KEY (`id`),
  INDEX `FK_Visits_periodid` (`periodid` ASC),
  CONSTRAINT `FK_Visits_periodid`
    FOREIGN KEY (`periodid`)
    REFERENCES `CaadiIntegrada`.`Periods` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CaadiIntegrada`.`usuarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CaadiIntegrada`.`usuarios` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(20) NOT NULL,
  `pass` VARCHAR(40) NOT NULL,
  `visible` BIT(1) NULL DEFAULT b'1',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `nombre_UNIQUE` (`nombre` ASC),
  UNIQUE INDEX `pass_UNIQUE` (`pass` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

ALTER TABLE `CaadiIntegrada`.`Groups` 
ADD COLUMN `idAlterno` VARCHAR(45) NULL AFTER `visible`;
ALTER TABLE `CaadiIntegrada`.`Periods` 
ADD COLUMN `idAlterno` VARCHAR(45) NULL AFTER `visible`;

INSERT INTO `CaadiIntegrada`.`usuarios` (`nombre`, `pass`, `visible`) VALUES ('admin', '1820_AdCa?!', b'1');
INSERT INTO `CaadiIntegrada`.`usuarios` (`nombre`, `pass`, `visible`) VALUES ('students', '1820_AdSt?!', b'1');
