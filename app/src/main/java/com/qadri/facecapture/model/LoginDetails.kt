package com.qadri.facecapture.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginDetails(
    val login: String = "",
    val password: String = ""
)

@Serializable
data class LoginResponse(
    @SerialName("user-token")
    val userToken: String? = "",
    @SerialName("userStatus")
    val userStatus: String? = "",
    val email: String? = "",
    val message: String? = ""
)
