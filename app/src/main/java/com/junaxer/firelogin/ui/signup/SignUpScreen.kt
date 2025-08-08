package com.junaxer.firelogin.ui.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.junaxer.firelogin.ui.login.LoginProcessState

@Composable
fun SignUpHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Crear Nueva Cuenta",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "¡Regístrate para empezar!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Composable
fun EmailInputField(
    email: String,
    onEmailChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        label = { Text("Correo Electrónico") },
        leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        modifier = modifier.fillMaxWidth(),
        isError = isError,
        supportingText = {
            if (isError && errorMessage != null) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            }
        },
        singleLine = true
    )
}

@Composable
fun PasswordInputField(
    password: String,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Contraseña",
    isError: Boolean = false,
    errorMessage: String? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text(label) },
        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password Icon") },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = description)
            }
        },
        modifier = modifier.fillMaxWidth(),
        isError = isError,
        supportingText = {
            if (isError && errorMessage != null) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            }
        },
        singleLine = true
    )
}


@Composable
fun SignUpButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isEnabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = isEnabled && !isLoading,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            disabledContainerColor =MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text("Registrarse", style = MaterialTheme.typography.titleMedium)
        }
    }
}


@Composable
fun LoginPrompt(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("¿Ya tienes una cuenta?", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Inicia Sesión",
            modifier = Modifier.clickable(onClick = onLoginClick),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit

) {
    val signUpState by viewModel.signUpUIState.collectAsState()
    val email = signUpState.email
    val emailError = signUpState.emailError
    val password = signUpState.password
    val passwordError = signUpState.passwordError
    val confirmPassword = signUpState.confirmPassword
    val confirmPasswordError = signUpState.confirmPasswordError
    val warningError = signUpState.warningError
    val isLoading = signUpState.loginProcessState == LoginProcessState.Loading


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Encabezado
        SignUpHeader()

        Spacer(modifier = Modifier.height(32.dp))


        EmailInputField(
            email = email,
            onEmailChange = {
                viewModel.onEmailChanged(it)
            },
            isError = emailError != null,
            errorMessage = emailError
        )

        Spacer(modifier = Modifier.height(16.dp))


        PasswordInputField(
            password = password,
            onPasswordChange = {
                viewModel.onPasswordChanged(it)
            },
            isError = passwordError != null,
            errorMessage = passwordError
        )

        Spacer(modifier = Modifier.height(16.dp))


        PasswordInputField(
            password = confirmPassword,
            onPasswordChange = {
                viewModel.onConfirmPasswordChanged(it)
            },
            label = "Confirmar Contraseña",
            isError = confirmPasswordError != null,
            errorMessage = confirmPasswordError
        )

        Spacer(modifier = Modifier.height(32.dp))

        warningError?.let {
            Text(
                text = it,
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                color = Color.Red,
                textAlign = TextAlign.Center
            )
        }
        SignUpButton(
            onClick = {
                viewModel.signup(onNavigateToLogin)
            },
            isLoading = isLoading,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Enlace para iniciar sesión
        LoginPrompt(onLoginClick = onNavigateToLogin)
    }
}


