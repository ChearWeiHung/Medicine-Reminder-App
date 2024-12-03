package com.example.medicineproject.UserAccountManagement.LoginFragment

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.medicineproject.R
import com.example.medicineproject.databinding.FragmentLoginBinding
import com.example.medicineproject.doctoractivity.DoctorActivity
import com.example.medicineproject.patientactivity.PatientActivity

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val patientLoginViewModel: LoginFragmentViewModel by viewModels() // ViewModel instance

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.tvClickToSignUp.paintFlags = binding.tvClickToSignUp.paintFlags or Paint.UNDERLINE_TEXT_FLAG // Set underline

        binding.tvClickToSignUp.setOnClickListener {
            it.findNavController().navigate(R.id.action_patientFragment_to_patientSignUpFragment) // Navigate to signup page
        }

        // Observe login result from ViewModel
        patientLoginViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is LoginFragmentViewModel.LoginResult.SuccessPatient -> {
                    val intent = Intent(activity, PatientActivity::class.java).apply {
                        putExtra("PatientEmail", binding.etPatientEmail.text.toString())
                    }
                    startActivity(intent)
                }
                is LoginFragmentViewModel.LoginResult.SuccessDoctor -> {
                    startActivity(Intent(activity, DoctorActivity::class.java))
                }
                is LoginFragmentViewModel.LoginResult.Failure -> {
                    Toast.makeText(activity, "Invalid Account", Toast.LENGTH_LONG).show()
                }
                is LoginFragmentViewModel.LoginResult.EmptyField -> {
                    Toast.makeText(activity, "Empty fields are not allowed", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.btnLoginPatient.setOnClickListener {
            val patientEmail = binding.etPatientEmail.text.toString()
            val patientLoginPassword = binding.etPatientPassword.text.toString()

            // Call login function in ViewModel
            patientLoginViewModel.login(patientEmail, patientLoginPassword)
        }

        return binding.root
    }
}
