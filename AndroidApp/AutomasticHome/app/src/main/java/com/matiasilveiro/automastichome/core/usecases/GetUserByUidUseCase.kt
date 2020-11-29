package com.manadigital.tecontactolocal.CoreFeature.usecases

import com.matiasilveiro.automastichome.core.data.UsersRepository

class GetUserByUidUseCase  (val usersRepository: UsersRepository) {
    suspend operator fun invoke(uid: String) = usersRepository.getUserByUid(uid)
}