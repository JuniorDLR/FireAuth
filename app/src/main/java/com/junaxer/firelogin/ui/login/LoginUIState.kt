package com.junaxer.firelogin.ui.login

data class LoginUIState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val warningError: String? = null,
    val loginProcessState: LoginProcessState = LoginProcessState.Idl
)

sealed class LoginProcessState {
    data object Idl : LoginProcessState()
    data object Loading : LoginProcessState()

}
