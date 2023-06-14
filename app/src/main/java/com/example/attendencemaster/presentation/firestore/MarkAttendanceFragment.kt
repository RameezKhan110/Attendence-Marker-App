package com.example.attendencemaster.presentation.firestore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.attendencemaster.R
import com.example.attendencemaster.databinding.FragmentMarkAttendanceBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class MarkAttendanceFragment : Fragment() {

    lateinit var binding: FragmentMarkAttendanceBinding
    private val attendanceViewModel: AttendanceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMarkAttendanceBinding.inflate(layoutInflater, container, false)


        binding.markAttendanceFragmentCurrentDate.text = attendanceViewModel.getDate()

        binding.markAttendanceFragmentBtn.setOnClickListener {

            attendanceViewModel.saveAttendance(true)
            findNavController().navigate(R.id.action_markAttendanceFragment_to_attendenceListFragment)
        }

        return binding.root
    }

}