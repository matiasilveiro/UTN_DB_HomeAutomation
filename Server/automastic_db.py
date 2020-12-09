#!/usr/bin/env python
# -*- coding: utf-8 -*-
import mysql.connector
import paho.mqtt.client as mqtt
import defs
import json
import atexit

import time
from apscheduler.schedulers.background import BackgroundScheduler

#--------------------------------
# Job dispatcher

sched = BackgroundScheduler()
sched.start()


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
    #print("message received " ,str(message.payload.decode("utf-8")))
    #print("message topic=",message.topic)
    #print("message qos=",message.qos)
    #print("message retain flag=",message.retain)
    decodeMqttMessage( message.topic, str(message.payload.decode("utf-8")) )

def on_mqtt_log(client, userdata, level, buf):
    print("log: ",buf)


client = mqtt.Client()
client.on_message = on_mqtt_message
#client.on_log=on_mqtt_log
client.connect(defs.BROKER)

client.subscribe(topic='utn_pf/db_broadcast/#')
#client.publish(topic='utn_pf/30:AE:A4:40:AE:EC/refresh',payload='Hello world!')
client.loop_start()


@atexit.register
def closeAutomasticDB():
    cursor.close()
    db.close()
    client.loop_stop()
    sched.shutdown()


#------------------------------------------------------------------------------------

def getCentralNodes():
    try:
        query = ("""SELECT * FROM Nodes_Central;""")
        cursor.execute(query)
        result = cursor.fetchall()
        return result
    except Exception as ex:
        print(ex)
        return ""

def getCentralNodesByAddr(addr: str):
    try:
        query = ("""SELECT Nodes_Central.NodeId, Name, Address, Password, Status, ImageUrl FROM Nodes_Central WHERE (Address = %s);""")
        cursor.execute(query, (addr,))
        result = cursor.fetchall()
        return result
    except Exception as ex:
        print(ex)
        return ""

def getCentralNodesByUser(userId: int):
    try:
        query = ("""SELECT Nodes_Central.NodeId, Name, Address, Password, Status, ImageUrl, Roles.Role FROM Nodes_Central 
                    JOIN Roles ON (Nodes_Central.NodeId = Roles.NodeId) AND (Roles.UserId = %s);"""
                )
        cursor.execute(query, (userId,))
        result = cursor.fetchall()
        return result
    except Exception as ex:
        print(ex)
        return ""
    
def setCentralNodeStatus(address: str, status: str):
    try:
        query = ("""UPDATE Nodes_Central SET Status = %s WHERE Address = %s;""")
        cursor.execute(query, (status,address,))

        return True
    except Exception as ex:
        print(ex)
        return False

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


def createCentralNodeRole(userId: str, centralId: str, role: int):
    try:
        query = ("""INSERT INTO Roles (`UserId`,`NodeId`,`Role`) VALUES (%s,%s,%s);""")
        cursor.execute(query, (userId,centralId,role,))

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

        query = ("""SELECT Nodes_Actuator.Address as RemoteAddress, Nodes_Central.Address as CentralAddress, Value FROM Nodes_Actuator
                LEFT JOIN Nodes_Central ON (Nodes_Actuator.CentralId = Nodes_Central.NodeId) WHERE (Nodes_Actuator.NodeId = %s);""")
        cursor.execute(query, (id,))
        result = cursor.fetchall()
        node = result[0]
        topic = 'utn_pf/{}/{}/set'.format(node['CentralAddress'],node['RemoteAddress'])
        client.publish(topic=topic, payload=node['Value'], qos=2)

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

def setRemoteSensorValueByAddr(addr: int, value: int):
    try:
        query = ("""UPDATE Nodes_Sensor SET Value = %s WHERE Address = %s;""")
        cursor.execute(query, (value,addr,))

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

def decodeMqttMessage(topic: str, msg: str):
    print(topic)
    topic = topic.split('/')
    central_node = topic[2]
    field = topic[3]
    #print('DecodeMQTT: {}, {}: {}'.format(central_node,field,msg))

    if(field == 'status'):
        setCentralNodeStatus(central_node, msg)
    elif(field.isdigit()):
        remote_node = field
        field = topic[4]
        setRemoteSensorValueByAddr(int(remote_node), int(msg))




def sendMqttStatusRequest(centralAddress: str):
    topic = 'utn_pf/{}/config/status'.format(centralAddress)
    client.publish(topic=topic, payload='dummy', qos=2)


#------------------------------------------------------------------------------------


@sched.scheduled_job('interval',seconds=5)
def timed_job_status():
    print('\n>> Updating central nodes status')
    nodes_list = getCentralNodes()
    for node in nodes_list:
        #print("Node: {}".format(node))
        setCentralNodeStatus(node['Address'], 'Offline')
        sendMqttStatusRequest(node['Address'])
