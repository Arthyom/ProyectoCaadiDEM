-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema CaadiIntegrada
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema CaadiIntegrada
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `CaadiIntegrada` DEFAULT CHARACTER SET utf8 ;
USE `CaadiIntegrada` ;

-- -----------------------------------------------------
-- Table `CaadiIntegrada`.`Periods`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CaadiIntegrada`.`Periods` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `start` DATETIME NOT NULL,
  `end` DATETIME NOT NULL,
  `description` VARCHAR(300) NULL DEFAULT NULL,
  `actual` TINYINT(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CaadiIntegrada`.`Teachers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CaadiIntegrada`.`Teachers` (
  `employeeNumber` VARCHAR(10) NOT NULL,
  `firstLastName` VARCHAR(100) NOT NULL,
  `secondLastName` VARCHAR(100) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `gender` VARCHAR(10) NOT NULL,
  `email` VARCHAR(500) NULL DEFAULT NULL,
  PRIMARY KEY (`employeeNumber`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CaadiIntegrada`.`Groups`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CaadiIntegrada`.`Groups` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `periodId` INT(11) NOT NULL,
  `employeeNumber` VARCHAR(10) NOT NULL,
  `learningUnit` VARCHAR(500) NOT NULL,
  `level` VARCHAR(10) NOT NULL,
  `identifier` VARCHAR(5) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `Groups_Periods_FK_idx` (`periodId` ASC),
  INDEX `Groups_Teachers_FK_idx` (`employeeNumber` ASC),
  CONSTRAINT `Groups_Periods_FK`
    FOREIGN KEY (`periodId`)
    REFERENCES `CaadiIntegrada`.`Periods` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `Groups_Teachers_FK`
    FOREIGN KEY (`employeeNumber`)
    REFERENCES `CaadiIntegrada`.`Teachers` (`employeeNumber`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CaadiIntegrada`.`Students`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CaadiIntegrada`.`Students` (
  `nua` VARCHAR(10) NOT NULL,
  `firstLastName` VARCHAR(100) NOT NULL,
  `secondLastName` VARCHAR(100) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `gender` VARCHAR(10) NOT NULL,
  `birthday` DATETIME NULL DEFAULT NULL,
  `program` VARCHAR(500) NULL DEFAULT NULL,
  `email` VARCHAR(500) NULL DEFAULT NULL,
  PRIMARY KEY (`nua`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CaadiIntegrada`.`GroupMembers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CaadiIntegrada`.`GroupMembers` (
  `groupId` INT(11) NOT NULL,
  `nua` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`groupId`, `nua`),
  INDEX `GroupMember_Student_FK_idx` (`nua` ASC),
  CONSTRAINT `GroupMember_Group_FK`
    FOREIGN KEY (`groupId`)
    REFERENCES `CaadiIntegrada`.`Groups` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `GroupMember_Student_FK`
    FOREIGN KEY (`nua`)
    REFERENCES `CaadiIntegrada`.`Students` (`nua`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `CaadiIntegrada`.`Visit`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CaadiIntegrada`.`Visit` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `periodId` INT(11) NOT NULL,
  `nua` VARCHAR(10) NOT NULL,
  `skill` VARCHAR(40) NULL DEFAULT NULL,
  `start` DATETIME NOT NULL,
  `end` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `Visit_Students_FK_idx` (`nua` ASC),
  INDEX `Visit_Period_FK_idx` (`periodId` ASC),
  CONSTRAINT `Visit_Period_FK`
    FOREIGN KEY (`periodId`)
    REFERENCES `CaadiIntegrada`.`Periods` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `Visit_Students_FK`
    FOREIGN KEY (`nua`)
    REFERENCES `CaadiIntegrada`.`Students` (`nua`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


CREATE TABLE `CaadiIntegrada`.`usuarios` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(20) NOT NULL,
  `pass` VARCHAR(40) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `nombre_UNIQUE` (`nombre` ASC),
  UNIQUE INDEX `pass_UNIQUE` (`pass` ASC));



ALTER TABLE `CaadiIntegrada`.`Groups` 
ADD COLUMN `visible` BIT(2) NULL AFTER `periodId`;

ALTER TABLE `CaadiIntegrada`.`Periods` 
ADD COLUMN `visible` BIT(2) NULL AFTER `start`;


ALTER TABLE `CaadiIntegrada`.`Students` 
ADD COLUMN `visible` VARCHAR(45) NULL AFTER `secondLastName`;

ALTER TABLE `CaadiIntegrada`.`Teachers` 
ADD COLUMN `visible` VARCHAR(45) NULL AFTER `secondLastName`;

ALTER TABLE `CaadiIntegrada`.`usuarios` 
ADD COLUMN `visible` BIT(2) NULL AFTER `pass`;

ALTER TABLE `CaadiIntegrada`.`Visit` 
ADD COLUMN `visible` BIT(2) NULL AFTER `periodId`;

ALTER TABLE `CaadiIntegrada`.`Visits` 
ADD COLUMN `visible` VARCHAR(45) NULL AFTER `periodid`;
}
ALTER TABLE `CaadiIntegrada`.`GroupMembers` 
ADD COLUMN `visible` BIT(2) NULL AFTER `groupId`;


ALTER TABLE `CaadiIntegrada`.`Students` 
CHANGE COLUMN `birthday` `birthday` DATETIME NULL ,
CHANGE COLUMN `email` `email` VARCHAR(255) NULL ,
CHANGE COLUMN `firstLastName` `firstLastName` VARCHAR(255) NOT NULL ,
CHANGE COLUMN `gender` `gender` VARCHAR(255) NOT NULL ,
CHANGE COLUMN `name` `name` VARCHAR(255) NOT NULL ,
CHANGE COLUMN `secondLastName` `secondLastName` VARCHAR(255) NOT NULL ,
CHANGE COLUMN `visible` `visible` VARCHAR(45) NULL DEFAULT 0 ;

ALTER TABLE `CaadiIntegrada`.`Teachers` 
CHANGE COLUMN `firstLastName` `firstLastName` VARCHAR(255) NOT NULL ,
CHANGE COLUMN `gender` `gender` VARCHAR(255) NOT NULL ,
CHANGE COLUMN `name` `name` VARCHAR(255) NOT NULL ,
CHANGE COLUMN `secondLastName` `secondLastName` VARCHAR(255) NOT NULL ,
CHANGE COLUMN `visible` `visible` VARCHAR(45) NULL DEFAULT 0 ;


ALTER TABLE `CaadiIntegrada`.`Periods` 
CHANGE COLUMN `end` `end` DATETIME NOT NULL ,
CHANGE COLUMN `start` `start` DATETIME NOT NULL ;

ALTER TABLE `CaadiIntegrada`.`Students` 
CHANGE COLUMN `visible` `visible` BIT(2) NULL DEFAULT 0 ;

ALTER TABLE `CaadiIntegrada`.`Teachers` 
CHANGE COLUMN `visible` `visible` BIT(2) NULL DEFAULT 0 ;

ALTER TABLE `CaadiIntegrada`.`Visits` 
CHANGE COLUMN `visible` `visible` BIT(2) NULL DEFAULT 0 ;


ALTER TABLE `CaadiIntegrada`.`Groups` 
ADD COLUMN `visible` BIT(2) NULL AFTER `periodId`;

ALTER TABLE `CaadiIntegrada`.`Periods` 
ADD COLUMN `visible` BIT(2) NULL AFTER `start`;


ALTER TABLE `CaadiIntegrada`.`Students` 
ADD COLUMN `visible` VARCHAR(45) NULL AFTER `secondLastName`;

ALTER TABLE `CaadiIntegrada`.`Teachers` 
ADD COLUMN `visible` VARCHAR(45) NULL AFTER `secondLastName`;

ALTER TABLE `CaadiIntegrada`.`usuarios` 
ADD COLUMN `visible` BIT(2) NULL AFTER `pass`;

ALTER TABLE `CaadiIntegrada`.`Visit` 
ADD COLUMN `visible` BIT(2) NULL AFTER `periodId`;

ALTER TABLE `CaadiIntegrada`.`Visits` 
ADD COLUMN `visible` VARCHAR(45) NULL AFTER `periodid`;
}
ALTER TABLE `CaadiIntegrada`.`GroupMembers` 
ADD COLUMN `visible` BIT(2) NULL AFTER `groupId`;


ALTER TABLE `CaadiIntegrada`.`Students` 
CHANGE COLUMN `birthday` `birthday` DATETIME NULL ,
CHANGE COLUMN `email` `email` VARCHAR(255) NULL ,
CHANGE COLUMN `firstLastName` `firstLastName` VARCHAR(255) NOT NULL ,
CHANGE COLUMN `gender` `gender` VARCHAR(255) NOT NULL ,
CHANGE COLUMN `name` `name` VARCHAR(255) NOT NULL ,
CHANGE COLUMN `secondLastName` `secondLastName` VARCHAR(255) NOT NULL ,
CHANGE COLUMN `visible` `visible` VARCHAR(45) NULL DEFAULT 0 ;

ALTER TABLE `CaadiIntegrada`.`Teachers` 
CHANGE COLUMN `firstLastName` `firstLastName` VARCHAR(255) NOT NULL ,
CHANGE COLUMN `gender` `gender` VARCHAR(255) NOT NULL ,
CHANGE COLUMN `name` `name` VARCHAR(255) NOT NULL ,
CHANGE COLUMN `secondLastName` `secondLastName` VARCHAR(255) NOT NULL ,
CHANGE COLUMN `visible` `visible` VARCHAR(45) NULL DEFAULT 0 ;


ALTER TABLE `CaadiIntegrada`.`Periods` 
CHANGE COLUMN `end` `end` DATETIME NOT NULL ,
CHANGE COLUMN `start` `start` DATETIME NOT NULL ;

ALTER TABLE `CaadiIntegrada`.`Students` 
CHANGE COLUMN `visible` `visible` BIT(2) NULL DEFAULT 0 ;

ALTER TABLE `CaadiIntegrada`.`Teachers` 
CHANGE COLUMN `visible` `visible` BIT(2) NULL DEFAULT 0 ;

ALTER TABLE `CaadiIntegrada`.`Visits` 
CHANGE COLUMN `visible` `visible` BIT(2) NULL DEFAULT 0 ;

ALTER TABLE `CaadiIntegrada`.`Teachers` 
CHANGE COLUMN `visible` `visible` BIT(1) NULL DEFAULT 1 ;

ALTER TABLE `CaadiIntegrada`.`Students` 
CHANGE COLUMN `visible` `visible` BIT(1) NULL DEFAULT 1 ;

ALTER TABLE `CaadiIntegrada`.`Periods` 
CHANGE COLUMN `visible` `visible` BIT(1) NULL DEFAULT NULL ;

ALTER TABLE `CaadiIntegrada`.`Groups` 
CHANGE COLUMN `visible` `visible` BIT(1) NULL DEFAULT NULL ;

ALTER TABLE `CaadiIntegrada`.`Groups` 
CHANGE COLUMN `visible` `visible` BIT(1) NULL DEFAULT 1 ;


INSERT INTO `CaadiIntegrada`.`usuarios` (`id`, `nombre`, `pass`, `visible`) VALUES ('', 'admin', '1820_adCa?!', '1');
INSERT INTO `CaadiIntegrada`.`usuarios` (`nombre`, `pass`, `visible`) VALUES ('stdunt', '1820_stCa?!', '1');
