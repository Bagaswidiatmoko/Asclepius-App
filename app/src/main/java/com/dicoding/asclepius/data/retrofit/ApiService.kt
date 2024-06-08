package com.dicoding.asclepius.data.retrofit

import com.dicoding.asclepius.data.response.ArticleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/v2/top-headlines")
    fun getArticle(
        @Query("q") q: String,
        @Query("category") category: String,
        @Query("language") language: String
    ): Call<ArticleResponse>
}