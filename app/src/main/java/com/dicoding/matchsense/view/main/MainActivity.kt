package com.dicoding.matchsense.view.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.dicoding.matchsense.R
import com.dicoding.matchsense.data.pref.UserPreference
import com.dicoding.matchsense.data.pref.dataStore
import com.dicoding.matchsense.databinding.ActivityMainBinding
import com.dicoding.matchsense.view.synonym.SynonymActivity
import com.dicoding.matchsense.view.settings.SettingsActivity
import com.dicoding.matchsense.view.translate.TranslateActivity
import com.dicoding.matchsense.view.ViewModelFactory
import com.dicoding.matchsense.view.compare.CompareActivity
import com.dicoding.matchsense.view.profile.ProfileActivity
import com.dicoding.matchsense.view.welcome.WelcomeActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(applicationContext)
    }

    private lateinit var binding: ActivityMainBinding

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                Log.w("MainActivity", "Notification permission denied")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        val userPreference = UserPreference.getInstance(this.dataStore)
        val language = runBlocking { userPreference.getLanguage().first() }
        LocaleHelper.setLocale(this, language)
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !mainViewModel.isReady.value
            }
        }
        isLogin()
        checkAndRequestNotificationPermission()
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun isLogin() {
        mainViewModel.getSession().observe(this) { user ->
            Log.d("MainActivity", "User: $user")
            if (!user.isLogin) {
                val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                binding = ActivityMainBinding.inflate(layoutInflater)
                setContentView(binding.root)
                action()
            }
        }
    }

    private fun action() {
        binding.apply {
            profileButton.setOnClickListener {
                intent = Intent(this@MainActivity, ProfileActivity::class.java)
                startActivity(intent)
            }

            // Compare Card
            compareCard.cardTitle.text = getString(R.string.compare)
            compareCard.cardDescription.text = getString(R.string.compare_description)
            compareCard.card.setCardBackgroundColor(
                ContextCompat.getColor(
                    this@MainActivity,
                    R.color.soft_blue
                )
            )
            compareCard.card.setOnClickListener {
                intent = Intent(this@MainActivity, CompareActivity::class.java)
                startActivity(intent)
            }

            // Translation
            translateCard.cardTitle.text = getString(R.string.translate)
            translateCard.cardDescription.text = getString(R.string.translate_description)
             translateCard.card.setCardBackgroundColor(
                ContextCompat.getColor(
                    this@MainActivity,
                    R.color.soft_green
                )
            )
            translateCard.icon.setBackgroundResource(R.drawable.translate)
            translateCard.card.setOnClickListener {
                intent = Intent(this@MainActivity, TranslateActivity::class.java)
                startActivity(intent)
            }

            //Synonym Search
            convertCard.cardTitle.text = getString(R.string.synonym)
            convertCard.cardDescription.text = getString(R.string.synonym_description)
            convertCard.card.setCardBackgroundColor(
                ContextCompat.getColor(
                    this@MainActivity,
                    R.color.soft_orange
                )
            )
            convertCard.icon.setBackgroundResource(R.drawable.synonym_icon)
            convertCard.card.setOnClickListener {
                intent = Intent(this@MainActivity, SynonymActivity::class.java)
                startActivity(intent)
            }

            //Settings
            settingsCard.cardTitle.text = getString(R.string.settings)
            settingsCard.cardDescription.text = getString(R.string.settings_description)
            settingsCard.card.setCardBackgroundColor(
                ContextCompat.getColor(
                    this@MainActivity,
                    R.color.soft_gray
                )
            )
            settingsCard.icon.setBackgroundResource(R.drawable.settings)
            settingsCard.icon.scaleType = ImageView.ScaleType.FIT_XY
            settingsCard.card.setOnClickListener {
                intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                mainViewModel.logout()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val userPreference = UserPreference.getInstance(newBase.dataStore)
        val language = runBlocking { userPreference.getLanguage().first() }
        val context = LocaleHelper.setLocale(newBase, language)
        super.attachBaseContext(context)
    }




}