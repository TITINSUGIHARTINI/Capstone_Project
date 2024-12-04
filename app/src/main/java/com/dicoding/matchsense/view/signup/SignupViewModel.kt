package com.dicoding.matchsense.view.signup

import androidx.lifecycle.ViewModel
import com.dicoding.matchsense.data.repository.UserRepository

class SignupViewModel(private val repository: UserRepository) : ViewModel()  {
    fun signUp(name: String, email: String, password: String) = repository.signUp(name, email, password)
}