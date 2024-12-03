package com.example.medicineproject.doctoractivity.MedicationMonitoring.TrackName

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicineproject.databinding.FragmentDoctorTrackBinding
import com.example.medicineproject.doctoractivity.MedicationMonitoring.TrackDate.DoctorTrackDateActivity
import java.util.ArrayList

class DoctorTrackNameFragment : Fragment() {
    private lateinit var binding: FragmentDoctorTrackBinding
    private lateinit var DoctorTrackNameFragmentRVAdaptor: DoctorTrackNameFragmentRVAdaptor

    // Access the ViewModel instance
    private val doctorTrackNameViewModel: DoctorTrackNameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDoctorTrackBinding.inflate(inflater, container, false)

        // Initialize the adapter
        DoctorTrackNameFragmentRVAdaptor = DoctorTrackNameFragmentRVAdaptor(ArrayList())

        // Set up RecyclerView
        binding.rvPatientNameTrack.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
            adapter = DoctorTrackNameFragmentRVAdaptor
        }

        // Observe LiveData from ViewModel
        doctorTrackNameViewModel.patientList.observe(viewLifecycleOwner, Observer { patientList ->
            DoctorTrackNameFragmentRVAdaptor.updateFullList(patientList)
            DoctorTrackNameFragmentRVAdaptor.notifyDataSetChanged()
        })

        doctorTrackNameViewModel.emailMap.observe(viewLifecycleOwner, Observer { emailMap ->
            DoctorTrackNameFragmentRVAdaptor.setOnItemClickListener(object :
                DoctorTrackNameFragmentRVAdaptor.OnItemClickListener {
                override fun onItemClick(position: Int) { //triggered when item is clicked
                    val patientName = DoctorTrackNameFragmentRVAdaptor.filteredPatientList[position].name //  filtered clicked item
                    val patientEmail = emailMap[patientName]  // Get email from the map using the patient name
                    Log.d("Patient", "Query: $patientName $patientEmail")

                    if (patientEmail != null) {
                        val intent = Intent(activity, DoctorTrackDateActivity::class.java)
                        intent.putExtra("PatientEmail", patientEmail)
                        intent.putExtra("PatientName", patientName)
                        startActivity(intent)
                    }
                }
            })
        })

        // Set up search functionality
        setupSearchView()

        return binding.root
    }

    private fun setupSearchView() {
        binding.SearchViewTrackName.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // Called when the user submits a search query but not using here
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {//triggered whenever the text in the SearchView changes
                //calls the filter() method on the adapter (DoctorTrackNameFragmentRVAdaptor), passing the new search text (newText).
                DoctorTrackNameFragmentRVAdaptor.filter(newText ?: "")
                return true // Indicate that the query text change has been handled
            }
        })
    }
}
