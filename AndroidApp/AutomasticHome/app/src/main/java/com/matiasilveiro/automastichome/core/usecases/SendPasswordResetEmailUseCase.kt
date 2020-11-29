package com.manadigital.tecontactolocal.CoreFeature.usecases

import com.matiasilveiro.automastichome.core.data.UsersRepository
import com.matiasilveiro.automastichome.core.utils.MyResult

class SendPasswordResetEmailUseCase (val usersRepository: UsersRepository) {
    suspend operator fun invoke(email: String): MyResult<Boolean> = usersRepository.sendPasswordResetEmail(email)
}