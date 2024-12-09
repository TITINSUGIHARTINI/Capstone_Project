package com.dicoding.matchsense.view.synonym

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.matchsense.data.Result
import com.dicoding.matchsense.data.remote.response.ThesaurusResponse
import com.dicoding.matchsense.data.repository.SynonymRepository

class SynonymViewModel(private val synonymRepository: SynonymRepository) : ViewModel() {

    fun getSynonym(word: String): LiveData<Result<ThesaurusResponse>> = synonymRepository.getSynonym(word)

}