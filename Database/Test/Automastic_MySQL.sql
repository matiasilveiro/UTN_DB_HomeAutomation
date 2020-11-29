
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
    CONSTRAINT `PK_Users` PRIMARY KEY  (`UserId`)
);

CREATE TABLE `Roles`
(
    `RoleId` INT NOT NULL AUTO_INCREMENT,
    `UserId` INT NOT NULL,
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
    CONSTRAINT `PK_Node` PRIMARY KEY  (`NodeId`)
);

CREATE TABLE `Nodes_Actuator`
(
    `NodeId` INT NOT NULL AUTO_INCREMENT,
    `CentralId` INT NOT NULL,
    `Name` CHAR(50),
    `Address` CHAR(50) NOT NULL,
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
ALTER TABLE `Roles` ADD CONSTRAINT `FK_User`
    FOREIGN KEY (`UserId`) REFERENCES `Users` (`UserId`) ON DELETE NO ACTION;
CREATE INDEX `IFK_UserRoleId` ON `Roles` (`UserId`);

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


INSERT INTO `Nodes_Central` (`Name`,`Address`,`Password`) VALUES (N'Departamento de Mati',N'1234-5678-9012',N'password');
INSERT INTO `Nodes_Central` (`Name`,`Address`,`Password`) VALUES (N'Cabaña de Sergio',N'1234-5678-9013',N'password');
INSERT INTO `Nodes_Central` (`Name`,`Address`,`Password`) VALUES (N'Oficina de Mana Digital',N'1234-5678-9014',N'password');


INSERT INTO `Roles` (`UserId`,`NodeId`,`Role`) VALUES (1,1,0);  /* Matias admin Departamento de Mati */
INSERT INTO `Roles` (`UserId`,`NodeId`,`Role`) VALUES (2,1,1);  /* Sergio share  Departamento de Mati */
INSERT INTO `Roles` (`UserId`,`NodeId`,`Role`) VALUES (3,1,2);  /* Roberto share  Departamento de Mati */

INSERT INTO `Roles` (`UserId`,`NodeId`,`Role`) VALUES (1,2,1);  /* Matias share Cabaña de Sergio */
INSERT INTO `Roles` (`UserId`,`NodeId`,`Role`) VALUES (2,2,0);  /* Sergio admin Cabaña de Sergio */
INSERT INTO `Roles` (`UserId`,`NodeId`,`Role`) VALUES (4,2,2);  /* Alejandro invite Cabaña de Sergio */

INSERT INTO `Roles` (`UserId`,`NodeId`,`Role`) VALUES (1,3,1);  /* Matias share Oficina de Mana Digital */
INSERT INTO `Roles` (`UserId`,`NodeId`,`Role`) VALUES (3,3,2);  /* Roberto invite Oficina de Mana Digital */
INSERT INTO `Roles` (`UserId`,`NodeId`,`Role`) VALUES (4,3,0);  /* Alejandro admin Oficina de Mana Digital */


INSERT INTO `Nodes_Actuator` (`CentralId`,`Name`,`Address`,`Status`,`Type`,`Value`) VALUES (1,N'Luz cocina',N'0',N'online',N'Light',1);     /* Id = 1 */
INSERT INTO `Nodes_Actuator` (`CentralId`,`Name`,`Address`,`Status`,`Type`,`Value`) VALUES (1,N'Luz living',N'1',N'offline',N'Light',0);    /* Id = 2 */
INSERT INTO `Nodes_Actuator` (`CentralId`,`Name`,`Address`,`Status`,`Type`,`Value`) VALUES (1,N'Televisor',N'2',N'online',N'ON-OFF',1);     /* Id = 3 */
INSERT INTO `Nodes_Actuator` (`CentralId`,`Name`,`Address`,`Status`,`Type`,`Value`) VALUES (1,N'Ventilador',N'3',N'online',N'Dimmer',100);  /* Id = 4 */

INSERT INTO `Nodes_Actuator` (`CentralId`,`Name`,`Address`,`Status`,`Type`,`Value`) VALUES (2,N'Luz habitacion',N'1',N'online',N'Light',0); /* Id = 5 */
INSERT INTO `Nodes_Actuator` (`CentralId`,`Name`,`Address`,`Status`,`Type`,`Value`) VALUES (2,N'Servidor',N'2',N'online',N'ON-OFF',1);      /* Id = 6 */
INSERT INTO `Nodes_Actuator` (`CentralId`,`Name`,`Address`,`Status`,`Type`,`Value`) VALUES (2,N'Ventilador',N'3',N'online',N'Dimmer',120);  /* Id = 7 */
INSERT INTO `Nodes_Actuator` (`CentralId`,`Name`,`Address`,`Status`,`Type`,`Value`) VALUES (2,N'Luz exterior',N'4',N'online',N'Light',0);   /* Id = 8 */

INSERT INTO `Nodes_Actuator` (`CentralId`,`Name`,`Address`,`Status`,`Type`,`Value`) VALUES (3,N'Cafetera',N'0',N'online',N'ON-OFF',1);      /* Id = 9 */
INSERT INTO `Nodes_Actuator` (`CentralId`,`Name`,`Address`,`Status`,`Type`,`Value`) VALUES (3,N'Iluminacion general',N'1',N'offline',N'Light',0);   /* Id = 10 */
INSERT INTO `Nodes_Actuator` (`CentralId`,`Name`,`Address`,`Status`,`Type`,`Value`) VALUES (3,N'Ventilador',N'2',N'online',N'Dimmer',100);  /* Id = 11 */


INSERT INTO `Nodes_Sensor` (`CentralId`,`Name`,`Address`,`Status`,`Unit`,`Value`) VALUES (1,N'Temperatura',N'0',N'online',N'C',27);         /* Id = 1 */
INSERT INTO `Nodes_Sensor` (`CentralId`,`Name`,`Address`,`Status`,`Unit`,`Value`) VALUES (1,N'Humedad',N'1',N'online',N'%',30);             /* Id = 2 */
INSERT INTO `Nodes_Sensor` (`CentralId`,`Name`,`Address`,`Status`,`Unit`,`Value`) VALUES (1,N'Luminosidad',N'1',N'online',N'Lumens',30);    /* Id = 3 */
INSERT INTO `Nodes_Sensor` (`CentralId`,`Name`,`Address`,`Status`,`Unit`,`Value`) VALUES (2,N'Luminosidad',N'1',N'online',N'Lumens',35);    /* Id = 4 */


INSERT INTO `Control` (`Name`,`ReferenceValue`,`ActionTrue`,`ActionFalse`,`Condition`) VALUES (N'Control ventilador',28,1,0,N'>=');
INSERT INTO `Control` (`Name`,`ReferenceValue`,`ActionTrue`,`ActionFalse`,`Condition`) VALUES (N'Luz automatica',20,1,0,N'<');


INSERT INTO `Sensor_Actuator` (`ActuatorId`,`SensorId`,`ActionId`) VALUES (4,1,1);
INSERT INTO `Sensor_Actuator` (`ActuatorId`,`SensorId`,`ActionId`) VALUES (8,4,1);

/*******************************************************************************
   Create users
********************************************************************************/

CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin1234';
GRANT ALL PRIVILEGES ON Automastic.* TO 'admin'@'localhost';