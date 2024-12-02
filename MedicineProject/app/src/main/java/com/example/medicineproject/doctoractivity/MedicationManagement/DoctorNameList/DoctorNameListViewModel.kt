package com.example.medicineproject.doctoractivity.MedicationManagement.DoctorNameList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.medicineproject.doctoractivity.DataClasses.UserAccount
import com.google.firebase.database.*

class DoctorNameListViewModel : ViewModel() {
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
            override fun onDataChange(snapshot: DataSnapshot) { //triggered when detects data changes in firebase database
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

                _patientList.value = patientList
                _emailMap.value = emailMap
                _isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) { //failed to fetch data
                _errorMessage.value = "Error: ${error.message}"
                _isLoading.value = false
            }
        })
    }
}
