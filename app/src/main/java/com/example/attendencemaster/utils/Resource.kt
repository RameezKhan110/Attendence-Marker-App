package com.example.attendencemaster.utils


sealed class Resource<out R>{

    data class Success<out R>(val result: R): Resource<R>()
    data class Error(val exception: Exception): Resource<Nothing>()
    object Loading: Resource<Nothing>()
}


