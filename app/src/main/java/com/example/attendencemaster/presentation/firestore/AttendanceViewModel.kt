package com.example.attendencemaster.presentation.firestore

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendencemaster.domain.repository.Attendance
import com.example.attendencemaster.domain.repository.AttendanceRepository
import com.example.attendencemaster.utils.NetworkResponse
import com.example.attendencemaster.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(private val attendanceRepository: AttendanceRepository) :
    ViewModel() {

    private val _getAttendance = MutableLiveData<NetworkResponse<List<Attendance>>>()
    val getAttendance: LiveData<NetworkResponse<List<Attendance>>> = _getAttendance

    private val _getState = MutableLiveData<Status>()
    val getState: LiveData<Status> = _getState

    fun saveAttendance(status: Boolean, latitude: String, longitude: String, address: String, state: Status) = viewModelScope.launch {
        attendanceRepository.saveAttendance(getDate(), getTime(), status, latitude, longitude, address, state)
    }

    fun saveCheckoutTime(state: Status) = viewModelScope.launch {
        attendanceRepository.saveCheckoutTime(getTime(), state)
    }

    fun addState() = viewModelScope.launch {
        attendanceRepository.addingState()
    }

    fun checkState() = viewModelScope.launch {
        val state = attendanceRepository.checkingState()
        _getState.value = state
    }

    fun saveNeutralState(state: Status) = viewModelScope.launch {
        attendanceRepository.saveNeutralState(state)
    }

    fun getAttendanceForUsers() = viewModelScope.launch {
        val result = attendanceRepository.getAttendanceRecordsForUser()
        _getAttendance.value = result
    }

    fun getAttendanceByDate(date: String) = viewModelScope.launch {
        val result = attendanceRepository.getAttendanceRecordsByDate(date)
        _getAttendance.value = result
    }

    fun getLastWeekAttendance() = viewModelScope.launch {
        val result =
            attendanceRepository.getLastWeekAttendance(getPreviousDate(), getLastWeekDate())
        _getAttendance.value = result
    }

    fun getLastMonthAttendance() = viewModelScope.launch {
        when (getLastMonthDate()) {
            0 -> {
                val result = attendanceRepository.getLastMonthAttendance("01/01/2023", "31/01/2023")
                _getAttendance.value = result

            }
            1 -> {
                val result = attendanceRepository.getLastMonthAttendance("01/02/2023", "28/02/2023")
                _getAttendance.value = result
            }
            2 -> {
                val result = attendanceRepository.getLastMonthAttendance("01/03/2023", "31/03/2023")
                _getAttendance.value = result
            }
            3 -> {
                val result = attendanceRepository.getLastMonthAttendance("01/04/2023", "30/04/2023")
                _getAttendance.value = result
            }
            4 -> {
                val result = attendanceRepository.getLastMonthAttendance("01/05/2023", "31/05/2023")
                _getAttendance.value = result
                Log.d("TAG", "4 is running")
            }
            5 -> {
                val result = attendanceRepository.getLastMonthAttendance("01/06/2023", "30/06/2023")
                _getAttendance.value = result
                Log.d("TAG", "5 is running")
            }
            6 -> {
                val result = attendanceRepository.getLastMonthAttendance("01/07/2023", "31/07/2023")
                _getAttendance.value = result
            }
            7 -> {
                val result = attendanceRepository.getLastMonthAttendance("01/08/2023", "31/08/2023")
                _getAttendance.value = result
            }
            8 -> {
                val result = attendanceRepository.getLastMonthAttendance("01/09/2023", "30/09/2023")
                _getAttendance.value = result
            }
            9 -> {
                val result = attendanceRepository.getLastMonthAttendance("01/10/2023", "31/10/2023")
                _getAttendance.value = result
            }
            10 -> {
                val result = attendanceRepository.getLastMonthAttendance("01/11/2023", "30/11/2023")
                _getAttendance.value = result
            }
            11 -> {
                val result = attendanceRepository.getLastMonthAttendance("01/12/2023", "31/12/2023")
                _getAttendance.value = result
            }
        }
    }


    fun getDate(): String {
        val currentDate = Date()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(currentDate)
    }

    fun getTime(): String {
        val currentTime = LocalTime.now()
        return currentTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
    }

    fun getDayOfWeek(): Int {
        val calender = Calendar.getInstance()
        return calender.get(Calendar.DAY_OF_WEEK)
    }

    private fun getPreviousDate(): String {
        val calender = Calendar.getInstance()
        calender.add(Calendar.DAY_OF_YEAR, -1)
        val previousDate = calender.time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(previousDate)
    }

    private fun getLastWeekDate(): String {
        val calender = Calendar.getInstance()
        calender.add(Calendar.DAY_OF_YEAR, -7)
        val lastWeekDate = calender.time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(lastWeekDate)
    }

    private fun getLastMonthDate(): Int {
        val calender = Calendar.getInstance()
        calender.add(Calendar.MONTH, -1)
        val month = calender.get(Calendar.MONTH)
        return month

    }

    fun getCurrentDay(): String {
        return LocalDate.now().dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    }

}