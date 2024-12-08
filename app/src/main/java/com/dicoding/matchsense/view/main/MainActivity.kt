package com.dicoding.matchsense.view.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.dicoding.matchsense.R
import com.dicoding.matchsense.databinding.ActivityMainBinding
import com.dicoding.matchsense.view.convert.ConvertActivity
import com.dicoding.matchsense.view.settings.SettingsActivity
import com.dicoding.matchsense.view.translate.TranslateActivity
import com.dicoding.matchsense.view.ViewModelFactory
import com.dicoding.matchsense.view.compare.CompareActivity
import com.dicoding.matchsense.view.profile.ProfileActivity
import com.dicoding.matchsense.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(applicationContext)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !mainViewModel.isReady.value
            }
        }
        isLogin()
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

            //Convert To PDF
            convertCard.cardTitle.text = getString(R.string.convert)
            convertCard.cardDescription.text = getString(R.string.convert_description)
            convertCard.card.setCardBackgroundColor(
                ContextCompat.getColor(
                    this@MainActivity,
                    R.color.soft_orange
                )
            )
            convertCard.card.setOnClickListener {
                intent = Intent(this@MainActivity, ConvertActivity::class.java)
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

}