package com.example.medicineproject.UserAccountManagement.SignUp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medicineproject.UserAccountManagement.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PatientSignUpViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    //live data
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> get() = _password

    private val _confirmPassword = MutableLiveData<String>()
    val confirmPassword: LiveData<String> get() = _confirmPassword

    private val _registrationResult = MutableLiveData<String>()
    val registrationResult: LiveData<String> get() = _registrationResult

    // Register user function
    fun registerUser() { //value from live data
        val nameValue = _name.value
        val emailValue = _email.value
        val passwordValue = _password.value
        val confirmPasswordValue = _confirmPassword.value

        // validation to check empty field
        if (emailValue.isNullOrEmpty() || passwordValue.isNullOrEmpty() || confirmPasswordValue.isNullOrEmpty() || nameValue.isNullOrEmpty()) {
            _registrationResult.postValue("Empty fields are not allowed") // message for toast
            return
        }
        // validation for confirming password
        if (passwordValue != confirmPasswordValue) {
            _registrationResult.postValue("Passwords do not match") // message for toast
            return
        }
        // create account after sign up sucessfully
        firebaseAuth.createUserWithEmailAndPassword(emailValue, passwordValue)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val sanitizedEmail = emailValue.replace(".", ",") // as firebase does not allow "." So replace with ","
                    val databaseUrl = "https://medicinereminderappproje-17b6b-default-rtdb.asia-southeast1.firebasedatabase.app"
                    val firebaseRef = FirebaseDatabase.getInstance(databaseUrl).getReference("Users") // reference to the path
                    val user = User(nameValue) // Instantiate

                    firebaseRef.child(sanitizedEmail).setValue(user) // save email account and name to firebase database
                        .addOnCompleteListener { mission ->
                            if (mission.isSuccessful) {
                                _registrationResult.postValue("Account saved successfully")
                                clearFields()
                            } else {
                                _registrationResult.postValue("Failed to save account")
                            }
                        }
                        .addOnFailureListener { exception ->
                            _registrationResult.postValue("Failed to save account: ${exception.message}")
                        }
                } else {
                    val errorMessage = task.exception?.message ?: "Registration failed"
                    _registrationResult.postValue(errorMessage)
                }
            }
    }

    // Set LiveData values
    fun setName(name: String) {
        _name.postValue(name)
    }

    fun setEmail(email: String) {
        _email.postValue(email)
    }

    fun setPassword(password: String) {
        _password.postValue(password)
    }

    fun setConfirmPassword(confirmPassword: String) {
        _confirmPassword.postValue(confirmPassword)
    }
    // Clear all LiveData values
    private fun clearFields() {
        _name.postValue("")
        _email.postValue("")
        _password.postValue("")
        _confirmPassword.postValue("")
    }
}
