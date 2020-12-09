package com.matiasilveiro.automastichome.main.framework.mappers

import com.matiasilveiro.automastichome.main.domain.CentralNode

class FirebaseCentralNode(val uid: String, val name: String, val address: String, val password: String, var status: String, var imageUrl: String) {
    constructor() : this("","","","","","")
}

fun FirebaseCentralNode.toCentralNode(): CentralNode = CentralNode(uid,name,address,password,status,imageUrl,2)
fun CentralNode.toFirebaseCentralNode(): FirebaseCentralNode = FirebaseCentralNode(uid,name,address,password,status,imageUrl)