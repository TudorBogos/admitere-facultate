
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `idAdmin` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `last_login` datetime DEFAULT NULL,
  PRIMARY KEY (`idAdmin`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1,'TudorAdmin','1234','2025-06-03 17:36:45'),(2,'AdminUser1','1234','2025-01-08 21:38:18'),(3,'AdminUser2','1234','2025-05-29 23:20:52'),(4,'KekiusMaximus','1234','2025-01-12 21:24:55');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;
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

LOCK TABLES `admitere_status` WRITE;
/*!40000 ALTER TABLE `admitere_status` DISABLE KEYS */;
INSERT INTO `admitere_status` VALUES (1,3,'admis'),(2,2,'respins'),(3,1,'respins'),(4,3,'admis'),(5,5,'admis'),(6,1,'admis'),(7,3,'admis'),(8,2,'admis'),(9,2,'respins'),(10,1,'admis'),(11,4,'admis'),(12,3,'admis'),(13,2,'respins'),(14,1,'respins'),(15,2,'admis'),(16,4,'admis'),(17,5,'respins'),(18,1,'respins'),(19,2,'respins'),(20,1,'admis'),(21,5,'respins'),(22,1,'pending');
/*!40000 ALTER TABLE `admitere_status` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `facultate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `facultate` (
  `idFacultate` int NOT NULL AUTO_INCREMENT,
  `Nume_Facultate` varchar(45) DEFAULT NULL,
  `Adresa` varchar(45) DEFAULT NULL,
  `numar_locuri` int DEFAULT NULL,
  PRIMARY KEY (`idFacultate`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `facultate` WRITE;
/*!40000 ALTER TABLE `facultate` DISABLE KEYS */;
INSERT INTO `facultate` VALUES (1,'Facultatea de Informatica','Strada Mihail Kogalniceanu 1',3),(2,'Facultatea de Medicina','Strada Victor Babes 8',2),(3,'Facultatea de Drept','Bulevardul Carol I 11',4),(4,'Facultatea de Inginerie Electrica','Bulevardul Independentei 2',3),(5,'Facultatea de Biologie','Strada Natura 12',1);
/*!40000 ALTER TABLE `facultate` ENABLE KEYS */;
UNLOCK TABLES;
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
  KEY `idFacultate` (`idFacultateOptiune`),
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`idFacultateOptiune`) REFERENCES `facultate` (`idFacultate`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES (1,'Popescu','Maria','1990512245889',9.60,3,'Facultatea de Drept'),(2,'Jilavu','Andrei','1880713356788',7.40,2,'Facultatea de Medicina'),(3,'Popa','Elena','2910824467890',8.20,1,'Facultatea de Informatica'),(4,'Calin','Alexandru','1920601234567',6.90,3,'Facultatea de Drept'),(5,'Stanescu','Ana','2900309123456',9.30,5,'Facultatea de Biologie'),(6,'Dumitrescu','Mihai','1891215789012',8.80,1,'Facultatea de Informatica'),(7,'Georgescu','Diana','2880418345678',6.50,3,'Facultatea de Drept'),(8,'Vasilescu','Cristian','1900925901234',9.10,2,'Facultatea de Medicina'),(9,'Marinescu','Andreea','2871103567890',7.80,2,'Facultatea de Medicina'),(10,'Diaconu','Radu','1861230123456',9.80,1,'Facultatea de Informatica'),(11,'Popescu','Ion','1234567890123',7.70,4,'Facultatea de Inginerie Electrica'),(12,'Ionescu','Maria','2345678901234',8.50,3,'Facultatea de Drept'),(13,'Georgescu','Andrei','3456789012345',6.80,2,'Facultatea de Medicina'),(14,'Dumitrescu','Elena','4567890123456',7.60,1,'Facultatea de Informatica'),(15,'Stan','Cristina','5678901234567',8.90,2,'Facultatea de Medicina'),(16,'Constantinescu','Dan','6789012345678',6.70,4,'Facultatea de Inginerie Electrica'),(17,'Vasilescu','Ana','7890123456789',9.20,5,'Facultatea de Biologie'),(18,'Marinescu','Paul','8901234567890',7.40,1,'Facultatea de Informatica'),(19,'Radu','Ioana','9012345678901',8.30,2,'Facultatea de Medicina'),(20,'Chelariu','Adelin','5001127170069',9.10,1,'Facultatea de Informatica'),(21,'Bogos','Tudor','5011219170012',8.00,5,'Facultatea de Biologie'),(22,'Popescu','Ionut','1980111223456',9.50,1,'A');
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
/*!50003 CREATE*/ /*!50003 TRIGGER `after_student_insert` AFTER INSERT ON `student` FOR EACH ROW BEGIN
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

