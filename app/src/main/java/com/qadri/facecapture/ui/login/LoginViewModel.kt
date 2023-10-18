package com.qadri.facecapture.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qadri.facecapture.data.AuthRepository
import com.qadri.facecapture.model.LoginDetails
import com.qadri.facecapture.model.LoginResponse
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
class LoginViewModel @Inject constructor(
    private val loginRepository: AuthRepository
) : ViewModel() {

    private val _signInState = Channel<SignInState>()
    val signInState = _signInState.receiveAsFlow()

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

    fun loginUser(email: String, password: String) = viewModelScope.launch {
        loginRepository.loginUser(email, password).collect { result ->
            when (result) {
                is Resource.Error -> {
                    _signInState.send(SignInState(isError = result.message))
                }

                is Resource.Loading -> {
                    _signInState.send(SignInState(isLoading = true))
                }

                is Resource.Success -> {
                    _signInState.send(SignInState(isSuccess = "Sign In Success"))
                }
            }
        }
    }

    private val _loginUiState = MutableStateFlow(LoginDetails())
    val loginUiState = _loginUiState.asStateFlow()

    fun updateUiState(loginDetails: LoginDetails) {
        _loginUiState.value = loginDetails
    }

    private var _loginResponse = MutableStateFlow(LoginResponse())
    val loginResponse = _loginResponse.asStateFlow()

//    fun fetchToken() {
//        viewModelScope.launch {
//            _loginResponse.value = try {
//                loginRepository.login(_loginUiState.value)
//            } catch (ex: ConnectException) {
//                LoginResponse(message = "Network Error")
//            }
//        }
//    }
}
