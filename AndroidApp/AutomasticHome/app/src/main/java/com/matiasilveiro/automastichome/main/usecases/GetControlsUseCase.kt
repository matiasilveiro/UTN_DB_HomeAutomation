package com.matiasilveiro.automastichome.main.usecases

import com.matiasilveiro.automastichome.core.utils.MyResult
import com.matiasilveiro.automastichome.main.data.NodesRepository
import com.matiasilveiro.automastichome.main.domain.ControlFeedback

class GetControlsUseCase (private val nodesRepository: NodesRepository) {
    suspend operator fun invoke(uid: String): MyResult<ArrayList<ControlFeedback>?> {
        return nodesRepository.getRemoteControlsByCentral(uid)
    }
}