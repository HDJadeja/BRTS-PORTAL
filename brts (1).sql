-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 24, 2024 at 05:38 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `brts`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `addBUSnumber` (IN `bn` VARCHAR(30), IN `ss` VARCHAR(30), IN `ds` VARCHAR(30), IN `td` DOUBLE)   BEGIN
insert into bus_details values(bn,ss,ds,td,'RUNNING');
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `ADDemployee` (IN `wid` INT, IN `wn` VARCHAR(30), IN `sn` VARCHAR(30))   BEGIN
insert into employee_details(e_id,name,station_name) values(wid,wn,sn);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `addrevenue` (IN `sn` VARCHAR(30), IN `amt` INT)   BEGIN
update station_details set total_revenue = total_revenue + amt where station_name = sn;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `bb` (IN `bn` VARCHAR(30))   BEGIN
update bus_details set STATUS = 'BREAKDOWN' where bus_number = bn;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `employeeLOGin` (IN `wid` INT)   BEGIN
update employee_details set login_time = CURRENT_TIME and datee = CURRENT_DATE where e_id = wid;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `employeeLOGout` (IN `wid` INT)   BEGIN
update employee_details set logout_time = CURRENT_TIME  where e_id = wid;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `removeBUSnumber` (IN `bn` VARCHAR(30))   BEGIN
delete from bus_details where bus_number = bn;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `RUNNINGbus` (IN `id` VARCHAR(30), IN `bn` VARCHAR(30))   BEGIN
update driver_details set busno4 = bn where busno1 is not null and busno2 is not null and busno3 is not null and did = id;
update driver_details set busno3 = bn where busno1 is not null and busno2 is not null and busno4 is null and did = id;
update driver_details set busno2 = bn where busno1 is not null and busno3 is null and busno4 is null and did = id;
update driver_details set busno1 = bn where busno2 is null and busno3 is null and busno4 is null and did = id;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `stationADD` (IN `sn` VARCHAR(30), IN `sc` VARCHAR(30), IN `tr` INT, IN `ps` VARCHAR(30), IN `nrs` VARCHAR(30), IN `nls` VARCHAR(30))   BEGIN
 insert into station_details values(sn,sc,tr,ps,nrs,nls);
 UPDATE station_details set nextr_station = sn where nextl_station is not null and station_name = ps and nextr_station is null;
 update station_details set nextl_station = sn where nextl_station is null and station_name = ps;
 END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `admin_details`
--

CREATE TABLE `admin_details` (
  `admin_name` varchar(30) NOT NULL,
  `a_id` varchar(30) NOT NULL,
  `a_pass` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin_details`
--

INSERT INTO `admin_details` (`admin_name`, `a_id`, `a_pass`) VALUES
('MS.MITSI', '12M21', '1221'),
('MR.NOHARA', '12N21', '1221');

-- --------------------------------------------------------

--
-- Table structure for table `buslogs`
--

CREATE TABLE `buslogs` (
  `LOG_ID` int(11) NOT NULL,
  `BUSNO` varchar(30) NOT NULL,
  `ACTION` varchar(30) NOT NULL,
  `DATE_TIME` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `buslogs`
--

INSERT INTO `buslogs` (`LOG_ID`, `BUSNO`, `ACTION`, `DATE_TIME`) VALUES
(1, '3U', 'INSERTED', '2024-08-22 05:54:49'),
(3, '3U', 'DELETED', '2024-08-22 05:57:39'),
(4, '201', 'INSERTED', '2024-08-23 08:43:36'),
(5, '201', 'DELETED', '2024-08-23 08:45:22'),
(6, '12U', 'INSERTED', '2024-08-23 08:56:06'),
(7, '3U', 'INSERTED', '2024-08-23 09:38:58'),
(8, '9u', 'INSERTED', '2024-08-23 09:40:22'),
(9, '19E', 'INSERTED', '2024-08-23 11:38:14'),
(10, '19E', 'DELETED', '2024-08-23 11:38:43'),
(11, '17E', 'INSERTED', '2024-08-23 11:39:43'),
(12, '17E', 'UPDATED', '2024-08-23 11:39:52'),
(13, '16D', 'INSERTED', '2024-08-23 15:55:25'),
(14, '4D', 'INSERTED', '2024-08-24 03:25:05'),
(15, '4D', 'UPDATED', '2024-08-24 03:28:45');

-- --------------------------------------------------------

--
-- Table structure for table `bus_details`
--

CREATE TABLE `bus_details` (
  `BUS_NUMBER` varchar(30) NOT NULL,
  `SOURCE` varchar(30) NOT NULL,
  `DESTINATION` varchar(30) NOT NULL,
  `ONE-WAY DISTANCE` int(11) NOT NULL,
  `status` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bus_details`
--

INSERT INTO `bus_details` (`BUS_NUMBER`, `SOURCE`, `DESTINATION`, `ONE-WAY DISTANCE`, `status`) VALUES
('101', 'nehrunager', 'jaimangle', 3, 'RUNNING'),
('12U', 'shivranjni', 'himmatlal park', 4, 'BREAKDOWN'),
('16D', 'science city', 'pragatinager', 2, 'RUNNING'),
('17E', 'jaimangle', 'shivranjni', 3, 'BREAKDOWN'),
('1U', 'iskon', 'nehrunager', 5, 'RUNNING'),
('3U', 'jaimangle', 'star bazar', 9, 'RUNNING'),
('4D', 'pragatinager', 'south bopal', 5, 'RUNNING'),
('9u', 'sola crossroad', 'jhansi ki rani', 7, 'RUNNING');

--
-- Triggers `bus_details`
--
DELIMITER $$
CREATE TRIGGER `busLOGStriggerDELETE` BEFORE DELETE ON `bus_details` FOR EACH ROW insert into buslogs(busno,action,date_time) values(old.bus_number,'DELETED',now())
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `busLOGStriggerINSERT` BEFORE INSERT ON `bus_details` FOR EACH ROW insert into buslogs(busno,action,date_time) values(new.bus_number,'INSERTED',now())
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `busLOGStriggerUPDATE` BEFORE UPDATE ON `bus_details` FOR EACH ROW insert into buslogs(busno,action,date_time) values(new.bus_number,'UPDATED',now())
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `bus_schedule`
--

CREATE TABLE `bus_schedule` (
  `busno` varchar(30) NOT NULL,
  `sstime` time NOT NULL,
  `eetime` time NOT NULL,
  `stime` time DEFAULT NULL,
  `delay_time` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bus_schedule`
--

INSERT INTO `bus_schedule` (`busno`, `sstime`, `eetime`, `stime`, `delay_time`) VALUES
('101', '12:00:00', '01:00:00', '12:15:00', '00:15:00'),
('101', '12:00:00', '13:00:00', '12:15:00', '00:15:00'),
('1U', '11:20:00', '12:00:00', '11:50:34', '00:30:34'),
('1U', '11:20:00', '12:00:00', '11:50:34', '00:30:34'),
('1U', '11:20:00', '12:00:00', '11:51:02', '00:31:02'),
('1U', '11:50:00', '12:15:00', '12:38:48', '00:48:48'),
('17E', '17:10:00', '18:00:00', '17:16:33', '00:06:33'),
('12U', '08:30:00', '09:00:00', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `driver_details`
--

CREATE TABLE `driver_details` (
  `did` varchar(30) NOT NULL,
  `dname` varchar(30) NOT NULL,
  `busno1` varchar(30) DEFAULT NULL,
  `busno2` varchar(30) DEFAULT NULL,
  `busno3` varchar(30) DEFAULT NULL,
  `busno4` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `driver_details`
--

INSERT INTO `driver_details` (`did`, `dname`, `busno1`, `busno2`, `busno3`, `busno4`) VALUES
('97e556y', 'MR.J', '101', '1U', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `employee_details`
--

CREATE TABLE `employee_details` (
  `E_ID` int(11) NOT NULL,
  `LOGIN_TIME` time NOT NULL,
  `LOGOUT_TIME` time NOT NULL,
  `name` varchar(30) DEFAULT NULL,
  `station_name` varchar(30) DEFAULT NULL,
  `Datee` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `employee_details`
--

INSERT INTO `employee_details` (`E_ID`, `LOGIN_TIME`, `LOGOUT_TIME`, `name`, `station_name`, `Datee`) VALUES
(17009, '00:00:00', '00:00:00', 'MR.JKL', 'jaimangle', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `station_details`
--

CREATE TABLE `station_details` (
  `station_name` varchar(30) NOT NULL,
  `station_id` varchar(30) NOT NULL,
  `total_revenue` int(11) NOT NULL,
  `prev_station` varchar(30) DEFAULT NULL,
  `nextr_station` varchar(30) DEFAULT NULL,
  `nextL_station` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin_details`
--
ALTER TABLE `admin_details`
  ADD PRIMARY KEY (`a_id`);

--
-- Indexes for table `buslogs`
--
ALTER TABLE `buslogs`
  ADD PRIMARY KEY (`LOG_ID`);

--
-- Indexes for table `bus_details`
--
ALTER TABLE `bus_details`
  ADD PRIMARY KEY (`BUS_NUMBER`);

--
-- Indexes for table `bus_schedule`
--
ALTER TABLE `bus_schedule`
  ADD KEY `FK` (`busno`);

--
-- Indexes for table `driver_details`
--
ALTER TABLE `driver_details`
  ADD PRIMARY KEY (`did`);

--
-- Indexes for table `employee_details`
--
ALTER TABLE `employee_details`
  ADD PRIMARY KEY (`E_ID`);

--
-- Indexes for table `station_details`
--
ALTER TABLE `station_details`
  ADD PRIMARY KEY (`station_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `buslogs`
--
ALTER TABLE `buslogs`
  MODIFY `LOG_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `employee_details`
--
ALTER TABLE `employee_details`
  MODIFY `E_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1769737;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `bus_schedule`
--
ALTER TABLE `bus_schedule`
  ADD CONSTRAINT `FK` FOREIGN KEY (`busno`) REFERENCES `bus_details` (`BUS_NUMBER`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
