package com.example.medicineproject.doctoractivity.MedicationManagement

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicineproject.databinding.ActivityDoctorAddMedicationBinding
import java.time.LocalDate

class DoctorAddMedicationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorAddMedicationBinding
    private val viewModel: DoctorAddMedicationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorAddMedicationBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(binding.root)

        val patientName = intent.getStringExtra("PatientName") ?: ""
        val patientEmail = intent.getStringExtra("PatientEmail") ?: ""
        val formattedDate = LocalDate.now().toString()

        binding.tvNamePatientMedication.text = patientName
        binding.backAddMedication.setOnClickListener {
            finish()
        }

        // Set up RecyclerView
        val rvPatientMedicineListAdapter = DoctorAddMedicationRVAdaptor()
        binding.rvPatientMedicineList.apply {
            layoutManager = LinearLayoutManager(this@DoctorAddMedicationActivity)
            adapter = rvPatientMedicineListAdapter
        }

        // Observe LiveData from ViewModel for patient medicine list
        viewModel.patientMedicineList.observe(this) { patientMedicineList ->
            rvPatientMedicineListAdapter.updateList(patientMedicineList)
        }

        // Fetch data from Firebase
        viewModel.fetchData(patientEmail, formattedDate)

        // Link EditText fields to ViewModel
        binding.etMedicineDoctor.addTextChangedListener { text ->
            if (text.toString() != viewModel.medicineName.value) {
                viewModel.setMedicineName(text.toString())
            }
        }
        binding.etHours.addTextChangedListener { text ->
            if (text.toString() != viewModel.hours.value) {
                viewModel.setHours(text.toString())
            }
        }
        binding.etPill.addTextChangedListener { text ->
            if (text.toString() != viewModel.pills.value) {
                viewModel.setPills(text.toString())
            }
        }
        binding.etTotalPill.addTextChangedListener { text ->
            if (text.toString() != viewModel.totalPills.value) {
                viewModel.setTotalPills(text.toString())
            }
        }

        // Add and Remove medicine logic
        binding.btnAdd.setOnClickListener {
            val medicineName = viewModel.medicineName.value ?: ""
            val hours = viewModel.hours.value ?: ""
            val pills = viewModel.pills.value ?: ""
            val totalPills = viewModel.totalPills.value ?: ""
            if (medicineName.isNotEmpty() && hours.isNotEmpty() && pills.isNotEmpty() && totalPills.isNotEmpty()) { //validation for empty fields
                if (hours.toInt() <= 36) { //validation for time interval input lesser than 36hours(not make sense if above 36hours)
                    viewModel.addMedicineToFirebase(
                        medicineName,
                        hours,
                        pills,
                        totalPills,
                        formattedDate,
                        patientEmail
                    )
                } else {

                    Toast.makeText(this, "Please fill hours less than 36", Toast.LENGTH_LONG).show()
                }
            }else{

                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show()
            }

        }

        binding.btnRemove.setOnClickListener { //to remove the selected medicine
            val selectedMedicine = rvPatientMedicineListAdapter.getSelectedItem() // get selected item
            selectedMedicine?.let { selected ->
                viewModel.removeMedicineFromFirebase(selected.medicineName.toString(), formattedDate, patientEmail)
            }
        }

        // Observe ViewModel LiveData and update EditText fields if values change
        viewModel.medicineName.observe(this) {
            if (binding.etMedicineDoctor.text.toString() != it) {
                binding.etMedicineDoctor.setText(it)
            }
        }
        viewModel.hours.observe(this) {
            if (binding.etHours.text.toString() != it) {
                binding.etHours.setText(it)
            }
        }
        viewModel.pills.observe(this) {
            if (binding.etPill.text.toString() != it) {
                binding.etPill.setText(it)
            }
        }
        viewModel.totalPills.observe(this) {
            if (binding.etTotalPill.text.toString() != it) {
                binding.etTotalPill.setText(it)
            }
        }
    }
}
