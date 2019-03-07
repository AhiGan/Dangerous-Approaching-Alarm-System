-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: localhost    Database: dangerisapproaching
-- ------------------------------------------------------
-- Server version	5.6.24

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `pooldata`
--

DROP TABLE IF EXISTS `pooldata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pooldata` (
  `id` varchar(255) NOT NULL DEFAULT '',
  `lab` varchar(255) DEFAULT NULL,
  `prov` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `block` varchar(255) DEFAULT NULL,
  `addr` varchar(255) DEFAULT NULL,
  `long` double(15,6) DEFAULT NULL,
  `lat` double(15,6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pooldata`
--

LOCK TABLES `pooldata` WRITE;
/*!40000 ALTER TABLE `pooldata` DISABLE KEYS */;
INSERT INTO `pooldata` VALUES ('数据点1','1','江苏省','南京市','江宁区','江苏省南京市江宁区三江西路',118.825419,31.892604),('数据点10','1','江苏省','南京市','江宁区','江苏省南京市江宁区南工路',118.827351,31.892677),('数据点11','1','江苏省','南京市','江宁区','江苏省南京市江宁区南工路',118.827580,31.892688),('数据点12','1','江苏省','南京市','江宁区','江苏省南京市江宁区南工路',118.827750,31.892688),('数据点13','1','江苏省','南京市','江宁区','江苏省南京市江宁区三江西路',118.825181,31.892604),('数据点14','1','江苏省','南京市','江宁区','江苏省南京市江宁区三江西路',118.824970,31.892600),('数据点15','1','江苏省','南京市','江宁区','江苏省南京市江宁区三江西路',118.824746,31.892592),('数据点16','1','江苏省','南京市','江宁区','江苏省南京市江宁区三江西路',118.824517,31.892589),('数据点17','1','江苏省','南京市','江宁区','江苏省南京市江宁区三江西路',118.824296,31.892581),('数据点18','1','江苏省','南京市','江宁区','江苏省南京市江宁区三江西路',118.824094,31.892573),('数据点19','1','江苏省','南京市','江宁区','江苏省南京市江宁区三江西路',118.823910,31.892573),('数据点2','1','江苏省','南京市','江宁区','江苏省南京市江宁区三江西路',118.825559,31.892512),('数据点20','1','江苏省','南京市','江宁区','江苏省南京市江宁区三江西路',118.823744,31.892577),('数据点3','1','江苏省','南京市','江宁区','江苏省南京市江宁区三江东路',118.825855,31.892504),('数据点4','1','江苏省','南京市','江宁区','江苏省南京市江宁区三江东路',118.826012,31.892631),('数据点5','1','江苏省','南京市','江宁区','江苏省南京市江宁区三江东路',118.826228,31.892638),('数据点6','1','江苏省','南京市','江宁区','江苏省南京市江宁区三江东路',118.826475,31.892650),('数据点7','1','江苏省','南京市','江宁区','江苏省南京市江宁区三江东路',118.826708,31.892658),('数据点8','1','江苏省','南京市','江宁区','江苏省南京市江宁区南工路',118.826937,31.892661),('数据点9','1','江苏省','南京市','江宁区','江苏省南京市江宁区三江东路',118.827158,31.892669);
/*!40000 ALTER TABLE `pooldata` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-12-24 22:03:32
