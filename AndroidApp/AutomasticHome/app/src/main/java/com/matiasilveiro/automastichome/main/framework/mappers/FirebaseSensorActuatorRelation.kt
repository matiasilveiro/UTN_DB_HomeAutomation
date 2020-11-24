package com.matiasilveiro.automastichome.main.framework.mappers

import com.matiasilveiro.automastichome.main.domain.SensorActuatorRelation

class FirebaseSensorActuatorRelation(var uid: String, var centralUid: String, var actuatorUid: String, var sensorUid: String, var controlUid: String) {
    constructor() : this("","","","","")
}

fun FirebaseSensorActuatorRelation.toSensorActuatorRelation(): SensorActuatorRelation = SensorActuatorRelation(uid,centralUid,actuatorUid,sensorUid,controlUid)
fun SensorActuatorRelation.toFirebaseSensorActuatorRelation(): FirebaseSensorActuatorRelation = FirebaseSensorActuatorRelation(uid,centralUid,actuatorUid,sensorUid,controlUid)
