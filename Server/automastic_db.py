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
    query = ("SELECT Nodes_Central.NodeId, Name, Address, Password, Roles.Role FROM Nodes_Central" 
            "JOIN Roles ON (Nodes_Central.NodeId = Roles.NodeId) AND (%s = Roles.UserId);"
            )
    
    cursor.execute(query, (userId,))
    result = cursor.fetchall()
    return result