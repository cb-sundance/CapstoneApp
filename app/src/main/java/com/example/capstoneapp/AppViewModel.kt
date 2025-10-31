package com.example.capstoneapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AppViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode

    init {
        // Observe dark mode from DataStore
        viewModelScope.launch {
            dataStoreManager.darkModeFlow.collect { enabled ->
                _isDarkMode.value = enabled
            }
        }
    }

    fun setUserName(name: String) {
        _userName.value = name
    }

    fun setDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
        viewModelScope.launch {
            dataStoreManager.setDarkMode(enabled)
        }
    }
}
