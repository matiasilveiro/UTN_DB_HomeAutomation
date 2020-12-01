#!/usr/bin/env python
# -*- coding: utf-8 -*-
from flask import Flask, jsonify
from flask_restful import Resource, Api
import automastic_db as db
import os

app = Flask(__name__)
api = Api(app)
ASSETS_DIR = os.path.dirname(os.path.abspath(__file__))


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


@api.resource('/remote_actuators/centralId=<centralId>')
class RemoteActuatorsByCentral(Resource):
    def get(self, centralId):
        result = db.getRemoteActuatorsByCentralId(centralId)
        return jsonify(result)

@api.resource('/remote_sensors/centralId=<centralId>')
class RemoteSensorsByCentral(Resource):
    def get(self, centralId):
        result = db.getRemoteSensorsByCentralId(centralId)
        return jsonify(result)

if __name__ == '__main__':
    context = ('server.crt', 'server.key')#certificate and key files
    app.run(host='192.168.56.1', debug=True, ssl_context=context)

# Certificate and key files generated with method 2:
# https://kracekumar.com/post/54437887454/ssl-for-flask-local-development/