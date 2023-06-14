package com.example.attendencemaster.presentation.firestore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendencemaster.domain.repository.Attendance
import com.example.attendencemaster.domain.repository.AttendanceRepository
import com.example.attendencemaster.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(private val attendanceRepository: AttendanceRepository) :
    ViewModel() {

    fun saveAttendance(status: Boolean) = viewModelScope.launch {
        attendanceRepository.saveAttendance(getDate(), getTime(), status)
    }

    private val _getAttendance = MutableLiveData<NetworkResponse<List<Attendance>>>()
    val getAttendance: LiveData<NetworkResponse<List<Attendance>>> = _getAttendance


    fun getAttendanceForUsers() = viewModelScope.launch {
        val result = attendanceRepository.getAttendanceRecordsForUser()
        _getAttendance.value = result
    }

    fun getAttendanceByDate(date: String) = viewModelScope.launch {
        val result = attendanceRepository.getAttendanceRecordsByDate(date)
        _getAttendance.value = result
    }

    fun getDate(): String {
        val currentDate = Date()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(currentDate)
    }

    private fun getTime(): String {
        val currentTime = LocalTime.now()
        return currentTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
    }

}