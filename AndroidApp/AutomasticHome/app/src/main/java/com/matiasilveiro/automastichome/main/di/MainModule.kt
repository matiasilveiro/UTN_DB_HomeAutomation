package com.matiasilveiro.automastichome.main.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.matiasilveiro.automastichome.main.data.NodesRepository
import com.matiasilveiro.automastichome.main.framework.FirebaseNodesSource
import com.matiasilveiro.automastichome.main.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@Module
@InstallIn(ActivityComponent::class)
object MainModule {

    //----------------------------
    // Data source provides
    @Provides
    fun provideFirebaseNodesSource(firestore: FirebaseFirestore, storage: FirebaseStorage) : FirebaseNodesSource{
        return FirebaseNodesSource(firestore, storage)
    }

    @Provides
    fun provideNodesRepository(firebaseNodesSource: FirebaseNodesSource): NodesRepository {
        return NodesRepository(firebaseNodesSource)
    }

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
}