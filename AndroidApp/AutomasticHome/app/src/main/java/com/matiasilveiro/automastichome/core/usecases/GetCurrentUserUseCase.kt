package com.manadigital.tecontactolocal.CoreFeature.usecases

import com.matiasilveiro.automastichome.core.data.UsersRepository

class GetCurrentUserUseCase (val usersRepository: UsersRepository) {
    suspend operator fun invoke() = usersRepository.getCurrentUser()
}