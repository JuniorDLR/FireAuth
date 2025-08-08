package com.junaxer.firelogin.ui.login

import androidx.compose.ui.graphics.vector.ImageVector

data class InputLoginData(
    val value: String,
    val placeholder: String,
    val warningError: String? = null,
    val isPassword: Boolean = false,
    val icon: ImageVector,
    val isLoading: Boolean,
    val onValueChange: (String) -> Unit
)
