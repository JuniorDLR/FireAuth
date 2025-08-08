package com.junaxer.firelogin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.junaxer.firelogin.ui.detail.DetailScreen
import com.junaxer.firelogin.ui.login.LoginScreen
import com.junaxer.firelogin.ui.signup.SignUpScreen
import com.junaxer.firelogin.ui.theme.FireLoginTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navHostController = rememberNavController()
            FireLoginTheme {
                NavHost(navController = navHostController, startDestination = "Login") {
                    composable(route = "Login") {
                        LoginScreen(
                            onNavigateToDetail = {
                                navHostController.navigate("Detail")
                            }, onNavigateToRegister = {
                                navHostController.navigate("Register")
                            }
                        )
                    }

                    composable(route = "Detail") { DetailScreen() }
                    composable(route = "Register") {
                        SignUpScreen(
                            onNavigateToLogin = {
                                navHostController.navigate("Login")
                            }
                        )
                    }
                }
            }
        }
    }
}