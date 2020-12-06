#!/usr/bin/env python
# -*- coding: utf-8 -*-
import mysql.connector
import paho.mqtt.client as mqtt
import defs
import json
import atexit

#--------------------------------
# MySQL connector and cursor

db = mysql.connector.connect(host=defs.HOST,
                             user=defs.USER,
                             passwd=defs.PASSWORD,
                             db=defs.DBASE, 
                             autocommit=True)

cursor = db.cursor(dictionary=True)


#--------------------------------
# MQTT Client and connection

def on_mqtt_message(client, userdata, message):
    print("message received " ,str(message.payload.decode("utf-8")))
    print("message topic=",message.topic)
    print("message qos=",message.qos)
    print("message retain flag=",message.retain)

def on_mqtt_log(client, userdata, level, buf):
    print("log: ",buf)


client = mqtt.Client("Automastic MQTT")
client.on_message = on_mqtt_message
#client.on_log=on_mqtt_log
client.connect(defs.BROKER)

client.loop_start()
client.subscribe(topic='utn_pf/#')



@atexit.register
def closeAutomasticDB():
    cursor.close()
    db.close()
    client.loop_stop()


#------------------------------------------------------------------------------------

def getCentralNodesByUser(userId: int):
    try:
        query = ("""SELECT Nodes_Central.NodeId, Name, Address, Password, ImageUrl, Roles.Role FROM Nodes_Central 
                    JOIN Roles ON (Nodes_Central.NodeId = Roles.NodeId) AND (Roles.UserId = %s);"""
                )
        cursor.execute(query, (userId,))
        result = cursor.fetchall()
        return result
    except Exception as ex:
        print(ex)
        return ""

def setCentralNode(node: str):
    try:
        node = json.loads(node)
        query = ("""UPDATE Nodes_Central SET Name = %s, Password = %s, Address = %s, ImageUrl = %s WHERE NodeId = %s;""")
        cursor.execute(query, (node['name'],node['password'],node['address'],node['imageUrl'],int(node['uid']),))
        topic = 'utn_pf/{}/set'.format(node['address'])
        client.publish(topic=topic, payload=node, qos=2)

        return True
    except Exception as ex:
        print(ex)
        return False

def deleteCentralNode(id: int):
    try:
        query = ("""DELETE Nodes_Central WHERE NodeId = %s;""")
        cursor.execute(query, (id,))

        return True
    except Exception as ex:
        print(ex)
        return False


#------------------------------------------------------------------------------------

def getRemoteActuatorsByCentralId(centralId: int):
    query = ("""SELECT * FROM Nodes_Actuator WHERE (CentralId = %s);""")
    
    cursor.execute(query, (centralId,))
    result = cursor.fetchall()
    return result

def createRemoteActuator(node: str):
    try:
        query = ("""INSERT INTO Nodes_Actuator (CentralId, Name, Address, ImageUrl, Status, Type, Value) VALUES (%s, %s, %s, %s, %s, %s, %s);""")
        node = json.loads(node)
        cursor.execute(query, (node['centralUid'],node['name'],node['address'],node['imageUrl'],node['status'],node['type'],int(node['value']),))

        return True
    except Exception as ex:
        print(ex)
        return False
    

def setRemoteActuator(node: str):
    try:
        node = json.loads(node)
        query = ("""UPDATE Nodes_Actuator SET Name = %s, ImageUrl = %s, Status = %s WHERE NodeId = %s;""")
        cursor.execute(query, (node['name'],node['imageUrl'],node['status'],int(node['uid']),))

        return True
    except Exception as ex:
        print(ex)
        return False


def setRemoteActuatorValue(id: int, value: int):
    try:
        query = ("""UPDATE Nodes_Actuator SET Value = %s WHERE NodeId = %s;""")
        cursor.execute(query, (value,id,))
        topic = 'utn_pf/centralAddress/{}/value'.format(str(id))
        client.publish(topic=topic, payload=str(value), qos=2)

        return True
    except Exception as ex:
        print(ex)
        return False


def deleteRemoteActuator(id: int):
    try:
        query = ("""DELETE Nodes_Actuator WHERE NodeId = %s;""")
        cursor.execute(query, (id,))

        return True
    except Exception as ex:
        print(ex)
        return False


#------------------------------------------------------------------------------------

def getRemoteSensorsByCentralId(centralId: int):
    query = ("""SELECT * FROM Nodes_Sensor WHERE (CentralId = %s);""")
    
    cursor.execute(query, (centralId,))
    result = cursor.fetchall()
    return result


def createRemoteSensor(node: str):
    try:
        query = ("""INSERT INTO Nodes_Sensor (CentralId, Name, Address, ImageUrl, Status, Unit, Value) VALUES (%s, %s, %s, %s, %s, %s, %s);""")
        node = json.loads(node)
        cursor.execute(query, (node['centralUid'],node['name'],node['address'],node['imageUrl'],node['status'],node['unit'],int(node['value']),))

        return True
    except Exception as ex:
        print(ex)
        return False
    

def setRemoteSensor(node: str):
    try:
        node = json.loads(node)
        query = ("""UPDATE Nodes_Sensor SET Name = %s, ImageUrl = %s, Status = %s, Unit = %s WHERE NodeId = %s;""")
        cursor.execute(query, (node['name'],node['imageUrl'],node['status'],node['unit'],int(node['uid']),))

        return True
    except Exception as ex:
        print(ex)
        return False


def setRemoteSensorValue(id: int, value: int):
    try:
        query = ("""UPDATE Nodes_Sensor SET Value = %s WHERE NodeId = %s;""")
        cursor.execute(query, (value,id,))

        return True
    except Exception as ex:
        print(ex)
        return False


def deleteRemoteSensor(id: int):
    try:
        query = ("""DELETE Nodes_Sensor WHERE NodeId = %s;""")
        cursor.execute(query, (id,))

        return True
    except Exception as ex:
        print(ex)
        return False


#------------------------------------------------------------------------------------
