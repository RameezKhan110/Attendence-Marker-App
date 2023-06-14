package com.example.attendencemaster.data.repository


import android.util.Log
import com.example.attendencemaster.domain.repository.Attendance
import com.example.attendencemaster.domain.repository.AttendanceRepository
import com.example.attendencemaster.utils.NetworkResponse
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AttendanceRepoImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
) : AttendanceRepository {


    override suspend fun saveAttendance(date: String, time: String, status: Boolean) {
        if (status) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val attendanceId = fireStore.collection("Attendance").document().id
            val attendance =
                Attendance(date, time, "Present", userId, attendanceId)
            fireStore.collection("Attendance").document(attendanceId).set(attendance)
        }
    }

    override suspend fun getAttendanceRecordsForUser(): NetworkResponse<List<Attendance>> {
        val attendanceList = mutableListOf<Attendance>()
        val attendanceRef = fireStore.collection("Attendance")
            .orderBy("date", Query.Direction.DESCENDING)
            .whereEqualTo("userId", FirebaseAuth.getInstance().currentUser?.uid)
        attendanceRef.get().await().let {
            for (doc in it.documents) {
                val attendance = doc.toObject<Attendance>()
                attendance?.let { attendacne ->
                    attendanceList.add(attendacne)
                    Log.d("TAG", "attendanceList$attendanceList")
                }
            }
        }
        return NetworkResponse.Success(attendanceList)
    }

    override suspend fun getAttendanceRecordsByDate(date: String?): NetworkResponse<List<Attendance>> {
        val attendanceList = mutableListOf<Attendance>()
        val attendanceRef = fireStore.collection("Attendance")
            .whereEqualTo("userId", FirebaseAuth.getInstance().currentUser?.uid)
            .whereEqualTo("date", date)
        attendanceRef.get().await().let {
            for (doc in it.documents) {
                val attendance = doc.toObject<Attendance>()
                attendance?.let { attendacne ->
                    attendanceList.add(attendacne)
                    Log.d("TAG", "attendanceList$attendanceList")
                }
            }
        }
        return NetworkResponse.Success(attendanceList)
    }
}
