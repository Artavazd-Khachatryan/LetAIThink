package com.example.chatgptapi.data

import com.example.chatgptapi.model.AiModels
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface ChatGPTApi {

    companion object{
        const val BASE_URL = "https://api.openai.com/v1/"
        private const val TOKEN_KAY = "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"
    }

    @Headers("Authorization: Bearer $TOKEN_KAY")
    @GET("models")
    fun getModels(): Call<AiModels>
}