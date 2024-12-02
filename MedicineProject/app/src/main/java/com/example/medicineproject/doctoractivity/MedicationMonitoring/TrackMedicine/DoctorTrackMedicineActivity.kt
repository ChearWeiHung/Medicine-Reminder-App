package com.example.medicineproject.doctoractivity.MedicationMonitoring

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicineproject.databinding.ActivityDoctorTrackMedicineBinding
import com.example.medicineproject.doctoractivity.DataClasses.UserMedicineDetail
import com.example.medicineproject.doctoractivity.MedicationMonitoring.TrackMedicine.DoctorTrackMedicineRVAdaptor
import com.example.medicineproject.doctoractivity.MedicationMonitoring.TrackProgress.DoctorTrackProgressActivity

class DoctorTrackMedicineActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorTrackMedicineBinding
    private val doctorTrackMedicineViewModel: DoctorTrackMedicineViewModel by viewModels() // ViewModel instance
    private lateinit var medicineList: ArrayList<UserMedicineDetail>

    private val databaseUrl =
        "https://medicinereminderappproje-17b6b-default-rtdb.asia-southeast1.firebasedatabase.app"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorTrackMedicineBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(binding.root)

        medicineList = arrayListOf()
        val date = intent.getStringExtra("Date") ?: ""
        val patientEmail = intent.getStringExtra("PatientEmail") ?: ""

        // Observe the LiveData from ViewModel
        doctorTrackMedicineViewModel.medicineList.observe(this, Observer { medicines ->
            setupRecyclerView(medicines)
        })

        // Fetch data when activity is created
        doctorTrackMedicineViewModel.fetchMedicineData(patientEmail, date)
        // Set up the RecyclerView for displaying the medicine list
        binding.rvDoctorTrackMedicine.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DoctorTrackMedicineActivity)
        }

        binding.backDoctorTrackMedicine.setOnClickListener { //navigate to previous activity
            finish()
        }
    }

    // Function to set up the RecyclerView with the list of medicines
    private fun setupRecyclerView(medicines: List<UserMedicineDetail>) {
        val adapter = DoctorTrackMedicineRVAdaptor(medicines)
        binding.rvDoctorTrackMedicine.adapter = adapter

        adapter.setOnItemClickListener(object : DoctorTrackMedicineRVAdaptor.OnItemClickListener {
            override fun onItemClick(position: Int) { //triggered when the item in recycle view is clicked
                val date = intent.getStringExtra("Date") ?: ""
                val patientEmail = intent.getStringExtra("PatientEmail") ?: ""
                val intent = Intent(this@DoctorTrackMedicineActivity, DoctorTrackProgressActivity::class.java) //navigation
                intent.putExtra("PatientEmail", patientEmail)
                intent.putExtra("Date", date)
                intent.putExtra("MedicineName", medicines[position].medicineName)
                startActivity(intent)
            }
        })
    }
}
