package com.dicoding.matchsense.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.matchsense.R
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.dicoding.matchsense.R
import com.dicoding.matchsense.view.signup.SignupActivity
import com.dicoding.matchsense.databinding.ActivityMainBinding
import com.dicoding.matchsense.view.ViewModelFactory
import com.dicoding.matchsense.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(applicationContext)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !mainViewModel.isReady.value
            }
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.4f,
                    0.0f
                )
                zoomX.duration = 500L

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.4f,
                    0.0f
                )
                zoomY.duration = 500L

                val fadeOut = ObjectAnimator.ofFloat(
                    screen.view,
                    View.ALPHA,
                    1f,
                    0f
                )
                fadeOut.duration = 300L
                fadeOut.doOnEnd {
                    screen.remove()
                    isLogin()
                }

                zoomX.start()
                zoomY.start()
                fadeOut.start()
            }
        }
    }
    
    private fun isLogin() {
        if(!user.IsLogin) {
            val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            setContentView(binding.root)
            setSupportActionBar(binding.toolbar)
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