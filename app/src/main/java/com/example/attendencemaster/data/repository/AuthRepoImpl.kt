package com.example.attendencemaster.data.repository

import com.example.attendencemaster.utils.Resource
import com.example.attendencemaster.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(private val firebaseAuth: FirebaseAuth)
    : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser


    override suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Error(e)
        }
    }

    override suspend fun logIn(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Error(e)
        }
    }

    override suspend fun logOut() {
        firebaseAuth.signOut()
    }


}