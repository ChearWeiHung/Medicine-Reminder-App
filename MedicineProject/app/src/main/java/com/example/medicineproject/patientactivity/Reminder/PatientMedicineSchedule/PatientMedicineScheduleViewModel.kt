package com.example.medicineproject.patientactivity.Reminder.PatientMedicineSchedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.medicineproject.doctoractivity.DataClasses.UserMedicineDetail
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PatientMedicineScheduleViewModel : ViewModel() {

    private val _medicineList = MutableLiveData<ArrayList<UserMedicineDetail>>()
    val medicineList: LiveData<ArrayList<UserMedicineDetail>> = _medicineList

    private val databaseUrl =
        "https://medicinereminderappproje-17b6b-default-rtdb.asia-southeast1.firebasedatabase.app"
    private lateinit var firebaseRef: DatabaseReference

    // Function to fetch data from Firebase
    fun fetchData(patientEmail: String) {
        firebaseRef = FirebaseDatabase.getInstance(databaseUrl).getReference("Users")
            .child(patientEmail).child("MedicineSchedule")

        firebaseRef.addValueEventListener(object : ValueEventListener {  //  ValueEventListener to fetch the data whenever it changes
            override fun onDataChange(snapshot: DataSnapshot) { // called when data changed in datbase
                val medicineList = ArrayList<UserMedicineDetail>()
                if (snapshot.exists()) {
                    for (medicine in snapshot.children) {
                        val med = medicine.getValue(UserMedicineDetail::class.java) // Get each child as an instance of UserMedicineDetail
                        med?.let {
                            medicineList.add(it)
                        }
                    }
                }
                _medicineList.postValue(medicineList)  // Update LiveData with fetched data
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle Firebase error
                _medicineList.postValue(ArrayList())  // Clear the list if error occurs
            }
        })
    }
}
