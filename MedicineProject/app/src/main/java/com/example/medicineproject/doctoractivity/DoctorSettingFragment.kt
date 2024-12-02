package com.example.medicineproject.doctoractivity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.medicineproject.databinding.FragmentDoctorSettingBinding
import com.example.medicineproject.UserAccountManagement.LoginActivity.LoginActivity
import com.google.firebase.auth.FirebaseAuth


class DoctorSettingFragment : Fragment() {
    val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var binding: FragmentDoctorSettingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDoctorSettingBinding.inflate(inflater, container, false)



        binding.ivDoctorLogOut.setOnClickListener{
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
        binding.tvDoctorAbout.setOnClickListener {
            val intent = Intent(activity, DoctorAboutActivity::class.java).apply {
                // Clear the activity stack and start a new task

            }
            startActivity(intent)
        }
        binding.tvDoctorSupport.setOnClickListener {
            val intent = Intent(activity, DoctorSupportActivity::class.java).apply {
                // Clear the activity stack and start a new task

            }
            startActivity(intent)
        }
        return binding.root
        }

}

