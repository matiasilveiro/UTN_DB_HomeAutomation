package com.matiasilveiro.automastichome.main.framework.mappers

import com.matiasilveiro.automastichome.main.domain.ControlFeedback

class FirebaseControlFeedback(
        val uid: String,
        val name: String,
        val referenceValue: Int,
        val actionTrue: Int,
        val actionFalse: Int,
        val condition: String
)
{
    constructor() : this("","",0,0,0,"")
}

fun FirebaseControlFeedback.toControlFeedback(): ControlFeedback = ControlFeedback(uid,name,referenceValue,actionTrue,actionFalse,condition)
fun ControlFeedback.toFirebaseControlFeedback(): FirebaseControlFeedback = FirebaseControlFeedback(uid,name,referenceValue,actionTrue,actionFalse,condition)