package com.example.medicineproject.patientactivity.Setting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.medicineproject.databinding.FragmentPatientSettingBinding
import com.example.medicineproject.UserAccountManagement.LoginActivity.LoginActivity
import com.google.firebase.auth.FirebaseAuth


class PatientSettingFragment : Fragment() {

    val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var binding: FragmentPatientSettingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPatientSettingBinding.inflate(inflater, container, false)



        binding.ivLogOut.setOnClickListener{
            firebaseAuth.signOut()
            // Redirect to the login screen after logout
            val intent = Intent(activity, LoginActivity::class.java).apply {
                // Clear the activity stack and start a new task
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            //Start the new activity and finish the current one
            startActivity(intent)
            requireActivity().finish()
        }

        binding.tvFAQ.setOnClickListener {
            val intent = Intent(activity, PatientFAQActivity::class.java).apply {
                // Clear the activity stack and start a new task

            }
            startActivity(intent)
        }
        binding.tvAbout.setOnClickListener {
            val intent = Intent(activity, PatientAboutActivity::class.java).apply {
                // Clear the activity stack and start a new task

            }
            startActivity(intent)
        }
        binding.tvPatientSupport.setOnClickListener {
            val intent = Intent(activity, PatientSupportActivity::class.java).apply {
                // Clear the activity stack and start a new task

            }
            startActivity(intent)
        }
        return binding.root
    }


}