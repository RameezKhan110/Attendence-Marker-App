package com.example.attendencemaster.presentation.firestore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.attendencemaster.databinding.AttendanceItemBinding
import com.example.attendencemaster.domain.repository.Attendance

class AttendanceAdapter(): ListAdapter<Attendance, AttendanceAdapter.AttendanceViewHolder>(DiffUtil()){


    inner class AttendanceViewHolder(binding: AttendanceItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val attendance = AttendanceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AttendanceViewHolder(attendance)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val attendanceDetails = getItem(position)
        AttendanceItemBinding.bind(holder.itemView).apply {
            date.text = attendanceDetails.date
            time.text = attendanceDetails.time
            status.text = attendanceDetails.status
        }
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Attendance>() {
        override fun areItemsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
            return oldItem.attendanceId == newItem.attendanceId
        }

        override fun areContentsTheSame(
            oldItem: Attendance, newItem: Attendance
        ): Boolean {
            return oldItem == newItem
        }
    }
}