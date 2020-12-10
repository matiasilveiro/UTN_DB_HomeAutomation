package com.matiasilveiro.automastichome.main.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.matiasilveiro.automastichome.main.data.NodesRepository
import com.matiasilveiro.automastichome.main.framework.FirebaseNodesSource
import com.matiasilveiro.automastichome.main.framework.RetrofitNodesSource
import com.matiasilveiro.automastichome.main.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object CoreModule {

    //----------------------------
    // Data source provides
    @Singleton
    @Provides
    fun provideFirebaseNodesSource(firestore: FirebaseFirestore, storage: FirebaseStorage) : FirebaseNodesSource{
        return FirebaseNodesSource(firestore, storage)
    }

    @Singleton
    @Provides
    fun provideRetrofitNodesSource(): RetrofitNodesSource {
        return RetrofitNodesSource()
    }

    @Singleton
    @Provides
    fun provideNodesRepository(firebaseNodesSource: FirebaseNodesSource, retrofitNodesSource: RetrofitNodesSource): NodesRepository {
        return NodesRepository(firebaseNodesSource, retrofitNodesSource)
    }
}

@Module
@InstallIn(ActivityComponent::class)
object MainModule {

    //----------------------------
    // Use cases provides
    @Provides
    fun provideGetCentralNodesUseCase(repository: NodesRepository): GetCentralNodesUseCase {
        return GetCentralNodesUseCase(repository)
    }

    @Provides
    fun provideGetRemoteNodesUseCase(repository: NodesRepository): GetRemoteNodesUseCase {
        return GetRemoteNodesUseCase(repository)
    }
    @Provides
    fun provideGetControlsUseCase(repository: NodesRepository): GetControlsUseCase {
        return GetControlsUseCase(repository)
    }

    @Provides
    fun provideCreateCentralRoleUseCase(repository: NodesRepository): CreateCentralNodeRoleUseCase {
        return CreateCentralNodeRoleUseCase(repository)
    }

    @Provides
    fun provideManageActuatorNodesUseCase(repository: NodesRepository): ManageActuatorNodesUseCase {
        return ManageActuatorNodesUseCase(repository)
    }

    @Provides
    fun provideManageCentralNodeUseCase(repository: NodesRepository): ManageCentralNodeUseCase {
        return ManageCentralNodeUseCase(repository)
    }

    @Provides
    fun provideManageSensorNodesUseCase(repository: NodesRepository): ManageSensorNodesUseCase {
        return ManageSensorNodesUseCase(repository)
    }

    @Provides
    fun provideManageControlsUseCase(repository: NodesRepository): ManageControlsUseCase {
        return ManageControlsUseCase(repository)
    }
}