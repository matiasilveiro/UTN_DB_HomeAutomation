package com.matiasilveiro.automastichome.main.usecases

import com.matiasilveiro.automastichome.core.utils.MyResult
import com.matiasilveiro.automastichome.main.data.NodesRepository
import com.matiasilveiro.automastichome.main.domain.CentralNode

class CreateCentralNodeRoleUseCase (private val nodesRepository: NodesRepository) {
    suspend operator fun invoke(user: String, node: String, role: Int): MyResult<Boolean> {
        return nodesRepository.createCentralNodeRole(user, node, role)
    }
}