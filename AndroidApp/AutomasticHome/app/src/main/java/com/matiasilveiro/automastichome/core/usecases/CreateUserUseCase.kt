package com.manadigital.tecontactolocal.CoreFeature.usecases

import com.matiasilveiro.automastichome.core.data.UsersRepository
import com.matiasilveiro.automastichome.core.domain.User

class CreateUserUseCase (val usersRepository: UsersRepository) {
    suspend operator fun invoke(user: User) = usersRepository.createNewUser(user)
    suspend operator fun invoke(user: User, email: String, password: String) = usersRepository.createNewUser(user, email, password)
}