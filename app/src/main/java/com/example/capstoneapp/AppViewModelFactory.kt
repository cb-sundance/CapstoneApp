package com.example.capstoneapp

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AppViewModelFactory(private val dataStoreManager: DataStoreManager, private val app: Application) :
    ViewModelProvider.AndroidViewModelFactory(app) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(app, dataStoreManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
