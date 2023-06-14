package com.example.attendencemaster.presentation.firestore

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendencemaster.R
import com.example.attendencemaster.databinding.FragmentAttendenceListBinding
import com.example.attendencemaster.presentation.auth.AuthViewModel
import com.example.attendencemaster.presentation.firestore.adapter.AttendanceAdapter
import com.example.attendencemaster.utils.NetworkResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class AttendenceListFragment : Fragment() {

    private lateinit var binding: FragmentAttendenceListBinding
    private val authViewModel: AuthViewModel by viewModels()
    private val attendanceViewModel: AttendanceViewModel by viewModels()
    private val attendanceAdapter = AttendanceAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAttendenceListBinding.inflate(layoutInflater, container, false)

        signOutMenu()



        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = attendanceAdapter

        attendanceViewModel.getAttendanceForUsers()
        attendanceViewModel.getAttendance.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResponse.Error -> {
                    binding.attendanceListFragmentProgressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                }
                is NetworkResponse.Loading -> {
                    binding.attendanceListFragmentProgressBar.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                }
                is NetworkResponse.Success -> {
                    binding.attendanceListFragmentProgressBar.visibility = View.GONE
                    attendanceAdapter.submitList(it.data)
                }
            }
        })

        binding.datePicker.setOnClickListener {
            openDatePicker()
        }


        binding.searchAttendance.setOnClickListener {
            val searchedDate = binding.datePicker.text.toString()
            attendanceViewModel.getAttendanceByDate(searchedDate)
            Log.d("TAG", "selectedDate" + searchedDate)

        }

        binding.gotoMarkAttendance.setOnClickListener {
            findNavController().navigate(R.id.action_attendenceListFragment_to_markAttendanceFragment)
        }
        return binding.root
    }

    private fun openDatePicker() {

        val calender = Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate =
                    String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
                binding.datePicker.text = formattedDate
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun signOutMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.signout -> {
                        authViewModel.logout()
                        findNavController().navigate(R.id.action_attendenceListFragment_to_loginFragment)
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding == null
    }
}