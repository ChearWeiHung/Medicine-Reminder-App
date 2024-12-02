package com.example.medicineproject.patientactivity.Setting

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medicineproject.R
import com.example.medicineproject.databinding.ActivityDoctorSupportBinding
import com.example.medicineproject.databinding.ActivityPatientSupportBinding

class PatientSupportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatientSupportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPatientSupportBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(binding.root)



        binding.backPatientSupport.setOnClickListener {
            finish()
        }

    }
}