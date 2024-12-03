package com.example.medicineproject.doctoractivity.MedicationMonitoring.TrackProgress

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*

class DoctorTrackProgressViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseDatabase = FirebaseDatabase.getInstance("https://medicinereminderappproje-17b6b-default-rtdb.asia-southeast1.firebasedatabase.app")
    private val _dateAndTimeList = MutableLiveData<List<String>>()
    val dateAndTimeList: LiveData<List<String>> get() = _dateAndTimeList

    // Function to fetch the Date and Time data from Firebase
    fun fetchDateAndTimeData(patientEmail: String, medicineName: String, date: String) {
        val firebaseRef: DatabaseReference = firebaseDatabase.getReference("Users")
            .child(patientEmail)
            .child("DateAndTime")
            .child(date)
            .child(medicineName)

        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dateAndTime = mutableListOf<String>()
                if (snapshot.exists()) {
                    for (time in snapshot.children) {
                        time.key?.let { //key(Date and time)
                            dateAndTime.add(it)
                        }
                    }
                }
                _dateAndTimeList.value = dateAndTime // Update LiveData
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
