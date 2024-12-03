package com.example.medicineproject.patientactivity.History.PatientVisitHistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PatientVisitHistoryFragmentViewModel : ViewModel() {

    private val _dates = MutableLiveData<List<String>>()
    val dates: LiveData<List<String>> get() = _dates

    private val databaseUrl = "https://medicinereminderappproje-17b6b-default-rtdb.asia-southeast1.firebasedatabase.app"
    private lateinit var firebaseRef: DatabaseReference

    fun fetchData(patientEmail: String) { // retrieve data from firebase database
        firebaseRef = FirebaseDatabase.getInstance(databaseUrl).getReference("Users").child(patientEmail).child("date")

        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) { //called when data in database is changed
                val dateList = mutableListOf<String>()

                if (snapshot.exists()) {
                    for (list in snapshot.children) { //children of the list
                        list.key?.let { dateList.add(it) } //the key of the list
                    }
                }

                _dates.value = dateList
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
