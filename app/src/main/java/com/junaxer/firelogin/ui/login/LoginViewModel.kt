package com.junaxer.firelogin.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junaxer.firelogin.data.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException // Importa esta excepción específica si quieres manejarla de forma particular

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    private val _loginUIState = MutableStateFlow(value = LoginUIState())
    val loginUIState: StateFlow<LoginUIState> = _loginUIState

    fun login(navigateToDetail: () -> Unit) {

        viewModelScope.launch {
            val email = _loginUIState.value.email
            val password = _loginUIState.value.password


            _loginUIState.update { loginUIState ->
                loginUIState.copy(
                    loginProcessState = LoginProcessState.Loading,
                    warningError = null
                )
            }

            try {
                delay(timeMillis = 2000)

                if (email.isBlank() && password.isBlank()) {
                    _loginUIState.update { loginUIState ->
                        loginUIState.copy(
                            passwordError = "Contraseña vacia!",
                            emailError = "Correo vacio!"
                        )
                    }
                    return@launch
                }

                if (email.isBlank()) {
                    _loginUIState.update { loginUIState ->
                        loginUIState.copy()
                    }
                    return@launch
                }

                if (password.isBlank()) {
                    _loginUIState.update { loginUIState ->
                        loginUIState.copy(passwordError = "Contraseña vacia!")
                    }
                    return@launch
                }

                val result = withContext(Dispatchers.IO) {
                    authService.login(
                        email = email,
                        password = password
                    )
                }
                if (result != null) {
                    navigateToDetail()
                }
            } catch (e: Exception) {

                if (e is FirebaseAuthInvalidCredentialsException) {
                    _loginUIState.update { loginUIState ->
                        loginUIState.copy(warningError = "Credenciales incorrectas")
                    }
                } else {
                    _loginUIState.update { loginUIState ->
                        loginUIState.copy(warningError = "Credenciales incorrectas")
                    }
                }
            } finally {
                _loginUIState.update { loginUIState -> loginUIState.copy(loginProcessState = LoginProcessState.Idl) }
            }
        }
    }

    fun onEmailChanged(email: String) {
        _loginUIState.update { loginUIState ->
            loginUIState.copy(
                email = email
            )
        }
    }

    fun onPasswordChanged(password: String) {
        _loginUIState.update { loginUIState ->
            loginUIState.copy(
                password = password
            )
        }
    }
}