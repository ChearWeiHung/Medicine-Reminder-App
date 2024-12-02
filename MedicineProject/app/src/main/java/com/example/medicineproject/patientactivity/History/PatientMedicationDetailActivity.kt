package com.example.medicineproject.patientactivity.History

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicineproject.databinding.ActivityPatientMedicationDetailBinding
import com.example.medicineproject.doctoractivity.DataClasses.UserMedicineDetail

class PatientMedicationDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPatientMedicationDetailBinding
    private val viewModel: PatientMedicationDetailViewModel by viewModels()  // ViewModel instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPatientMedicationDetailBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(binding.root)

        val date = intent.getStringExtra("Date") ?: ""
        val patientEmail = intent.getStringExtra("PatientEmail") ?: ""

        // Set up the RecyclerView
        val rvPatientMedicineDetailHistoryAdaptor = PatientMedicationDetailActivityRVAdaptor(ArrayList())
        binding.rvPatientMedicineDetailHistory.adapter = rvPatientMedicineDetailHistoryAdaptor
        binding.rvPatientMedicineDetailHistory.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
        }

        // Observe LiveData from the ViewModel to update the UI
        viewModel.medicineList.observe(this, Observer { medicineList ->
            // Update the RecyclerView with the new data
            rvPatientMedicineDetailHistoryAdaptor.updateList(medicineList)
        })

        // Fetch data from Firebase
        viewModel.fetchData(patientEmail, date)

        // Back button listener
        binding.backPatientMedicineDetailHistory.setOnClickListener {
            finish()
        }

        // Add selected medicine to the schedule
        binding.btnAddToSchedule.setOnClickListener {
            val selectedMedicine = rvPatientMedicineDetailHistoryAdaptor.getSelectedItem() //get selected item
            if (selectedMedicine != null) {
                viewModel.addMedicineToSchedule(patientEmail, selectedMedicine) //save selected item to the firebase database
                Toast.makeText(this, "Selected item added to Schedule", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please select an item first", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
