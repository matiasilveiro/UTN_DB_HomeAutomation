package com.matiasilveiro.automastichome.core.usecases

import com.matiasilveiro.automastichome.core.data.UsersRepository

class LoginWithEmailAndPasswordUseCase (val usersRepository: UsersRepository) {
    suspend operator fun invoke(email: String, password: String) = usersRepository.loginWithEmailAndPassword(email, password)
}