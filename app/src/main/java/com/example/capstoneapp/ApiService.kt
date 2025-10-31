package com.example.capstoneapp

import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class PhotoResponse(val urls: Urls)
data class Urls(val small: String)

interface ApiService {
    @GET("photos/random?client_id=YOUR_ACCESS_KEY") // replace with your Unsplash API key
    suspend fun getRandomPhoto(): PhotoResponse

    companion object {
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.unsplash.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}
