package com.matiasilveiro.automastichome.core.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

val <T> T.exhaustive: T
    get() = this

fun View.snack(message: String, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, message, duration).show()
}

fun View.setVisible(){
    this.visibility = View.VISIBLE
}

fun View.setInvisible(){
    this.visibility = View.INVISIBLE
}

fun View.setEnabled(){
    this.isEnabled = true
}

fun View.disable(){
    this.isEnabled = false
}

fun String.isEmailValid(): Boolean{
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isPhoneValid(): Boolean{
    return android.util.Patterns.PHONE.matcher(this).matches()
}

fun String.isWEB_URL_Valid(): Boolean{
    return android.util.Patterns.WEB_URL.matcher(this).matches()
}