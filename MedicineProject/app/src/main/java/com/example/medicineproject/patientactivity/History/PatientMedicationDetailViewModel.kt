package com.example.medicineproject.patientactivity.History

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.medicineproject.doctoractivity.DataClasses.UserMedicineDetail
import com.google.firebase.database.*

class PatientMedicationDetailViewModel : ViewModel() {

    private val _medicineList = MutableLiveData<List<UserMedicineDetail>>()
    val medicineList: LiveData<List<UserMedicineDetail>> = _medicineList

    private val databaseUrl = "https://medicinereminderappproje-17b6b-default-rtdb.asia-southeast1.firebasedatabase.app"
    private lateinit var firebaseRef: DatabaseReference

    // Function to fetch data from Firebase
    fun fetchData(patientEmail: String, date: String) { // fetch data based on this path
        firebaseRef = FirebaseDatabase.getInstance(databaseUrl)
            .getReference("Users")
            .child(patientEmail)
            .child("date")
            .child(date)

        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) { //called when detects data changed in the database
                val medicineList = mutableListOf<UserMedicineDetail>()
                if (snapshot.exists()) {
                    for (medicineSnapshot in snapshot.children) { // Map the snapshot data to the UserMedicineDetail data class
                        val medicine = medicineSnapshot.getValue(UserMedicineDetail::class.java)
                        medicine?.let {
                            medicineList.add(it)
                        }
                    }
                }
                _medicineList.postValue(medicineList)  // Update the LiveData with the fetched medicine list
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


    fun addMedicineToSchedule(patientEmail: String, selectedMedicine: UserMedicineDetail) {
        if ((selectedMedicine.pillsleft?.toInt() ?: 0) > 0) { // Check if there are enough pills left before adding to the schedule
            val firebaseRef = FirebaseDatabase.getInstance(databaseUrl)
                .getReference("Users")
                .child(patientEmail)
                .child("MedicineSchedule")
                .child(selectedMedicine.medicineName ?: "")
            firebaseRef.setValue(selectedMedicine) // Save the selected medicine to the database under the patient's MedicineSchedule
        }
    }
}
