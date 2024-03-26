package com.example.authentication.utils

import com.example.authentication.data.UniqueEmailValidationResponse
import com.example.authentication.data.ValidateEmailBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIConsumer {
    @POST("user/sign-up")
    suspend fun validateEmailAddress(@Body body: ValidateEmailBody): Response<UniqueEmailValidationResponse>
}