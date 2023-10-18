package com.qadri.facecapture.ui.login

data class SignInState(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)

data class SignInState1(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)