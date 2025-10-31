package com.example.capstoneapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AppViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    init {
        viewModelScope.launch {
            dataStoreManager.darkModeFlow.collect { _isDarkMode.value = it }
        }
        viewModelScope.launch {
            dataStoreManager.userNameFlow.collect { _userName.value = it }
        }
    }

    fun setDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
        viewModelScope.launch { dataStoreManager.setDarkMode(enabled) }
    }

    fun setUserName(name: String) {
        _userName.value = name
        viewModelScope.launch { dataStoreManager.setUserName(name) }
    }
}

class AppViewModelFactory(private val dataStoreManager: DataStoreManager) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            return AppViewModel(dataStoreManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
