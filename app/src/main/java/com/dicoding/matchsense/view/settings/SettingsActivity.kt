package com.dicoding.matchsense.view.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.dicoding.matchsense.R
import com.dicoding.matchsense.data.pref.UserPreference
import com.dicoding.matchsense.data.pref.dataStore
import com.dicoding.matchsense.databinding.ActivitySettingsBinding
import com.dicoding.matchsense.helper.LocaleHelper
import com.dicoding.matchsense.view.ViewModelFactory
import com.dicoding.matchsense.view.main.MainActivity
import com.dicoding.matchsense.view.settings.reminder.ReminderWorker
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModels {
        ViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val userPreference = UserPreference.getInstance(this.dataStore)
        val language = runBlocking { userPreference.getLanguage().first() }
        LocaleHelper.setLocale(this, language)


        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNotificationSwitch()
        setupAction()
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnLanguage.setOnClickListener {
            val languages = arrayOf("English", "Bahasa Indonesia", "Bahasa Jawa")
            val languageCodes = arrayOf("en", "id", "jv")


            val currentLanguageCode = runBlocking {
                UserPreference.getInstance(this@SettingsActivity.dataStore).getLanguage().first()
            }
            val currentSelection = languageCodes.indexOf(currentLanguageCode)

            AlertDialog.Builder(this)
                .setTitle(R.string.choose_language)
                .setSingleChoiceItems(languages, currentSelection) { dialog, which ->
                    val selectedLanguage = languageCodes[which]
                    viewModel.saveLanguage(selectedLanguage)

                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()

                    dialog.dismiss()
                }
                .setNegativeButton(R.string.cancel, null)
                .show()
        }
    }

    private fun setupNotificationSwitch() {
        val userPreference = UserPreference.getInstance(this.dataStore)
        val isNotificationEnabled = runBlocking { userPreference.getNotificationEnabled().first() }
        binding.switchNotification.isChecked = isNotificationEnabled

        binding.switchNotification.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveNotificationEnabled(isChecked)
            if (isChecked) {
                scheduleReminderNotification()
            } else {
                WorkManager.getInstance(this).cancelAllWorkByTag("REMINDER")
            }
        }
    }

    private fun scheduleReminderNotification() {
        val workManager = WorkManager.getInstance(this)

        val reminderRequest = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(1, TimeUnit.MINUTES) // Atur delay sesuai kebutuhan
            .addTag("REMINDER")
            .build()


        workManager.enqueueUniquePeriodicWork(
            "DailyReminder",
            ExistingPeriodicWorkPolicy.REPLACE,
            reminderRequest
        )
    }

    override fun attachBaseContext(newBase: Context) {
        val userPreference = UserPreference.getInstance(newBase.dataStore)
        val language =
            runBlocking { userPreference.getLanguage().first() }
        val context = LocaleHelper.setLocale(newBase, language)
        super.attachBaseContext(context)
    }


}