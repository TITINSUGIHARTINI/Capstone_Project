package com.dicoding.matchsense.view.welcome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.matchsense.data.pref.UserPreference
import com.dicoding.matchsense.data.pref.dataStore
import com.dicoding.matchsense.databinding.ActivityWelcomeBinding
import com.dicoding.matchsense.helper.LocaleHelper
import com.dicoding.matchsense.view.login.LoginActivity
import com.dicoding.matchsense.view.main.MainActivity
import com.dicoding.matchsense.view.signup.SignupActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {

//        runBlocking {
//            val isLogin = userPreference.getSession().first().isLogin
//            if (isLogin) {
//                val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//        }
        val userPreference = UserPreference.getInstance(this.dataStore)
        val language = runBlocking { userPreference.getLanguage().first() }
        LocaleHelper.setLocale(this, language)

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

    override fun attachBaseContext(newBase: Context) {
        val userPreference = UserPreference.getInstance(newBase.dataStore)
        val language = runBlocking { userPreference.getLanguage().first() }
        val context = LocaleHelper.setLocale(newBase, language)
        super.attachBaseContext(context)
    }

}
