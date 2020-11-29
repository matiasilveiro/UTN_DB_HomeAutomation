package com.manadigital.tecontactolocal.CoreFeature.usecases

import com.matiasilveiro.automastichome.core.data.UsersRepository
import com.matiasilveiro.automastichome.core.domain.User

class ModifyUserUseCase  (val usersRepository: UsersRepository) {
    suspend operator fun invoke(user: User) = usersRepository.modifyUser(user)
}