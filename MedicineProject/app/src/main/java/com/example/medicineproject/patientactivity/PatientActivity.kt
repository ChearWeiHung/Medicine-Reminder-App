package com.example.medicineproject.patientactivity

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.medicineproject.R
import com.example.medicineproject.databinding.ActivityPatientBinding
import com.example.medicineproject.patientactivity.Reminder.PatientMedicineSchedule.PatientMedicineScheduleFragment

import com.example.medicineproject.patientactivity.Setting.PatientSettingFragment
import com.example.medicineproject.patientactivity.History.PatientVisitHistory.PatientVisitHistoryFragment

class PatientActivity : AppCompatActivity() {
private lateinit var binding: ActivityPatientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(binding.root)

        val Email = intent.getStringExtra("PatientEmail") // Retrieve patient email
        val patientEmail= Email?.replace(".",",")

        if (savedInstanceState == null) {  // Check if it's a fresh start
            val fragment = PatientMedicineScheduleFragment()

            // Create a Bundle to pass data
            val args = Bundle()
            args.putString("PatientEmail", patientEmail)  // Replace "key" and "value" with your actual key-value pair

            // Set arguments to the fragment
            fragment.arguments = args

            // Replace the fragment with the one that has the data
            replaceFragment(fragment)
        }

        // Bottom Navigation View setup
        binding.BottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {

                R.id.medication -> {
                    val medicationFragment = PatientMedicineScheduleFragment().apply {
                        arguments = Bundle().apply {
                            putString("PatientEmail", patientEmail)  // Pass patientEmail to the fragment
                        }
                    }
                    replaceFragment(medicationFragment)
                }
                R.id.History -> {
                    val medicationFragment = PatientVisitHistoryFragment().apply {
                        arguments = Bundle().apply {
                            putString("PatientEmail", patientEmail)  // Pass patientEmail to the fragment
                        }
                    }
                    replaceFragment(medicationFragment)
                }
                R.id.setting -> {
                    replaceFragment(PatientSettingFragment())
                }
                else -> {}
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentlayout, fragment)
        fragmentTransaction.commit()
    }
}
