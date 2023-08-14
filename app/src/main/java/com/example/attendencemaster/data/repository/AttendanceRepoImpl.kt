package com.example.attendencemaster.data.repository


import android.util.Log
import com.example.attendencemaster.domain.repository.Attendance
import com.example.attendencemaster.domain.repository.AttendanceRepository
import com.example.attendencemaster.utils.NetworkResponse
import com.example.attendencemaster.utils.Status
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AttendanceRepoImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
) : AttendanceRepository {

    override suspend fun saveAttendance(
        date: String,
        time: String,
        status: Boolean,
        latitude: String,
        longitude: String,
        address: String,
        state: Status
    ) {
        if (status) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val attendanceId = fireStore.collection("Attendance").document().id
            val attendance =
                Attendance(date, time,"Present", latitude, longitude, address, state, userId, attendanceId)
            fireStore.collection("Attendance").document(attendanceId).set(attendance)
        }
    }

    override suspend fun saveCheckoutTime(checkout: String, state: Status) {
        val attendanceRef = fireStore.collection("Attendance")
            .orderBy("date", Query.Direction.ASCENDING)
            .orderBy("time", Query.Direction.DESCENDING)
            .whereEqualTo("userId", FirebaseAuth.getInstance().currentUser?.uid)
        attendanceRef.get().await().let {
            val documentId = it.documents[0]
            val attendanceId = documentId.id
            Log.d("TAG", "ID: $attendanceId")
            val map = hashMapOf<String, Any>("Checkout" to checkout, "state" to state)
            fireStore.collection("Attendance").document(attendanceId).update(map).await()
        }

    }

    override suspend fun checkingState(): Status {
        val checkState: Status
        val attendanceRef = fireStore.collection("Attendance")
            .orderBy("date", Query.Direction.ASCENDING)
            .orderBy("time", Query.Direction.DESCENDING)
            .whereEqualTo("userId", FirebaseAuth.getInstance().currentUser?.uid)
        attendanceRef.get().await().let {
            val documentId = it.documents[0]
            val attendanceId = documentId.id
            Log.d("TAG", "attendaceId for state "+ attendanceId)
            val documentRef = fireStore.collection("Attendance").document(attendanceId)

            documentRef.get().await().let {
                val state = it.getString("state")
                checkState = Status.valueOf(state!!)
            }
        }
        return checkState
    }

    override suspend fun saveNeutralState(state: Status) {
        val attendanceRef = fireStore.collection("Attendance")
            .orderBy("date", Query.Direction.ASCENDING)
            .orderBy("time", Query.Direction.DESCENDING)
            .whereEqualTo("userId", FirebaseAuth.getInstance().currentUser?.uid)
        attendanceRef.get().await().let {
            val documentId = it.documents[0]
            val attendanceId = documentId.id
            Log.d("TAG", "ID: $attendanceId")
            val map = mutableMapOf<String, Any>("state" to state)
            fireStore.collection("Attendance").document(attendanceId).update(map).await()
        }
    }

    override suspend fun addingState() {
        val state: Status = Status.NEUTRAL
        val attendanceRef = fireStore.collection("Attendance")
            .orderBy("date", Query.Direction.ASCENDING)
            .orderBy("time", Query.Direction.DESCENDING)
            .whereEqualTo("userId", FirebaseAuth.getInstance().currentUser?.uid)
        attendanceRef.get().await().let {
            val map = hashMapOf<String, Any>("state" to state)
            fireStore.collection("Attendance").document("QnSFk632k51W9ViFPVvJ").update(map).await()
        }
    }

    override suspend fun getAttendanceRecordsForUser(): NetworkResponse<List<Attendance>> {
        val attendanceList = mutableListOf<Attendance>()
        val attendanceRef = fireStore.collection("Attendance")
            .orderBy("date", Query.Direction.ASCENDING)
            .orderBy("time", Query.Direction.DESCENDING)
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
            .orderBy("time", Query.Direction.DESCENDING)
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

    override suspend fun getLastWeekAttendance(
        previousDate: String?,
        lastWeekDate: String?
    ): NetworkResponse<List<Attendance>> {

        val attendanceList = mutableListOf<Attendance>()
        val attendanceRef = fireStore.collection("Attendance")
            .whereEqualTo("userId", FirebaseAuth.getInstance().currentUser?.uid)
            .whereGreaterThanOrEqualTo("date", lastWeekDate!!)
            .whereLessThanOrEqualTo("date", previousDate!!)
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

    override suspend fun getLastMonthAttendance(
        startDate: String?,
        endDate: String?
    ): NetworkResponse<List<Attendance>> {

        val attendanceList = mutableListOf<Attendance>()
        val attendanceRef = fireStore.collection("Attendance")
            .whereEqualTo("userId", FirebaseAuth.getInstance().currentUser?.uid)
            .whereGreaterThanOrEqualTo("date", endDate!!)
            .whereLessThanOrEqualTo("date", startDate!!)
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
