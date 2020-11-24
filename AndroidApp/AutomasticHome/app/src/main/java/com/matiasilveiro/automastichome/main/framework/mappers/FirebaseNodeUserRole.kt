package com.matiasilveiro.automastichome.main.framework.mappers

import com.matiasilveiro.automastichome.main.domain.NodeUserRole

data class FirebaseNodeUserRole(val uid: String, val userUid: String, val centralUid: String, val role: Int)
{
    constructor() : this("","","",0)
}

fun FirebaseNodeUserRole.toNodeUserRole(): NodeUserRole = NodeUserRole(uid,userUid,centralUid,role)
fun NodeUserRole.toFirebaseNodeUserRole(): FirebaseNodeUserRole = FirebaseNodeUserRole(uid,userUid,centralUid,role)