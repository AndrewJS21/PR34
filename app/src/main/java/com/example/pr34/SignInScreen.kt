package com.example.pr34

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pr34.ui.theme.PR31Theme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    navController: NavController,
    onSignInSuccess: () -> Unit,
    onRegisterClick: () -> Unit, // Новый колбэк
    onForgotPasswordClick: () -> Unit // Новый колбэк
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val userViewModel: UserViewModel = viewModel()

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Привет!",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Заполните Свои Данные Или",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Продолжите Через Социальные Медиа",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() && it.isNotEmpty()
                },
                label = { Text("Email") },
                isError = emailError,
                supportingText = {
                    if (emailError) {
                        Text("Некорректный email")
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = it.length < 6 && it.isNotEmpty()
                },
                label = { Text("Пароль") },
                isError = passwordError,
                supportingText = {
                    if (passwordError) {
                        Text("Пароль должен быть не менее 6 символов")
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),

                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Восстановить",
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { onForgotPasswordClick() } // Используем новый колбэк
                    .padding(top = 8.dp),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    passwordError = password.length < 6

                    if (!emailError && !passwordError && email.isNotEmpty() && password.isNotEmpty()) {
                        scope.launch {
                            val isAuthenticated = userViewModel.authenticateUser(email, password)
                            if (isAuthenticated) {
                                onSignInSuccess()
                            } else {
                                snackbarHostState.showSnackbar("Неверный email или пароль")
                            }
                        }
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Пожалуйста, заполните все поля корректно")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Войти", color = MaterialTheme.colorScheme.onPrimary)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Вы впервые?")
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Создать пользователя",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onRegisterClick() } // Используем новый колбэк
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignInScreen() {
    PR31Theme {
        SignInScreen(rememberNavController(), onSignInSuccess = {}, onRegisterClick = {}, onForgotPasswordClick = {})
    }
}