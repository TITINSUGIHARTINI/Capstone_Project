package com.dicoding.matchsense.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.matchsense.data.pref.UserPreference
import com.dicoding.matchsense.data.pref.dataStore
import com.dicoding.matchsense.databinding.ActivitySignupBinding
import com.dicoding.matchsense.helper.LocaleHelper
import com.dicoding.matchsense.view.ViewModelFactory
import com.dicoding.matchsense.view.login.LoginActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val userPreference = UserPreference.getInstance(this.dataStore)
        val language = runBlocking { userPreference.getLanguage().first() }
        LocaleHelper.setLocale(this, language)

        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
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
        binding.signupButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val name = binding.nameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            viewModel.signUp(name, email, password).observe(this) { result ->
                when (result) {
                    is com.dicoding.matchsense.data.Result.Success -> {
                        Log.d("SignupActivity", "Response: ${result.data}")
                        binding.progressBar.visibility = View.GONE
                        val registerResponse = result.data
                        if (registerResponse.msg.isNullOrEmpty()) {
                            Toast.makeText(
                                this,
                                "Register Failed: Unknown error",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(this, "${registerResponse.msg}", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }

                    is com.dicoding.matchsense.data.Result.Error -> {
                        Log.e("SignupActivity", "Error: ${result.error}")
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, "Register Failed: ${result.error}", Toast.LENGTH_SHORT)
                            .show()
                    }

                    com.dicoding.matchsense.data.Result.Loading -> {
                        Log.d("SignupActivity", "Loading state triggered")
                        binding.progressBar.visibility = View.VISIBLE
                        Log.d("SignupActivity", "Loading state triggered")
                    }
                }
            }
        }
        binding.tvBtnSignin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun playAnimation() {

        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
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