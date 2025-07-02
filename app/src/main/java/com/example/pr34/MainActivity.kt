package com.example.pr34

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pr34.SignInScreen
import com.example.pr34.UserViewModel
import com.example.pr34.ui.theme.PR31Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PR31Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val userViewModel: UserViewModel = viewModel()
                    LaunchedEffect(Unit) {
                        userViewModel.createInitialUsers()
                    }

                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "signIn") {
                        composable("signIn") {
                            SignInScreen(
                                navController = navController,
                                onSignInSuccess = {
                                    navController.navigate("home") {
                                        popUpTo("signIn") { inclusive = true }
                                    }
                                },
                                onRegisterClick = {
                                    navController.navigate("registerAccount")
                                },
                                onForgotPasswordClick = {
                                    navController.navigate("forgotPassword")
                                }
                            )
                        }
                        composable("registerAccount") {
                            RegisterAccountScreen(navController = navController) {
                                // После успешной регистрации, можно вернуться на экран логина
                                navController.popBackStack()
                            }
                        }
                        composable("forgotPassword") {
                            ForgotPasswordScreen(navController = navController) {
                                // После отправки, можно вернуться на экран логина
                                navController.popBackStack()
                            }
                        }
                        composable("home") {
                            HomeScreen(navController = navController)
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PR31Theme {
        // Для предпросмотра SignInScreen (нужен NavController для Preview)
        SignInScreen(rememberNavController(), onSignInSuccess = {}, onRegisterClick = {}, onForgotPasswordClick = {})
    }
}