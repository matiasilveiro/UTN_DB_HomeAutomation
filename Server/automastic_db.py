#!/usr/bin/env python
# -*- coding: utf-8 -*-
import mysql.connector
import defs

db = mysql.connector.connect(host=defs.HOST,
                             user=defs.USER,
                             passwd=defs.PASSWORD,
                             db=defs.DBASE)

cursor = db.cursor(dictionary=True)

def getCentralNodesByUser(userId: int):
    query = ("""SELECT Nodes_Central.NodeId, Name, Address, Password, ImageUrl, Roles.Role FROM Nodes_Central 
                JOIN Roles ON (Nodes_Central.NodeId = Roles.NodeId) AND (Roles.UserId = %s);"""
            )
    
    cursor.execute(query, (userId,))
    result = cursor.fetchall()
    return result


def getRemoteActuatorsByCentralId(centralId: int):
    query = ("""SELECT * FROM Nodes_Actuator WHERE (CentralId = %s);""")
    
    cursor.execute(query, (centralId,))
    result = cursor.fetchall()
    return result

def getRemoteSensorsByCentralId(centralId: int):
    query = ("""SELECT * FROM Nodes_Sensor WHERE (CentralId = %s);""")
    
    cursor.execute(query, (centralId,))
    result = cursor.fetchall()
    return result