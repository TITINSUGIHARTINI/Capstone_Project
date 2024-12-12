package com.dicoding.matchsense.view.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.matchsense.R
import com.dicoding.matchsense.data.pref.UserPreference
import com.dicoding.matchsense.data.pref.dataStore
import com.dicoding.matchsense.databinding.ActivityProfileBinding
import com.dicoding.matchsense.helper.LocaleHelper
import com.dicoding.matchsense.view.ViewModelFactory
import com.dicoding.matchsense.view.login.LoginActivity
import com.dicoding.matchsense.view.welcome.WelcomeActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ProfileActivity : AppCompatActivity() {

    private val viewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(applicationContext)
    }

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val userPreference = UserPreference.getInstance(this.dataStore)
        val language = runBlocking { userPreference.getLanguage().first() }
        LocaleHelper.setLocale(this, language)

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
        val userPreference = UserPreference.getInstance(this.dataStore)
        val language = runBlocking { userPreference.getLanguage().first() }
        LocaleHelper.setLocale(this, language) // Atur ulang bahasa


        val intent = Intent(this, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    override fun attachBaseContext(newBase: Context) {
        val userPreference = UserPreference.getInstance(newBase.dataStore)
        val language = runBlocking { userPreference.getLanguage().first() }
        val context = LocaleHelper.setLocale(newBase, language)
        super.attachBaseContext(context)
    }


}