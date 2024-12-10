package com.dicoding.matchsense.view.synonym

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.matchsense.R
import com.dicoding.matchsense.databinding.ActivitySynonymBinding
import com.dicoding.matchsense.view.ViewModelFactory
import com.dicoding.matchsense.data.Result

class SynonymActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySynonymBinding
    private lateinit var synonymAdapter: SynonymAdapter

    private val synonymViewModel by viewModels<SynonymViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySynonymBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupAction()
    }

    private fun setupAction() {
        binding.searchButton.setOnClickListener {
            val word = binding.searchEditText.text.toString()
            if(::synonymAdapter.isInitialized) {
                synonymAdapter.clear()
                binding.synonymRv.adapter = synonymAdapter
            }
            if(word.isEmpty() || word.isBlank()) {
                Toast.makeText(this, getString(R.string.no_word_toast), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            binding.notFoundTv.visibility = View.GONE
            getSynonym(word)
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun getSynonym(word: String) {
        synonymViewModel.getSynonym(word).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val synonyms = result.data.synonyms
                    if (!synonyms.isNullOrEmpty()) {
                        synonymAdapter = SynonymAdapter(synonyms as ArrayList)
                        binding.synonymRv.adapter = synonymAdapter
                        val layoutManager = LinearLayoutManager(this)
                        binding.synonymRv.layoutManager = layoutManager
                    } else {
                        binding.notFoundTv.visibility = View.VISIBLE
                    }
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}