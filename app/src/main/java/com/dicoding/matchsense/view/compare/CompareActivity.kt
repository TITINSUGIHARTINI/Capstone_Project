package com.dicoding.matchsense.view.compare

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.matchsense.R
import com.dicoding.matchsense.data.Result
import com.dicoding.matchsense.data.pref.UserPreference
import com.dicoding.matchsense.data.pref.dataStore
import com.dicoding.matchsense.databinding.ActivityCompareBinding
import com.dicoding.matchsense.helper.LocaleHelper
import com.dicoding.matchsense.view.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class CompareActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompareBinding
    private val viewModel by viewModels<CompareViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val userPreference = UserPreference.getInstance(this.dataStore)
        val language = runBlocking { userPreference.getLanguage().first() }
        LocaleHelper.setLocale(this, language)
        super.onCreate(savedInstanceState)
        binding = ActivityCompareBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel.result.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val similarityScore = result.data.similarityScore ?: 0f
                    binding.tvResult.text = getString(R.string.similarity_result, similarityScore)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnCompare.setOnClickListener {
            val sentence1 = binding.inputFirstLayout.editText?.text.toString()
            val sentence2 = binding.inputSecondLayout.editText?.text.toString()

            if (sentence1.isNotBlank() && sentence2.isNotBlank()) {
                viewModel.compareTexts(sentence1, sentence2)
            } else {
                Toast.makeText(this, R.string.error_empty_fields, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val userPreference = UserPreference.getInstance(newBase.dataStore)
        val language = runBlocking { userPreference.getLanguage().first() }
        val context = LocaleHelper.setLocale(newBase, language)
        super.attachBaseContext(context)
    }

}