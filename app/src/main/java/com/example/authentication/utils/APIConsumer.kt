package com.example.authentication.utils

import com.example.authentication.data.RegisterBody
import com.example.authentication.data.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIConsumer {
    @POST("user/sign-up")
    suspend fun registerUser(@Body body: RegisterBody) : Response<RegisterResponse>
}