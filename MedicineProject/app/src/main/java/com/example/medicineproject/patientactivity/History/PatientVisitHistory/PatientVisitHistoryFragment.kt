package com.example.medicineproject.patientactivity.History.PatientVisitHistory

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicineproject.databinding.FragmentPatientVisitHistoryBinding
import com.example.medicineproject.patientactivity.History.PatientMedicationDetailActivity

class PatientVisitHistoryFragment : Fragment() {
    private lateinit var binding: FragmentPatientVisitHistoryBinding
    private val viewModel: PatientVisitHistoryFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPatientVisitHistoryBinding.inflate(inflater, container, false)

        // Set up RecyclerView
        binding.rvPatientMedicineList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }

        // Get patient email from arguments
        val patientEmail = arguments?.getString("PatientEmail")!!

        // Fetch data from Firebase through the ViewModel
        viewModel.fetchData(patientEmail)

        // Observe the dates LiveData
        viewModel.dates.observe(viewLifecycleOwner, Observer { dateList ->
            val adapter = PatientVisitHistoryFragmentRVAdaptor(dateList.toMutableList())
            binding.rvPatientMedicineList.adapter = adapter

            // Set up the item click listener for the RecyclerView items
            adapter.setOnItemClickListener(object : PatientVisitHistoryFragmentRVAdaptor.OnItemClickListener {
                override fun onItemClick(position: Int) {  // When an item is clicked, navigate to the PatientMedicationDetailActivity
                    // Pass the selected patient's email and visit date to the next activity
                    val intent = Intent(activity, PatientMedicationDetailActivity::class.java)
                    intent.putExtra("PatientEmail", patientEmail)
                    intent.putExtra("Date", dateList[position])
                    startActivity(intent)
                }
            })
        })

        return binding.root
    }
}
