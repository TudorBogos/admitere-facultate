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
-- Table structure for table `admitere_status`
--

DROP TABLE IF EXISTS `admitere_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admitere_status` (
  `idStudent` int NOT NULL,
  `idFacultate` int NOT NULL,
  `status` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`idStudent`,`idFacultate`),
  KEY `idFacultate` (`idFacultate`),
  CONSTRAINT `admitere_status_ibfk_1` FOREIGN KEY (`idStudent`) REFERENCES `student` (`idStudent`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `admitere_status_ibfk_2` FOREIGN KEY (`idFacultate`) REFERENCES `facultate` (`idFacultate`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admitere_status`
--

LOCK TABLES `admitere_status` WRITE;
/*!40000 ALTER TABLE `admitere_status` DISABLE KEYS */;
INSERT INTO `admitere_status` VALUES (1,3,'admis'),(2,2,'respins'),(3,1,'respins'),(4,3,'admis'),(5,5,'respins'),(6,1,'admis'),(7,3,'admis'),(8,2,'admis'),(9,2,'respins'),(10,1,'admis'),(11,4,'admis'),(12,3,'admis'),(13,2,'respins'),(14,1,'respins'),(15,2,'admis'),(16,4,'admis'),(17,5,'admis'),(18,1,'respins'),(19,2,'respins'),(20,1,'admis'),(21,5,'respins');
/*!40000 ALTER TABLE `admitere_status` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-17  0:28:46
