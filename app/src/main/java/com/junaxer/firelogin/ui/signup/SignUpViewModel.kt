package com.junaxer.firelogin.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junaxer.firelogin.data.AuthService
import com.junaxer.firelogin.ui.login.LoginProcessState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    private val _signUpUIState = MutableStateFlow(value = SignUpState())
    val signUpUIState: StateFlow<SignUpState> = _signUpUIState

    fun signup(onNavigateToLogin: () -> Unit) {

        viewModelScope.launch {
            val email = _signUpUIState.value.email
            val password = _signUpUIState.value.password
            val confirmPassword = _signUpUIState.value.confirmPassword


            _signUpUIState.update { signUpState ->
                signUpState.copy(
                    loginProcessState = LoginProcessState.Loading,
                    warningError = null
                )
            }

            try {
                delay(timeMillis = 2000)

                if (email.isBlank() && password.isBlank() && confirmPassword.isBlank()) {
                    _signUpUIState.update { signUpState ->
                        signUpState.copy(
                            passwordError = "Contraseña vacia!",
                            emailError = "Correo vacio!",
                            confirmPasswordError = "confirmacion vacia!",
                            loginProcessState = LoginProcessState.Idl,
                        )
                    }
                    return@launch
                }

                if (email.isBlank()) {
                    _signUpUIState.update { signUpState ->
                        signUpState.copy()
                    }
                    return@launch
                }

                if (password.isBlank()) {
                    _signUpUIState.update { signUpState ->
                        signUpState.copy(
                            passwordError = "Contraseña vacia!",
                            loginProcessState = LoginProcessState.Idl
                        )
                    }
                    return@launch
                }

                if (confirmPassword.isBlank()) {
                    _signUpUIState.update { signUpState ->
                        signUpState.copy(
                            confirmPasswordError = "Confirmacion de contraseña vacia!",
                            loginProcessState = LoginProcessState.Idl,
                        )
                    }
                    return@launch
                }

                if (confirmPassword != password) {
                    _signUpUIState.update { signUpState ->
                        signUpState.copy(
                            confirmPasswordError = "La contraseña no coincide!",
                            loginProcessState = LoginProcessState.Idl,
                        )
                    }
                    return@launch
                }

                val result = withContext(Dispatchers.IO) {
                    authService.signUp(email = email, password = confirmPassword)
                }

                if (result != null) {
                    _signUpUIState
                    onNavigateToLogin()
                }
            } catch (e: Exception) {
                _signUpUIState.update { signUpState -> signUpState.copy(warningError = e.message.orEmpty()) }

            } finally {
                _signUpUIState.update { signUpState -> signUpState.copy(loginProcessState = LoginProcessState.Idl) }
            }
        }
    }

    fun onEmailChanged(email: String) {
        _signUpUIState.update { signUpState ->
            signUpState.copy(
                email = email
            )
        }
    }

    fun onPasswordChanged(password: String) {
        _signUpUIState.update { signUpState ->
            signUpState.copy(
                password = password
            )
        }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _signUpUIState.update { loginUIState ->
            loginUIState.copy(
                confirmPassword = confirmPassword
            )
        }
    }
}