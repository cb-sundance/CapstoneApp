package com.example.capstoneapp

class PhotoRepository(private val apiService: ApiService) {

    suspend fun fetchFunFact(): String {
        return try {
            apiService.getFunFact().text
        } catch (e: Exception) {
            "Could not fetch fun fact"
        }
    }
}
