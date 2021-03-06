package com.example.dictionary.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitInstance {

    val api: WordApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://172.20.10.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WordApi::class.java)
    }
}