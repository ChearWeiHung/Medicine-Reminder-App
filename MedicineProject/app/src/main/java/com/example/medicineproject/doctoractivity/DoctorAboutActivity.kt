package com.example.medicineproject.doctoractivity

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import com.example.medicineproject.databinding.ActivityDoctorAboutBinding


class DoctorAboutActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDoctorAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDoctorAboutBinding.inflate(layoutInflater)

        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        binding.backDoctorAbout.setOnClickListener{
            finish()
        }

    }
}