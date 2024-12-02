package com.example.medicineproject.patientactivity.Setting

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.medicineproject.databinding.ActivityDoctorAboutBinding
import com.example.medicineproject.databinding.ActivityPatientAboutBinding


class PatientAboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatientAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPatientAboutBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(binding.root)

        binding.backPatientAbout.setOnClickListener{
            finish()
        }

    }
}