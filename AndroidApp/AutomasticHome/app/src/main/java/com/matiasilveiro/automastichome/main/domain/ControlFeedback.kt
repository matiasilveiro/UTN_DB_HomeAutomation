package com.matiasilveiro.automastichome.main.domain

data class ControlFeedback(
        var uid: String,
        var name: String,
        var referenceValue: Int,
        var actionTrue: Int,
        var actionFalse: Int,
        var condition: String
)
