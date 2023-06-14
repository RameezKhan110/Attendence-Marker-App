package com.example.attendencemaster.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.attendencemaster.utils.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface AttendanceRepository {

    suspend fun saveAttendance(date: String, time: String, status: Boolean)
    suspend fun getAttendanceRecordsForUser(): NetworkResponse<List<Attendance>>
    suspend fun getAttendanceRecordsByDate(date: String?): NetworkResponse<List<Attendance>>
}