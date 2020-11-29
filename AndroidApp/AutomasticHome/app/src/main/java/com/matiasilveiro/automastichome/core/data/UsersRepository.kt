package com.matiasilveiro.automastichome.core.data

import com.matiasilveiro.automastichome.core.domain.User
import com.matiasilveiro.automastichome.core.utils.MyResult
import javax.inject.Inject

class UsersRepository @Inject constructor (
    private val userDataSource : UsersDataSource
){
    suspend fun getUserByUid(uid: String): MyResult<User?> {
        return userDataSource.getUserByUid(uid)
    }

    suspend fun getCurrentUser(): MyResult<User?> {
        return userDataSource.getCurrentUser()
    }

    suspend fun modifyUser(user: User): MyResult<Boolean> {
        return userDataSource.modifyUser(user)
    }

    suspend fun createNewUser(user: User): MyResult<Boolean> {
        return userDataSource.createNewUser(user)
    }

    suspend fun createNewUser(user: User, email: String, password: String): MyResult<Boolean> {
        return userDataSource.createNewUser(user, email, password)
    }

    suspend fun loginWithEmailAndPassword(email: String, password: String): MyResult<Boolean> {
        return userDataSource.loginWithEmailAndPassword(email, password)
    }

    suspend fun sendPasswordResetEmail(email: String): MyResult<Boolean> {
        return userDataSource.sendPasswordResetEmail(email)
    }

    fun logout(): MyResult<Boolean> {
        return userDataSource.logout()
    }
    // TODO: separar repositorio de usuario y de manejo de sesion. Current user, remember, logout
}