package com.example.attendencemaster.domain.repository

import com.example.attendencemaster.utils.Status
import com.google.firebase.Timestamp
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

data class Attendance(
    val date: String? = null,
    val time: String? = null,
    val status: String? = null,
    val latitude: String? = null,
    val longitude: String? = null,
    val address: String? = null,
    val state: Status? = null,
    val userId: String? = null,
    val attendanceId: String? = null
)
