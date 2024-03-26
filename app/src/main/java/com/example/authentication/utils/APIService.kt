package com.example.authentication.utils

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object APIService {
    private const val BASE_URL = "https://common-api-v1.vercel.app/"

    fun getService(): APIConsumer {

        val client: OkHttpClient = OkHttpClient.Builder()
                                    .connectTimeout(60, TimeUnit.SECONDS)
                                    .readTimeout(60, TimeUnit.SECONDS)
                                    .writeTimeout(60, TimeUnit.SECONDS)
                                    .build()

        val builder: Retrofit.Builder = Retrofit.Builder()
                                    .baseUrl(BASE_URL).client(client)
                                    .client(client)
                                    .addConverterFactory(GsonConverterFactory.create())

        val retrofit: Retrofit = builder.build()

        return retrofit.create(APIConsumer::class.java)
    }
}