package com.matiasilveiro.automastichome.core.framework.mappers

import com.matiasilveiro.automastichome.core.domain.User


class FirebaseUser(var uid: String, var name: String, var email: String, var profileImage: String) {
    constructor() : this("","","","")
}

fun FirebaseUser.toUser(): User = User(uid,name,email,profileImage)
fun User.toFirebaseUser(): FirebaseUser = FirebaseUser(uid,name,email,profileImage)