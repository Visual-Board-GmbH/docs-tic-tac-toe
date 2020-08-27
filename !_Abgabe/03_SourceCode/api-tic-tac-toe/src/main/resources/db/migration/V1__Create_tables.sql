-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema tictactoe
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema tictactoe
-- -----------------------------------------------------
USE `olivereg_tictactoe` ;

-- -----------------------------------------------------
-- Table `olivereg_tictactoe`.`Player`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `olivereg_tictactoe`.`Player` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `nickname` VARCHAR(255) NULL,
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `salt` VARCHAR(255) NOT NULL,
  `tileImage` LONGBLOB NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC));


-- -----------------------------------------------------
-- Table `olivereg_tictactoe`.`Game`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `olivereg_tictactoe`.`Game` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `state` ENUM('OPEN', 'PENDING', 'ACTIVE') NOT NULL,
  `usingMatrix` TINYINT NOT NULL DEFAULT 0,
  `gameData` LONGTEXT NULL,
  `lastModified` DATE NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `olivereg_tictactoe`.`GameHistory`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `olivereg_tictactoe`.`GameHistory` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `gameId` INT NOT NULL,
  `gameName` VARCHAR(255) NOT NULL,
  `gameData` LONGTEXT NOT NULL,
  `playerData` LONGTEXT NOT NULL,
  `datePlayed` DATE NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `olivereg_tictactoe`.`LEDRGBMatrix`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `olivereg_tictactoe`.`LEDRGBMatrix` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `url` VARCHAR(255) NOT NULL,
  `available` TINYINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `olivereg_tictactoe`.`Player_has_Game`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `olivereg_tictactoe`.`Player_has_Game` (
  `Player_id` INT NOT NULL,
  `Game_id` INT NOT NULL,
  PRIMARY KEY (`Player_id`, `Game_id`),
  INDEX `fk_Player_has_Lobby_Lobby1_idx` (`Game_id` ASC),
  INDEX `fk_Player_has_Lobby_Player_idx` (`Player_id` ASC),
  CONSTRAINT `fk_Player_has_Lobby_Player`
    FOREIGN KEY (`Player_id`)
    REFERENCES `olivereg_tictactoe`.`Player` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Player_has_Lobby_Lobby1`
    FOREIGN KEY (`Game_id`)
    REFERENCES `olivereg_tictactoe`.`Game` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `olivereg_tictactoe`.`Player_has_GameHistory`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `olivereg_tictactoe`.`Player_has_GameHistory` (
  `Player_id` INT NOT NULL,
  `GameHistory_id` INT NOT NULL,
  PRIMARY KEY (`Player_id`, `GameHistory_id`),
  INDEX `fk_Player_has_GameHistory_GameHistory1_idx` (`GameHistory_id` ASC),
  INDEX `fk_Player_has_GameHistory_Player1_idx` (`Player_id` ASC),
  CONSTRAINT `fk_Player_has_GameHistory_Player1`
    FOREIGN KEY (`Player_id`)
    REFERENCES `olivereg_tictactoe`.`Player` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Player_has_GameHistory_GameHistory1`
    FOREIGN KEY (`GameHistory_id`)
    REFERENCES `olivereg_tictactoe`.`GameHistory` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `olivereg_tictactoe`.`Game_has_LEDRGBMatrix`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `olivereg_tictactoe`.`Game_has_LEDRGBMatrix` (
  `Game_id` INT NOT NULL,
  `LEDRGBMatrix_id` INT NOT NULL,
  PRIMARY KEY (`Game_id`, `LEDRGBMatrix_id`),
  INDEX `fk_Game_has_LEDRGBMatrix_LEDRGBMatrix1_idx` (`LEDRGBMatrix_id` ASC),
  INDEX `fk_Game_has_LEDRGBMatrix_Game1_idx` (`Game_id` ASC),
  CONSTRAINT `fk_Game_has_LEDRGBMatrix_Game1`
    FOREIGN KEY (`Game_id`)
    REFERENCES `olivereg_tictactoe`.`Game` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Game_has_LEDRGBMatrix_LEDRGBMatrix1`
    FOREIGN KEY (`LEDRGBMatrix_id`)
    REFERENCES `olivereg_tictactoe`.`LEDRGBMatrix` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
