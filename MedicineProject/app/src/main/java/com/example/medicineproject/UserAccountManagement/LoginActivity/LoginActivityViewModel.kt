package com.example.medicineproject.UserAccountManagement.LoginActivity

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginActivityViewModel : ViewModel() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // Function to determine the next activity based on the current user’s email
    fun getNextActivity(): NavigationDestination {
        val currentUser = firebaseAuth.currentUser // check if current account exist or did not log out previously

        return when {
            currentUser == null -> NavigationDestination.Login //no user
            currentUser.email == "doctor2@gmail.com" -> NavigationDestination.Doctor //doctor's authorized account
            else -> NavigationDestination.Patient(currentUser.email!!) //patient
        }
    }

    // Enum to define navigation destinations
    sealed class NavigationDestination {
        object Login : NavigationDestination() // Login Pages
        object Doctor : NavigationDestination() //Doctor Activity
        data class Patient(val email: String) : NavigationDestination() //Patient Activity
    }

}
