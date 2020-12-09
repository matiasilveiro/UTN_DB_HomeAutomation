#!/usr/bin/env python
# -*- coding: utf-8 -*-
from flask import Flask, jsonify, request
from flask_restful import Resource, Api
import automastic_db as db
import json
import os

app = Flask(__name__)
api = Api(app)
ASSETS_DIR = os.path.dirname(os.path.abspath(__file__))

class Result():
    def __init__(self, status, message):
        self.status = status
        self.message = message

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__, 
            sort_keys=True, indent=4)


#-------------------------------------------------------------------------


@api.resource('/')
class AutomasticDB(Resource):
    def get(self):
        msg = "Hello from AutomasticDB Team"
        return msg


@api.resource('/central_nodes/userId=<userId>')
class CentralNodeById(Resource):
    def get(self, userId):
        result = db.getCentralNodesByUser(userId)
        return jsonify(result)

    def put(self, userId):
        jsonNode = request.form['jsonNode']
        result = db.setCentralNode(jsonNode)
        if(result):
            return Result('success','created successfully').toJSON()
        else:
            return Result('failure','operation failed').toJSON()
    
    def delete(self, userId):
        id = int(request.form['nodeId'])
        result = db.deleteCentralNode(id)
        if(result):
            return Result('success','created successfully').toJSON()
        else:
            return Result('failure','operation failed').toJSON()


#-------------------------------------------------------------------------


@api.resource('/remote_actuators/centralId=<centralId>')
class RemoteActuatorsByCentral(Resource):
    def get(self, centralId):
        result = db.getRemoteActuatorsByCentralId(centralId)
        return jsonify(result)

    def post(self, centralId):
        jsonNode = request.form['jsonNode']
        result = db.createRemoteActuator(jsonNode)
        if(result):
            return Result('success','created successfully').toJSON()
        else:
            return Result('failure','operation failed').toJSON()

    def put(self, centralId):
        jsonNode = request.form['jsonNode']
        result = db.setRemoteActuator(jsonNode)
        if(result):
            return Result('success','created successfully').toJSON()
        else:
            return Result('failure','operation failed').toJSON()
    
    def delete(self, centralId):
        id = int(request.form['nodeId'])
        result = db.deleteRemoteActuator(id)
        if(result):
            return Result('success','created successfully').toJSON()
        else:
            return Result('failure','operation failed').toJSON()



@app.route('/remote_actuators/new',methods=['POST'])
def createRemoteActuator():
    jsonNode = request.form['jsonNode']
    result = db.createRemoteActuator(jsonNode)
    if(result):
        return Result('success','created successfully').toJSON()
    else:
        return Result('failure','operation failed').toJSON()


@app.route('/remote_actuators/value',methods=['PUT'])
def setRemoteActuatorValue():
    id = int(request.form['id'])
    value = int(request.form['value'])
    result = db.setRemoteActuatorValue(id,value)
    if(result):
        return Result('success','modified successfully').toJSON()
    else:
        return Result('failure','operation failed').toJSON()


#-------------------------------------------------------------------------


@api.resource('/remote_sensors/centralId=<centralId>')
class RemoteSensorsByCentral(Resource):
    def get(self, centralId):
        result = db.getRemoteSensorsByCentralId(centralId)
        return jsonify(result)

    def put(self, centralId):
        jsonNode = request.form['jsonNode']
        result = db.setRemoteSensor(jsonNode)
        if(result):
            return Result('success','created successfully').toJSON()
        else:
            return Result('failure','operation failed').toJSON()
    
    def delete(self, centralId):
        id = int(request.form['nodeId'])
        result = db.deleteRemoteSensor(id)
        if(result):
            return Result('success','created successfully').toJSON()
        else:
            return Result('failure','operation failed').toJSON()


@app.route('/remote_sensors/new',methods=['POST'])
def createRemoteSensor():
    jsonNode = request.form['jsonNode']
    result = db.createRemoteSensor(jsonNode)
    if(result):
        return Result('success','created successfully').toJSON()
    else:
        return Result('failure','operation failed').toJSON()


@app.route('/remote_sensors/value',methods=['PUT'])
def setRemoteSensorValue():
    id = int(request.form['id'])
    value = int(request.form['value'])
    result = db.setRemoteSensorValue(id,value)
    if(result):
        return Result('success','modified successfully').toJSON()
    else:
        return Result('failure','operation failed').toJSON()


#-------------------------------------------------------------------------


if __name__ == '__main__':
    context = ('server.crt', 'server.key')
    app.run(host='192.168.56.1', ssl_context=context)

# Certificate and key files generated with method 2:
# https://kracekumar.com/post/54437887454/ssl-for-flask-local-development/