package com.example.capstoneapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotoRepository(private val apiService: ApiService) {

    suspend fun fetchRandomPhoto(): String? = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getRandomPhoto()
            response.urls.small
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
