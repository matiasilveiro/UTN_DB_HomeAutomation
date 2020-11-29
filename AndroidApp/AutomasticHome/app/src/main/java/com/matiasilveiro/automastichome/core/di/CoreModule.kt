package com.matiasilveiro.automastichome.core.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.manadigital.tecontactolocal.CoreFeature.usecases.*
import com.matiasilveiro.automastichome.core.data.UsersRepository
import com.matiasilveiro.automastichome.core.framework.FirebaseUsersSource
import com.matiasilveiro.automastichome.core.usecases.LoginWithEmailAndPasswordUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object CoreModule{

    @Singleton
    @Provides
    fun providesFirestore(): FirebaseFirestore = Firebase.firestore

    @Singleton
    @Provides
    fun providesFireauth(): FirebaseAuth = Firebase.auth

    @Singleton
    @Provides
    fun providesStorage(): FirebaseStorage = Firebase.storage


    //----------------------------
    // Data source provides
    @Provides
    fun provideFirebaseUserSource(firestore: FirebaseFirestore, storage: FirebaseStorage, auth: FirebaseAuth): FirebaseUsersSource {
        return FirebaseUsersSource(firestore, storage, auth)
    }

    @Provides
    fun provideUsersRepository(firebaseUsersSource: FirebaseUsersSource): UsersRepository {
        return UsersRepository(firebaseUsersSource)
    }

    //----------------------------
    // Usecases provides
    @Provides
    fun provideGetUserByUid(usersRepository: UsersRepository): GetUserByUidUseCase = GetUserByUidUseCase(usersRepository)

    @Provides
    fun provideGetCurrentUser(usersRepository: UsersRepository): GetCurrentUserUseCase = GetCurrentUserUseCase(usersRepository)

    @Provides
    fun provideModifyUser(usersRepository: UsersRepository): ModifyUserUseCase = ModifyUserUseCase(usersRepository)

    @Provides
    fun provideCreateUser(usersRepository: UsersRepository): CreateUserUseCase = CreateUserUseCase(usersRepository)

    @Provides
    fun provideLoginWithEmailAndPassword(usersRepository: UsersRepository): LoginWithEmailAndPasswordUseCase = LoginWithEmailAndPasswordUseCase(usersRepository)

    @Provides
    fun provideSendPasswordResetEmail(usersRepository: UsersRepository): SendPasswordResetEmailUseCase = SendPasswordResetEmailUseCase(usersRepository)

    @Provides
    fun provideLogout(usersRepository: UsersRepository): LogoutUseCase = LogoutUseCase(usersRepository)
}