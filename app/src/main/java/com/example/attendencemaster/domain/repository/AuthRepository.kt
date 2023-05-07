package com.example.attendencemaster.domain.repository

import com.example.attendencemaster.utils.Resource
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    val currentUser: FirebaseUser?
    suspend fun signUp(name: String, email: String, password: String ): Resource<FirebaseUser>
    suspend fun logIn(email: String, password: String): Resource<FirebaseUser>
    suspend fun logOut()
}