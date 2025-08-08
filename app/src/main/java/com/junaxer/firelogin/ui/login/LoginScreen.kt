package com.junaxer.firelogin.ui.login


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToDetail: () -> Unit,
    onNavigateToRegister: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .safeContentPadding(), contentAlignment = Alignment.Center
    ) {
        LoginBody(
            viewModel = viewModel,
            onNavigateToDetail = onNavigateToDetail,
            onNavigateToRegister = onNavigateToRegister
        )

    }
}

@Composable
fun LoginBody(
    viewModel: LoginViewModel,
    onNavigateToDetail: () -> Unit,
    onNavigateToRegister: () -> Unit
) {


    val loginUIState by viewModel.loginUIState.collectAsState()
    val warningError = loginUIState.warningError
    val loginProcessState = loginUIState.loginProcessState
    val isLoading = loginProcessState == LoginProcessState.Loading
    val emailError = loginUIState.emailError
    val passwordError = loginUIState.passwordError



    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item { Welcome() }

        item {
            InputLoginShared(
                inputLoginData = InputLoginData(
                    value = loginUIState.email,
                    warningError = emailError,
                    placeholder = "Correo",
                    icon = Icons.Default.Email,
                    isLoading = isLoading,
                    onValueChange = { newValue ->
                        viewModel.onEmailChanged(email = newValue)
                    },
                )
            )
        }
        item {
            InputLoginShared(
                inputLoginData = InputLoginData(
                    value = loginUIState.password,
                    warningError = passwordError,
                    placeholder = "Contraseña",
                    icon = Icons.Default.Lock,
                    isPassword = true,
                    isLoading = isLoading,
                    onValueChange = { newValue ->
                        viewModel.onPasswordChanged(password = newValue)
                    },
                )
            )
        }

        if (warningError != null) {
            item {
                Text(
                    text = warningError,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
        }

        item {
            LoginButton(
                isLoading = isLoading,
                onLoginClicked = {
                    viewModel.login(onNavigateToDetail)
                })
        }
        item {
            RegisterText(
                onRegisterClick = {
                    onNavigateToRegister()
                })
        }


    }


}


@Composable
fun RegisterText(onRegisterClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "¿No tienes una cuenta? ",
            color = Color.Black,
            fontSize = 16.sp,
            modifier = Modifier.alignByBaseline()
        )
        Spacer(modifier = Modifier.padding(horizontal = 5.dp))
        Text(
            text = "Regístrate",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .clickable { onRegisterClick() }
                .alignByBaseline()
        )
    }
}


@Composable
fun LoginButton(onLoginClicked: () -> Unit, isLoading: Boolean) {
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onLoginClicked,
            enabled = !isLoading,
            border = BorderStroke(width = 1.dp, color = Color.Transparent),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor =MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 3.dp
                )
            } else {
                Text(text = "Iniciar sesión", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun InputLoginShared(
    inputLoginData: InputLoginData
) {

    val (value,
        placeholder,
        warningError,
        isPassword,
        icon,
        isLoading,
        onValueChange) = inputLoginData
    OutlinedTextField(
        value,
        onValueChange,
        Modifier
            .fillMaxWidth(),
        placeholder = { Text(text = placeholder, color = Color.Gray.copy(alpha = 0.8f)) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = placeholder,
                tint = Color.Gray
            )
        },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = true,
        maxLines = 1,
        minLines = 1,
        isError = warningError != null,
        supportingText = {
            if (warningError != null) {
                Text(
                    text = warningError,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
            }
        },
        enabled = !isLoading,
        colors = OutlinedTextFieldDefaults.colors(
            errorBorderColor = MaterialTheme.colorScheme.error,
            unfocusedTextColor = Color.Gray,
            focusedTextColor = Color.Black,
            unfocusedBorderColor = Color.Gray,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
        )
    )
}


@Composable
fun Welcome() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(size = 128.dp)
                .background(color = Color.White, shape = CircleShape)
                .padding(all = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = "Logo",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(size = 80.dp)
            )
        }

        Text(
            text = "Bienvenida",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Inicia sesión para continuar",
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black.copy(alpha = 0.8f)
        )
    }
}

