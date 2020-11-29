package com.manadigital.tecontactolocal.CoreFeature.usecases

import com.matiasilveiro.automastichome.core.data.UsersRepository


class LogoutUseCase(val usersRepository: UsersRepository) {
    operator fun invoke() = usersRepository.logout()
}