
/*******************************************************************************
   Automastic Database - Version 0.1
   Script: Automastic_MySQL.sql
   Description: Creates and populates the Automastic database.
   DB Server: MySql
   Author: Matias Silveiro
   License: coming soon
********************************************************************************/

/*******************************************************************************
   Drop database if it exists
********************************************************************************/
DROP DATABASE IF EXISTS `Automastic`;


/*******************************************************************************
   Create database
********************************************************************************/
CREATE DATABASE `Automastic`;

USE `Automastic`;


/*******************************************************************************
   Create Tables
********************************************************************************/
CREATE TABLE `Users`
(
    `UserId` INT NOT NULL AUTO_INCREMENT,
    `Displayname` CHAR(50),
    `Email` CHAR(50),
    `Password` CHAR(50),
    `ProfileImage` CHAR(120),
    CONSTRAINT `PK_Users` PRIMARY KEY  (`UserId`)
);

CREATE TABLE `Roles`
(
    `RoleId` INT NOT NULL AUTO_INCREMENT,
    `UserId` CHAR(50) NOT NULL,
    `NodeId` INT NOT NULL,
    `Role` INT NOT NULL,
    CONSTRAINT `PK_Roles` PRIMARY KEY  (`RoleId`)
);

CREATE TABLE `Nodes_Central`
(
    `NodeId` INT NOT NULL AUTO_INCREMENT,
    `Name` CHAR(50) NOT NULL,
    `Address` CHAR(50) NOT NULL,
    `Password` CHAR(50) NOT NULL,
    `Status` CHAR(10),
    `ImageUrl` CHAR(120),
    CONSTRAINT `PK_Node` PRIMARY KEY  (`NodeId`)
);

CREATE TABLE `Nodes_Actuator`
(
    `NodeId` INT NOT NULL AUTO_INCREMENT,
    `CentralId` INT NOT NULL,
    `Name` CHAR(50),
    `Address` CHAR(50) NOT NULL,
    `ImageUrl` CHAR(120),
    `Status` CHAR(10),
    `Type` CHAR(10),
    `Value` INT,
    CONSTRAINT `PK_Actuator` PRIMARY KEY  (`NodeId`)
);


CREATE TABLE `Nodes_Sensor`
(
    `NodeId` INT NOT NULL AUTO_INCREMENT,
    `CentralId` INT NOT NULL,
    `Name` CHAR(50),
    `Address` CHAR(50) NOT NULL,
    `ImageUrl` CHAR(120),
    `Status` CHAR(10),
    `Unit` CHAR(10),
    `Value` INT,
    CONSTRAINT `PK_Actuator` PRIMARY KEY  (`NodeId`)
);

CREATE TABLE `Sensor_Actuator`
(
    `RelationId` INT NOT NULL AUTO_INCREMENT,
    `ActuatorId` INT NOT NULL,
    `SensorId` INT NOT NULL,
    `ActionId` INT NOT NULL,
    CONSTRAINT `PK_Relation` PRIMARY KEY  (`RelationId`)
);

CREATE TABLE `Control`
(
    `ActionId` INT NOT NULL AUTO_INCREMENT,
    `Name` CHAR(50),
    `ReferenceValue` INT NOT NULL,
    `ActionTrue` INT,
    `ActionFalse` INT,
    `Condition` CHAR(2) NOT NULL,
    CONSTRAINT `PK_Control` PRIMARY KEY  (`ActionId`)
);



/*******************************************************************************
   Create Primary Key Unique Indexes
********************************************************************************/

/*******************************************************************************
   Create Foreign Keys
********************************************************************************/
/*
ALTER TABLE `Roles` ADD CONSTRAINT `FK_User`
    FOREIGN KEY (`UserId`) REFERENCES `Users` (`UserId`) ON DELETE NO ACTION;
CREATE INDEX `IFK_UserRoleId` ON `Roles` (`UserId`);
*/

ALTER TABLE `Roles` ADD CONSTRAINT `FK_Node`
    FOREIGN KEY (`NodeId`) REFERENCES `Nodes_Central` (`NodeId`) ON DELETE NO ACTION;
CREATE INDEX `IFK_NodeRoleId` ON `Roles` (`NodeId`);

ALTER TABLE `Nodes_Actuator` ADD CONSTRAINT `FK_CentralActuator`
    FOREIGN KEY (`CentralId`) REFERENCES `Nodes_Central` (`NodeId`) ON DELETE NO ACTION;
CREATE INDEX `IFK_CentralActuatorId` ON `Nodes_Actuator` (`CentralId`);

ALTER TABLE `Nodes_Sensor` ADD CONSTRAINT `FK_CentralSensor`
    FOREIGN KEY (`CentralId`) REFERENCES `Nodes_Central` (`NodeId`) ON DELETE NO ACTION;
CREATE INDEX `IFK_CentralActuatorId` ON `Nodes_Sensor` (`CentralId`);

ALTER TABLE `Sensor_Actuator` ADD CONSTRAINT `FK_Sensor`
    FOREIGN KEY (`SensorId`) REFERENCES `Nodes_Sensor` (`NodeId`) ON DELETE NO ACTION;
CREATE INDEX `IFK_SensorActuatorId` ON `Sensor_Actuator` (`SensorId`);

ALTER TABLE `Sensor_Actuator` ADD CONSTRAINT `FK_Actuator`
    FOREIGN KEY (`ActuatorId`) REFERENCES `Nodes_Actuator` (`NodeId`) ON DELETE NO ACTION;
CREATE INDEX `IFK_ActuatorSensorId` ON `Sensor_Actuator` (`ActuatorId`);

ALTER TABLE `Sensor_Actuator` ADD CONSTRAINT `FK_Action`
    FOREIGN KEY (`ActionId`) REFERENCES `Control` (`ActionId`) ON DELETE NO ACTION;
CREATE INDEX `IFK_ControlId` ON `Sensor_Actuator` (`ActionId`);


/*******************************************************************************
   Populate Tables
********************************************************************************/
INSERT INTO `Users` (`Displayname`,`Email`,`Password`) VALUES (N'Matias',N'matias@gmail.com',N'holamundo');
INSERT INTO `Users` (`Displayname`,`Email`,`Password`) VALUES (N'Sergio',N'sergio@gmail.com',N'sergio');
INSERT INTO `Users` (`Displayname`,`Email`,`Password`) VALUES (N'Roberto',N'roberto@gmail.com',N'roberto');
INSERT INTO `Users` (`Displayname`,`Email`,`Password`) VALUES (N'Alejandro',N'alejandro@gmail.com',N'alejandro');


INSERT INTO `Nodes_Central` (`Name`,`Address`,`Password`,`Status`,`ImageUrl`) VALUES (N'Departamento de Mati',N'30:AE:A4:40:AE:EC',N'password',N'Online',N'https://images.pexels.com/photos/1267438/pexels-photo-1267438.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940');
INSERT INTO `Nodes_Central` (`Name`,`Address`,`Password`,`Status`,`ImageUrl`) VALUES (N'Cabaña de Sergio',N'1234-5678-9013',N'password',N'Online',N'https://images.pexels.com/photos/463734/pexels-photo-463734.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260');
INSERT INTO `Nodes_Central` (`Name`,`Address`,`Password`,`Status`,`ImageUrl`) VALUES (N'Oficina de Mana Digital',N'1234-5678-9014',N'password',N'Online',N'https://images.pexels.com/photos/1170412/pexels-photo-1170412.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940');


INSERT INTO `Roles` (`UserId`,`NodeId`,`Role`) VALUES (N'fv3uTNta01e8d8ssA4MYM2RbLGE2',1,0);  /* Matias admin Departamento de Mati */
INSERT INTO `Roles` (`UserId`,`NodeId`,`Role`) VALUES (N'czWVEMTbFGVQyBYmDlgQpAGVYcm2',1,1);  /* Sergio share  Departamento de Mati */
INSERT INTO `Roles` (`UserId`,`NodeId`,`Role`) VALUES (N'xM75CdZ1sjOxtee8GMVBq5f8wlG2',1,2);  /* Roberto share  Departamento de Mati */

INSERT INTO `Roles` (`UserId`,`NodeId`,`Role`) VALUES (N'fv3uTNta01e8d8ssA4MYM2RbLGE2',2,1);  /* Matias share Cabaña de Sergio */
INSERT INTO `Roles` (`UserId`,`NodeId`,`Role`) VALUES (N'czWVEMTbFGVQyBYmDlgQpAGVYcm2',2,0);  /* Sergio admin Cabaña de Sergio */
INSERT INTO `Roles` (`UserId`,`NodeId`,`Role`) VALUES (N'xM75CdZ1sjOxtee8GMVBq5f8wlG2',2,2);  /* Roberto invite Cabaña de Sergio */

INSERT INTO `Roles` (`UserId`,`NodeId`,`Role`) VALUES (N'fv3uTNta01e8d8ssA4MYM2RbLGE2',3,1);  /* Matias share Oficina de Mana Digital */
INSERT INTO `Roles` (`UserId`,`NodeId`,`Role`) VALUES (N'czWVEMTbFGVQyBYmDlgQpAGVYcm2',3,2);  /* Sergio invite Oficina de Mana Digital */
INSERT INTO `Roles` (`UserId`,`NodeId`,`Role`) VALUES (N'xM75CdZ1sjOxtee8GMVBq5f8wlG2',3,0);  /* Roberto admin Oficina de Mana Digital */


INSERT INTO `Nodes_Actuator` (`CentralId`,`Name`,`Address`,`Status`,`Type`,`Value`,`ImageUrl`) VALUES (1,N'TV',N'0',N'Online',N'Light',1,N'https://images.pexels.com/photos/1201996/pexels-photo-1201996.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940');     /* Id = 1 */
INSERT INTO `Nodes_Actuator` (`CentralId`,`Name`,`Address`,`Status`,`Type`,`Value`,`ImageUrl`) VALUES (1,N'Consola',N'1',N'Offline',N'ON-OFF',0,N'https://images.pexels.com/photos/687811/pexels-photo-687811.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500');    /* Id = 2 */
INSERT INTO `Nodes_Actuator` (`CentralId`,`Name`,`Address`,`Status`,`Type`,`Value`,`ImageUrl`) VALUES (1,N'Televisor',N'2',N'Online',N'ON-OFF',1,N'https://i.pinimg.com/originals/0e/5e/29/0e5e29216c932224cb4d12c8dc89d446.jpg');     /* Id = 3 */
INSERT INTO `Nodes_Actuator` (`CentralId`,`Name`,`Address`,`Status`,`Type`,`Value`,`ImageUrl`) VALUES (1,N'Ventilador',N'3',N'Online',N'Dimmer',100,N'');  /* Id = 4 */

INSERT INTO `Nodes_Actuator` (`CentralId`,`Name`,`Address`,`Status`,`Type`,`Value`,`ImageUrl`) VALUES (2,N'Mac Mini',N'1',N'Online',N'Light',0,N'https://i.blogs.es/d550b2/type-c-aluminum-stand-hub-for-mac-mini-usb-hubs-satechi-907708/450_1000.jpg'); /* Id = 5 */
INSERT INTO `Nodes_Actuator` (`CentralId`,`Name`,`Address`,`Status`,`Type`,`Value`,`ImageUrl`) VALUES (2,N'Servidor',N'2',N'Online',N'ON-OFF',1,N'');      /* Id = 6 */
INSERT INTO `Nodes_Actuator` (`CentralId`,`Name`,`Address`,`Status`,`Type`,`Value`,`ImageUrl`) VALUES (2,N'Ventilador',N'3',N'Online',N'Dimmer',120,N'');  /* Id = 7 */
INSERT INTO `Nodes_Actuator` (`CentralId`,`Name`,`Address`,`Status`,`Type`,`Value`,`ImageUrl`) VALUES (2,N'Luz exterior',N'4',N'Online',N'Light',0,N'');   /* Id = 8 */

INSERT INTO `Nodes_Actuator` (`CentralId`,`Name`,`Address`,`Status`,`Type`,`Value`,`ImageUrl`) VALUES (3,N'Mac Mini',N'0',N'Online',N'ON-OFF',1,N'https://i.blogs.es/d550b2/type-c-aluminum-stand-hub-for-mac-mini-usb-hubs-satechi-907708/450_1000.jpg');      /* Id = 9 */
INSERT INTO `Nodes_Actuator` (`CentralId`,`Name`,`Address`,`Status`,`Type`,`Value`,`ImageUrl`) VALUES (3,N'Iluminacion general',N'1',N'Offline',N'Light',0,N'');   /* Id = 10 */
INSERT INTO `Nodes_Actuator` (`CentralId`,`Name`,`Address`,`Status`,`Type`,`Value`,`ImageUrl`) VALUES (3,N'Aire acondicionado',N'2',N'Online',N'Dimmer',100,N'');  /* Id = 11 */


INSERT INTO `Nodes_Sensor` (`CentralId`,`Name`,`Address`,`Status`,`Unit`,`Value`,`ImageUrl`) VALUES (1,N'Temperatura',N'0',N'Online',N'C',27,N'');         /* Id = 1 */
INSERT INTO `Nodes_Sensor` (`CentralId`,`Name`,`Address`,`Status`,`Unit`,`Value`,`ImageUrl`) VALUES (1,N'Humedad',N'1',N'Online',N'%',30,N'');             /* Id = 2 */
INSERT INTO `Nodes_Sensor` (`CentralId`,`Name`,`Address`,`Status`,`Unit`,`Value`,`ImageUrl`) VALUES (1,N'Luminosidad',N'1',N'Online',N'Lumens',30,N'');    /* Id = 3 */
INSERT INTO `Nodes_Sensor` (`CentralId`,`Name`,`Address`,`Status`,`Unit`,`Value`,`ImageUrl`) VALUES (2,N'Luminosidad',N'1',N'Online',N'Lumens',35,N'');    /* Id = 4 */


INSERT INTO `Control` (`Name`,`ReferenceValue`,`ActionTrue`,`ActionFalse`,`Condition`) VALUES (N'Control ventilador',28,1,0,N'>=');
INSERT INTO `Control` (`Name`,`ReferenceValue`,`ActionTrue`,`ActionFalse`,`Condition`) VALUES (N'Luz automatica',20,1,0,N'<');


INSERT INTO `Sensor_Actuator` (`ActuatorId`,`SensorId`,`ActionId`) VALUES (4,1,1);
INSERT INTO `Sensor_Actuator` (`ActuatorId`,`SensorId`,`ActionId`) VALUES (7,4,1);
INSERT INTO `Sensor_Actuator` (`ActuatorId`,`SensorId`,`ActionId`) VALUES (8,4,2);

/*******************************************************************************
   Create users
********************************************************************************/

CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY 'admin1234';
GRANT ALL PRIVILEGES ON Automastic.* TO 'admin'@'localhost';