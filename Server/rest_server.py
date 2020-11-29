#!/usr/bin/env python
# -*- coding: utf-8 -*-
from flask import Flask, jsonify
from flask_restful import Resource, Api
import automastic_db as db

app = Flask(__name__)
api = Api(app)


@api.resource('/')
class AutomasticDB(Resource):
    def get(self):
        msg = "Hello from AutomasticDB Team"
        return msg


@api.resource('/central_nodes/<userId>')
class CentralNodeById(Resource):
    def get(self, userId):
        result = db.getCentralNodesByUser(userId)
        return jsonify(result)


if __name__ == '__main__':
    app.run(debug=True)
