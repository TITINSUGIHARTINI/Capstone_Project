package com.dicoding.matchsense.view.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.matchsense.databinding.ActivityWelcomeBinding
import com.dicoding.matchsense.view.login.LoginActivity
import com.dicoding.matchsense.view.signup.SignupActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAction()

    }

    private fun setupAction() {
        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        binding.tvBtnSignin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
