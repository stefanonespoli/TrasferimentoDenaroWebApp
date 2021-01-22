-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: progettoweb
-- ------------------------------------------------------
-- Server version	8.0.19

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
-- Table structure for table `conto`
--

DROP TABLE IF EXISTS `conto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `conto` (
  `codice` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) NOT NULL,
  `saldo` decimal(13,2) NOT NULL,
  `idUtente` int NOT NULL,
  PRIMARY KEY (`codice`),
  KEY `idUtente_idx` (`idUtente`),
  CONSTRAINT `idUtente` FOREIGN KEY (`idUtente`) REFERENCES `utenti` (`idutente`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conto`
--

LOCK TABLES `conto` WRITE;
/*!40000 ALTER TABLE `conto` DISABLE KEYS */;
INSERT INTO `conto` VALUES (1,'conto emergenze',2025.51,1),(2,'conto deposito',4546.84,2),(3,'conto corrente',1003.23,1),(4,'conto generico',38.97,1),(5,'conto postale',309.00,1),(7,'conto deposito',500.00,1),(12,'Conto generico',51.00,1),(27,'Casa',4500.00,44),(28,'Casa',990.00,45),(29,'Deposito',2000.00,46);
/*!40000 ALTER TABLE `conto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rubrica`
--

DROP TABLE IF EXISTS `rubrica`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rubrica` (
  `idUtenteDestinazione` int NOT NULL,
  `contoDestinazione` int NOT NULL,
  `proprietarioRubrica` int NOT NULL,
  PRIMARY KEY (`contoDestinazione`,`proprietarioRubrica`),
  KEY `proprietarioRubrica_idx` (`proprietarioRubrica`),
  KEY `cvf_idx` (`idUtenteDestinazione`),
  CONSTRAINT `idUtenteDestinazione` FOREIGN KEY (`idUtenteDestinazione`) REFERENCES `utenti` (`idutente`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `proprietarioRubrica` FOREIGN KEY (`proprietarioRubrica`) REFERENCES `utenti` (`idutente`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rubrica`
--

LOCK TABLES `rubrica` WRITE;
/*!40000 ALTER TABLE `rubrica` DISABLE KEYS */;
/*!40000 ALTER TABLE `rubrica` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trasferimento`
--

DROP TABLE IF EXISTS `trasferimento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trasferimento` (
  `data` datetime NOT NULL,
  `contoOrigine` int NOT NULL,
  `contoDestinazione` int NOT NULL,
  `causale` varchar(300) NOT NULL,
  `importo` decimal(13,2) NOT NULL,
  PRIMARY KEY (`contoOrigine`,`data`),
  KEY `contoDestinazione_idx` (`contoDestinazione`),
  CONSTRAINT `contoDestinazione` FOREIGN KEY (`contoDestinazione`) REFERENCES `conto` (`codice`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `contoOrigine` FOREIGN KEY (`contoOrigine`) REFERENCES `conto` (`codice`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trasferimento`
--

LOCK TABLES `trasferimento` WRITE;
/*!40000 ALTER TABLE `trasferimento` DISABLE KEYS */;
INSERT INTO `trasferimento` VALUES ('2020-07-20 12:00:00',1,2,'tasse universitarie',100.20),('2020-07-24 13:01:00',1,4,'imposte',100.00),('2020-07-28 19:06:39',1,2,'acquisto online',1.00),('2020-07-28 21:42:54',1,2,'prestazione lavorativa',2.10),('2020-07-28 21:45:01',1,2,'prestazione lavorativa',0.15),('2020-07-28 21:48:26',1,2,'pagamento bollettino ',0.08),('2020-07-29 18:38:25',1,2,'Tassa sui rifiuti',100.14),('2020-07-31 17:56:09',1,2,'acquisto online',3.00),('2020-07-31 18:27:55',1,2,'acquisto onlineeeee',3.00),('2020-07-31 18:54:32',1,2,'pagamento bolletti',3.00),('2020-07-31 19:09:10',1,2,'pagamento bollettino ',3.00),('2020-07-31 19:32:39',1,2,'pagamento bollettino ',3.00),('2020-07-31 20:31:48',1,2,'pagamento bollettino ',22.00),('2020-08-02 18:40:38',1,3,'pagamento bollettino ',3.00),('2020-07-24 13:00:00',2,1,'pagamento affitto',200.00),('2020-07-28 21:59:57',3,2,'pagamento IRES',0.32),('2020-07-24 13:02:00',4,2,'bolletta acqua',200.00),('2020-07-28 21:53:53',4,2,'pagamento IRPEF',2.00),('2020-07-29 18:40:31',4,2,'tassa sui rifiuti',0.03),('2020-08-02 20:21:38',27,1,'spese uni',500.00),('2020-08-02 20:22:09',28,2,'tasse',10.00),('2020-08-02 20:22:43',29,2,'Cena Cracco',1000.00);
/*!40000 ALTER TABLE `trasferimento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utenti`
--

DROP TABLE IF EXISTS `utenti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utenti` (
  `idutente` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) NOT NULL,
  `password` varchar(70) NOT NULL,
  `mail` varchar(45) NOT NULL,
  PRIMARY KEY (`idutente`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utenti`
--

LOCK TABLES `utenti` WRITE;
/*!40000 ALTER TABLE `utenti` DISABLE KEYS */;
INSERT INTO `utenti` VALUES (1,'root','b133a0c0e9bee3be20163d2ad31d6248db292aa6dcb1ee087a2aa50e0fc75ae2','root@hotmail.it'),(2,'root1','1b158839f8aa339e89bb50b1603e90ec68e0fd5565fe10a7cb32729fddf7070a','root2@hotmail.it'),(3,'root3','2e7d2c03a9507ae265ecf5b5356885a53393a2029d241394997265a1a25aefc6','root3@hotmail.it'),(44,'user','04f8996da763b7a969b1028ee3007569eaf3a635486ddab211d512c85b9df8fb','user@m.it'),(45,'user2','6025d18fe48abd45168528f18a82e265dd98d421a7084aa09f61b341703901a3','user2@m.it'),(46,'user3','5860faf02b6bc6222ba5aca523560f0e364ccd8b67bee486fe8bf7c01d492ccb','user3@m.it');
/*!40000 ALTER TABLE `utenti` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-08-02 20:24:25
