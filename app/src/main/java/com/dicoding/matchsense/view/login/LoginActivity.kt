package com.dicoding.matchsense.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.matchsense.R
import com.dicoding.matchsense.data.Result
import com.dicoding.matchsense.data.pref.UserModel
import com.dicoding.matchsense.data.pref.UserPreference
import com.dicoding.matchsense.data.pref.dataStore
import com.dicoding.matchsense.databinding.ActivityLoginBinding
import com.dicoding.matchsense.helper.LocaleHelper
import com.dicoding.matchsense.view.ViewModelFactory
import com.dicoding.matchsense.view.main.MainActivity
import com.dicoding.matchsense.view.signup.SignupActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val userPreference = UserPreference.getInstance(this.dataStore)
        val language = runBlocking { userPreference.getLanguage().first() }
        LocaleHelper.setLocale(this, language)

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
    }


    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.btnSignin.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            viewModel.login(email, password).observe(this) { result ->
                when (result) {
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val loginResponse = result.data
                        if (loginResponse.accessToken != null) {
                            Toast.makeText(
                                this,
                                "Login Success",
                                Toast.LENGTH_SHORT
                            ).show()
                            viewModel.saveSession(
                                UserModel(
                                    username = loginResponse.name.orEmpty(),
                                    email = email,
                                    token = loginResponse.accessToken,
                                    isLogin = true
                                )
                            )
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Login Failed: Invalid Credentials", Toast.LENGTH_SHORT).show()
                        }
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, "Login Failed: ${result.error}", Toast.LENGTH_SHORT)
                            .show()
                    }

                    Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
        binding.tvBtnSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }


    private fun playAnimation() {

        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.btnSignin, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }

    override fun attachBaseContext(newBase: Context) {
        val userPreference = UserPreference.getInstance(newBase.dataStore)
        val language = runBlocking { userPreference.getLanguage().first() }
        val context = LocaleHelper.setLocale(newBase, language)
        super.attachBaseContext(context)
    }


}