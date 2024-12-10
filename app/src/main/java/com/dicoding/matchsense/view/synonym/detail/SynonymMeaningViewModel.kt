package com.dicoding.matchsense.view.synonym.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.matchsense.data.Result
import com.dicoding.matchsense.data.repository.GeminiRepository

class SynonymMeaningViewModel(private val geminiRepository: GeminiRepository): ViewModel() {

    fun getResponse(request: String): LiveData<Result<String?>> = geminiRepository.getResponse(request)

}