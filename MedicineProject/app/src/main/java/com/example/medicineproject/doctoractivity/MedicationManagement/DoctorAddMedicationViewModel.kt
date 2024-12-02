package com.example.medicineproject.doctoractivity.MedicationManagement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.medicineproject.doctoractivity.DataClasses.UserMedicineDetail
import com.google.firebase.database.*

class DoctorAddMedicationViewModel : ViewModel() {

    // LiveData for each field in the activity
    private val _medicineName = MutableLiveData<String>()
    val medicineName: LiveData<String> get() = _medicineName

    private val _hours = MutableLiveData<String>()
    val hours: LiveData<String> get() = _hours

    private val _pills = MutableLiveData<String>()
    val pills: LiveData<String> get() = _pills

    private val _totalPills = MutableLiveData<String>()
    val totalPills: LiveData<String> get() = _totalPills

    private val _patientMedicineList = MutableLiveData<ArrayList<UserMedicineDetail>>()
    val patientMedicineList: LiveData<ArrayList<UserMedicineDetail>> get() = _patientMedicineList

    private val databaseUrl = "https://medicinereminderappproje-17b6b-default-rtdb.asia-southeast1.firebasedatabase.app"
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var fireRef: DatabaseReference

    // Setters for each field to update LiveData
    fun setMedicineName(medicineName: String) {
        _medicineName.value = medicineName
    }

    fun setHours(hours: String) {
        _hours.value = hours
    }

    fun setPills(pills: String) {
        _pills.value = pills
    }

    fun setTotalPills(totalPills: String) {
        _totalPills.value = totalPills
    }

    fun setPatientMedicineList(list: ArrayList<UserMedicineDetail>) {
        _patientMedicineList.value = list
    }

    // Function to fetch data from Firebase and update the LiveData
    fun fetchData(patientEmail: String, formattedDate: String) {
        firebaseRef = FirebaseDatabase.getInstance(databaseUrl).getReference("Users").child(patientEmail).child("date").child(formattedDate)
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val patientMedicineList = arrayListOf<UserMedicineDetail>()
                if (snapshot.exists()) {
                    for (medicine in snapshot.children) {
                        val med = medicine.getValue(UserMedicineDetail::class.java)
                        med?.let { patientMedicineList.add(it) }
                    }
                }
                setPatientMedicineList(patientMedicineList) // Update the LiveData
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    // Function to add medicine to Firebase
    fun addMedicineToFirebase(medicineName: String, hours: String, pills: String, totalPills: String, formattedDate: String, patientEmail: String) {
        val remainingTimeMillis: Long = 0
        val lastUpdatedTimes = ""
        val alarmState = false

        if (medicineName.isNotEmpty() && hours.isNotEmpty() && pills.isNotEmpty() && totalPills.isNotEmpty()) {
            if(hours.toInt()<=36){
            val medicineDetail = UserMedicineDetail(medicineName.uppercase(), hours, pills, totalPills, totalPills, formattedDate, remainingTimeMillis, lastUpdatedTimes, alarmState)
            firebaseRef = FirebaseDatabase.getInstance(databaseUrl).getReference("Users")
                .child(patientEmail).child("date").child(formattedDate).child(medicineName.uppercase())

            firebaseRef.setValue(medicineDetail).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    clearFields()
                }
            }
        }
        }
    }

    // Function to remove medicine from Firebase
    fun removeMedicineFromFirebase(medicineName: String, formattedDate: String, patientEmail: String) {
        fireRef = FirebaseDatabase.getInstance(databaseUrl).getReference("Users")
            .child(patientEmail).child("date").child(formattedDate).child(medicineName)

        fireRef.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
            }
        }
    }


    //  Method to clear all fields
    fun clearFields() {
        _medicineName.value = ""
        _hours.value = ""
        _pills.value = ""
        _totalPills.value = ""
    }
}
