package com.dicoding.matchsense.view.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.matchsense.data.pref.UserPreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(private val userPreference: UserPreference) : ViewModel() {
    private val _language = MutableStateFlow("en")
    val language: StateFlow<String> get() = _language

    private val _notificationEnabled = MutableStateFlow(false)
    val notificationEnabled: StateFlow<Boolean> get() = _notificationEnabled

    init {
        viewModelScope.launch {
            userPreference.getLanguage().collect {
                _language.value = it
            }
        }
        viewModelScope.launch {
            userPreference.getNotificationEnabled().collect {
                _notificationEnabled.value = it
            }
        }
    }

    fun saveLanguage(language: String) {
        viewModelScope.launch {
            userPreference.saveLanguage(language)
        }
    }

    fun saveNotificationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            userPreference.saveNotificationStatus(enabled)
        }
    }
}