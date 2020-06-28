package com.example.codingchallenge.network

import com.example.codingchallenge.response.PageData
import retrofit2.Call
import retrofit2.http.GET

interface ApiServices {

    @GET("test/home")
    fun fetchHomePageResponse(): Call<PageData>

}