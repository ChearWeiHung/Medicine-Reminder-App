package com.example.medicineproject.doctoractivity.MedicationMonitoring.TrackDate

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicineproject.databinding.ActivityDoctorTrackDataBinding
import com.example.medicineproject.doctoractivity.MedicationMonitoring.DoctorTrackMedicineActivity


class DoctorTrackDateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorTrackDataBinding
    private val doctorTrackDateViewModel: DoctorTrackDateViewModel by viewModels() // ViewModel instance

    private val databaseUrl =
        "https://medicinereminderappproje-17b6b-default-rtdb.asia-southeast1.firebasedatabase.app"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorTrackDataBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT //restrict the orientation to portrait
        setContentView(binding.root)

        val patientName = intent.getStringExtra("PatientName") ?: ""
        val patientEmail = intent.getStringExtra("PatientEmail") ?: ""

        // Observe the patient date list from ViewModel
        doctorTrackDateViewModel.patientDateList.observe(this, Observer { patientDate ->
            setupRecyclerView(patientDate)
        })

        // Fetch data when activity is created
        doctorTrackDateViewModel.fetchData(patientEmail)

        binding.backDoctorTrackVisitDate.setOnClickListener {
            finish()
        }
    }

    // Function to set up the RecyclerView
    private fun setupRecyclerView(patientDate: List<String>) {
        binding.rvMedicationTracking.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DoctorTrackDateActivity)
            val adapter = DoctorTrackDateActivityRVAdaptor(patientDate)
            this.adapter = adapter

            adapter.setOnItemClickListener(object :
                DoctorTrackDateActivityRVAdaptor.OnItemClickListener {
                override fun onItemClick(position: Int) { //triggered when the item in recycle view is clicked
                    val patientEmail = intent.getStringExtra("PatientEmail") ?: ""
                    val intent = Intent(this@DoctorTrackDateActivity, DoctorTrackMedicineActivity::class.java)
                    intent.putExtra("PatientEmail", patientEmail)
                    intent.putExtra("Date", patientDate[position])
                    startActivity(intent)
                }
            })
        }
    }
}
