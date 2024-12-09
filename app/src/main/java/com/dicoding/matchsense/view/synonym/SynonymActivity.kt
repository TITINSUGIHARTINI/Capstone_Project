package com.dicoding.matchsense.view.synonym

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.matchsense.databinding.ActivitySynonymBinding
import com.dicoding.matchsense.view.ViewModelFactory

class SynonymActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySynonymBinding

    private val synonymViewModel by viewModels<SynonymViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySynonymBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}