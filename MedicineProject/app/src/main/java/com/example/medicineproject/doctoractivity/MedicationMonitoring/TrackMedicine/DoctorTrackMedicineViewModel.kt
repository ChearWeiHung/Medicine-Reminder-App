package com.example.medicineproject.doctoractivity.MedicationMonitoring

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medicineproject.doctoractivity.DataClasses.UserMedicineDetail
import com.google.firebase.database.*

class DoctorTrackMedicineViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseDatabase = FirebaseDatabase.getInstance("https://medicinereminderappproje-17b6b-default-rtdb.asia-southeast1.firebasedatabase.app")
    private val _medicineList = MutableLiveData<List<UserMedicineDetail>>()
    val medicineList: LiveData<List<UserMedicineDetail>> get() = _medicineList

    // Function to fetch medicine data from Firebase
    fun fetchMedicineData(patientEmail: String, date: String) {
        val firebaseRef: DatabaseReference = firebaseDatabase.getReference("Users").child(patientEmail).child("date").child(date)

        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) { //triggered when detects data changes in firebase database
                val medicines = mutableListOf<UserMedicineDetail>()
                if (snapshot.exists()) {
                    for (medicineSnapshot in snapshot.children) {
                        val medicine = medicineSnapshot.getValue(UserMedicineDetail::class.java)
                        medicine?.let {
                            medicines.add(it) //saved medicine details
                        }
                    }
                }
                _medicineList.value = medicines // Update LiveData
            }

            override fun onCancelled(error: DatabaseError) {
                // Log error (or notify the UI)
            }
        })
    }
}
