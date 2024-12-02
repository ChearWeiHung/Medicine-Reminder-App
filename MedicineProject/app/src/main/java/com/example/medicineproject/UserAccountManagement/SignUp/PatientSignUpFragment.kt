package com.example.medicineproject.UserAccountManagement.SignUp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.medicineproject.R
import com.example.medicineproject.databinding.FragmentPatientSignUpBinding
import com.example.medicineproject.patientactivity.PatientActivity

class PatientSignUpFragment : Fragment() {

    private lateinit var binding: FragmentPatientSignUpBinding
    private val patientSignUpViewModel: PatientSignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPatientSignUpBinding.inflate(inflater, container, false)

        // Observe the registration result
        patientSignUpViewModel.registrationResult.observe(viewLifecycleOwner, { result ->
            Toast.makeText(activity, result, Toast.LENGTH_LONG).show()
            if (result == "Account saved successfully") {
                val intent = Intent(activity, PatientActivity::class.java)
                intent.putExtra("PatientEmail", binding.etEmailPatientSignUp.text.toString().replace(".", ","))
                startActivity(intent)
            }
        })

        binding.ivBackPatientSignUp.setOnClickListener {
            it.findNavController().navigate(R.id.action_patientSignUpFragment_to_patientFragment) //back to previous fragment
        }

        binding.btnPatientSubmit.setOnClickListener {
            // Get values from EditTexts
            val name = binding.etUsernamePatientSignUp.text.toString()
            val email = binding.etEmailPatientSignUp.text.toString()
            val password = binding.etPatientPasswordsignup.text.toString()
            val confirmPassword = binding.etConfirmPasswordSignUpPatient.text.toString()

            // Set LiveData values for ViewModel
            patientSignUpViewModel.setName(name)
            patientSignUpViewModel.setEmail(email)
            patientSignUpViewModel.setPassword(password)
            patientSignUpViewModel.setConfirmPassword(confirmPassword)

            // Call registerUser function from ViewModel
            patientSignUpViewModel.registerUser()
        }

        return binding.root
    }
}
