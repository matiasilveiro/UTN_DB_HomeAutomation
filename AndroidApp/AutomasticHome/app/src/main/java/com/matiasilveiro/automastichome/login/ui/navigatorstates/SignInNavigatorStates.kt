package com.matiasilveiro.automastichome.login.ui.navigatorstates

sealed class SignInNavigatorStates {
    object ToMainApplication: SignInNavigatorStates()
    object ToSignUp: SignInNavigatorStates()
}