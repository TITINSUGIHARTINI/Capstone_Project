package com.dicoding.matchsense.view.translate

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.matchsense.R
import com.dicoding.matchsense.data.pref.UserPreference
import com.dicoding.matchsense.data.pref.dataStore
import com.dicoding.matchsense.databinding.ActivityTranslateBinding
import com.dicoding.matchsense.helper.LocaleHelper
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class TranslateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTranslateBinding
    private var translateTo = "English"

    override fun onCreate(savedInstanceState: Bundle?) {
        val userPreference = UserPreference.getInstance(this.dataStore)
        val language = runBlocking { userPreference.getLanguage().first() }
        LocaleHelper.setLocale(this, language)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTranslateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupAction()
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.translateButton.setOnClickListener {
            var text = binding.translateEditText.text.toString()
            if(text.isEmpty() || text.isBlank()) {
                Toast.makeText(this, getString(R.string.no_text_toast), Toast.LENGTH_SHORT).show()
                binding.TranslatedTextTv.visibility = View.GONE
                return@setOnClickListener
            }

            translate(text)
        }
        binding.switchTranslateTarget.setOnClickListener {
            if(translateTo == "English") {
                binding.sourceLanguage.text = translateTo
                translateTo = "Indonesian"
                binding.targetLanguage.text = translateTo
            } else {
                binding.sourceLanguage.text = translateTo
                translateTo = "English"
                binding.targetLanguage.text = translateTo
            }
        }
    }

    private fun translate(text: String) {
        val options = TranslatorOptions.Builder().apply {
            if(translateTo == "English") {
                setSourceLanguage(TranslateLanguage.INDONESIAN)
                setTargetLanguage(TranslateLanguage.ENGLISH)
            } else {
                setSourceLanguage(TranslateLanguage.ENGLISH)
                setTargetLanguage(TranslateLanguage.INDONESIAN)
            }
        }
        val translator = Translation.getClient(options.build())

        translator.downloadModelIfNeeded()
            .addOnSuccessListener {
                val paragraphs = text.split("\n")
                val translate = mutableListOf<String>()

                paragraphs.forEachIndexed { index, line ->
                    translator.translate(line)
                        .addOnSuccessListener { translatedLine ->
                            translate.add(translatedLine)
                            if (translate.size == paragraphs.size) {
                                val finalText = translate.joinToString("\n")
                                binding.TranslatedTextTv.text = finalText
                                binding.TranslatedTextTv.visibility = View.VISIBLE
                                translator.close()
                            }
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, exception.message.toString(), Toast.LENGTH_SHORT)
                                .show()
                            print(exception.stackTrace)

                            translator.close()
                        }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to Download", Toast.LENGTH_SHORT).show()
            }
    }

    override fun attachBaseContext(newBase: Context) {
        val userPreference = UserPreference.getInstance(newBase.dataStore)
        val language = runBlocking { userPreference.getLanguage().first() }
        val context = LocaleHelper.setLocale(newBase, language)
        super.attachBaseContext(context)
    }
}