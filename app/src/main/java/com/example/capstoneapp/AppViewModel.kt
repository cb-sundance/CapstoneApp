package com.example.capstoneapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AppViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    init {
        viewModelScope.launch {
            // collect DataStore flows to keep UI state in sync
            dataStoreManager.darkModeFlow.collectLatest { value ->
                _isDarkMode.value = value
            }
        }
        viewModelScope.launch {
            dataStoreManager.userNameFlow.collectLatest { name ->
                _userName.value = name
            }
        }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setDarkMode(enabled)
        }
    }

    fun setUserName(name: String) {
        viewModelScope.launch {
            dataStoreManager.setUserName(name)
        }
    }
}

class AppViewModelFactory(private val dataStoreManager: DataStoreManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(dataStoreManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

