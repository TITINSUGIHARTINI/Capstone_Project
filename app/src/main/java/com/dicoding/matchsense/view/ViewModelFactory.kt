package com.dicoding.matchsense.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.matchsense.data.repository.SynonymRepository
import com.dicoding.matchsense.data.repository.UserRepository
import com.dicoding.matchsense.data.repository.GeminiRepository
import com.dicoding.matchsense.di.Injection
import com.dicoding.matchsense.view.login.LoginViewModel
import com.dicoding.matchsense.view.main.MainViewModel
import com.dicoding.matchsense.view.profile.ProfileViewModel
import com.dicoding.matchsense.view.signup.SignupViewModel
import com.dicoding.matchsense.view.synonym.SynonymViewModel
import com.dicoding.matchsense.view.synonym.detail.SynonymMeaningViewModel

class ViewModelFactory(
    private val repository: UserRepository,
    private val synonymRepository: SynonymRepository,
    private val geminiRepository: GeminiRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(repository) as T

            }

            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SynonymViewModel::class.java) -> {
                SynonymViewModel(synonymRepository) as T
            }

            modelClass.isAssignableFrom(SynonymMeaningViewModel::class.java) -> {
                SynonymMeaningViewModel(geminiRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(
                        Injection.provideRepository(context),
                        Injection.synonymRepository(),
                        Injection.geminiRepository()
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}