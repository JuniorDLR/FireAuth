package com.junaxer.firelogin.ui.signup

import com.junaxer.firelogin.ui.login.LoginProcessState

data class SignUpState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val warningError: String? = null,
    val loginProcessState: LoginProcessState = LoginProcessState.Idl
)
