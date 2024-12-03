package com.example.medicineproject.UserAccountManagement.LoginActivity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.medicineproject.databinding.ActivityLoginBinding
import com.example.medicineproject.doctoractivity.DoctorActivity
import com.example.medicineproject.patientactivity.PatientActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        when (val destination = viewModel.getNextActivity()) {
            is LoginActivityViewModel.NavigationDestination.Login -> {
            }
            is LoginActivityViewModel.NavigationDestination.Doctor -> {
                val intent = Intent(this, DoctorActivity::class.java) // navigate to doctor mode
                startActivity(intent)
            }
            is LoginActivityViewModel.NavigationDestination.Patient -> {
                val intent = Intent(this, PatientActivity::class.java).apply { //navigate to patient mode
                    putExtra("PatientEmail", destination.email) //pass the user email to patient mode to allow data retrieval for this patient
                }
                startActivity(intent)
            }
        }
    }
}
