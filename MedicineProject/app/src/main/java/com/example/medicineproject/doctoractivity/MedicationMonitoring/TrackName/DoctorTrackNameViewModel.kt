package com.example.medicineproject.doctoractivity.MedicationMonitoring.TrackName

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.medicineproject.doctoractivity.DataClasses.UserAccount
import com.google.firebase.database.*

class DoctorTrackNameViewModel : ViewModel() {
    private val firebaseRef: DatabaseReference = FirebaseDatabase.getInstance("https://medicinereminderappproje-17b6b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users")
    private val _patientList = MutableLiveData<List<UserAccount>>()
    val patientList: LiveData<List<UserAccount>> get() = _patientList

    private val _emailMap = MutableLiveData<Map<String, String>>()
    val emailMap: LiveData<Map<String, String>> get() = _emailMap

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        fetchData()
    }

    private fun fetchData() {
        _isLoading.value = true
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val patientList = arrayListOf<UserAccount>()
                val emailMap = mutableMapOf<String, String>()

                if (snapshot.exists()) {
                    for (account in snapshot.children) {
                        val user = account.getValue(UserAccount::class.java)
                        val email = account.key
                        val name = user?.name

                        if (email != null && name != null) {
                            emailMap[name] = email
                            patientList.add(user)
                        }
                    }
                }
                // Update the LiveData values with the fetched data

                _patientList.value = patientList
                _emailMap.value = emailMap
                _isLoading.value = false // Set isLoading to false as data fetching is complete

            }

            override fun onCancelled(error: DatabaseError) {
                _errorMessage.value = "Error: ${error.message}"
                _isLoading.value = false
            }
        })
    }
}
