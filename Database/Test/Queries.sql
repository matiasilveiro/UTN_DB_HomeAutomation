
/*******************************************************************************
   User Repository queries
********************************************************************************/

-- fun userIsAdmin(userId: Int, centralId: Int)
SELECT * FROM Nodes_Central
JOIN Roles ON (Nodes_Central.NodeId = Roles.NodeId) AND (Nodes_Central.NodeId = centralId) AND (userId = Roles.UserId)
HAVING Role = 0;

-- fun userIsShared(userId: Int, centralId: Int)
SELECT * FROM Nodes_Central
JOIN Roles ON (Nodes_Central.NodeId = Roles.NodeId) AND (Nodes_Central.NodeId = centralId) AND (userId = Roles.UserId)
HAVING Role = 1;

-- fun userIsInvited(userId: Int, centralId: Int)
SELECT * FROM Nodes_Central
JOIN Roles ON (Nodes_Central.NodeId = Roles.NodeId) AND (Nodes_Central.NodeId = centralId) AND (userId = Roles.UserId)
HAVING Role = 2;

-- fun userCanInteract(userId: Int, centralId: Int)
SELECT * FROM Nodes_Central
JOIN Roles ON (Nodes_Central.NodeId = Roles.NodeId) AND (Nodes_Central.NodeId = centralId) AND (userId = Roles.UserId)
HAVING Role = 0 OR Role = 1;


/*******************************************************************************
   Central nodes queries
********************************************************************************/
-- fun getCentralNodesByUser(userId: Int)
SELECT Nodes_Central.NodeId, Name, Address, Password, Roles.Role FROM Nodes_Central
JOIN Roles ON (Nodes_Central.NodeId = Roles.NodeId) AND (userId = Roles.UserId);

-- fun createCentralNode(name: String, pswd: String, address: String)
INSERT INTO Nodes_Central (Name, Password, Address) VALUES (name, pswd, address);

-- fun addCentralNodeToUser(nodeId: Int, userId: Int, role: Int)
INSERT INTO Roles (UserId, NodeId, Role) VALUES (userId, nodeId, role);

-- fun setCentralNodeName(id: Int, userId: Int, name: String)
userIsAdmin(userId, id);
UPDATE Nodes_Central SET Name = name WHERE NodeId = 2 AND FOUND_ROWS() > 0;

-- fun setCentralNodePassword(id: Int, pswd: String)
userIsAdmin(userId, id);
UPDATE Nodes_Central SET Password = pswd WHERE NodeId = id;

-- fun deleteCentralNode(id: Int)
userIsAdmin(userId, id);
DELETE FROM Nodes_Central WHERE NodeId = id;



/*******************************************************************************
   Remote actuator nodes queries
********************************************************************************/
-- fun getRemoteActuatorsByCentralNode(id: Int)
SELECT * FROM Nodes_Actuator WHERE (CentralId = id);

-- fun setRemoteActuatorValue(id: Int, value: Int)
UPDATE Nodes_Actuator SET Value = value WHERE NodeId = id;

-- fun addRemoteActuator(centralId: Int, name: String, pswd: String, address: String, type: String)
INSERT INTO Nodes_Actuator (CentralId, Name, Address, Status, Type, Value) VALUES (centralId, name, address, 'Online', type, 0);

-- fun setRemoteActuatorName(id: Int, userId: Int, name: String)
userIsAdmin(userId, id);
UPDATE Nodes_Actuator SET Name = name WHERE NodeId = id;

-- fun setRemoteActuatorPassword(id: Int, userId: Int, pswd: String)
userIsAdmin(userId, id);
UPDATE Nodes_Actuator SET Password = pswd WHERE NodeId = id;

-- fun deleteRemoteActuator(id: Int, userId: Int)
userIsAdmin(userId, id);
DELETE FROM Nodes_Actuator WHERE NodeId = id;


/*******************************************************************************
   Controls queries
********************************************************************************/
-- fun getControlsByCentralId(id: Int)
SELECT * FROM Control WHERE ActionId IN (SELECT ActionId FROM Sensor_Actuator WHERE ActuatorId IN (SELECT NodeId FROM Nodes_Actuator WHERE CentralId = id));

-- fun addControl(name: String, referenceValue: Int, actionTrueValue: Int, actionFalseValue: Int, condition: String)
INSERT INTO Control (Name, ReferenceValue, ActionTrue, ActionFalse, Condition) VALUES (name, referenceValue, actionTrueValue, actionFalseValue, condition);

-- fun assignControlWithActuatorSensor(controlId: Int, actuatorId: Int, sensorId: Int)
INSERT INTO Sensor_Actuator (ActuatorId, SensorId, ActionId) VALUES (controlId, actuatorId, sensorId);

-- fun unassignControlWithActuatorSensor(controlId: Int, actuatorId: Int, sensorId: Int)
DELETE FROM Sensor_Actuator WHERE (ActionId = control Id) AND (ActuatorId = actuatorId) AND (SensorId = sensorId);

-- fun deleteControl(controlId: Int)
DELETE FROM Sensor_Actuator WHERE ActionId = controlId;
DELETE FROM Control WHERE ActionId = controlId;