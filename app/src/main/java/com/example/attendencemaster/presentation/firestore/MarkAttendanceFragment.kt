package com.example.attendencemaster.presentation.firestore

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.attendencemaster.R
import com.example.attendencemaster.databinding.FragmentMarkAttendanceBinding
import com.example.attendencemaster.utils.Status.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MarkAttendanceFragment : Fragment() {

    lateinit var binding: FragmentMarkAttendanceBinding
    private val attendanceViewModel: AttendanceViewModel by viewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var address = ""

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMarkAttendanceBinding.inflate(layoutInflater, container, false)

        binding.currentDay.text = attendanceViewModel.getCurrentDay()
        binding.currentDate.text = attendanceViewModel.getDate()
        binding.currentTime.text = attendanceViewModel.getTime()

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            getCurrentLocation()
        }

        attendanceViewModel.checkState()
        attendanceViewModel.getState.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { Status ->
                when (Status) {
                    NEUTRAL -> {
                        binding.markAttendanceText.visibility = View.VISIBLE
                        binding.checkinLayout.visibility = View.VISIBLE
                        binding.checkoutLayout.visibility = View.GONE
                        Log.d("TAG", "Neutral Working")
                    }
                    CHECKED_IN -> {
                        binding.markAttendanceText.visibility = View.GONE
                        binding.checkinLayout.visibility = View.GONE
                        binding.checkoutLayout.visibility = View.VISIBLE
                        Log.d("TAG", "Checked In Working")
                    }
                    CHECKED_OUT -> {
                        binding.markAttendanceText.visibility = View.GONE
                        binding.checkinLayout.visibility = View.GONE
                        binding.checkoutLayout.visibility = View.GONE
                        Log.d("TAG", "Checked Out Working")
                    }
                }
            })

        binding.MarkAttendanceOrCheckIn.setOnClickListener {
            if (attendanceViewModel.getCurrentDay() == "Sunday" || attendanceViewModel.getCurrentDay() == "Saturday") {
                Toast.makeText(
                    requireContext(),
                    "Today is Weekend, You can't Mark Attendance",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val latitude = binding.latitude.text.toString()
                val longitude = binding.longitude.text.toString()
                val address = binding.address.text.toString()
                attendanceViewModel.saveAttendance(true, latitude, longitude, address, CHECKED_IN)
                findNavController().navigate(R.id.action_markAttendanceFragment_to_attendenceListFragment)
                binding.checkoutLayout.visibility = View.VISIBLE
            }
        }

        binding.checkOut.setOnClickListener {
            attendanceViewModel.saveCheckoutTime(CHECKED_OUT)
            findNavController().navigate(R.id.action_markAttendanceFragment_to_attendenceListFragment)
//            binding.checkoutLayout.visibility = View.GONE
            Toast.makeText(requireContext(), "You have completed your day", Toast.LENGTH_SHORT)
                .show()
        }
        return binding.root
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                getAddress(location.latitude, location.longitude)
                binding.latitude.text = location.latitude.toString()
                binding.longitude.text = location.longitude.toString()
                Log.d("TAG", "lat and long" + location.latitude + location.longitude)
                binding.address.text = address
            }else{
                Toast.makeText(requireContext(), "Null Location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            }
        }
    }

    private fun getAddress(lat: Double, lang: Double) {
        try {
            val geoCoder = Geocoder(requireContext(), Locale.getDefault())
            val myAddress = geoCoder.getFromLocation(lat, lang, 3)
            address = myAddress!![0].getAddressLine(0)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Loading Current Address", Toast.LENGTH_SHORT).show()
        }
    }

}