package com.example.authentication.data

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id") val id: String,
    @SerializedName("user_name") val userName: String,
    val email: String,
    @SerializedName("date_of_birth") val dateOfBirth: String,
    val gender: String,
    val phoneNumber: String,
    val isVerifiedEmail: Boolean,
    val token: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
)
