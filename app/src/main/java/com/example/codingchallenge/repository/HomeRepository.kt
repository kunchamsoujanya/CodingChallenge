package com.example.codingchallenge.repository

import com.example.codingchallenge.network.ApiServices
import com.example.codingchallenge.response.Cards

class HomeRepository(private val service: ApiServices)  {

    suspend fun fetchHomePageResponse() : Result<List<Cards>> {
        val response = service.fetchHomePageResponse()
        if (response.isSuccessful) {
            response.body()?.let {
                return Result.Success(it.page.cards)
            } ?:  return Result.Error("No Data found")
        } else {
            return Result.Error(response.errorBody().toString())
        }
    }
}