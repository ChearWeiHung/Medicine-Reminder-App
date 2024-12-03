package com.example.medicineproject.patientactivity.Reminder.PatientMedicineSchedule


import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicineproject.databinding.FragmentPatientMedicineScheduleBinding
import com.example.medicineproject.patientactivity.Reminder.MedicineReminderActivity


class PatientMedicineScheduleFragment : Fragment() {

    private lateinit var binding: FragmentPatientMedicineScheduleBinding
    private val viewModel: PatientMedicineScheduleViewModel by viewModels() // ViewModel instance


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPatientMedicineScheduleBinding.inflate(inflater, container, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        }


        // Fetch data from previous activity
        val patientEmail = arguments?.getString("PatientEmail")!!

        // Set up RecyclerView
        binding.rvMedicationListPatient.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)

        }

        // Observe LiveData from the ViewModel to update the RecyclerView
        viewModel.medicineList.observe(viewLifecycleOwner, Observer { medicineList ->
            val adapter = PatientMedicineScheduleFragmentRVAdaptor(medicineList)
            binding.rvMedicationListPatient.adapter = adapter

            // Set up the item click listener for each item in the RecyclerView
            adapter.setOnItemClickListener(object :
                PatientMedicineScheduleFragmentRVAdaptor.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    // Navigate and pass data of that utem to Medicine Reminder Activity
                    val selectedMedicine = medicineList[position]
                    val intent = Intent(activity, MedicineReminderActivity::class.java).apply {
                        putExtra("PatientEmail", patientEmail)
                        putExtra("MedicineName", selectedMedicine.medicineName)
                        putExtra("MedicineHours", selectedMedicine.hours)
                        putExtra("PillAmount", selectedMedicine.pills)
                        putExtra("PillLeft", selectedMedicine.pillsleft)
                        putExtra("Date", selectedMedicine.date)
                    }
                    startActivity(intent)
                }
            })
        })

        // Fetch data from ViewModel
        viewModel.fetchData(patientEmail)

        return binding.root
    }

    private fun requestNotificationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is already granted

            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                POST_NOTIFICATIONS
            ) -> {

            }

            else -> {
                // Directly request the permission
                requestPermissionLauncher.launch(POST_NOTIFICATIONS)
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
        } else {
           Toast.makeText(requireContext(),"Please grant notification in the setting",Toast.LENGTH_LONG).show()
        }
    }
}
