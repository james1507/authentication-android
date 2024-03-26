package com.example.authentication.data

import com.google.gson.annotations.SerializedName

data class RegisterBody(
    @SerializedName("user_name") val userName: String,
    val email: String,
    val password: String,
)
