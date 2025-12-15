package com.example.level_up_gamer.remote

import com.example.level_up_gamer.model.Post
import retrofit2.http.GET

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>
}