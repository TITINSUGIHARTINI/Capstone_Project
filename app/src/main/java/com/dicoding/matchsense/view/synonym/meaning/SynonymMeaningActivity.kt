package com.dicoding.matchsense.view.synonym.meaning

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.matchsense.R
import com.dicoding.matchsense.data.Result
import com.dicoding.matchsense.databinding.ActivitySynonymMeaningBinding

import com.dicoding.matchsense.view.ViewModelFactory

class SynonymMeaningActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySynonymMeaningBinding

    private val synonymMeaningViewModel by viewModels<SynonymMeaningViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySynonymMeaningBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val wordChosen = intent.getStringExtra(SYNONYM_MEANING)
        val word = "describe the meaning of word \"$wordChosen\" in a paragraph style"

        binding.textMeaningTv.text = wordChosen

        setupAction()
        getResponse(word)
    }

    private fun getResponse(word: String) {
        synonymMeaningViewModel.getResponse(word).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val response = result.data
                    binding.textMeaningDescriptionTv.text = response
                    binding.textMeaningDescriptionTv.visibility = View.VISIBLE
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    companion object {
        const val SYNONYM_MEANING = "SYNONYM_MEANING"
    }
}