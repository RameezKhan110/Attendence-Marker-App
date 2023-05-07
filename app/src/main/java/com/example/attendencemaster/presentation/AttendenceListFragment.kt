package com.example.attendencemaster.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.attendencemaster.R
import com.example.attendencemaster.databinding.FragmentAttendenceListBinding
import com.example.attendencemaster.presentation.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AttendenceListFragment : Fragment() {

    lateinit var binding: FragmentAttendenceListBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAttendenceListBinding.inflate(layoutInflater, container, false)

        binding.signOut.setOnClickListener {
            authViewModel.logout()
            findNavController().navigate(R.id.action_attendenceListFragment_to_loginFragment)
        }
        return binding.root
    }

}