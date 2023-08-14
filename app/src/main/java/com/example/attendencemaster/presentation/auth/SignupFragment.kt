package com.example.attendencemaster.presentation.auth


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.attendencemaster.R
import com.example.attendencemaster.databinding.FragmentSignupBinding
import com.example.attendencemaster.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupFragment : Fragment() {

    lateinit var binding: FragmentSignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var firebaseUser: FirebaseUser? = null
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(layoutInflater, container, false)


        binding.registerBtn.setOnClickListener {
            val registerUserName = binding.registerName.text.trim().toString()
            val registerUserEmail = binding.registerEmail.text.trim().toString()
            val registerUserPassword = binding.registerPassword.text.trim().toString()
            authViewModel.signup(registerUserName, registerUserEmail, registerUserPassword)
        }

        lifecycleScope.launch() {
            authViewModel.signupFlow.collect {
                when (it) {
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.exception.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        findNavController().navigate(R.id.action_signupFragment_to_attendenceListFragment)
                    }
                    else -> {

                    }
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding == null
    }
}