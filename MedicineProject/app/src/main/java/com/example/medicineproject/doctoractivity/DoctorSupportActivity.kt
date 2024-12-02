
package com.example.medicineproject.doctoractivity

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medicineproject.R
import com.example.medicineproject.databinding.ActivityDoctorAboutBinding
import com.example.medicineproject.databinding.ActivityDoctorSupportBinding

class DoctorSupportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorSupportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDoctorSupportBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(binding.root)



        binding.backDoctorSupport.setOnClickListener {
            finish()
        }
    }
}