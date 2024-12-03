package com.example.medicineproject.UserAccountManagement.LoginFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginFragmentViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> get() = _loginResult

    // LiveData to hold email and password values to survive configuration changes
    private val _patientEmail = MutableLiveData<String>()
    val patientEmail: LiveData<String> get() = _patientEmail

    private val _patientLoginPassword = MutableLiveData<String>()
    val patientLoginPassword: LiveData<String> get() = _patientLoginPassword

    // Enum to represent different outcomes of login
    sealed class LoginResult {
        object SuccessPatient : LoginResult()
        object SuccessDoctor : LoginResult()
        data class Failure(val error: String) : LoginResult()
        object EmptyField : LoginResult()
    }


    fun login(patientEmail: String, patientLoginPassword: String) { // Login function
        // Save the values to LiveData
        _patientEmail.postValue(patientEmail)
        _patientLoginPassword.postValue(patientLoginPassword)

        if (patientEmail.isEmpty() || patientLoginPassword.isEmpty()) { // validation for empty field
            _loginResult.postValue(LoginResult.EmptyField)
            return
        }

        if (patientEmail != "doctor2@gmail.com") { // check if it is doctor authorized account
            // Patient login
            firebaseAuth.signInWithEmailAndPassword(patientEmail, patientLoginPassword)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _loginResult.postValue(LoginResult.SuccessPatient) // Notify successful doctor login
                    } else {
                        _loginResult.postValue(LoginResult.Failure(it.exception.toString())) // Notify login failure with error message
                    }
                    // Clear the email and password LiveData after the login attempt
                    _patientEmail.postValue("")
                    _patientLoginPassword.postValue("")
                }
        } else {
            // Doctor login
            firebaseAuth.signInWithEmailAndPassword(patientEmail, patientLoginPassword) // Sign In With FireBase Authentication
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _loginResult.postValue(LoginResult.SuccessDoctor)
                    } else {
                        _loginResult.postValue(LoginResult.Failure(it.exception.toString()))
                    }
                    // Clear the email and password LiveData after login successfully
                    _patientEmail.postValue("")
                    _patientLoginPassword.postValue("")
                }
        }
    }
}
