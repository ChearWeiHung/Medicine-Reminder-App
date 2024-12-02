package com.example.medicineproject.doctoractivity.MedicationMonitoring.TrackProgress

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicineproject.databinding.ActivityDoctorTrackProgressBinding

class DoctorTrackProgressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorTrackProgressBinding
    private val doctorTrackProgressViewModel: DoctorTrackProgressViewModel by viewModels() // ViewModel instance
    private lateinit var dateAndTime: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorTrackProgressBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(binding.root)
        dateAndTime = mutableListOf()

        val patientEmail = intent.getStringExtra("PatientEmail") ?: ""
        val medicineName = intent.getStringExtra("MedicineName") ?: ""
        val date = intent.getStringExtra("Date") ?: ""

        // Observe the LiveData from the ViewModel
        doctorTrackProgressViewModel.dateAndTimeList.observe(this, Observer { dateTimes ->
            setupRecyclerView(dateTimes)
        })

        // Fetch the data when the activity is created
        doctorTrackProgressViewModel.fetchDateAndTimeData(patientEmail, medicineName, date)

        //Setup for recycler view
        binding.rvDoctorTrackProgress.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DoctorTrackProgressActivity)
        }

        binding.backMedicineIntakeLog.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView(dateTimes: List<String>) {
        val adapter = DoctorTrackProgressRVAdaptor(dateTimes)
        binding.rvDoctorTrackProgress.adapter = adapter
    }
}
