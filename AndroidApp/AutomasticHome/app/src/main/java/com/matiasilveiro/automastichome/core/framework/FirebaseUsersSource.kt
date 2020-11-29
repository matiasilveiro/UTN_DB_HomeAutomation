package com.matiasilveiro.automastichome.core.framework

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.matiasilveiro.automastichome.core.data.UsersDataSource
import com.matiasilveiro.automastichome.core.domain.User
import com.matiasilveiro.automastichome.core.framework.mappers.FirebaseUser
import com.matiasilveiro.automastichome.core.framework.mappers.toUser
import com.matiasilveiro.automastichome.core.utils.MyResult
import com.matiasilveiro.automastichome.main.domain.*
import com.matiasilveiro.automastichome.main.framework.mappers.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseUsersSource @Inject constructor(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth
) : UsersDataSource {


    companion object Collections {
        const val USERS = "Users"
    }

    override suspend fun createNewUser(user: User): MyResult<Boolean> {
        try {
            db.collection(USERS).document(user.uid).set(user).await()

            Log.d("FirebaseUserSource", "User created with uid ${user.uid}")
        } catch (e: Exception) {
            Log.w("FirebaseUserSource", e)
            return MyResult.Failure(e)
        }
        return MyResult.Success(true)
    }

    override suspend fun createNewUser(user: User, email: String, password: String): MyResult<Boolean> {
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val profileUpdater = UserProfileChangeRequest.Builder()
                .setDisplayName(user.name)
                .build()
            result.user?.updateProfile(profileUpdater)?.await()

            val reference = db.collection(USERS).add(user).await()
            user.uid = result.user!!.uid
            db.collection("UsersShop").document(reference.id).set(user).await()

            Log.d("FirebaseUserSource", "Shop created with uid ${reference.id}")
        } catch (e: Exception) {
            Log.w("FirebaseUserSource", e)
            return MyResult.Failure(e)
        }
        return MyResult.Success(true)
    }

    override suspend fun getUserByUid(uid: String): MyResult<User?> {
        return try {
            val document = db.collection(USERS).document(uid).get().await()
            if(document.exists()) {
                Log.d("FirebaseUserSource", "User retrieved with uid ${document.id}")
                val user = document.toObject<FirebaseUser>()?.toUser()!!
                MyResult.Success(user)
            } else {
                MyResult.Success(null)
            }
        } catch (e: Exception) {
            Log.w("FirebaseUserSource", e)
            MyResult.Failure(e)
        }
    }

    override suspend fun getCurrentUser(): MyResult<User?> {
        return try {
            val currentUser = auth.currentUser
            val document = db.collection(USERS).document(currentUser!!.uid).get().await()
            if(document.exists()) {
                Log.d("FirebaseUserSource", "User retrieved with uid ${document.id}")
                val user = document.toObject<FirebaseUser>()?.toUser()!!
                MyResult.Success(user)
            } else {
                MyResult.Success(null)
            }
        } catch (e: Exception) {
            Log.w("FirebaseUserSource", e)
            MyResult.Failure(e)
        }
    }

    override suspend fun modifyUser(user: User): MyResult<Boolean> {
        return try {
            db.collection(USERS).document(user.uid).set(user).await()

            Log.d("FirebaseUserSource", "User modified with uid ${user.uid}")
            MyResult.Success(true)
        } catch (e: Exception) {
            Log.w("FirebaseUserSource", e)
            MyResult.Failure(e)
        }
    }

    override suspend fun loginWithEmailAndPassword(email: String, password: String): MyResult<Boolean> {
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.let {
                return MyResult.Success(true)
            }
        } catch (e: Exception) {
            Log.w("FirebaseUserSource", e)
            return MyResult.Failure(e)
        }
        return MyResult.Success(false)
    }

    override suspend fun sendPasswordResetEmail(email: String): MyResult<Boolean> {
        try {
            val result = auth.sendPasswordResetEmail(email).await()
            result?.let {
                return MyResult.Success(true)
            }
            return MyResult.Success(true)
        } catch (e: Exception) {
            Log.w("FirebaseUserSource", e)
            return MyResult.Failure(e)
        }
    }

    override fun logout(): MyResult<Boolean> {
        return try {
            auth.signOut()
            MyResult.Success(true)
        } catch (e: Exception) {
            Log.w("FirebaseUserSource", e)
            MyResult.Failure(e)
        }
    }
}