package com.example.attendencemaster.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.attendencemaster.utils.NetworkResponse
import com.example.attendencemaster.utils.Status
import kotlinx.coroutines.flow.Flow

interface AttendanceRepository {

    suspend fun saveAttendance(date: String, time: String, status: Boolean, latitude: String, longitude: String, address: String, state: Status)
    suspend fun getAttendanceRecordsForUser(): NetworkResponse<List<Attendance>>
    suspend fun getAttendanceRecordsByDate(date: String?): NetworkResponse<List<Attendance>>
    suspend fun getLastWeekAttendance(previousDate: String?, lastWeekDate: String?): NetworkResponse<List<Attendance>>
    suspend fun getLastMonthAttendance(startDate: String?, endDate: String?): NetworkResponse<List<Attendance>>
    suspend fun saveCheckoutTime(checkout: String, state: Status)
    suspend fun checkingState(): Status
    suspend fun saveNeutralState(state: Status)
    suspend fun addingState()
}