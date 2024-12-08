package com.dicoding.matchsense.view.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.matchsense.R
import com.dicoding.matchsense.databinding.ActivityProfileBinding
import com.dicoding.matchsense.view.ViewModelFactory
import com.dicoding.matchsense.view.login.LoginActivity
import com.dicoding.matchsense.view.welcome.WelcomeActivity

class ProfileActivity : AppCompatActivity() {

    private val viewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(applicationContext)
    }

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        userSession()
    }

    private fun userSession() {
        viewModel.getSession().observe(this) { user ->
            if (user != null) {
                binding.tvUsn.text = user.username
                binding.tvEmail.text = user.email
            }
        }
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.logout_confirmation))
                .setMessage(getString(R.string.are_you_sure_you_want_to_logout))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    viewModel.logout()
                    exitToWelcome()
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
        }
    }

    private fun exitToWelcome() {
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}