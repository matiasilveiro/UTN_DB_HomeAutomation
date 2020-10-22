package com.matiasilveiro.automastichome.core.data

import com.matiasilveiro.automastichome.core.domain.User
import com.matiasilveiro.automastichome.core.utils.MyResult


interface UsersDataSource {
    suspend fun getUserByUid(uid: String): MyResult<User?>
    suspend fun getCurrentUser(): MyResult<User?>
    suspend fun modifyUser(user: User): MyResult<Boolean>
    suspend fun createNewUser(user: User): MyResult<Boolean>
    suspend fun createNewUser(user: User, email: String, password: String): MyResult<Boolean>
    suspend fun loginWithEmailAndPassword(email: String, password: String): MyResult<Boolean>
    suspend fun sendPasswordResetEmail(email: String): MyResult<Boolean>
}