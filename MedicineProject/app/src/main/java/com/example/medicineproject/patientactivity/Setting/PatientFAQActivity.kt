package com.example.medicineproject.patientactivity.Setting

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.medicineproject.databinding.ActivityPatientAboutBinding
import com.example.medicineproject.databinding.ActivityPatientFaqBinding


class PatientFAQActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPatientFaqBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPatientFaqBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(binding.root)

        binding.backPatientFAQ.setOnClickListener{//back to previous and destroy current activity
            finish()
        }

    }
}