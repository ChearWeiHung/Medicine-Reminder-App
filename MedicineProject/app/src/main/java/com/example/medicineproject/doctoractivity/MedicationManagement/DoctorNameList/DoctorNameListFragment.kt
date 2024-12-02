package com.example.medicineproject.doctoractivity.MedicationManagement.DoctorNameList

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicineproject.databinding.FragmentDoctorNameListBinding
import com.example.medicineproject.doctoractivity.MedicationManagement.DoctorAddMedicationActivity

class DoctorNameListFragment : Fragment() {
    private lateinit var binding: FragmentDoctorNameListBinding
    private lateinit var doctorNameListRVAdaptor: DoctorNameListRVAdaptor
    private val doctorNameListViewModel: DoctorNameListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDoctorNameListBinding.inflate(inflater, container, false)

        // Initialize the adapter
        doctorNameListRVAdaptor = DoctorNameListRVAdaptor(arrayListOf())

        // Set up RecyclerView
        binding.rvPatientList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
            adapter = doctorNameListRVAdaptor
        }

        // Observe ViewModel data
        doctorNameListViewModel.patientList.observe(viewLifecycleOwner, Observer { patientList ->
            doctorNameListRVAdaptor.updateFullList(patientList)
            doctorNameListRVAdaptor.notifyDataSetChanged()
        })
        //setup recycler view
        doctorNameListViewModel.emailMap.observe(viewLifecycleOwner, Observer { emailMap ->
            doctorNameListRVAdaptor.setOnItemClickListener(object :
                DoctorNameListRVAdaptor.OnItemClickListener {
                override fun onItemClick(position: Int) { //triggered when the item is clicked
                    val patientName = doctorNameListRVAdaptor.filteredPatientList[position].name //get the item clicked
                    val patientEmail = emailMap[patientName]

                    if (patientEmail != null) {
                        val intent = Intent(activity, DoctorAddMedicationActivity::class.java) //navigation
                        intent.putExtra("PatientEmail", patientEmail) //passes data
                        intent.putExtra("PatientName", patientName)
                        startActivity(intent)
                    }
                }
            })
        })


        doctorNameListViewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        })

        setupSearchView()

        return binding.root
    }

    private fun setupSearchView() {
        binding.SearchViewPatientList.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // Called when the user submits a search query but not using here
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean { //triggered whenever the text in the SearchView changes
                //calls the filter() method on the adapter (DoctorNameListFragmentRVAdaptor), passing the new search text (newText).
                doctorNameListRVAdaptor.filter(newText ?: "")
                return true
            }
        })
    }
}
