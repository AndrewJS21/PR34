package com.example.pr34

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pr34.data.AppDatabase
import com.example.pr34.data.User
import com.example.pr34.data.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        userRepository = UserRepository(userDao)
    }

    suspend fun getUser(email: String): User? {
        return userRepository.getUserByEmail(email)
    }

    fun createInitialUsers() {
        viewModelScope.launch {
            val existingUser = userRepository.getUserByEmail("user1@example.com")
            if (existingUser == null) {
                userRepository.insertUser(User(email = "user1@example.com", passwordHash = "password1"))
                userRepository.insertUser(User(email = "user2@example.com", passwordHash = "password2"))
                userRepository.insertUser(User(email = "user3@example.com", passwordHash = "password3"))
                userRepository.insertUser(User(email = "user4@example.com", passwordHash = "password4"))
                userRepository.insertUser(User(email = "user5@example.com", passwordHash = "password5"))
            }
        }
    }

    suspend fun authenticateUser(email: String, passwordAttempt: String): Boolean {
        val user = userRepository.getUserByEmail(email)
        return user != null && user.passwordHash == passwordAttempt
    }

    suspend fun registerUser(email: String, passwordHash: String) {
        userRepository.insertUser(User(email = email, passwordHash = passwordHash))
    }

    fun clearAllUsers() {
        viewModelScope.launch {
            userRepository.deleteAllUsers()
        }
    }
}