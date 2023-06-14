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
import com.example.attendencemaster.databinding.FragmentLoginBinding
import com.example.attendencemaster.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    lateinit var firebaseAuth: FirebaseAuth
     var firebaseUser: FirebaseUser? = null
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser


        binding.goToRegisterFrag.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        binding.loginBtn.setOnClickListener {
            val loginEmail = binding.loginEmail.text.trim().toString()
            val loginPassword = binding.loginPassword.text.trim().toString()
            authViewModel.login(loginEmail, loginPassword)
        }

        lifecycleScope.launch(){
            authViewModel.loginFlow.collect{
                when(it){
                    is Resource.Error -> {
                        binding.progressBar2.visibility = View.GONE
                        Toast.makeText(requireContext(), it.exception.message, Toast.LENGTH_SHORT).show()
                    }
                    Resource.Loading -> {
                        binding.progressBar2.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar2.visibility = View.GONE
                        findNavController().navigate(R.id.action_loginFragment_to_attendenceListFragment3)
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