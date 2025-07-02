package com.example.pr34

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pr34.ui.theme.PR31Theme

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Добро пожаловать!", // "Welcome!"
            style = MaterialTheme.typography.headlineMedium
        )
        // Здесь будет ваш дизайн домашнего экрана
        // Например, список элементов, навигационные кнопки и т.д.
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    PR31Theme {
        HomeScreen(rememberNavController())
    }
}