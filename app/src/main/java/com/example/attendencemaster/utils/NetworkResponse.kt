package com.example.attendencemaster.utils

sealed class NetworkResponse<T>(private val data:T?=null, private val msg:String?=null)
{
    class Success<T>(val data:T?) : NetworkResponse<T>(data)
    class Error<T>(private val msg: String?): NetworkResponse<T>(null,msg)
    class Loading<T>: NetworkResponse<T>()
}
