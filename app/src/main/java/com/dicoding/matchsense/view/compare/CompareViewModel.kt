package com.dicoding.matchsense.view.compare

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.matchsense.data.Result
import com.dicoding.matchsense.data.remote.response.CompareResponse
import com.dicoding.matchsense.data.repository.CompareRepository
import kotlinx.coroutines.launch

class CompareViewModel(private val repository: CompareRepository) : ViewModel() {

//    suspend fun getSimilarity(sentence1: String, sentence2: String): LiveData<Result<CompareResponse>> = repository.getSimilarity(sentence1, sentence2)

    private val _result = MutableLiveData<Result<CompareResponse>>()
    val result: LiveData<Result<CompareResponse>> = _result

    fun compareTexts(sentence1: String, sentence2: String) {
        viewModelScope.launch {
            try {
                Log.d("TAG", "TEST")
                val similarityResult = repository.getSimilarity(sentence1, sentence2)
                _result.value = Result.Success(similarityResult)
            } catch (e: Exception) {
                _result.value = Result.Error(e.message ?: "Unknown error")
            }
        }
    }

}