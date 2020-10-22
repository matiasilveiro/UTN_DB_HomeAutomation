package com.matiasilveiro.automastichome.login.ui.navigatorstates

sealed class SplashNavigatorStates {
    object ToSignIn: SplashNavigatorStates()
    object ToMainApplication: SplashNavigatorStates()
}