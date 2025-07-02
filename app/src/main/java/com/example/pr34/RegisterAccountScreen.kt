package com.example.pr34

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pr34.data.User
import com.example.pr34.ui.theme.PR31Theme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterAccountScreen(navController: NavController, onRegistrationSuccess: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var agreeToTerms by remember { mutableStateOf(false) }

    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var termsError by remember { mutableStateOf(false) } // Для валидации согласия

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val userViewModel: UserViewModel = viewModel()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Регистрация") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Регистрация",
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
                value = name,
                onValueChange = {
                    name = it
                    nameError = it.isBlank()
                },
                label = { Text("Ваше имя") },
                isError = nameError,
                supportingText = {
                    if (nameError) {
                        Text("Имя не может быть пустым")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

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
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = agreeToTerms,
                    onCheckedChange = {
                        agreeToTerms = it
                        termsError = !it // Убираем ошибку, если пользователь соглашается
                    }
                )
                Text(
                    text = "Даю согласие на обработку персональных данных",
                    modifier = Modifier.clickable {
                        agreeToTerms = !agreeToTerms
                        termsError = !agreeToTerms
                    },
                    color = if (termsError) MaterialTheme.colorScheme.error else LocalContentColor.current
                )
            }
            if (termsError) {
                Text(
                    text = "Необходимо согласиться с условиями",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.Start)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    nameError = name.isBlank()
                    emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    passwordError = password.length < 6
                    termsError = !agreeToTerms // Проверяем согласие

                    if (!nameError && !emailError && !passwordError && !termsError &&
                        name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()
                    ) {
                        scope.launch {
                            val existingUser = userViewModel.getUser(email) // Проверяем, существует ли пользователь с таким email
                            if (existingUser != null) {
                                snackbarHostState.showSnackbar("Пользователь с таким Email уже существует")
                            } else {
                                // Регистрируем пользователя
                                userViewModel.registerUser(email, password) // В реальном приложении: хэш пароля
                                snackbarHostState.showSnackbar("Регистрация успешна!")
                                onRegistrationSuccess() // Переход на предыдущий экран (логин)
                            }
                        }
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Пожалуйста, заполните все поля корректно и согласитесь с условиями")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Зарегистрироваться", color = MaterialTheme.colorScheme.onPrimary)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Есть аккаунт?")
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Войти",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        navController.popBackStack() // Просто возвращаемся на экран логина
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterAccountScreen() {
    PR31Theme {
        RegisterAccountScreen(rememberNavController(), onRegistrationSuccess = {})
    }
}