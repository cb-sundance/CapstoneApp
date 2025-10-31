package com.example.capstoneapp

import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class FunFact(val text: String)

interface ApiService {
    @GET("random/trivia") // Example endpoint
    suspend fun getFunFact(): FunFact

    companion object {
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.example.com/") // Replace with actual API
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}
