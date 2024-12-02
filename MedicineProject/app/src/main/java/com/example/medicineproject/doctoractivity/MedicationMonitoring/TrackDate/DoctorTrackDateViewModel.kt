package com.example.medicineproject.doctoractivity.MedicationMonitoring.TrackDate

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*

class DoctorTrackDateViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseRef: DatabaseReference =
        FirebaseDatabase.getInstance("https://medicinereminderappproje-17b6b-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("Users")

    private val _patientDateList = MutableLiveData<List<String>>()
    val patientDateList: LiveData<List<String>> get() = _patientDateList

    // Function to fetch data from Firebase
    fun fetchData(patientEmail: String) {
        firebaseRef.child(patientEmail).child("date").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dateList = mutableListOf<String>()
                if (snapshot.exists()) {
                    for (date in snapshot.children) {
                        date.key?.let { dateList.add(it) }
                    }
                }
                _patientDateList.value = dateList
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DoctorTrackDateViewModel", "Data fetch cancelled: $error")
            }
        })
    }
}
