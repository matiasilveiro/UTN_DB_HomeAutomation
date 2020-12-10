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

def getCentralAddrByRemoteActuator(actuatorId: int):
    try:
        query = ("""SELECT Nodes_Central.Address FROM Nodes_Central WHERE NodeId IN (SELECT CentralId FROM Nodes_Actuator WHERE NodeId = %s);""")
        cursor.execute(query, (actuatorId,))
        result = cursor.fetchone()
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

def createRemoteActuator(node: str, centralAddr: str = ""):
    try:
        if(len(centralAddr) > 0):
            centralNode = getCentralNodesByAddr(centralAddr)[0]
            centralAddr = centralNode['NodeId']
        else:
            node = json.loads(node)
            centralAddr = node['centralId']
        query = ("""INSERT INTO Nodes_Actuator (CentralId, Name, Address, ImageUrl, Status, Type, Value) VALUES (%s, %s, %s, %s, %s, %s, %s);""")
        cursor.execute(query, (centralAddr,node['name'],node['address'],node['imageUrl'],node['status'],node['type'],int(node['value']),))

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


def setRemoteActuatorStatus(id: int, status: str):
    try:
        query = ("""UPDATE Nodes_Actuator SET Status = %s WHERE NodeId = %s;""")
        cursor.execute(query, (status,id,))

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


def createRemoteSensor(node: str, centralAddr: str = ""):
    try:
        if(len(centralAddr) > 0):
            centralNode = getCentralNodesByAddr(centralAddr)[0]
            centralAddr = centralNode['NodeId']
        else:
            node = json.loads(node)
            centralAddr = node['centralId']
        query = ("""INSERT INTO Nodes_Sensor (CentralId, Name, Address, ImageUrl, Status, Unit, Value) VALUES (%s, %s, %s, %s, %s, %s, %s);""")
        cursor.execute(query, (centralAddr,node['name'],node['address'],node['imageUrl'],node['status'],node['unit'],int(node['value']),))

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

def getControlsByCentralId(centralId: int):
    query = ("""SELECT * FROM Control WHERE ActionId IN (SELECT ActionId FROM Sensor_Actuator WHERE ActuatorId IN (SELECT NodeId FROM Nodes_Actuator WHERE CentralId = %s));""")
    
    cursor.execute(query, (centralId,))
    result = cursor.fetchall()
    return result

def getControlsByCentralAddr(centralAddr: str):
    query = ("""SELECT * FROM Control WHERE ActionId IN (SELECT ActionId FROM Sensor_Actuator WHERE ActuatorId IN (SELECT NodeId FROM Nodes_Actuator WHERE CentralId IN (SELECT NodeId FROM Nodes_Central WHERE Address = %s)));""")
    
    cursor.execute(query, (centralAddr,))
    result = cursor.fetchall()
    return result

def setControl(controL: str):
    try:
        # TODO
        return True
    except Exception as ex:
        print(ex)
        return False

def createControl(control: str, actuatorId: int, sensorId: int):
    try:
        control = json.loads(control)
        query = ("""INSERT INTO Control (Name, ReferenceValue, ActionTrue, ActionFalse, `Condition`) VALUES (%s, %s, %s, %s, %s);""")
        cursor.execute(query, (control['name'],int(control['referenceValue']),int(control['actionTrue']),int(control['actionFalse']),control['condition'],))

        query = ("""INSERT INTO Sensor_Actuator (ActuatorId,SensorId,ActionId) VALUES (%s,%s,LAST_INSERT_ID());""")
        cursor.execute(query, (actuatorId,sensorId,))

        central_node = getCentralAddrByRemoteActuator(actuatorId)
        address = central_node['Address']
        topic = 'utn_pf/{}/controls/new'.format(address)
        msg = json.dumps(control)
        print(topic, msg)
        client.publish(topic=topic, payload=msg, qos=2)

        return True
    except Exception as ex:
        print(ex)
        return False


def assignControlWithActuatorSensor(controlId: int, actuatorId: int, sensorId: int):
    try:
        query = ("""INSERT INTO Sensor_Actuator (ActuatorId, SensorId, ActionId) VALUES (%s, %s, %s);""")
        cursor.execute(query, (controlId,sensorId,actuatorId,))

        return True
    except Exception as ex:
        print(ex)
        return False


def unassignControlWithActuatorSensor(controlId: int, actuatorId: int, sensorId: int):
    try:
        query = ("""DELETE FROM Sensor_Actuator WHERE (ActionId = %s) AND (ActuatorId = %s) AND (SensorId = %s);""")
        cursor.execute(query, (controlId,actuatorId,sensorId,))

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
    
    elif(field == 'new'):
        node = json.loads(msg)
        nodeType = node['type']
        node['name'] = 'Nuevo nodo'
        node['imageUrl'] = 'https://cutt.ly/NhTMI4r'
        node['type'] = 'ON-OFF'
        node['unit'] = 'TODO'
        if(nodeType == 'Actuator'):
            print("New actuator: {} at addr {}".format(node['name'], node['address']))
            createRemoteActuator(node, central_node)
        else:
            print("New sensor: {} at addr {}".format(node['name'], node['address']))
            createRemoteActuator(node, central_node)
    
    elif(field == 'get'):
        query = msg
        if(query == 'controls'):
            controls = getControlsByCentralAddr(central_node)
            topic = 'utn_pf/{}/controls/list'.format(central_node)
            for control in controls:
                msg = json.dumps(control)
                client.publish(topic=topic, payload=msg, qos=2)
            
    
    elif(field.isdigit()):
        remote_node = field
        field = topic[4]
        if(field == 'value'):
            setRemoteSensorValueByAddr(int(remote_node), int(msg))
        elif(field == 'status'):
            setRemoteActuatorStatus(int(remote_node),msg)
            print('Node status: {}'.format(msg))




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
