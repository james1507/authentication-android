package com.example.authentication.repository

import com.example.authentication.data.RegisterBody
import com.example.authentication.utils.APIConsumer
import com.example.authentication.utils.RequestStatus
import com.example.authentication.utils.SimplifiedMessage
import kotlinx.coroutines.flow.flow

class AuthRepository(private val consumer: APIConsumer) {
//    fun validateEmailAddress(body: ValidateEmailBody) = flow {
//        emit(RequestStatus.Waiting)
//        val response = consumer.validateEmailAddress(body)
//        if(response.isSuccessful) {
//            emit(RequestStatus.Success(response.body()))
//        } else {
//            emit(RequestStatus.Error(SimplifiedMessage.get(response.errorBody()!!.byteStream().reader().readText())))
//        }
//    }

    fun registerUser(body: RegisterBody) = flow {
        emit(RequestStatus.Waiting)
        val response = consumer.registerUser(body)
        if (response.isSuccessful) {
            emit(RequestStatus.Success(response.body()))
        } else {
            emit(
                RequestStatus.Error(
                    SimplifiedMessage.get(
                        response.errorBody()!!.byteStream().reader().readText()
                    )
                )
            )
        }
    }
}