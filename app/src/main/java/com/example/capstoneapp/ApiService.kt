package com.example.capstoneapp

import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class PhotoItem(val albumId: Int, val id: Int, val title: String, val url: String, val thumbnailUrl: String)

interface ApiService {
    @GET("photos?albumId=1")
    suspend fun getPhotos(): List<PhotoItem>

    companion object {
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}
