package com.example.medicineproject.doctoractivity

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.medicineproject.R
import com.example.medicineproject.databinding.ActivityDoctorBinding
import com.example.medicineproject.doctoractivity.MedicationManagement.DoctorNameList.DoctorNameListFragment
import com.example.medicineproject.doctoractivity.MedicationMonitoring.TrackName.DoctorTrackNameFragment

class DoctorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(binding.root)
        if (savedInstanceState == null) {  // Check if it's a fresh start
            replaceFragment(DoctorNameListFragment())  // Set the initial fragment to display

        }


        binding.bottomNavigationViewDoctor.setOnItemSelectedListener{
            when (it.itemId) {
                // If the home item is selected, change to the PatientHomeFragment


                // If the medication is selected, change to the MedicationFragment
                R.id.medicationdoctor ->replaceFragment(DoctorNameListFragment() )

                R.id.settingdoctor->replaceFragment(DoctorSettingFragment())

                R.id.Tracking-> replaceFragment(DoctorTrackNameFragment())
                else->{

                }
            }
            true
        }

    }


    private fun replaceFragment(fragment: Fragment){

        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayoutDoctor,fragment)
        fragmentTransaction.commit()
    }
}