package com.example.capstoneapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(application: Application, private val dataStoreManager: DataStoreManager) :
    AndroidViewModel(application) {

    // userName kept in memory (you can persist it if you add a key in DataStoreManager)
    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    // dark mode state from DataStore
    val isDarkMode = dataStoreManager.darkModeFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun setUserName(name: String) {
        viewModelScope.launch {
            _userName.emit(name)
        }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setDarkMode(enabled)
        }
    }

    // Helper to schedule immediate notification from UI (MainActivity)
    fun scheduleImmediateNotification() {
        // Not enqueueing WorkManager here because ViewModel shouldn't hold context.
        // Instead, MainActivity will call NotificationHelper.scheduleImmediateNotification(context)
    }
}

