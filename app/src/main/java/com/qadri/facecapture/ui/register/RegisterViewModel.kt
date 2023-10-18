package com.qadri.facecapture.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qadri.facecapture.data.AuthRepository
import com.qadri.facecapture.model.RegisterDetails
import com.qadri.facecapture.model.RegisterResponse
import com.qadri.facecapture.ui.login.SignInResult
import com.qadri.facecapture.ui.login.SignInState1
import com.qadri.facecapture.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val loginRepository: AuthRepository
): ViewModel() {

    private val _signUpState = Channel<RegisterState>()
    val signUpState = _signUpState.receiveAsFlow()

    private val _state = MutableStateFlow(SignInState1())
    val state = _state.asStateFlow()

    fun onSignInWithGoogleResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    fun resetState() {
        _state.update { SignInState1() }
    }

    fun registerUser(email: String, password: String) = viewModelScope.launch {
        loginRepository.registerUser(email, password).collect{ result ->
            when(result) {
                is Resource.Error -> {
                    _signUpState.send(RegisterState(isError = result.message))
                }
                is Resource.Loading -> {
                    _signUpState.send(RegisterState(isLoading = true))
                }
                is Resource.Success -> {
                    _signUpState.send(RegisterState(isSuccess = "Register Success"))
                }
            }
        }
    }

    private val _registerState = MutableStateFlow(RegisterDetails())
    val registerState = _registerState.asStateFlow()

    fun updateUiState(registerDetails: RegisterDetails) {
        _registerState.value = registerDetails
    }

    private var _registerResponse = MutableStateFlow(RegisterResponse())
    val registerResponse = _registerResponse.asStateFlow()

//    fun registerAction() {
//        viewModelScope.launch {
//            _registerResponse.value = try {
//                repository.register(_registerState.value)
//            } catch (ex: ConnectException) {
//                RegisterResponse(message = "Network Error")
//            }
//        }
//    }
}