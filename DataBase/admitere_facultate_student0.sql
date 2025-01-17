CREATE DATABASE  IF NOT EXISTS `admitere_facultate` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `admitere_facultate`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: admitere_facultate
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `idStudent` int NOT NULL AUTO_INCREMENT,
  `Nume` varchar(45) DEFAULT NULL,
  `Prenume` varchar(45) DEFAULT NULL,
  `CNP` varchar(45) DEFAULT NULL,
  `nota` decimal(4,2) DEFAULT NULL,
  `idFacultateOptiune` int DEFAULT NULL,
  `Optiune` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`idStudent`),
  KEY `idFacultateOptiune` (`idFacultateOptiune`),
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`idFacultateOptiune`) REFERENCES `facultate` (`idFacultate`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES (21,'Popescu','Maria','1990512245889',9.50,3,'Facultatea de Drept'),(22,'Jilavu','Andrei','1880713356788',7.40,2,'Facultatea de Medicina'),(23,'Popa','Elena','2910824467890',8.20,1,'Facultatea de Informatica'),(24,'Calin','Alexandru','1920601234567',6.90,3,'Facultatea de Drept'),(25,'Stanescu','Ana','2900309123456',7.30,5,'Facultatea de Biologie'),(26,'Dumitrescu','Mihai','1891215789012',8.80,1,'Facultatea de Informatica'),(27,'Georgescu','Diana','2880418345678',6.50,3,'Facultatea de Drept'),(28,'Vasilescu','Cristian','1900925901234',9.10,2,'Facultatea de Medicina'),(29,'Marinescu','Andreea','2871103567890',7.80,2,'Facultatea de Medicina'),(30,'Diaconu','Radu','1861230123456',9.30,1,'Facultatea de Informatica'),(31,'Popescu','Ion','1234567890123',7.70,4,'Facultatea de Inginerie Electrica'),(32,'Ionescu','Maria','2345678901234',8.50,3,'Facultatea de Drept'),(33,'Georgescu','Andrei','3456789012345',6.80,2,'Facultatea de Medicina'),(34,'Dumitrescu','Elena','4567890123456',7.60,1,'Facultatea de Informatica'),(35,'Stan','Cristina','5678901234567',8.90,2,'Facultatea de Medicina'),(36,'Constantinescu','Dan','6789012345678',6.70,4,'Facultatea de Inginerie Electrica'),(37,'Vasilescu','Ana','7890123456789',9.20,5,'Facultatea de Biologie'),(38,'Marinescu','Paul','8901234567890',7.40,1,'Facultatea de Informatica'),(39,'Radu','Ioana','9012345678901',8.30,2,'Facultatea de Medicina'),(40,'Chelariu','Adelin','5001127170069',9.10,1,'Facultatea de Informatica');
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `admitere_facultate` AFTER INSERT ON `student` FOR EACH ROW BEGIN
    INSERT INTO admitere_status (idStudent, idFacultate, status)
    VALUES (NEW.idStudent, NEW.idFacultateOptiune, 'pending');
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-17  2:05:52
