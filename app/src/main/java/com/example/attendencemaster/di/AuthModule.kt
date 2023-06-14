package com.example.attendencemaster.di

import com.example.attendencemaster.data.repository.AttendanceRepoImpl
import com.example.attendencemaster.domain.repository.AuthRepository
import com.example.attendencemaster.data.repository.AuthRepoImpl
import com.example.attendencemaster.domain.repository.AttendanceRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn (SingletonComponent::class)
object AuthModule {

    @Provides
    fun providesFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun providesFirebaseFireStore(): FirebaseFirestore = Firebase.firestore


    @Provides
    fun providesAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository = AuthRepoImpl(firebaseAuth)

    @Provides
    fun providesAttendanceRepository(fireStore: FirebaseFirestore): AttendanceRepository = AttendanceRepoImpl(fireStore)
}